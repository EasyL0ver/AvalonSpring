package game;

import game.exceptions.PhaseFailedException;

public interface GamePhase<T> {
    T resolve() throws PhaseFailedException, InterruptedException;
}
