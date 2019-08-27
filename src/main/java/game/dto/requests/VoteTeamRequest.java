package game.dto.requests;

import game.gamePhases.VoteType;

import java.util.List;

public class VoteTeamRequest implements Request{
    public List<Integer> teamId;
    public VoteType voteType;

    public VoteTeamRequest(List<Integer> teamIds, VoteType voteType){
        this.voteType = voteType;
        this.teamId = teamIds;
    }

    @Override
    public Integer getSecondsToRespond() {
        return null;
    }
}
