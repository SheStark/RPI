package pg.eti.ksg.ProjektInzynierski.DatabaseEntities;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class UserWithRoutes {
    @Embedded
    private Users user;
    @Relation(
            parentColumn = "userLogin",
            entityColumn = "routeId",
            associateBy = @Junction(UserRoutes.class)
    )
    private List<Routes> routes;

    public Users getUser() {
        return user;
    }

    public List<Routes> getRoutes() {
        return routes;
    }
}
