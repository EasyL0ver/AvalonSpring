package game.communication;

import game.Game;
import game.GameManager;
import game.Player;
import game.dto.gameActions.AssasinateGameAction;
import game.dto.gameActions.GameAction;
import game.dto.gameActions.NominateTeamGameAction;
import game.dto.gameActions.VoteGameAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

/**
 * Controller that handles incoming game messages from the client-side
 */
@Controller
public class IncomingGameCommunicationAPI {

    private final GameManager gameManager;

    @Autowired
    public IncomingGameCommunicationAPI(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    /**
     * Nominate game team as long as it's active players turn
     * @param nominateTeamGameAction parameters containing nominated players ids and active player id
     */
    @MessageMapping("/game/action/nominate")
    public void nominateTeam(NominateTeamGameAction nominateTeamGameAction){
        Player player = findPlayer(nominateTeamGameAction);

        player.getGameActionReceivedEvent().Invoke(nominateTeamGameAction);
    }

    /**
     * Vote for either game team or mission result depending on VoteGameAction implementation used
     * @param voteGameAction vote parameters
     */
    @MessageMapping("/game/action/vote")
    public void vote(VoteGameAction voteGameAction){
        Player player = findPlayer(voteGameAction);

        player.getGameActionReceivedEvent().Invoke(voteGameAction);
    }

    /**
     * Assassinate player as long as it's assasination player and active player is an assassin
     * @param assassinateGameAction
     */
    @MessageMapping("/game/action/assassinate")
    public void assassinate(AssasinateGameAction assassinateGameAction){
        Player player = findPlayer(assassinateGameAction);

        player.getGameActionReceivedEvent().Invoke(assassinateGameAction);
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
