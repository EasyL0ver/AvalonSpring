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
import game.dto.VoteGameAction;
import game.exceptions.PhaseFailedException;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class VotePhase implements GamePhase<Map<Integer, Boolean>> {
    private final Integer pollTimeMilliseconds = 500;
    private final Integer voteTimeSeconds;
    private final List<Player> votingPlayers;
    private final PlayerTeam votedTeam;
    private final PlayerCollection playerCollection;
    private final GamePhaseType responseVoteType;
    private final OutgoingGameCommunicationAPI outgoingGameCommunicationAPI;

    private final Map<Integer, Boolean> teamVotes = new ConcurrentHashMap<>();
    private final EventHandler<GameAction> responseEventHandler = new EventHandler<GameAction>() {
        @Override
        public void Handle(GameAction params) {
            if(!(params instanceof VoteGameAction))
                return;

            VoteGameAction request = (VoteGameAction) params;

            if(request.getCurrentGamePhase() != responseVoteType)
                return;

            teamVotes.put(request.getPlayerId(), request.isVote());
        }
    };

    public VotePhase(Integer voteTimeSeconds, List<Player> votingPlayers, GamePhaseType responseVoteType, OutgoingGameCommunicationAPI outgoingGameCommunicationAPI, PlayerTeam votedTeam, PlayerCollection playerCollection) {
        this.voteTimeSeconds = voteTimeSeconds;
        this.votingPlayers = votingPlayers;
        this.responseVoteType = responseVoteType;
        this.outgoingGameCommunicationAPI = outgoingGameCommunicationAPI;
        this.votedTeam = votedTeam;
        this.playerCollection = playerCollection;
    }

    @Override
    public Map<Integer, Boolean> resolve() throws PhaseFailedException, InterruptedException {

        try{
            for(Player player : votingPlayers){
                player.getGameActionReceivedEvent().AttachHandler(responseEventHandler);
            }

            outgoingGameCommunicationAPI.AnnouncePhaseChanged(playerCollection.getPlayerList().values(), getGamePhaseChangedInfo());

            Long timeElapsed = 0L;
            boolean timedOut = false;

            while(!isVoteFinished() && !timedOut){
                Thread.sleep(pollTimeMilliseconds);
                timeElapsed += pollTimeMilliseconds;
                timedOut = timeElapsed > voteTimeSeconds * 1000;
            }

        }finally {
            for(Player player : votingPlayers){
                player.getGameActionReceivedEvent().DetachHandler(responseEventHandler);
            }
        }


        return teamVotes;
    }

    @Override
    public GamePhaseInfo getGamePhaseChangedInfo() {
        return new GamePhaseInfo(responseVoteType, voteTimeSeconds, votedTeam.getTeamCreator().getPlayerId(), votedTeam.getPlayersIds(), votedTeam.getPlayersIds().size(), null);
    }

    private Boolean isVoteFinished(){
        return teamVotes.size() == votingPlayers.size();
    }
}
