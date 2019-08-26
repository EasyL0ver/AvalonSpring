package game.gamePhases;

import game.GamePhase;
import game.PlayerCollection;
import game.PlayerTeam;
import game.exceptions.PhaseFailedException;

public class VoteTeamGamePhase implements GamePhase<Boolean> {
    private final PlayerCollection playerCollection;
    private final PlayerTeam playerTeam;

    public VoteTeamGamePhase(PlayerCollection playerCollection, PlayerTeam playerTeam) {
        this.playerCollection = playerCollection;
        this.playerTeam = playerTeam;
    }

    @Override
    public Boolean resolve() throws PhaseFailedException {
        return null;
    }
}
