package lobby.gameroom;

import authentication.User;
import authentication.UserService;
import common.UserSpecificMessageController;
import lobby.GameLobby;
import lobby.dto.GameRoomMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.UUID;

@Controller
public class GameRoomActionController extends UserSpecificMessageController {

    private final GameLobby lobby;

    @Autowired
    public GameRoomActionController(UserService userService, GameLobby lobby) {
        super(userService);
        this.lobby = lobby;
    }

    @PostMapping("/lobby/room/start_game")
    public String startGame(GameRoomMessage message) {
        GameRoom gameRoom = FindGameRoom(message);
        User user = FindUser(message);

        if(user == null)
            throw new SecurityException("user doesn't exist");
        if(gameRoom == null)
            throw new SecurityException("game room doesn't exist");
        if(user != gameRoom.getHost())
            throw new SecurityException("only host can start the game");

        lobby.startGame(gameRoom);

        return "game";
    }

    @PostMapping("/lobby/room/abort")
    public String abortGame(GameRoomMessage message, Model model) {
        GameRoom gameRoom = FindGameRoom(message);
        User user = FindUser(message);

        if(user == null)
            throw new SecurityException("user doesn't exist");
        if(gameRoom == null)
            throw new SecurityException("game room doesn't exist");

        gameRoom.leave(user);

        if(user == gameRoom.getHost())
            lobby.abortGame(gameRoom);

        model.addAttribute("api_key", user.getUserApiKey());
        return "lobby";
    }

    private GameRoom FindGameRoom(GameRoomMessage gameRoomMessage){
        UUID gameUUId = gameRoomMessage.getGameUUID();
        if(!lobby.getLobbyGames().containsKey(gameUUId))
            return null;
        return lobby.getLobbyGames().get(gameUUId);
    }
}
