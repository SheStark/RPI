package pg.eti.ksg.ProjektInzynierski.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.UserWithFriends;
import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.UserWithRoutes;
import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.Users;

@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Users user);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Users> users);

    @Update
    void update(Users user);

    @Delete
    void delete(Users user);

    @Transaction
    @Query("SELECT * FROM Users WHERE user_login = :login")
    LiveData<UserWithFriends> getFriends(String login);

    @Query("SELECT * FROM Users WHERE user_login = :login")
    LiveData<Users> getUser(String login);

    @Query("SELECT * FROM Users")
    LiveData<List<Users>> getAllUsers();

    @Query("SELECT * FROM Users WHERE user_login = :login")
    Users getUserSync(String login);

    @Transaction
    @Query("SELECT * FROM Users WHERE user_login = :login")
    UserWithFriends getFriendsSync(String login);



}
