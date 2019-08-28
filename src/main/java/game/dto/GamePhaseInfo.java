package game.dto;

import java.util.List;

public class GamePhaseInfo {
    private final GamePhaseType gamePhaseType;
    private final int timeoutSeconds;
    private final Integer playerPickingTeamId;
    private final List<Integer> currentTeam;
    private final Integer teamSize;
    private final List<Integer> knownEvilPlayers;

    public GamePhaseInfo(GamePhaseType gamePhaseType, int timeoutSeconds, Integer playerPickingTeamId, List<Integer> currentTeam, Integer teamSize, List<Integer> knownEvilPlayers) {
        this.gamePhaseType = gamePhaseType;
        this.timeoutSeconds = timeoutSeconds;
        this.playerPickingTeamId = playerPickingTeamId;
        this.currentTeam = currentTeam;
        this.teamSize = teamSize;
        this.knownEvilPlayers = knownEvilPlayers;
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

    public Integer getTeamSize() {
        return teamSize;
    }
}
