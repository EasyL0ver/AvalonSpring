package lobby.dto;

import java.util.UUID;

public class GameRoomMessage extends UserSpecificMessage {
    private UUID gameUUID;

    public UUID getGameUUID() {
        return gameUUID;
    }

    public void setGameUUID(UUID gameUUID) {
        this.gameUUID = gameUUID;
    }
}
