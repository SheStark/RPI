package pg.eti.ksg.ProjektInzynierski.DatabaseEntities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(primaryKeys = {"invitation_login","user_login"})
public class UserInvitations {

    @NonNull
    @ColumnInfo(name = "invitation_login")
    private String invitationLogin;
    @NonNull
    @ColumnInfo(name = "user_login")
    private String userLogin;

    public UserInvitations(String invitationLogin, String userLogin) {
        this.invitationLogin = invitationLogin;
        this.userLogin = userLogin;
    }

    public String getInvitationLogin() {
        return invitationLogin;
    }

    public String getUserLogin() {
        return userLogin;
    }
}
