package game.communication;

import game.Game;
import game.GameManager;
import game.Player;
import game.dto.AssasinateGameAction;
import game.dto.GameAction;
import game.dto.NominateTeamGameAction;
import game.dto.VoteGameAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class IncomingGameCommunicationAPI {

    private final GameManager gameManager;

    @Autowired
    public IncomingGameCommunicationAPI(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @MessageMapping("/game/action/nominate")
    public void nominateTeam(NominateTeamGameAction gameAction){
        Player player = findPlayer(gameAction);

        player.getGameActionReceivedEvent().Invoke(gameAction);
    }

    @MessageMapping("/game/action/vote")
    public void vote(VoteGameAction gameAction){
        Player player = findPlayer(gameAction);

        player.getGameActionReceivedEvent().Invoke(gameAction);
    }

    @MessageMapping("/game/action/assassinate")
    public void assassinate(AssasinateGameAction gameAction){
        Player player = findPlayer(gameAction);

        player.getGameActionReceivedEvent().Invoke(gameAction);
    }

    private Player findPlayer(GameAction gameAction){
        if(!gameManager.getGameMap().containsKey(gameAction.getGameUUID()))
            throw new SecurityException("game doesnt exist");

        Game game = gameManager.getGameMap().get(gameAction.getGameUUID());

        if(!game.getPlayerCollection().getPlayerList().containsKey(gameAction.getUserApiKey()))
            throw new SecurityException("api key not found");

        return game.getPlayerCollection().getPlayerList().get(gameAction.getUserApiKey());
    }

}
