package lobby;

import authentication.User;

import java.util.List;

public interface LobbyGame {
    void join(User user);

    List<User> getUsersInGame();
}
