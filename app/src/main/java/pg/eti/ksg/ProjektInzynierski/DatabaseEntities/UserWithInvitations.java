package pg.eti.ksg.ProjektInzynierski.DatabaseEntities;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class UserWithInvitations {
    @Embedded
    private Users user;

    public void setUser(Users user) {
        this.user = user;
    }

    public void setInvitations(List<Invitations> invitations) {
        this.invitations = invitations;
    }

    @Relation(
            parentColumn = "user_login",
            entityColumn = "invitation_login",
            associateBy = @Junction(UserInvitations.class)
    )
    private List<Invitations> invitations;

    public Users getUser() {
        return user;
    }

    public List<Invitations> getInvitations() {
        return invitations;
    }
}
