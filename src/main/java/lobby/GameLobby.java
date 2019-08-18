package lobby;

import authentication.User;

import java.util.List;

public interface GameLobby {
    List<LobbyGame> getLobbyGames();

    void createGame(User userCreatingGame);
}
