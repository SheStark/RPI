package pg.eti.ksg.ProjektInzynierski.DatabaseEntities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(primaryKeys = {"user_login","route_Id"})
public class UserRoutes {

    @NonNull
    @ColumnInfo(name = "user_login")
    private String userLogin;
    @NonNull
    @ColumnInfo(name = "route_Id")
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
