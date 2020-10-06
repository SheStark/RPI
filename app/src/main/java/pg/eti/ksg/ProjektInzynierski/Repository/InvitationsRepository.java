package pg.eti.ksg.ProjektInzynierski.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Insert;

import java.util.List;

import pg.eti.ksg.ProjektInzynierski.Dao.FriendsDao;
import pg.eti.ksg.ProjektInzynierski.Dao.InvitationsDao;
import pg.eti.ksg.ProjektInzynierski.Dao.ProjectDatabase;
import pg.eti.ksg.ProjektInzynierski.Dao.UserFriendsDao;
import pg.eti.ksg.ProjektInzynierski.Dao.UserInvitationsDao;
import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.Friends;
import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.Invitations;
import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.UserFriends;
import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.UserInvitations;
import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.UserWithInvitations;

public class InvitationsRepository {
    private InvitationsDao invitationsDao;
    private UserInvitationsDao userInvitationsDao;
    private String userLogin;
    private LiveData<UserWithInvitations> invitations;


    public InvitationsRepository(Application application, String userLogin) {
        ProjectDatabase db = ProjectDatabase.getDatabase(application);
        invitationsDao = db.invitationsDao();
        userInvitationsDao = db.userInvitationsDao();
        this.userLogin=userLogin;
    }

    public LiveData<UserWithInvitations> getInvitations() {
        if(invitations == null)
            invitations = invitationsDao.getInvitations(userLogin);
        return invitations;
    }


    public void insert(Invitations invitation)
    {
        new InsertAsyncTask(invitationsDao,userInvitationsDao,userLogin).execute(invitation);
    }

    public void insert(List<Invitations> invitation)
    {
        new InsertListAsyncTask(invitationsDao,userInvitationsDao,userLogin).execute(invitation);
    }

    public void delete(Invitations invitation){

    }

    public void delete(String userLogin, String invitationLogin){
        new DeleteAsyncTask(invitationsDao,userInvitationsDao,userLogin,invitationLogin).execute();
    }


    private static class InsertAsyncTask extends AsyncTask<Invitations,Void,Void> {

        private InvitationsDao invitationsDao;
        private UserInvitationsDao userInvitationsDao;
        private String userLogin;

        public InsertAsyncTask(InvitationsDao invitationsDao,UserInvitationsDao userInvitationsDao,String userLogin) {
            this.invitationsDao = invitationsDao;
            this.userInvitationsDao =userInvitationsDao;
            this.userLogin=userLogin;
        }

        @Override
        protected Void doInBackground(Invitations... invitations) {
            if(invitationsDao.getInvitationFrom(invitations[0].getLogin()) == null)
                invitationsDao.insert(invitations[0]);
            if(userInvitationsDao.getUserInvitation(userLogin,invitations[0].getLogin()) == null)
                userInvitationsDao.insert(new UserInvitations(invitations[0].getLogin(),userLogin));
            return null;
        }
    }


    private static class InsertListAsyncTask extends AsyncTask<List<Invitations>,Void,Void> {

        private InvitationsDao invitationsDao;
        private UserInvitationsDao userInvitationsDao;
        private String userLogin;

        public InsertListAsyncTask(InvitationsDao invitationsDao,UserInvitationsDao userInvitationsDao,String userLogin) {
            this.invitationsDao = invitationsDao;
            this.userInvitationsDao =userInvitationsDao;
            this.userLogin=userLogin;
        }

        @Override
        protected Void doInBackground(List<Invitations>... invitations) {
            for(Invitations invitation : invitations[0]) {
                if (invitationsDao.getInvitationFrom(invitation.getLogin()) == null)
                    invitationsDao.insert(invitation);
                if (userInvitationsDao.getUserInvitation(userLogin, invitation.getLogin()) == null)
                    userInvitationsDao.insert(new UserInvitations(invitation.getLogin(), userLogin));
            }
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<Void,Void,Void> {

        private InvitationsDao invitationsDao;
        private UserInvitationsDao userInvitationsDao;
        private String userLogin;
        private String invitationLogin;

        public DeleteAsyncTask(InvitationsDao invitationsDao,UserInvitationsDao userInvitationsDao,String userLogin,String invitationLogin) {
            this.invitationsDao = invitationsDao;
            this.userInvitationsDao =userInvitationsDao;
            this.userLogin=userLogin;
            this.invitationLogin=invitationLogin;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            UserInvitations invitations = userInvitationsDao.getUserInvitation(userLogin,invitationLogin);
            userInvitationsDao.delete(invitations);
            List<UserInvitations> list = userInvitationsDao.getInvitationsFrom(invitationLogin);
            if(list.isEmpty()){
                invitationsDao.delete(invitationsDao.getInvitationFrom(invitationLogin));
            }


            return null;
        }
    }
}
