package authentication;

public class Login {
    private final String Username;
    private final String Password;

    public Login(String username, String password){
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
