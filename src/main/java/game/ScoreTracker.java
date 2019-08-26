package game;

import game.exceptions.GameOverException;

public class ScoreTracker {
    private final Integer failedAttemptsToLose;
    private final Integer pointsToWin;

    private Integer evilScore = 0;
    private Integer goodScore = 0;
    private Integer failedAttempts = 0;

    public ScoreTracker(Integer pointsToWin, Integer failedAttemptsToLose) {
        this.pointsToWin = pointsToWin;
        this.failedAttemptsToLose = failedAttemptsToLose;
    }

    public Integer getGoodScore() {
        return goodScore;
    }

    public Integer getFailedAttempts() {
        return failedAttempts;
    }

    public Integer getEvilScore() {
        return evilScore;
    }

    public void IncrementEvil() throws GameOverException {
        evilScore++;

        if(evilScore.equals(pointsToWin))
            throw new GameOverException(false, "evil otherPlayers got 3 points");
    }

    public void IncerementGood() throws GameOverException {
        goodScore++;

        if(goodScore.equals(pointsToWin))
            throw new GameOverException(true, "good otherPlayers got 3 points");
    }

    public void ReportFailure() throws GameOverException {
        failedAttempts++;

        if(failedAttempts.equals(failedAttemptsToLose))
            throw new GameOverException(false, "too much failed attemps");
    }


}
