package pg.eti.ksg.ProjektInzynierski.DatabaseEntities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import java.sql.Date;

@Entity
public class Messages {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private Long id;

    @ColumnInfo(name = "user_login")
    private String userLogin;
    @ColumnInfo(name = "friend_login")
    private String friendLogin;
    @ColumnInfo(name = "is_user_message")
    private boolean isUserMessage;
    @ColumnInfo(name = "send_date")
    private Date date;

    private String text;


    public Messages() {
    }

    public Messages(String userLogin, String friendLogin, boolean isUserMessage, Date date, String text) {
        this.userLogin = userLogin;
        this.friendLogin = friendLogin;
        this.isUserMessage = isUserMessage;
        this.date = date;
        this.text = text;
    }

    public Long getId() {
        return id;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public String getFriendLogin() {
        return friendLogin;
    }

    public boolean isUserMessage() {
        return isUserMessage;
    }

    public Date getDate() {
        return date;
    }

    public String getText() {
        return text;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public void setFriendLogin(String friendLogin) {
        this.friendLogin = friendLogin;
    }

    public void setUserMessage(boolean userMessage) {
        isUserMessage = userMessage;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setText(String text) {
        this.text = text;
    }
}
