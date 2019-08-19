package authentication;

import java.util.UUID;

public class BasicUser implements User {

    private final UUID apiKey;
    private final String userName;

    @Override
    public UUID getUserApiKey() {
        return apiKey;
    }

    public BasicUser(Login credentials){
        this.apiKey = UUID.randomUUID();
        this.userName = credentials.getUsername();
    }

    @Override
    public String getUserName() {
        return userName;
    }
}
