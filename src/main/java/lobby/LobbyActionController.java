package lobby;

import authentication.User;
import authentication.UserService;
import common.UserSpecificMessageController;
import lobby.dto.CreateGameLobbyMessage;
import lobby.dto.JoinGameLobbyMessage;
import lobby.dto.UserSpecificMessage;
import lobby.gameroom.GameRoom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletResponse;

@Controller
public class LobbyActionController extends UserSpecificMessageController {

    private final GameLobby gameLobby;

    @PostMapping("/lobby/join_game")
    public String joinGame(
            @ModelAttribute("join_game_params") JoinGameLobbyMessage message
            , Model model) {

        User user = FindRegisteredUserOrThrow(message);

        GameRoom joinedGameRoom = gameLobby.getLobbyGames().get(message.getJoinedGameUUID());

        joinedGameRoom.join(user);

        ConfigureGameModel(model, user, joinedGameRoom);

        return "game_room";
    }

    @PostMapping("/lobby/create_game")
    public String createGame(
            @ModelAttribute("create_game_params") CreateGameLobbyMessage message
            , Model model) {

        User user = FindRegisteredUserOrThrow(message);

        GameRoom gameRoom = gameLobby.createGameRoom(user, message.getGameRoomName());

        ConfigureGameModel(model, user, gameRoom);

        return "game_room";
    }




    public String logout(HttpServletResponse response) {

        return "todo";
    }

    @Autowired
    public LobbyActionController(
            UserService userService
            , GameLobby gameLobby)  {
        super(userService);

        this.gameLobby = gameLobby;
    }

    private User FindRegisteredUserOrThrow(UserSpecificMessage message){
        User user = FindUser(message);

        if(user == null)
            throw new SecurityException("user with api key: " + message.getUserApiKey() + " not found");

        return user;
    }

    private void ConfigureGameModel(Model model, User user, GameRoom room){
        model.addAttribute("host_name", room.getHost().getUserName());
        model.addAttribute("room_name", room.getGameName());
        model.addAttribute("room_uuid", room.getGameRoomUUID());
        model.addAttribute("api_key", user.getUserApiKey());
    }
}
