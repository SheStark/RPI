package pg.eti.ksg.ProjektInzynierski.Dao;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.Converters;
import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.Friends;
import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.Invitations;
import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.Messages;
import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.Points;
import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.Routes;
import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.UserFriends;
import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.UserInvitations;
import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.UserRoutes;
import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.Users;

@Database(entities = {Friends.class, Invitations.class, Messages.class, Points.class, Routes.class,
        UserFriends.class, UserInvitations.class, UserRoutes.class, Users.class}, version = 3)
@TypeConverters({Converters.class})
public abstract class ProjectDatabase extends RoomDatabase {

    private static ProjectDatabase database;

    public abstract UserDao userDao();

    public abstract RoutesDao routesDao();

    public abstract PointsDao pointsDao();

    public abstract FriendsDao friendsDao();

    public abstract InvitationsDao invitationsDao();

    public abstract MessagesDao messagesDao();

    public abstract UserFriendsDao userFriendsDao();

    public abstract UserInvitationsDao userInvitationsDao();

    public abstract UserRoutesDao userRoutesDao();

    public static synchronized ProjectDatabase getDatabase(Context context){
        if(database == null){
            database = Room.databaseBuilder(context.getApplicationContext(),
                    ProjectDatabase.class,"Logged users database")
                    .fallbackToDestructiveMigration()
                    //.addCallback(callback)
                    .build();
        }

        return database;
    }

    private static RoomDatabase.Callback callback =new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new AddAsyncTask(database).execute();
        }
    };

    private static class AddAsyncTask extends AsyncTask<Void,Void,Void>{

        private RoutesDao routesDao;
        private UserRoutesDao userRoutesDao;

        public AddAsyncTask(ProjectDatabase db) {
            this.routesDao = db.routesDao();
            this.userRoutesDao =db.userRoutesDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            String date = "2020-01-01 20:00:00";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            return null;
        }

    }



}
