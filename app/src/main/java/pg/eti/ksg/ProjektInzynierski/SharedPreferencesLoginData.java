package pg.eti.ksg.ProjektInzynierski;

public class SharedPreferencesLoginData {

    private String login;
    private String name;
    private String surname;
    //private String password; //tylko do celów testowych, po implementacji serwra pole to jest niepotrzebne

    public SharedPreferencesLoginData(String login, String name, String surname) //String password)
    {
        this.login=login;
        this.name=name;
        //this.password=password;
        this.surname=surname;
    }
    public String getLogin() {
        return login;
    }

    public String getName() {
        return name;
    }

   // public String getPassword() {
    //    return password;
    //}

    public String getSurname() {
        return surname;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setName(String name) {
        this.name = name;
    }

   // public void setPassword(String password) {
    //    this.password = password;
    //}

    public void setSurname(String surname) {
        this.surname = surname;
    }
}
