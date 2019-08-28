package lobby.gameroom;

import authentication.User;
import authentication.UserService;
import common.UserSpecificMessageController;
import lobby.GameLobby;
import lobby.dto.GameRoomMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
public class GameRoomController extends UserSpecificMessageController {

    private final GameLobby lobby;

    @Autowired
    public GameRoomController(UserService userService, GameLobby lobby) {
        super(userService);
        this.lobby = lobby;
    }

    @GetMapping("/lobby/room")
    public String goToRoom(
            @ModelAttribute("apiKey") UUID apiKey
            , @ModelAttribute("gameUUID") UUID gameUUID
            , Model model){
        GameRoom room = lobby.getLobbyGames().get(gameUUID);

        List<String> userNames = UserNamesInGame(gameUUID);

        model.addAttribute("host_name", room.getHost().getUserName());
        model.addAttribute("room_name", room.getGameName());
        model.addAttribute("room_uuid", room.getGameRoomUUID());
        model.addAttribute("api_key", apiKey);
        model.addAttribute("users", userNames);

        return "game_room";
    }

    @PostMapping("/lobby/room/start-game")
    public String startGame(GameRoomMessage message, Model model) {
        GameRoom gameRoom = FindGameRoom(message);
        User user = FindUser(message);

        if(user == null)
            throw new SecurityException("user doesn't exist");
        if(gameRoom == null)
            throw new SecurityException("game room doesn't exist");
        if(user != gameRoom.getHost())
            throw new SecurityException("only host can start the game");

        lobby.startGame(gameRoom);

        String gameURL = "redirect:/game?apiKey=" + user.getUserApiKey() + "&gameUUID=" + gameRoom.getGameRoomUUID();

        //todo propagate url to clients

        return gameURL;
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

        return "redirect:/lobby?apiKey=" + user.getUserApiKey();
    }

    private GameRoom FindGameRoom(GameRoomMessage gameRoomMessage){
        UUID gameUUId = gameRoomMessage.getGameUUID();
        if(!lobby.getLobbyGames().containsKey(gameUUId))
            return null;
        return lobby.getLobbyGames().get(gameUUId);
    }

    private List<String> UserNamesInGame(UUID gameRoomUUID){
        GameRoom gameRoom = lobby.getLobbyGames().get(gameRoomUUID);

        return gameRoom.getUsersInGame().stream().map(User::getUserName).collect(Collectors.toList());
    }
}
