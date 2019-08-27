package game.gamePhases;

import java.util.Collection;
import java.util.Map;

public interface VoteResultStrategy {
    Boolean resolveVoteResult(Collection<Boolean> voteResults);
}
