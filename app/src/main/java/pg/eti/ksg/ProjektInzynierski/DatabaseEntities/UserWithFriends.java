package pg.eti.ksg.ProjektInzynierski.DatabaseEntities;

import androidx.lifecycle.LiveData;
import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class UserWithFriends {
    @Embedded
    private Users user;

    public void setUser(Users user) {
        this.user = user;
    }

    public void setFriends(List<Friends> friends) {
        this.friends = friends;
    }

    @Relation(
            associateBy = @Junction(UserFriends.class),
            parentColumn = "user_login",
            entityColumn = "friend_login"

    )
    private List<Friends> friends;

    public Users getUser() {
        return user;
    }

    public List<Friends> getFriends() {
        return friends;
    }
}
