package authentication;

public class Login {
    private String username;
    private String password;

    public Login() {

    }

    public Login(String username, String password) {
        this.username = username;
        this.password = password;
    }

    String getPassword() {
        return password;
    }

    String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
