package lobby.dto;

import lobby.dto.UserSpecificMessage;

import java.util.UUID;

public class JoinGameLobbyMessage extends UserSpecificMessage {
    private UUID joinedGameUUID;

    public UUID getJoinedGameUUID() {
        return joinedGameUUID;
    }

    public void setJoinedGameUUID(UUID joinedGameUUID) {
        this.joinedGameUUID = joinedGameUUID;
    }
}
