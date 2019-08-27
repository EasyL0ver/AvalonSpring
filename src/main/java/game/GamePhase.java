package game;

import game.dto.GamePhaseInfo;
import game.exceptions.PhaseFailedException;

public interface GamePhase<T> {
    T resolve() throws PhaseFailedException, InterruptedException;

    GamePhaseInfo getGamePhaseChangedInfo();
}
