package game.dto;

import java.util.List;

public class GamePhaseInfo {
    private final GamePhaseType gamePhaseType;
    private final int timeoutSeconds;
    private final Integer playerPickingTeamId;
    private final List<Integer> currentTeam;

    public GamePhaseInfo(GamePhaseType gamePhaseType, int timeoutSeconds, Integer playerPickingTeamId, List<Integer> currentTeam) {
        this.gamePhaseType = gamePhaseType;
        this.timeoutSeconds = timeoutSeconds;
        this.playerPickingTeamId = playerPickingTeamId;
        this.currentTeam = currentTeam;
    }

    public GamePhaseType getGamePhaseType() {
        return gamePhaseType;
    }

    public int getTimeoutSeconds() {
        return timeoutSeconds;
    }

    public Integer getPlayerPickingTeamId() {
        return playerPickingTeamId;
    }

    public List<Integer> getCurrentTeam() {
        return currentTeam;
    }
}
