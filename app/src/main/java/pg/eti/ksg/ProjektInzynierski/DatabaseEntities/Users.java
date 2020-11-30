package pg.eti.ksg.ProjektInzynierski.DatabaseEntities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import java.sql.Date;

@Entity
public class Users {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "user_login")
    private String login;
    private String name;
    private String surname;
    private String email;
    private String city;
    private Date birth;

    public Users(@NotNull String login, String name, String surname, String email) {
        this.login = login;
        this.name = name;
        this.surname = surname;
        this.email = email;
    }

    public Users() {
        login = "";
    }

    @NotNull
    public String getLogin() {
        return login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public void setLogin(@NonNull String login) {
        this.login = login;
    }
}
