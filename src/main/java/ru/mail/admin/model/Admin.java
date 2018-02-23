package ru.mail.admin.model;


/**
 * @author Denis Monich
 */
public class Admin {

    private String login;
    private String password;

    public Admin(String userEmail, String userPassword) {
        this.login = userEmail;
        this.password = userPassword;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
