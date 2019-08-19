package lobby;

import authentication.User;
import common.Event;
import lobby.dto.GameAddedLobbyInfo;
import lobby.gameroom.GameRoom;

import java.util.List;

public interface GameLobby {
    List<GameRoom> getLobbyGames();

    GameRoom createGameRoom(User userCreatingGame, String gameRoomName);

    Event<GameAddedLobbyInfo> getGameAddedEvent();
}
