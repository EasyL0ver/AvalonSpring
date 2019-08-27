package game.gamePhases;

import common.EventHandler;
import game.GamePhase;
import game.Player;
import game.dto.requests.Request;
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
    private final VoteType responseVoteType;
    private final Request requestSent;
    private final VoteResultStrategy resultStrategy;

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

    public VotePhase(Integer voteTimeSeconds, List<Player> votingPlayers, VoteType responseVoteType, Request requestSent, VoteResultStrategy resultStrategy) {
        this.voteTimeSeconds = voteTimeSeconds;
        this.votingPlayers = votingPlayers;
        this.responseVoteType = responseVoteType;
        this.requestSent = requestSent;
        this.resultStrategy = resultStrategy;
    }

    @Override
    public Boolean resolve() throws PhaseFailedException, InterruptedException {

        try{
            for(Player player : votingPlayers){
                player.getResponseReceivedEvent().AttachHandler(responseEventHandler);
                player.Request(requestSent);
            }

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


    private Boolean isVoteFinished(){
        return teamVotes.size() == votingPlayers.size();
    }
}
