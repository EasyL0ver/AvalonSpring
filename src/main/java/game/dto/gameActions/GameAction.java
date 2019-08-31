package game.dto.gameActions;

import game.dto.GamePhaseType;

import java.util.UUID;

public class GameAction {
    private int playerId;
    private UUID userApiKey;
    private UUID gameUUID;
    private GamePhaseType currentGamePhase;

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public UUID getUserApiKey() {
        return userApiKey;
    }

    public void setUserApiKey(UUID userApiKey) {
        this.userApiKey = userApiKey;
    }

    public UUID getGameUUID() {
        return gameUUID;
    }

    public void setGameUUID(UUID gameUUID) {
        this.gameUUID = gameUUID;
    }

    public GamePhaseType getCurrentGamePhase() {
        return currentGamePhase;
    }

    public void setCurrentGamePhase(GamePhaseType currentGamePhase) {
        this.currentGamePhase = currentGamePhase;
    }
}
