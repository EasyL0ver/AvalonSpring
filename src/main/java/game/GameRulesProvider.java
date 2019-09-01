package game;

import game.gamePhases.VoteResultStrategy;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class GameRulesProvider {
    private int[][] teamSizeTable = {
            {1,1,1,1,1},
            {2,2,2,2,2},
            {2,2,2,2,2},
            {2,2,2,2,2},
            {2,3,2,3,3},
            {2,3,4,3,4},
            {2,3,3,4,4},
            {3,4,4,5,5},
            {3,4,4,5,5},
            {3,4,4,5,5},
    };

    private int[] evilPlayersTable = {1,1,1,1,2,2,3,3,3,4};

    /**
     *
     * @param round
     * @param playersInGame
     * @return team size for given round and game size
     */
    public Integer GetTeamSize(Integer round, Integer playersInGame){
        return teamSizeTable[playersInGame - 1][round];
    }

    /**
     *
     * @param playersInGame
     * @return evil players currently in the game
     */
    public Integer GetEvilPlayersCount(Integer playersInGame){
        return evilPlayersTable[playersInGame];
    }

    public VoteResultStrategy getMissionVoteResultStrategy(Integer round, Integer playersInGame){
        return new VoteResultStrategy() {
            @Override
            public Boolean resolveVoteResult(Collection<Boolean> voteResults) {
                int losesToFail = 1;

                if(playersInGame >= 7 && round == 4)
                    losesToFail++;

                long loses =  voteResults.stream().filter(x -> !x).count();

                return loses < losesToFail;
            }
        };
    }

    public VoteResultStrategy getTeamVoteResultStrategy(){
        return new VoteResultStrategy() {
            @Override
            public Boolean resolveVoteResult(Collection<Boolean> voteResults) {
                long loses =  voteResults.stream().filter(x -> x).count();
                long wins = voteResults.size() - loses;


                return loses > wins;
            }
        };
    }

}
