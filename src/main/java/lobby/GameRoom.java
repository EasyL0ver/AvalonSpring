package lobby;

import authentication.User;

import java.util.List;
import java.util.UUID;

public interface GameRoom {
    void join(User user);

    UUID getGameRoomUUID();

    String getGameName();
    String getHostName();

    List<User> getUsersInGame();
}
