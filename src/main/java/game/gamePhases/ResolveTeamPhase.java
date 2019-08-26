package game.gamePhases;

import game.GamePhase;
import game.PlayerCollection;
import game.PlayerTeam;
import game.exceptions.PhaseFailedException;

public class ResolveTeamPhase implements GamePhase<Boolean> {
    private final PlayerCollection playerCollection;
    private final PlayerTeam playerTeam;
    private final Integer failsAllowed;

    public ResolveTeamPhase(PlayerCollection playerCollection, PlayerTeam playerTeam, Integer failsAllowed) {
        this.playerCollection = playerCollection;
        this.playerTeam = playerTeam;
        this.failsAllowed = failsAllowed;
    }

    @Override
    public Boolean resolve() throws PhaseFailedException {
        return null;
    }
}
