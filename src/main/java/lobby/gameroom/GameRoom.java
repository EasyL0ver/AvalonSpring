package lobby.gameroom;

import authentication.User;
import common.Event;
import common.EventHandler;

import java.util.List;
import java.util.UUID;

public interface GameRoom {
    void join(User user);
    void leave(User user);

    UUID getGameRoomUUID();

    String getGameName();

    User getHost();

    List<User> getUsersInGame();

    Event<GameRoom> getGameAbortedEvent();
    Event<GameRoom> getPlayerListChangedEvent();
}
