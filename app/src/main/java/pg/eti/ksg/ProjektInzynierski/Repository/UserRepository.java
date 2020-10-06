package pg.eti.ksg.ProjektInzynierski.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;

import pg.eti.ksg.ProjektInzynierski.Dao.ProjectDatabase;
import pg.eti.ksg.ProjektInzynierski.Dao.UserDao;
import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.UserWithFriends;
import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.Users;

public class UserRepository {

    private UserDao userDao;
    private LiveData<Users> user;
    private LiveData<UserWithFriends> friends;
    private String login;

    public UserRepository(Application application,String login)
    {
        ProjectDatabase database = ProjectDatabase.getDatabase(application);
        userDao = database.userDao();
        this.login = login;
    }

    /*public LiveData<List<Users>> getAllUsers() {
        return allUsers;
    }*/

    public void insert(Users user)
    {
        new InsertAsyncTask(userDao).execute(user);
    }

    public void update(Users user)
    {
        new UpdateAsyncTask(userDao).execute(user);
    }

    public void delete(Users user)
    {
        new DeleteAsyncTask(userDao).execute(user);
    }


    public LiveData<Users> getUser() {
        if(user == null)
            user = userDao.getUser(login);
        return user;
    }

    public Users getUserSync()
    {
        return userDao.getUserSync(login);
    }

    public UserWithFriends getFriendsSync()
    {
        return userDao.getFriendsSync(login);
    }


    public LiveData<UserWithFriends> getFriends() {
        if(friends == null)
            friends = userDao.getFriends(login);
        return friends;
    }

    private static class InsertAsyncTask extends AsyncTask<Users,Void,Void>{

        private UserDao userDao;

        public InsertAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(Users... users) {
            userDao.insert(users[0]);
            return null;
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<Users,Void,Void>{

        private UserDao userDao;

        public UpdateAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(Users... users) {
            userDao.update(users[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<Users,Void,Void>{

        private UserDao userDao;

        public DeleteAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(Users... users) {
            userDao.delete(users[0]);
            return null;
        }
    }

}
