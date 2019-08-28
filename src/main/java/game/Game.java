package game;

import game.dto.GamePhaseInfo;
import game.dto.responses.ScoreBoard;
import game.exceptions.GameOverException;
import game.exceptions.PhaseFailedException;
import game.gameBuilder.GamePhaseFactory;


public class Game {
    private final PlayerCollection playerCollection;
    private final ScoreTracker scoreTracker;
    private final GamePhaseFactory gamePhaseFactory;

    private Integer round = 0;
    private GamePhase currentGamePhase;

    public Game(PlayerCollection playerCollection, ScoreTracker scoreTracker, GamePhaseFactory gamePhaseFactory) {
        this.playerCollection = playerCollection;
        this.scoreTracker = scoreTracker;
        this.gamePhaseFactory = gamePhaseFactory;
    }

    public ScoreBoard getScore(){
        return scoreTracker.getScoreBoard();
    }

    public GamePhaseInfo getGamePhaseInfo(){
        if(currentGamePhase == null)
            return null;

        return currentGamePhase.getGamePhaseChangedInfo();
    }

    public PlayerCollection getPlayerCollection(){
        return playerCollection;
    }

    void Start() throws InterruptedException, PhaseFailedException {

        Thread.sleep(1000*5);

        try {
            while(true){

                PlayerTeam playerTeam = chooseTeam();

                GamePhase<Boolean> missionPhase = gamePhaseFactory.BuildMissionPhase(playerCollection, playerTeam, round);

                currentGamePhase = missionPhase;

                if(missionPhase.resolve())
                    scoreTracker.IncerementGood();
                else
                    scoreTracker.IncrementEvil();

                round++;
                playerCollection.activePlayerMoveNext();
            }
        } catch (GameOverException e) {
            GamePhase<Boolean> revealPhase = gamePhaseFactory.BuildRevealPhase(playerCollection, e.getOutcome());
            Boolean result = revealPhase.resolve();

        } catch (PhaseFailedException e) {
            e.printStackTrace();
        }
    }

    private PlayerTeam proposeTeam() throws GameOverException, InterruptedException {
        PlayerTeam proposedTeam = null;

        while(proposedTeam == null){
            try{
                GamePhase<PlayerTeam> pickTeamGamePhase = gamePhaseFactory.BuildPickingPhase(playerCollection, round);
                currentGamePhase = pickTeamGamePhase;
                proposedTeam = pickTeamGamePhase.resolve();
            }catch (PhaseFailedException e){
                playerCollection.activePlayerMoveNext();
                scoreTracker.ReportFailure();
            }
        }

        return proposedTeam;
    }

    private PlayerTeam chooseTeam() throws GameOverException, InterruptedException, PhaseFailedException {
        PlayerTeam decidedTeam = null;

        while(decidedTeam == null){

            PlayerTeam proposedTeam = proposeTeam();

            GamePhase<Boolean> voteOnTeamPhase = gamePhaseFactory.BuildVotePhase(playerCollection, proposedTeam);

            currentGamePhase = voteOnTeamPhase;
            if(voteOnTeamPhase.resolve())
                decidedTeam = proposedTeam;
            else{
                scoreTracker.ReportFailure();
                playerCollection.activePlayerMoveNext();
            }
        }
        return decidedTeam;
    }
}
