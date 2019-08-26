package game;

import java.util.List;

public class PlayerTeam {
    private final List<Player> players;

    public PlayerTeam(List<Player> players) {
        this.players = players;
    }

    public List<Player> getPlayers() {
        return players;
    }
}
