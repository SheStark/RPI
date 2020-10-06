package pg.eti.ksg.ProjektInzynierski.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.Routes;
import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.UserFriends;
import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.UserInvitations;

@Dao
public interface UserInvitationsDao {
    @Insert
    public void insert(UserInvitations userInvitations);

    @Delete
    public void delete(UserInvitations userInvitations);

    @Query("SELECT * FROM userinvitations WHERE user_login =:userLogin AND invitation_login=:friendLogin")
    UserInvitations getUserInvitation(String userLogin, String friendLogin);

    @Query("SELECT * FROM userinvitations WHERE invitation_login =:invitationLogin")
    List<UserInvitations> getInvitationsFrom(String invitationLogin);


}
