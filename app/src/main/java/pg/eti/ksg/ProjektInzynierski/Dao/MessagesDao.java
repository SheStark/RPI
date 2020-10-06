package pg.eti.ksg.ProjektInzynierski.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.sql.Date;
import java.util.List;

import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.Messages;
import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.Routes;

@Dao
public interface MessagesDao {
    @Insert
    public void insert(Messages message);

    @Insert
    public void insert(List<Messages> message);

    @Update
    public void update(Messages message);

    @Delete
    public void delete(Messages message);

    @Query("SELECT * FROM messages WHERE user_login = :userLogin AND friend_login = :friendLogin ORDER BY send_date DESC")
    LiveData<Messages> getMessages(String userLogin,String friendLogin);

    @Query("SELECT send_date FROM messages WHERE user_login = :userLogin AND friend_login = :friendLogin ORDER BY send_date DESC LIMIT 1 ")
    Date getLastMessageDate(String userLogin,String friendLogin);

    //TO DO
    @Query("DELETE FROM messages WHERE user_login =:userLogin AND friend_login =:friendLogin")
    void deleteUserMessages(String userLogin, String friendLogin);

}
