package game;

import java.util.List;
import java.util.stream.Collectors;

public class PlayerTeam {
    private final List<Player> players;

    public PlayerTeam(List<Player> players) {
        this.players = players;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public List<Integer> getPlayersIds(){
        return players.stream().map(p -> p.getPlayerId()).collect(Collectors.toList());
    }
}
