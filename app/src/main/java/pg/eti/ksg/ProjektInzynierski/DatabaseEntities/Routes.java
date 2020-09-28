package pg.eti.ksg.ProjektInzynierski.DatabaseEntities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.sql.Date;

@Entity
public class Routes {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private Long id;

    @ColumnInfo(name = "friend_login")
    private String friendLogin;

    @ColumnInfo(name="is_dangerous")
    private boolean isDangerous;

    @ColumnInfo(name="start_date")
    private Date startDate;


    public Routes(String friendLogin, boolean isDangerous, Date startDate) {
        this.friendLogin = friendLogin;
        this.isDangerous = isDangerous;
        this.startDate = startDate;
    }


    public Long getId() {
        return id;
    }

    public String getFriendLogin() {
        return friendLogin;
    }

    public boolean isDangerous() {
        return isDangerous;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFriendLogin(String friendLogin) {
        this.friendLogin = friendLogin;
    }

    public void setDangerous(boolean dangerous) {
        isDangerous = dangerous;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
}
