package lobby;

import authentication.User;
import common.Event;
import lobby.gameroom.GameRoom;

import java.util.Map;
import java.util.UUID;

/**
 * interface defining game lobby
 */
public interface GameLobby {
    Map<UUID, GameRoom> getLobbyGames();

    GameRoom createGameRoom(User userCreatingGame, String gameRoomName);

    void startGame(GameRoom room);
    void abortGame(GameRoom room);

    Event<GameRoom> getGameAddedEvent();
    Event<GameRoom> getGameStartedEvent();
    Event<GameRoom> getGameAbortedEvent();
}
