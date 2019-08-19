package lobby;

import authentication.User;
import common.EventHandler;
import lobby.dto.GameAddedLobbyInfo;

import java.util.List;

public interface GameLobby {
    List<GameRoom> getLobbyGames();

    GameRoom createGameRoom(User userCreatingGame, String gameRoomName);

    void setOnGameAddedLobbyHandler(EventHandler<GameAddedLobbyInfo> onGameAddedLobbyHandler);

}
