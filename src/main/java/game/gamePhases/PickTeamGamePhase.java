package game.gamePhases;


import game.GamePhase;
import game.PlayerCollection;
import game.PlayerTeam;
import game.exceptions.PhaseFailedException;

public class PickTeamGamePhase implements GamePhase<PlayerTeam> {
    private final PlayerCollection playerCollection;
    private final Integer teamSize;

    public PickTeamGamePhase(PlayerCollection playerCollection, Integer teamSize) {
        this.playerCollection = playerCollection;
        this.teamSize = teamSize;
    }

    @Override
    public PlayerTeam resolve() throws PhaseFailedException {
        throw new PhaseFailedException();
    }
}


