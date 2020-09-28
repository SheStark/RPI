package pg.eti.ksg.ProjektInzynierski.DatabaseEntities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Invitations {

    @PrimaryKey
    @NonNull
    private String login;

    private String name;

    private String surname;

    private String city;

    public Invitations() {
    }

    public Invitations(String login, String name, String surname, String city) {
        this.login = login;
        this.name = name;
        this.surname = surname;
        this.city = city;
    }

    public Invitations(String login, String name, String surname) {
        this.login = login;
        this.name = name;
        this.surname = surname;
    }

    public String getLogin() {
        return login;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
}
