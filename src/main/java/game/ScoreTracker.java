package game;

import game.communication.OutgoingGameCommunicationAPI;
import game.dto.notifications.ScoreBoard;
import game.exceptions.GameOverException;

/**
 * keeps track of good and evil score, throws GameOverException when score exceeds threshold
 */
public class ScoreTracker {
    private final Integer failedAttemptsToLose;
    private final Integer pointsToWin;
    private final PlayerCollection playerCollection;
    private final OutgoingGameCommunicationAPI communicationAPI;

    private Integer evilScore = 0;
    private Integer goodScore = 0;
    private Integer failedAttempts = 0;

    public ScoreTracker(Integer pointsToWin, Integer failedAttemptsToLose, PlayerCollection playerCollection, OutgoingGameCommunicationAPI communicationAPI) {
        this.pointsToWin = pointsToWin;
        this.failedAttemptsToLose = failedAttemptsToLose;
        this.playerCollection = playerCollection;
        this.communicationAPI = communicationAPI;
    }

    ScoreBoard getScoreBoard(){
        ScoreBoard scoreBoard = new ScoreBoard();
        scoreBoard.evilScore = evilScore;
        scoreBoard.goodScore = goodScore;
        scoreBoard.failedAttempts = failedAttempts;

        return scoreBoard;
    }

    void incrementEvil() throws GameOverException {
        evilScore++;

        communicationAPI.NotifyScoreChanged(playerCollection.getPlayerList().values(),getScoreBoard());

        if(evilScore.equals(pointsToWin))
            throw new GameOverException(false, "evil otherPlayers got 3 points");
    }

    void incrementGood() throws GameOverException {
        goodScore++;

        communicationAPI.NotifyScoreChanged(playerCollection.getPlayerList().values(),getScoreBoard());

        if(goodScore.equals(pointsToWin))
            throw new GameOverException(true, "good otherPlayers got 3 points");
    }

    void reportFailure() throws GameOverException {
        failedAttempts++;

        communicationAPI.NotifyScoreChanged(playerCollection.getPlayerList().values(),getScoreBoard());

        if(failedAttempts.equals(failedAttemptsToLose))
            throw new GameOverException(false, "too much failed attemps");
    }


}
