package lobby;

import authentication.UserService;
import common.EventHandler;
import lobby.dto.GameAddedLobbyInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class LobbyInformationController implements EventHandler<GameAddedLobbyInfo> {

    private SimpMessagingTemplate simpMessagingTemplate;
    private GameLobby gameLobby;

    @Autowired
    public LobbyInformationController(SimpMessagingTemplate simpMessagingTemplate, GameLobby gameLobby) {
        this.gameLobby = gameLobby;
        this.simpMessagingTemplate = simpMessagingTemplate;

        this.gameLobby.setOnGameAddedLobbyHandler(this);

    }

    @Override
    public void Handle(GameAddedLobbyInfo params) {
        simpMessagingTemplate.convertAndSend("/topic/lobby/added", params);
    }


}
