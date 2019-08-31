package lobby.gameroom;

import authentication.User;
import common.EventHandler;
import lobby.GameLobby;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * controller used for asynchronous notification events broadcasted to users concerning game room
 */
@RestController
public class GameRoomInformationController {
    private final GameLobby gameLobby;
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public GameRoomInformationController(GameLobby gameLobby, SimpMessagingTemplate simpMessagingTemplate) {
        this.gameLobby = gameLobby;
        this.simpMessagingTemplate = simpMessagingTemplate;

        //todo detach handlers
        gameLobby.getGameAddedEvent().AttachHandler(new EventHandler<GameRoom>() {
            @Override
            public void Handle(GameRoom params) {
                params.getGameAbortedEvent().AttachHandler(new EventHandler<GameRoom>() {
                    @Override
                    public void Handle(GameRoom params) {
                        simpMessagingTemplate.convertAndSend("/topic/lobby/room/" + params.getGameRoomUUID() +"/abort", "aborted");
                    }
                });

                params.getPlayerListChangedEvent().AttachHandler(new EventHandler<GameRoom>() {
                    @Override
                    public void Handle(GameRoom params) {

                        List<String> userInfo = params.getUsersInGame().stream().map(User::getUserName).collect(Collectors.toList());

                        simpMessagingTemplate.convertAndSend("/topic/lobby/room/" + params.getGameRoomUUID() +"/users", userInfo);
                    }
                });
            }
        });


        gameLobby.getGameStartedEvent().AttachHandler(new EventHandler<GameRoom>() {
            @Override
            public void Handle(GameRoom params) {
                User host = params.getHost();
                List<User> nonHostUsers = params.getUsersInGame().stream().filter(u -> u != host).collect(Collectors.toList());
                for(User user : nonHostUsers){
                    simpMessagingTemplate.convertAndSend("/topic/lobby/room/" + params.getGameRoomUUID() + "/started/" + user.getUserApiKey(), "started");

                }


            }
        });
    }

    @GetMapping("lobby/room/all")
    public List<String> UserNamesInGame(String gameRoomUUID){
        UUID actualId = UUID.fromString(gameRoomUUID);
        GameRoom gameRoom = gameLobby.getLobbyGames().get(actualId);

        return gameRoom.getUsersInGame().stream().map(User::getUserName).collect(Collectors.toList());
    }
}
