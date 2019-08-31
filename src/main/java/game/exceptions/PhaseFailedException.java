package game.exceptions;

/**
 * Thrown to indicate irrecoverable error during game phase
 */
public class PhaseFailedException extends Exception {
    public PhaseFailedException(String message){
        super(message);
    }
}
