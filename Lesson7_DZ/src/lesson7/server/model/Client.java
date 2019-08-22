package lesson7.server.model;

public class Client {
    private String login;
    private String pass;
    private String nick;

    public Client() {
    }

    public Client(String login, String pass, String nick) {
        this.login = login;
        this.pass = pass;
        this.nick = nick;
    }

    public static Client from(String[] creds) {
        return new Client(creds[0], creds[1], creds[2]);
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }
}
