package lobby.gameroom;

import authentication.User;

import java.util.List;
import java.util.UUID;

public interface GameRoom {
    void join(User user);
    void leave(User user);

    UUID getGameRoomUUID();

    String getGameName();

    User getHost();

    List<User> getUsersInGame();
}
