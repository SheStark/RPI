package pg.eti.ksg.ProjektInzynierski.DatabaseEntities;

import androidx.annotation.NonNull;
import androidx.room.Entity;

@Entity(primaryKeys = {"invitationLogin","userLogin"})
public class UserInvitations {

    @NonNull
    private String invitationLogin;
    @NonNull
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
