package pg.eti.ksg.ProjektInzynierski.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.Invitations;
import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.Routes;
import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.UserWithFriends;
import pg.eti.ksg.ProjektInzynierski.DatabaseEntities.UserWithInvitations;

@Dao
public interface InvitationsDao {
    @Insert
    public void insert(Invitations invitation);

    @Delete
    public void delete(Invitations invitation);

    @Query("SELECT * FROM Users WHERE user_login =:login")
    LiveData<UserWithInvitations> getInvitations(String login);

    @Query("SELECT * FROM invitations WHERE invitation_login =:login")
    Invitations getInvitationFrom(String login);

}
