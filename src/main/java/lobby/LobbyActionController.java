package lobby;

import authentication.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LobbyActionController {

    private GameLobby lobby;
    private final UserService userService;

    @Autowired
    public LobbyActionController(
            UserService userService) {

        this.userService = userService;
    }


}
