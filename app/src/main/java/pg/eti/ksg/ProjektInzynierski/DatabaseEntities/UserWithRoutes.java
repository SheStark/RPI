package pg.eti.ksg.ProjektInzynierski.DatabaseEntities;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class UserWithRoutes {
    public void setUser(Users user) {
        this.user = user;
    }

    public void setRoutes(List<Routes> routes) {
        this.routes = routes;
    }

    @Embedded
    private Users user;
    @Relation(
            parentColumn = "user_login",
            entityColumn = "route_Id",
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
