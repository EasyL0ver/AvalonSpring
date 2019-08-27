package game.gamePhases;

import common.EventHandler;
import game.GamePhase;
import game.Player;
import game.PlayerTeam;
import game.communication.ClientGameCommunicationAPI;
import game.dto.GamePhaseInfo;
import game.dto.GamePhaseType;
import game.dto.responses.Response;
import game.dto.responses.VoteResponse;
import game.exceptions.PhaseFailedException;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class VotePhase implements GamePhase<Boolean> {
    private final Integer pollTimeMilliseconds = 500;

    private final Integer voteTimeSeconds;
    private final List<Player> votingPlayers;
    private final PlayerTeam votedTeam;
    private final GamePhaseType responseVoteType;
    private final VoteResultStrategy resultStrategy;

    private final ClientGameCommunicationAPI clientGameCommunicationAPI;

    private final Map<Integer, Boolean> teamVotes = new ConcurrentHashMap<>();
    private final EventHandler<Response> responseEventHandler = new EventHandler<Response>() {
        @Override
        public void Handle(Response params) {
            if(!(params instanceof VoteResponse))
                return;

            VoteResponse request = (VoteResponse) params;

            if(request.voteType != responseVoteType)
                return;

            teamVotes.put(request.playerId, request.voteResult);
        }
    };

    public VotePhase(Integer voteTimeSeconds, List<Player> votingPlayers, GamePhaseType responseVoteType, VoteResultStrategy resultStrategy, ClientGameCommunicationAPI clientGameCommunicationAPI, PlayerTeam votedTeam) {
        this.voteTimeSeconds = voteTimeSeconds;
        this.votingPlayers = votingPlayers;
        this.responseVoteType = responseVoteType;
        this.resultStrategy = resultStrategy;
        this.clientGameCommunicationAPI = clientGameCommunicationAPI;
        this.votedTeam = votedTeam;
    }

    @Override
    public Boolean resolve() throws PhaseFailedException, InterruptedException {

        try{
            for(Player player : votingPlayers){
                player.getResponseReceivedEvent().AttachHandler(responseEventHandler);
            }

            clientGameCommunicationAPI.AnnouncePhaseChanged(votingPlayers, getGamePhaseChangedInfo());

            Long timeElapsed = 0L;
            boolean timedOut = false;

            while(!isVoteFinished() && !timedOut){
                Thread.sleep(pollTimeMilliseconds);
                timeElapsed += pollTimeMilliseconds;
                timedOut = timeElapsed > voteTimeSeconds * 1000;
            }

        }finally {
            for(Player player : votingPlayers){
                player.getResponseReceivedEvent().DetachHandler(responseEventHandler);
            }
        }

        return resultStrategy.resolveVoteResult(teamVotes.values());
    }

    @Override
    public GamePhaseInfo getGamePhaseChangedInfo() {
        return new GamePhaseInfo(responseVoteType, voteTimeSeconds, votedTeam.getTeamCreator().getPlayerId(), votedTeam.getPlayersIds());
    }

    private Boolean isVoteFinished(){
        return teamVotes.size() == votingPlayers.size();
    }

}
