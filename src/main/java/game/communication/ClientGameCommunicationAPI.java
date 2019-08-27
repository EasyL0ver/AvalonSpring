package game.communication;

import game.Player;
import game.dto.GamePhaseInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
public class ClientGameCommunicationAPI {

    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public ClientGameCommunicationAPI(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public void AnnouncePhaseChanged(Collection<Player> players, GamePhaseInfo info){
        for(Player player : players)
            simpMessagingTemplate.convertAndSend("/topic/game/user-specific/" + player.getUserUUID(), info);
    }

}
