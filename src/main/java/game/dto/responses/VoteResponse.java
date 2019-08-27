package game.dto.responses;

import game.dto.GamePhaseType;
import game.gamePhases.VoteType;

public class VoteResponse {
    public Boolean voteResult;
    public Integer playerId;
    public GamePhaseType voteType;
}
