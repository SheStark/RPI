package pg.eti.ksg.ProjektInzynierski.Models;

public class RegisterModel {
    private String login;
    private String name;
    private String surname;
    private String email;
    private String password;

    public RegisterModel(String login, String name, String surname, String email, String password) {
        this.login = login;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
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

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
