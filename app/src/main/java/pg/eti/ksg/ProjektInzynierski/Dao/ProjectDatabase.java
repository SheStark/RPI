package pg.eti.ksg.ProjektInzynierski.Dao;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

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
        UserFriends.class, UserInvitations.class, UserRoutes.class, Users.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class ProjectDatabase extends RoomDatabase {

private static ProjectDatabase database;

public abstract UserDao userDao();

public static synchronized ProjectDatabase getDatabase(Context context){
    if(database == null){
        database = Room.databaseBuilder(context.getApplicationContext(),
                ProjectDatabase.class,"Logged users database")
                .fallbackToDestructiveMigration()
                .build();
    }

    return database;
}



}
