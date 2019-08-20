package lobby;

import authentication.UserService;
import common.EventHandler;
import lobby.dto.GameAddedLobbyInfo;
import lobby.gameroom.GameRoom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class LobbyInformationController{
    private SimpMessagingTemplate simpMessagingTemplate;
    private GameLobby gameLobby;

    @Autowired
    public LobbyInformationController(SimpMessagingTemplate simpMessagingTemplate, GameLobby gameLobby) {
        this.gameLobby = gameLobby;
        this.simpMessagingTemplate = simpMessagingTemplate;

        EventHandler<GameRoom> gameAddedHandler = new EventHandler<GameRoom>() {
            @Override
            public void Handle(GameRoom addedRoom) {
                GameAddedLobbyInfo params = new GameAddedLobbyInfo();

                params.setRoomName(addedRoom.getGameName());
                params.setPlayerCount(1);
                params.setHostName(addedRoom.getHost().getUserName());

                simpMessagingTemplate.convertAndSend("/topic/lobby/added", params);
            }
        };

        EventHandler<GameRoom> gameRemovedHandler = new EventHandler<GameRoom>() {
            @Override
            public void Handle(GameRoom params) {
                simpMessagingTemplate.convertAndSend("/topic/lobby/removed", params.getGameRoomUUID());
            }
        };

        gameLobby.getGameAddedEvent().AttachHandler(gameAddedHandler);
        gameLobby.getGameAbortedEvent().AttachHandler(gameRemovedHandler);
        gameLobby.getGameStartedEvent().AttachHandler(gameRemovedHandler);
    }
}
