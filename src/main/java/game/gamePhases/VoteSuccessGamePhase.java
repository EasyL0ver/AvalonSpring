package game.gamePhases;

import game.Player;
import game.dto.requests.Request;

import java.util.List;
import java.util.Map;

public class VoteSuccessGamePhase extends VotePhase {
    protected VoteSuccessGamePhase(Integer voteTimeSeconds, List<Player> votingPlayers, VoteType responseVoteType, Request requestSent) {
        super(voteTimeSeconds, votingPlayers, responseVoteType, requestSent);
    }

    @Override
    protected Boolean ResolveVoteResult(Map<Integer, Boolean> result) {
        return null;
    }
}
