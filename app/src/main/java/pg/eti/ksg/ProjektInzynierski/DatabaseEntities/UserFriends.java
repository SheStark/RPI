package pg.eti.ksg.ProjektInzynierski.DatabaseEntities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(primaryKeys = {"user_login","friend_login"})
public class UserFriends {

    @NonNull
    @ColumnInfo(name = "user_login")
    private String userLogin;
    @NonNull
    @ColumnInfo(name = "friend_login")
    private String friendLogin;

    public UserFriends(String userLogin, String friendLogin) {
        this.userLogin = userLogin;
        this.friendLogin = friendLogin;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public String getFriendLogin() {
        return friendLogin;
    }
}
