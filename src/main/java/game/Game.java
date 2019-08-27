package game;

import game.dto.requests.VoteTeamRequest;
import game.exceptions.GameOverException;
import game.exceptions.PhaseFailedException;
import game.gamePhases.*;

import java.util.List;


public class Game {
    private final PlayerCollection playerCollection;
    private final ScoreTracker scoreTracker;
    private final GameRules gameRules;

    private Integer round = 0;

    public Game(PlayerCollection playerCollection, ScoreTracker scoreTracker, GameRules gameRules) {
        this.playerCollection = playerCollection;
        this.scoreTracker = scoreTracker;
        this.gameRules = gameRules;
    }

    public PlayerCollection getPlayerCollection(){
        return playerCollection;
    }

    void Start() throws InterruptedException {
        //todo start game timer

        try {

            while(true){

                WaitFor(10);

                PlayerTeam decidedTeam = null;

                while(decidedTeam == null){
                    PlayerTeam proposedTeam = PickTeam();

                    if(VoteTeam(proposedTeam))
                        decidedTeam = proposedTeam;
                    else{
                        scoreTracker.ReportFailure();
                        playerCollection.activePlayerMoveNext();
                    }
                }

                if(ResolveTeamPhase(decidedTeam))
                    scoreTracker.IncerementGood();
                else
                    scoreTracker.IncrementEvil();

                round++;
            }
        } catch (GameOverException e) {
            //todo reveal assasin etc
            e.printStackTrace();
        }
    }

    private PlayerTeam PickTeam() throws GameOverException, InterruptedException {
        PlayerTeam resolvedPlayerTeam = null;

        while(resolvedPlayerTeam == null){
            try{
                PickTeamGamePhase pickTeamGamePhase = new PickTeamGamePhase(playerCollection.getActivePlayer(),playerCollection, gameRules.GetTeamSize(round),120);
                resolvedPlayerTeam = pickTeamGamePhase.resolve();
            }catch (PhaseFailedException e){
                playerCollection.activePlayerMoveNext();
                scoreTracker.ReportFailure();
            }
        }

        return resolvedPlayerTeam;
    }

    private Boolean VoteTeam(PlayerTeam playerTeam) throws InterruptedException {
        VoteTeamRequest voteRequest = new VoteTeamRequest(playerTeam.getPlayersIds(), VoteType.TeamVote);
        List<Player> allPlayers = playerTeam.getPlayers();
        VoteResultStrategy resultStrategy = gameRules.getTeamVoteResultStrategy();

        VotePhase voteTeamGamePhase = new VotePhase(120, allPlayers, VoteType.TeamVote, voteRequest, resultStrategy);

        try {
            return voteTeamGamePhase.resolve();
        } catch (PhaseFailedException e) {
            e.printStackTrace();
            return true;
        }
    }

    private Boolean ResolveTeamPhase(PlayerTeam playerTeam) throws InterruptedException {
        VoteTeamRequest voteTeamRequest = new VoteTeamRequest(playerTeam.getPlayersIds(), VoteType.MissionVote);
        VoteResultStrategy resultStrategy = gameRules.getMissionVoteResultStrategy(round);
        VotePhase missionGamePhase = new VotePhase(120, playerTeam.getPlayers(),VoteType.MissionVote, voteTeamRequest, resultStrategy);

        try {
            return missionGamePhase.resolve();
        } catch (PhaseFailedException e) {
            e.printStackTrace();
            return true;
        }
    }

    private void WaitFor(Integer seconds) throws InterruptedException {
        Thread.sleep(seconds * 1000);
    }

}
