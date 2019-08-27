package game.gamePhases;


import common.EventHandler;
import game.GamePhase;
import game.Player;
import game.PlayerCollection;
import game.PlayerTeam;
import game.communication.ClientGameCommunicationAPI;
import game.dto.GamePhaseInfo;
import game.dto.GamePhaseType;
import game.dto.responses.PickTeamResponse;
import game.dto.responses.Response;
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

    private final ClientGameCommunicationAPI communicationAPI;

    private final EventHandler<Response> responseHandler = new EventHandler<Response>() {
        @Override
        public void Handle(Response params) {
            activePlayerResponse = params;
            playerPickingTeam.getResponseReceivedEvent().DetachHandler(responseHandler);

        }
    };

    private Response activePlayerResponse = null;

    public PickTeamGamePhase(Player playerPickingTeam, PlayerCollection playerCollection, Integer teamSize, Integer responseTimeSeconds, ClientGameCommunicationAPI communicationAPI) {
        this.teamSize = teamSize;
        this.responseTimeSeconds = responseTimeSeconds;
        this.playerPickingTeam = playerPickingTeam;
        this.playerCollection = playerCollection;
        this.communicationAPI = communicationAPI;
    }

    @Override
    public PlayerTeam resolve() throws PhaseFailedException, InterruptedException {
        playerPickingTeam.getResponseReceivedEvent().AttachHandler(responseHandler);

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
                playerPickingTeam.getResponseReceivedEvent().DetachHandler(responseHandler);
        }


        return CreateFromResponse(activePlayerResponse);
    }

    @Override
    public GamePhaseInfo getGamePhaseChangedInfo() {
        return new GamePhaseInfo(GamePhaseType.PickTeam, responseTimeSeconds, playerCollection.getActivePlayer().getPlayerId(), null);
    }

    private PlayerTeam CreateFromResponse(Response response) throws PhaseFailedException {
        //response type mismatch
        if(!(response instanceof PickTeamResponse))
            throw new PhaseFailedException("response type mismatch");

        PickTeamResponse pickResponse = (PickTeamResponse) response;

        HashSet<Integer> responseHashSet = new HashSet<Integer>(pickResponse.pickedPlayers);

        if(pickResponse.pickedPlayers.size() != responseHashSet.size())
            throw new PhaseFailedException("player identifiers not unique");


        List<Player> teamPlayers = playerCollection.getPlayerList().values().stream()
                .filter(player -> responseHashSet.contains(player.getPlayerId()))
                .collect(Collectors.toList());

        return new PlayerTeam(teamPlayers, playerPickingTeam);
    }
}


