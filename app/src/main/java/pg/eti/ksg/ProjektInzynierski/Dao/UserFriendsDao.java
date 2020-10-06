package pg.eti.ksg.ProjektInzynierski.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.Routes;
import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.UserFriends;

@Dao
public interface UserFriendsDao {
    @Insert
    public void insert(UserFriends userFriends);

    @Delete
    public void delete(UserFriends userFriends);

    @Query("SELECT * FROM userfriends WHERE user_login =:userLogin AND friend_login=:friendLogin")
    UserFriends getUserFriend(String userLogin,String friendLogin);

}
