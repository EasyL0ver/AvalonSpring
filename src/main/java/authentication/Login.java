package authentication;

public class Login {
    private final String Username;
    private final String Password;

    //todo better exception
    public Login(String username, String password) throws Exception {
        Username = username;
        Password = password;
    }

    public String getPassword() {
        return Password;
    }

    public String getUsername() {
        return Username;
    }
}
