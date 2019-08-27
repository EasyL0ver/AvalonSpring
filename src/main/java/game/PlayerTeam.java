package game;

import java.util.List;
import java.util.stream.Collectors;

public class PlayerTeam {
    private final List<Player> players;
    private final Player teamCreator;

    public PlayerTeam(List<Player> players, Player teamCreator) {
        this.players = players;
        this.teamCreator = teamCreator;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public List<Integer> getPlayersIds(){
        return players.stream().map(p -> p.getPlayerId()).collect(Collectors.toList());
    }

    public Player getTeamCreator() {
        return teamCreator;
    }
}
