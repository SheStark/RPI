package pg.eti.ksg.ProjektInzynierski.Services;


import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import java.io.IOException;

import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.Users;
import pg.eti.ksg.ProjektInzynierski.Repository.UserRepository;
import pg.eti.ksg.ProjektInzynierski.SharedPreferencesLoginManager;
import pg.eti.ksg.ProjektInzynierski.server.ServerApi;
import pg.eti.ksg.ProjektInzynierski.server.ServerClient;
import retrofit2.Call;
import retrofit2.Response;

public class SynchronizeDataService extends JobIntentService {
    private static final int Id = 1000;
    private UserRepository repository;

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
                repository =new UserRepository(getApplication(),login);
                Users user = repository.getUserSync();
                if(user == null || user.getLogin().isEmpty())
                {
                    Call<Users> currentUser = api.getCurrentUser(login);
                    Response<Users> response = currentUser.execute();
                    if(response.isSuccessful())
                    {
                        repository.insert(response.body());
                        manager.setToSynchronized();
                    }

                }
                else
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
