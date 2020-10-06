package pg.eti.ksg.ProjektInzynierski.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

import java.util.List;

import pg.eti.ksg.ProjektInzynierski.Dao.FriendsDao;
import pg.eti.ksg.ProjektInzynierski.Dao.MessagesDao;
import pg.eti.ksg.ProjektInzynierski.Dao.ProjectDatabase;
import pg.eti.ksg.ProjektInzynierski.Dao.UserFriendsDao;
import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.Friends;
import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.Messages;
import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.UserFriends;
import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.UserWithFriends;

public class FriendsRepository {
    private FriendsDao friendsDao;
    private UserFriendsDao userFriendsDao;
    private String userLogin;
    private LiveData<UserWithFriends> friends;

    public FriendsRepository(Application application,String userLogin) {
        ProjectDatabase db = ProjectDatabase.getDatabase(application);
        friendsDao = db.friendsDao();
        userFriendsDao = db.userFriendsDao();
        this.userLogin=userLogin;
    }


    public LiveData<UserWithFriends> getFriends() {
        if(friends == null)
            friends = friendsDao.getFriends(userLogin);
        return friends;
    }



    public void insert(Friends friend)
    {
        new InsertAsyncTask(friendsDao,userFriendsDao,userLogin).execute(friend);
    }

    public void insertList(List<Friends> friend)
    {
        new InsertListAsyncTask(friendsDao,userFriendsDao,userLogin).execute(friend);
    }

    public void update(Friends friend)
    {
        new UpdateAsyncTask(friendsDao).execute(friend);
    }

    public void delete(Friends friend)
    {

    }

    private static class InsertAsyncTask extends AsyncTask<Friends,Void,Void> {

        private FriendsDao friendsDao;
        private UserFriendsDao userFriendsDao;
        private String userLogin;

        public InsertAsyncTask(FriendsDao friendsDao,UserFriendsDao userFriendsDao,String userLogin) {
            this.friendsDao = friendsDao;
            this.userFriendsDao =userFriendsDao;
            this.userLogin=userLogin;
        }

        @Override
        protected Void doInBackground(Friends... friends) {
            if(friendsDao.isFriendSync(friends[0].getLogin()) == null)
                friendsDao.insert(friends[0]);
            if(userFriendsDao.getUserFriend(userLogin,friends[0].getLogin()) == null)
                userFriendsDao.insert(new UserFriends(userLogin,friends[0].getLogin()));
            return null;
        }
    }
    private static class InsertListAsyncTask extends AsyncTask<List<Friends>,Void,Void> {

        private FriendsDao friendsDao;
        private UserFriendsDao userFriendsDao;
        private String userLogin;

        public InsertListAsyncTask(FriendsDao friendsDao,UserFriendsDao userFriendsDao,String userLogin) {
            this.friendsDao = friendsDao;
            this.userFriendsDao =userFriendsDao;
            this.userLogin=userLogin;
        }

        @Override
        protected Void doInBackground(List<Friends>... friends) {
            for(Friends friend : friends[0]){
                if(friendsDao.isFriendSync(friend.getLogin()) == null)
                    friendsDao.insert(friend);
                if(userFriendsDao.getUserFriend(userLogin,friend.getLogin()) == null)
                    userFriendsDao.insert(new UserFriends(userLogin,friend.getLogin()));
            }
            return null;
        }
    }
    private static class UpdateAsyncTask extends AsyncTask<Friends,Void,Void> {

        private FriendsDao friendsDao;

        public UpdateAsyncTask(FriendsDao friendsDao) {
            this.friendsDao = friendsDao;
        }

        @Override
        protected Void doInBackground(Friends... friends) {
            friendsDao.update(friends[0]);
            return null;
        }
    }
    private static class DeleteAsyncTask extends AsyncTask<Friends,Void,Void> {

        private FriendsDao friendsDao;
        private UserFriendsDao userFriendsDao;
        private String userLogin;

        public DeleteAsyncTask(FriendsDao friendsDao,UserFriendsDao userFriendsDao,String userLogin) {
            this.friendsDao = friendsDao;
            this.userFriendsDao =userFriendsDao;
            this.userLogin=userLogin;
        }

        @Override
        protected Void doInBackground(Friends... friends) {
            //TODO
            return null;
        }
    }
}
