package lobby.gameroom;

import authentication.User;
import common.AdditionalRules;
import common.Event;
import common.EventHandler;

import java.util.List;
import java.util.UUID;

/**
 * interface defining one singular game room in lobby
 */
public interface GameRoom {
    void join(User user);
    void leave(User user);

    UUID getGameRoomUUID();

    String getGameName();
    AdditionalRules getAdditionalRules();

    User getHost();

    List<User> getUsersInGame();

    /**
     * @return fires whenever host requested a game abort
     */
    Event<GameRoom> getGameAbortedEvent();

    /**
     * @return fires whenever non-host player joins or leaves the game
     */
    Event<GameRoom> getPlayerListChangedEvent();
}
