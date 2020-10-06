package pg.eti.ksg.ProjektInzynierski.ui.invitations;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.UserWithInvitations;
import pg.eti.ksg.ProjektInzynierski.Repository.InvitationsRepository;


public class InvitationsViewModel extends AndroidViewModel {

    private Application application;
    private InvitationsRepository invitationsRepository;
    private LiveData<UserWithInvitations> invitations;


    public InvitationsViewModel(@NonNull Application application){
        super(application);
        this.application = application;
    }

    public LiveData<UserWithInvitations> getInvitations(String userLogin) {
        if(invitationsRepository == null)
            invitationsRepository =new InvitationsRepository(application,userLogin);
        if(invitations == null)
            invitations = invitationsRepository.getInvitations();
        return invitations;
    }
}