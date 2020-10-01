package pg.eti.ksg.ProjektInzynierski.Models;

public class LoginModel {
    private String login;
    private String password;
    private String token;

    public LoginModel(String login, String password,String token) {
        this.login = login;
        this.password = password;
        this.token = token;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getToken() {
        return token;
    }
}
