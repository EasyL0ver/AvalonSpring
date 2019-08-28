package game.gameBuilder;

import game.*;
import game.communication.OutgoingGameCommunicationAPI;
import game.dto.GamePhaseType;
import game.gamePhases.PickTeamGamePhase;
import game.gamePhases.RevealPhase;
import game.gamePhases.VotePhase;
import game.gamePhases.VoteResultStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GamePhaseFactory {
    private final GameRulesProvider gameRulesProvider;
    private final OutgoingGameCommunicationAPI outgoingGameCommunicationAPI;

    @Autowired
    public GamePhaseFactory(GameRulesProvider gameRulesProvider, OutgoingGameCommunicationAPI outgoingGameCommunicationAPI) {
        this.gameRulesProvider = gameRulesProvider;
        this.outgoingGameCommunicationAPI = outgoingGameCommunicationAPI;
    }

    public GamePhase<Boolean> BuildVotePhase(PlayerCollection playerCollection, PlayerTeam playerTeam){
        List<Player> allPlayers = playerTeam.getPlayers();
        VoteResultStrategy resultStrategy = gameRulesProvider.getTeamVoteResultStrategy();

        return new VotePhase(120, allPlayers, GamePhaseType.VoteTeam, resultStrategy, outgoingGameCommunicationAPI, playerTeam, playerCollection);
    }

    public GamePhase<Boolean> BuildMissionPhase(PlayerCollection playerCollection, PlayerTeam playerTeam, Integer round){
        int playerCount = playerCollection.getPlayerList().size();
        VoteResultStrategy resultStrategy = gameRulesProvider.getMissionVoteResultStrategy(round, playerCount);
        return new VotePhase(120, playerTeam.getPlayers(), GamePhaseType.Mission, resultStrategy, outgoingGameCommunicationAPI, playerTeam, playerCollection);
    }

    public GamePhase<PlayerTeam> BuildPickingPhase(PlayerCollection playerCollection, Integer round){
        int playerCount = playerCollection.getPlayerList().size();
        return new PickTeamGamePhase(playerCollection.getActivePlayer(),playerCollection, gameRulesProvider.GetTeamSize(round, playerCount),120, outgoingGameCommunicationAPI);
    }

    public GamePhase<Boolean> BuildRevealPhase(PlayerCollection playerCollection, Boolean outcome){
        return new RevealPhase(outcome, playerCollection, outgoingGameCommunicationAPI);
    }
}
