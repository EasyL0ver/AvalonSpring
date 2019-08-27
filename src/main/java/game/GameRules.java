package game;

import game.gamePhases.VoteResultStrategy;

import java.util.Collection;

//todo implement
public class GameRules {

    private final Integer playersInGame;

    public GameRules(Integer playersInGame) {
        this.playersInGame = playersInGame;
    }

    public Integer GetTeamSize(Integer round){
        return 2;
    }

    public VoteResultStrategy getMissionVoteResultStrategy(Integer round){
        return new VoteResultStrategy() {
            @Override
            public Boolean resolveVoteResult(Collection<Boolean> voteResults) {
                return null;
            }
        };
    }

    public VoteResultStrategy getTeamVoteResultStrategy(){
        return new VoteResultStrategy() {
            @Override
            public Boolean resolveVoteResult(Collection<Boolean> voteResults) {
                return null;
            }
        };
    }


}
