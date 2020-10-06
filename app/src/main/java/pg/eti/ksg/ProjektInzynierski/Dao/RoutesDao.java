package pg.eti.ksg.ProjektInzynierski.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Ignore;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.Routes;
import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.UserWithRoutes;

@Dao
public interface RoutesDao {

    @Insert
    public void insert(Routes route);

    @Update
    public void update(Routes route);

    @Delete
    public void delete(Routes route);

    @Transaction
    @Query("SELECT * FROM Users WHERE user_login = :login")
    LiveData<UserWithRoutes> getUserRoutes(String login);

    @Query("SELECT * FROM Routes WHERE route_Id = :id")
    Routes getRoute(Long id);

    @Query("SELECT * FROM Routes")
    List<Routes> getRoutes();
}
