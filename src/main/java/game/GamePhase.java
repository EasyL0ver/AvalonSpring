package game;

import game.dto.notifications.GamePhaseInfo;
import game.exceptions.PhaseFailedException;

/**
 * single unit of work in game context
 * @param <T> type containing information about finished game phase
 */
public interface GamePhase<T> {
    T resolve() throws PhaseFailedException, InterruptedException;

    GamePhaseInfo getGamePhaseChangedInfo();
}
