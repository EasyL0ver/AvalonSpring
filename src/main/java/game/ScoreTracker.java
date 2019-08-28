package game;

import game.communication.OutgoingGameCommunicationAPI;
import game.dto.responses.ScoreBoard;
import game.exceptions.GameOverException;

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

    public ScoreBoard getScoreBoard(){
        ScoreBoard scoreBoard = new ScoreBoard();
        scoreBoard.evilScore = evilScore;
        scoreBoard.goodScore = goodScore;
        scoreBoard.failedAttempts = failedAttempts;

        return scoreBoard;
    }

    public void IncrementEvil() throws GameOverException {
        evilScore++;

        communicationAPI.NotifyScoreChanged(playerCollection.getPlayerList().values(),getScoreBoard());

        if(evilScore.equals(pointsToWin))
            throw new GameOverException(false, "evil otherPlayers got 3 points");
    }

    public void IncerementGood() throws GameOverException {
        goodScore++;

        communicationAPI.NotifyScoreChanged(playerCollection.getPlayerList().values(),getScoreBoard());

        if(goodScore.equals(pointsToWin))
            throw new GameOverException(true, "good otherPlayers got 3 points");
    }

    public void ReportFailure() throws GameOverException {
        failedAttempts++;

        communicationAPI.NotifyScoreChanged(playerCollection.getPlayerList().values(),getScoreBoard());

        if(failedAttempts.equals(failedAttemptsToLose))
            throw new GameOverException(false, "too much failed attemps");
    }


}
