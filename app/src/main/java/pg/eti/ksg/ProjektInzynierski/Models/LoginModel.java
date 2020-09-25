package pg.eti.ksg.ProjektInzynierski.Models;

public class LoginModel {
    private String login;
    private String password;

    public LoginModel(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
