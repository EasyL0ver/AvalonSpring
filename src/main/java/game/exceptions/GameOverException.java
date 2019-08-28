package game.exceptions;

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
