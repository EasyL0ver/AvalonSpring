package game.gameBuilder;

import game.*;
import game.communication.ClientGameCommunicationAPI;
import game.dto.GamePhaseType;
import game.gamePhases.PickTeamGamePhase;
import game.gamePhases.VotePhase;
import game.gamePhases.VoteResultStrategy;
import game.gamePhases.VoteType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GamePhaseFactory {
    private final GameRulesProvider gameRulesProvider;
    private final ClientGameCommunicationAPI clientGameCommunicationAPI;

    @Autowired
    public GamePhaseFactory(GameRulesProvider gameRulesProvider, ClientGameCommunicationAPI clientGameCommunicationAPI) {
        this.gameRulesProvider = gameRulesProvider;
        this.clientGameCommunicationAPI = clientGameCommunicationAPI;
    }

    public GamePhase<Boolean> BuildVotePhase(PlayerCollection playerCollection, PlayerTeam playerTeam){
        List<Player> allPlayers = playerTeam.getPlayers();
        VoteResultStrategy resultStrategy = gameRulesProvider.getTeamVoteResultStrategy();

        return new VotePhase(120, allPlayers, GamePhaseType.VoteTeam, resultStrategy, clientGameCommunicationAPI, playerTeam);
    }

    public GamePhase<Boolean> BuildMissionPhase(PlayerCollection playerCollection, PlayerTeam playerTeam, Integer round){
        int playerCount = playerCollection.getPlayerList().size();
        VoteResultStrategy resultStrategy = gameRulesProvider.getMissionVoteResultStrategy(round, playerCount);
        return new VotePhase(120, playerTeam.getPlayers(), GamePhaseType.Mission, resultStrategy, clientGameCommunicationAPI, playerTeam);
    }

    public GamePhase<PlayerTeam> BuildPickingPhase(PlayerCollection playerCollection, Integer round){
        int playerCount = playerCollection.getPlayerList().size();
        return new PickTeamGamePhase(playerCollection.getActivePlayer(),playerCollection, gameRulesProvider.GetTeamSize(round, playerCount),120, clientGameCommunicationAPI);
    }
}
