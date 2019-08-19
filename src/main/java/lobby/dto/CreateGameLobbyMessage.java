package lobby.dto;

import lobby.dto.UserSpecificMessage;

public class CreateGameLobbyMessage extends UserSpecificMessage {
    private String gameRoomName;

    public CreateGameLobbyMessage() {

    }

    public String getGameRoomName() {
        return gameRoomName;
    }

    public void setGameRoomName(String gameRoomName) {
        this.gameRoomName = gameRoomName;
    }
}
