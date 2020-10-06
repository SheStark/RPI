package pg.eti.ksg.ProjektInzynierski.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.Routes;
import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.UserRoutes;

@Dao
public interface UserRoutesDao {
    @Insert
    public void insert(UserRoutes userRoutes);

    @Delete
    public void delete(UserRoutes userRoutes);

    @Query("SELECT * FROM userRoutes")
    List<UserRoutes> getUserRoutes();

    @Query("SELECT * FROM userroutes WHERE user_login = :login AND route_Id = :id")
    UserRoutes getUserRoute(String login,Long id);

    @Query("SELECT * FROM userroutes WHERE route_Id = :id")
    List<UserRoutes> getUserRoutesByRouteId(Long id);

    @Query("DELETE FROM userroutes WHERE route_Id = :id")
    void deleteByRouteId(Long id);

    @Query("DELETE FROM userroutes WHERE user_login =:login")
    void deleteByUserLogin(String login);
}
