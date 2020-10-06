package pg.eti.ksg.ProjektInzynierski.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;


import java.util.List;

import pg.eti.ksg.ProjektInzynierski.Dao.MessagesDao;
import pg.eti.ksg.ProjektInzynierski.Dao.PointsDao;
import pg.eti.ksg.ProjektInzynierski.Dao.ProjectDatabase;
import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.Friends;
import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.Messages;
import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.Points;

public class MessagesRepository {

    private MessagesDao messagesDao;
    private LiveData<Messages> messages;
    //private LiveData<Friends> friends;

    public MessagesRepository(Application application) {
        ProjectDatabase db = ProjectDatabase.getDatabase(application);
        messagesDao = db.messagesDao();
    }

    public void insert(Messages message){
        new InsertAsyncTask(messagesDao).execute(message);
    }


    public void update(Messages message){
        new UpdateAsyncTask(messagesDao).execute(message);
    }


    public void delete(Messages message)
    {
        new DeleteAsyncTask(messagesDao).execute(message);
    }

    public LiveData<Messages> getMessages(String userLogin, String friendLogin)
    {
        if(messages == null)
            messages = messagesDao.getMessages(userLogin,friendLogin);
        return messages;
    }

    private static class InsertAsyncTask extends AsyncTask<Messages,Void,Void> {

        private MessagesDao messagesDao;

        public InsertAsyncTask(MessagesDao messagesDao) {
            this.messagesDao = messagesDao;
        }

        @Override
        protected Void doInBackground(Messages... messages) {
            messagesDao.insert(messages[0]);
            return null;
        }
    }

    private static class InsertAllAsyncTask extends AsyncTask<List<Messages>,Void,Void> {

        private MessagesDao messagesDao;

        public InsertAllAsyncTask(MessagesDao messagesDao) {
            this.messagesDao = messagesDao;
        }

        @Override
        protected Void doInBackground(List<Messages>... messages) {
            messagesDao.insert(messages[0]);

            return null;
        }
    }


    private static class UpdateAsyncTask extends AsyncTask<Messages,Void,Void> {

        private MessagesDao messagesDao;

        public UpdateAsyncTask(MessagesDao messagesDao) {
            this.messagesDao = messagesDao;
        }

        @Override
        protected Void doInBackground(Messages... messages) {
            messagesDao.update(messages[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<Messages,Void,Void> {

        private MessagesDao messagesDao;

        public DeleteAsyncTask(MessagesDao messagesDao) {
            this.messagesDao = messagesDao;
        }

        @Override
        protected Void doInBackground(Messages... messages) {
            messagesDao.delete(messages[0]);
            return null;
        }
    }

}
