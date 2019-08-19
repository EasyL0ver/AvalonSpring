package lobby.dto;

import java.util.UUID;

public class UserSpecificMessage {
    private UUID userApiKey;

    public UserSpecificMessage() {}

    public UserSpecificMessage(UUID appKey){
        this.userApiKey = appKey;
    }

    public UUID getUserApiKey() {
        return userApiKey;
    }

    public void setUserApiKey(UUID userApiKey) {
        this.userApiKey = userApiKey;
    }
}
