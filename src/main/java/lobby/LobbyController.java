package lobby;

import authentication.User;
import authentication.UserService;
import common.UserSpecificMessageController;
import lobby.dto.CreateGameLobbyMessage;
import lobby.dto.GameAddedLobbyInfo;
import lobby.dto.JoinGameLobbyMessage;
import lobby.dto.UserSpecificMessage;
import lobby.gameroom.GameRoom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
public class LobbyController extends UserSpecificMessageController {

    private final GameLobby gameLobby;

    @GetMapping("/lobby")
    public String goToLobby(
            @ModelAttribute("apiKey") UUID apiKey
            , Model model) {

        List<GameAddedLobbyInfo> gamesInLobby = gameLobby.getLobbyGames().values().stream().map(this::Map).collect(Collectors.toList());

        model.addAttribute("api_key", apiKey);
        model.addAttribute("games", gamesInLobby);

        return "lobby";
    }

    @PostMapping("/lobby/join-game")
    public String joinGame(
            @ModelAttribute("join_game_params") JoinGameLobbyMessage message
            , Model model) {

        User user = FindRegisteredUserOrThrow(message);

        GameRoom joinedGameRoom = gameLobby.getLobbyGames().get(message.getJoinedGameUUID());

        joinedGameRoom.join(user);

        return formatGameRoomRedirectUrl(user, joinedGameRoom);
    }

    @PostMapping("/lobby/create-game")
    public String createGame(
            @ModelAttribute("create_game_params") CreateGameLobbyMessage message
            , Model model) {

        User user = FindRegisteredUserOrThrow(message);

        GameRoom gameRoom = gameLobby.createGameRoom(user, message.getGameRoomName());

        return formatGameRoomRedirectUrl(user, gameRoom);
    }

    @Autowired
    public LobbyController(
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

    private String formatGameRoomRedirectUrl(User user, GameRoom room){
        return "redirect:room?apiKey=" + user.getUserApiKey() + "&gameUUID=" + room.getGameRoomUUID();
    }

    private GameAddedLobbyInfo Map(GameRoom addedRoom){
        GameAddedLobbyInfo params = new GameAddedLobbyInfo();

        params.setRoomName(addedRoom.getGameName());
        params.setPlayerCount(addedRoom.getUsersInGame().size());
        params.setHostName(addedRoom.getHost().getUserName());
        params.setGameUUID(addedRoom.getGameRoomUUID());

        return params;
    }
}
