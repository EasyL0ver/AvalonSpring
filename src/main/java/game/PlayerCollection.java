package game;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PlayerCollection {
    private final List<Player> playerList;
    private Integer activePlayerIndex;

    public PlayerCollection(List<Player> playerList, Integer activePlayerIndex) {
        this.playerList = playerList;
        this.activePlayerIndex = activePlayerIndex;
    }

    public List<Player> getPlayerList() {
        return playerList;
    }

    public List<Player> getPlayersWithRoles(AvalonRole... roles){

        Set<AvalonRole> filteredRolesSet = new HashSet<>(Arrays.asList(roles));

        return playerList.stream().filter(player -> filteredRolesSet.contains(player.getPlayerRole())).collect(Collectors.toList());
    }

    public void activePlayerMoveNext(){
        Integer nextActive = activePlayerIndex++;

        if(nextActive >= playerList.size())
            nextActive = 0;

        activePlayerIndex = nextActive;
    }

    public Player getActivePlayer(){
        return playerList.get(activePlayerIndex);
    }

    public List<Player> getNonActivePlayers(){
        return playerList.stream().filter(player -> player != getActivePlayer()).collect(Collectors.toList());
    }
}
