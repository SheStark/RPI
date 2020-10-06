package pg.eti.ksg.ProjektInzynierski.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.Friends;
import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.Routes;
import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.UserWithFriends;

@Dao
public interface FriendsDao {
    @Insert
    public void insert(Friends friend);

    @Update
    public void update(Friends friend);

    @Delete
    public void delete(Friends friend);

    @Query("SELECT * FROM Users WHERE user_login =:login")
    LiveData<UserWithFriends> getFriends(String login);

    @Query("SELECT * FROM Friends WHERE friend_login = :login")
    Friends isFriendSync(String login);

}
