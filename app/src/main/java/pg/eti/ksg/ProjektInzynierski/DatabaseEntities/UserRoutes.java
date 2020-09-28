package pg.eti.ksg.ProjektInzynierski.DatabaseEntities;

import androidx.annotation.NonNull;
import androidx.room.Entity;

@Entity(primaryKeys = {"userLogin","routeId"})
public class UserRoutes {

    @NonNull
    private String userLogin;
    @NonNull
    private Long routeId;

    public UserRoutes(String userLogin, Long routeId) {
        this.userLogin = userLogin;
        this.routeId = routeId;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public Long getRouteId() {
        return routeId;
    }
}
