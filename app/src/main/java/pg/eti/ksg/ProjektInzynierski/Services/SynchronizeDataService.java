package pg.eti.ksg.ProjektInzynierski.Services;


import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import java.io.IOException;
import java.util.List;

import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.Friends;
import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.Invitations;
import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.Routes;
import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.Users;
import pg.eti.ksg.ProjektInzynierski.Repository.FriendsRepository;
import pg.eti.ksg.ProjektInzynierski.Repository.InvitationsRepository;
import pg.eti.ksg.ProjektInzynierski.Repository.RoutesRepository;
import pg.eti.ksg.ProjektInzynierski.Repository.UserRepository;
import pg.eti.ksg.ProjektInzynierski.SharedPreferencesLoginData;
import pg.eti.ksg.ProjektInzynierski.SharedPreferencesLoginManager;
import pg.eti.ksg.ProjektInzynierski.server.ServerApi;
import pg.eti.ksg.ProjektInzynierski.server.ServerClient;
import pg.eti.ksg.ProjektInzynierski.ui.register.RegisterActivity;
import retrofit2.Call;
import retrofit2.Response;

public class SynchronizeDataService extends JobIntentService {
    private static final int Id = 1000;
    private UserRepository repository;
    private boolean isSynchronized;
    private String userLogin;

    private SharedPreferencesLoginManager manager;
    private ServerApi api;
    public static void enqueueWork(Context context,Intent work)
    {
        enqueueWork(context,SynchronizeDataService.class, Id ,work);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        manager = new SharedPreferencesLoginManager(this);
        api = ServerClient.getClient();
    }


    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        try {
            String login = manager.logged();
            if(login != null && !login.isEmpty()){
                manager.setToNonSynchronized();
                isSynchronized =true;
                repository = new UserRepository(getApplication(),login);
                Users user = repository.getUserSync();
                if(user == null || user.getLogin().isEmpty())
                {
                    Call<Users> currentUser = api.getCurrentUser(login);
                    Response<Users> response = currentUser.execute();
                    if(response.isSuccessful())
                    {
                        repository.insert(response.body());
                        userLogin = response.body().getLogin();
                        SharedPreferencesLoginData newUser = new SharedPreferencesLoginData(userLogin,response.body().getName(),response.body().getSurname());
                        manager.addData(newUser);
                    }
                    else {
                        isSynchronized = false;
                    }

                }
                else
                    userLogin = user.getLogin();

                if(userLogin != null){
                    InvitationsRepository invitationsRepository =new InvitationsRepository(getApplication(),userLogin);
                    Call<List<Users>> invitations = api.getUserInvitations(userLogin);
                    Response<List<Users>> response = invitations.execute();
                    if(response.isSuccessful())
                    {
                        if(response.body() != null) {
                            for (Users users : response.body()) {
                                invitationsRepository.insert(new Invitations(users.getLogin(), users.getName(), users.getSurname(), users.getCity()));
                            }
                        }
                    }
                    else
                        isSynchronized = false;
                }
                if(userLogin != null)
                {
                    FriendsRepository friendsRepository =new FriendsRepository(getApplication(),userLogin);
                    Call<List<Friends>> friends = api.getUserFriends(userLogin);
                    Response<List<Friends>> response = friends.execute();
                    if(response.isSuccessful())
                    {
                        if(response.body() != null)
                            friendsRepository.insertList(response.body());
                    }
                    else
                        isSynchronized = false;

                }
                if(userLogin != null)
                {
                    RoutesRepository routesRepository =new RoutesRepository(getApplication(),userLogin);
                    Call<List<Routes>> myRoutes = api.getMyRoutes(userLogin);
                    Response<List<Routes>> response = myRoutes.execute();
                    if(response.isSuccessful())
                    {
                        if(response.body() != null)
                            for(Routes route: response.body()){
                                routesRepository.insert(route);
                            }
                    }
                    else
                        isSynchronized = false;

                }
                if(userLogin != null)
                {
                    RoutesRepository routesRepository =new RoutesRepository(getApplication(),userLogin);
                    Call<List<Routes>> friendsRoutes = api.getFriendsRoutes(userLogin);
                    Response<List<Routes>> response = friendsRoutes.execute();
                    if(response.isSuccessful())
                    {
                        if(response.body() != null)
                            for(Routes route: response.body()){
                                routesRepository.insert(route);
                            }
                    }
                    else
                        isSynchronized = false;

                }


                if(isSynchronized)
                    manager.setToSynchronized();
            }
        }catch (IOException ex){

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
