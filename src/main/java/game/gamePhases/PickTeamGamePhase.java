package game.gamePhases;


import common.EventHandler;
import game.GamePhase;
import game.Player;
import game.PlayerCollection;
import game.PlayerTeam;
import game.communication.OutgoingGameCommunicationAPI;
import game.dto.GameAction;
import game.dto.GamePhaseInfo;
import game.dto.GamePhaseType;
import game.dto.NominateTeamGameAction;
import game.exceptions.PhaseFailedException;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class PickTeamGamePhase implements GamePhase<PlayerTeam> {
    private final Integer teamSize;
    private final Integer responseTimeSeconds;
    private final Integer pollOffsetMillis = 200;

    private final Player playerPickingTeam;
    private final PlayerCollection playerCollection;

    private final OutgoingGameCommunicationAPI communicationAPI;

    private final EventHandler<GameAction> responseHandler = new EventHandler<GameAction>() {
        @Override
        public void Handle(GameAction params) {
            activePlayerResponse = params;
            playerPickingTeam.getGameActionReceivedEvent().DetachHandler(responseHandler);

        }
    };

    private GameAction activePlayerResponse = null;

    public PickTeamGamePhase(Player playerPickingTeam, PlayerCollection playerCollection, Integer teamSize, Integer responseTimeSeconds, OutgoingGameCommunicationAPI communicationAPI) {
        this.teamSize = teamSize;
        this.responseTimeSeconds = responseTimeSeconds;
        this.playerPickingTeam = playerPickingTeam;
        this.playerCollection = playerCollection;
        this.communicationAPI = communicationAPI;
    }

    @Override
    public PlayerTeam resolve() throws PhaseFailedException, InterruptedException {
        playerPickingTeam.getGameActionReceivedEvent().AttachHandler(responseHandler);

        try{

            communicationAPI.AnnouncePhaseChanged(playerCollection.getPlayerList().values(), getGamePhaseChangedInfo());

            Long timeElapsedMillis = 0L;

            while(activePlayerResponse == null){

                Thread.sleep(pollOffsetMillis);
                timeElapsedMillis += pollOffsetMillis;

                if(timeElapsedMillis >= responseTimeSeconds * 1000)
                    throw new PhaseFailedException("timed out waiting for response");

            }

        }finally {
            if(activePlayerResponse == null)
                playerPickingTeam.getGameActionReceivedEvent().DetachHandler(responseHandler);
        }


        return CreateFromResponse(activePlayerResponse);
    }

    @Override
    public GamePhaseInfo getGamePhaseChangedInfo() {
        return new GamePhaseInfo(GamePhaseType.PickTeam, responseTimeSeconds, playerCollection.getActivePlayer().getPlayerId(), null, teamSize, null);
    }

    private PlayerTeam CreateFromResponse(GameAction response) throws PhaseFailedException {
        //response type mismatch
        if(!(response instanceof NominateTeamGameAction))
            throw new PhaseFailedException("response type mismatch");

        NominateTeamGameAction pickResponse = (NominateTeamGameAction) response;

        HashSet<Integer> responseHashSet = new HashSet<Integer>(pickResponse.getNominatedPlayers());

        if(pickResponse.getNominatedPlayers().size() != responseHashSet.size())
            throw new PhaseFailedException("player identifiers not unique");


        List<Player> teamPlayers = playerCollection.getPlayerList().values().stream()
                .filter(player -> responseHashSet.contains(player.getPlayerId()))
                .collect(Collectors.toList());

        return new PlayerTeam(teamPlayers, playerPickingTeam);
    }
}


