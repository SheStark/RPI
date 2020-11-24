package pg.eti.ksg.ProjektInzynierski.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.Points;
import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.Routes;
import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.UserWithRoutes;

@Dao
public interface PointsDao {

    @Insert
    void insert(Points point);

    @Insert
    void insert(List<Points> point);

    @Update
    void update(Points point);

    @Delete
    void delete(Points point);


    @Query("SELECT * FROM Points WHERE route_id =:routeId ORDER BY point_date ASC")
    LiveData<List<Points>> getRoutePoints(Long routeId);

    @Query("DELETE FROM points WHERE route_id = :routeId")
    void deleteRoutesPoints(Long routeId);

    @Query("SELECT * FROM Points WHERE point_id = :id")
    Points getPoint(Long id);
}
