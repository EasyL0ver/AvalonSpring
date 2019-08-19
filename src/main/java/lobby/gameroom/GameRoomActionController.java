package lobby.gameroom;

import authentication.User;
import authentication.UserService;
import common.UserSpecificMessageController;
import lobby.GameLobby;
import lobby.dto.UserSpecificMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class GameRoomActionController extends UserSpecificMessageController {

    private final GameLobby lobby;

    @Autowired
    public GameRoomActionController(UserService userService, GameLobby lobby) {
        super(userService);
        this.lobby = lobby;
    }

    @PostMapping("/lobby/room/start_game")
    public String startGame(UserSpecificMessage message) {
        User user = FindUser(message);

        if(user == null)
            throw new SecurityException("user doesn't exist");

        //todo throw if invoking user is not an admin
        //if(user.getUserApiKey() != )


        return "game";
    }

    @PostMapping("/lobby/room/abort")
    public String abortGame(UserSpecificMessage message) {

        return "lobby";
    }
}
