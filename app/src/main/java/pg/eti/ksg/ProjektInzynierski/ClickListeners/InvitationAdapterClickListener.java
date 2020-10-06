package pg.eti.ksg.ProjektInzynierski.ClickListeners;

import android.app.Application;
import android.content.Context;

import pg.eti.ksg.ProjektInzynierski.AlertDialogs;
import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.Friends;
import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.Invitations;
import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.UserInvitations;
import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.Users;
import pg.eti.ksg.ProjektInzynierski.Models.ResponseModel;
import pg.eti.ksg.ProjektInzynierski.Repository.FriendsRepository;
import pg.eti.ksg.ProjektInzynierski.Repository.InvitationsRepository;
import pg.eti.ksg.ProjektInzynierski.server.ServerApi;
import pg.eti.ksg.ProjektInzynierski.server.ServerClient;
import pg.eti.ksg.ProjektInzynierski.ui.RecyclerViews.InvitationsRVAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InvitationAdapterClickListener implements InvitationsRVAdapter.ClickListener {
    private Application application;
    private Context context;
    private InvitationsRepository repository;
    private FriendsRepository friendsRepository;
    private String userLogin;
    private ServerApi api;


    public InvitationAdapterClickListener(Application application,Context context,String userLogin) {
        this.application = application;
        this.userLogin = userLogin;
        this.context=context;
        repository = new InvitationsRepository(application,userLogin);
        friendsRepository = new FriendsRepository(application,userLogin);
        api = ServerClient.getClient();
    }

    @Override
    public void onAcceptClick(Invitations invitation) {

        Call<Friends> call = api.acceptInvitation(userLogin,invitation.getLogin());
        call.enqueue(new Callback<Friends>() {
            @Override
            public void onResponse(Call<Friends> call, Response<Friends> response) {
                if(!response.isSuccessful()){
                    AlertDialogs.serverError(context);
                }
                Friends newFriend = response.body();
                friendsRepository.insert(newFriend);
                repository.delete(userLogin,newFriend.getLogin());
            }

            @Override
            public void onFailure(Call<Friends> call, Throwable t) {
                AlertDialogs.networkError(context);
            }
        });
    }

    @Override
    public void onDismissClick(Invitations invitation) {
        Call<ResponseModel> call =api.dismissInvitation(userLogin,invitation.getLogin());
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if(!response.isSuccessful()){
                    AlertDialogs.serverError(context);
                }
                

            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                AlertDialogs.networkError(context);
            }
        });
    }
}
