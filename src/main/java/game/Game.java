package game;

import game.exceptions.GameOverException;
import game.exceptions.PhaseFailedException;
import game.gamePhases.PickTeamGamePhase;
import game.gamePhases.ResolveTeamPhase;
import game.gamePhases.VoteTeamGamePhase;


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

    private PlayerTeam PickTeam() throws GameOverException {
        PlayerTeam resolvedPlayerTeam = null;

        while(resolvedPlayerTeam == null){
            try{
                PickTeamGamePhase pickTeamGamePhase = new PickTeamGamePhase(playerCollection, gameRules.GetTeamSize(round));
                resolvedPlayerTeam = pickTeamGamePhase.resolve();
            }catch (PhaseFailedException e){
                playerCollection.activePlayerMoveNext();
                scoreTracker.ReportFailure();
            }
        }

        return resolvedPlayerTeam;
    }

    private Boolean VoteTeam(PlayerTeam playerTeam) {

        VoteTeamGamePhase voteTeamGamePhase = new VoteTeamGamePhase(playerCollection, playerTeam);

        try {
            return voteTeamGamePhase.resolve();
        } catch (PhaseFailedException e) {
            e.printStackTrace();
            return true;
        }
    }

    private Boolean ResolveTeamPhase(PlayerTeam playerTeam){

        ResolveTeamPhase resolveTeamPhase = new ResolveTeamPhase(playerCollection, playerTeam, gameRules.GetAllowedFails(round));

        try {
            return resolveTeamPhase.resolve();
        } catch (PhaseFailedException e) {
            e.printStackTrace();
            return true;
        }
    }

    private void WaitFor(Integer seconds) throws InterruptedException {
        Thread.sleep(seconds * 1000);
    }

}
