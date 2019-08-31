package game.exceptions;

/**
 * Thrown when either good or evil reached 3 points or when failed attempts counter is above threshold
 */
public class GameOverException extends Exception{
    private final Boolean outcome;

    public GameOverException(Boolean outcome, String reason) {

        super(reason);

        this.outcome = outcome;
    }

    public Boolean getOutcome() {
        return outcome;
    }
}
