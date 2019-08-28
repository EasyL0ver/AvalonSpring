package game;

import java.util.*;
import java.util.stream.Collectors;

public class PlayerCollection {
    private final Map<UUID, Player> playerList;
    private Integer activePlayerIndex;

    public PlayerCollection(Map<UUID, Player> playerList, Integer activePlayerIndex) {
        this.playerList = playerList;
        this.activePlayerIndex = activePlayerIndex;

    }

    public Map<UUID, Player> getPlayerList(){
        return playerList;
    }

    public List<Player> getPlayersWithRoles(AvalonRole... roles){

        Set<AvalonRole> filteredRolesSet = new HashSet<>(Arrays.asList(roles));

        return playerList.values().stream().filter(player -> filteredRolesSet.contains(player.getPlayerRole())).collect(Collectors.toList());
    }

    public void activePlayerMoveNext(){
        Integer nextActive = activePlayerIndex++;

        if(nextActive >= playerList.size())
            nextActive = 0;

        activePlayerIndex = nextActive;
    }

    public int GetPlayerCount(){
        return playerList.values().size();
    }

    public Player getActivePlayer(){
        return playerList.values().stream().filter(x -> x.getPlayerId().equals(activePlayerIndex)).collect(Collectors.toList()).get(0);
    }

    public List<Player> getNonActivePlayers(){
        return playerList.values().stream().filter(player -> player != getActivePlayer()).collect(Collectors.toList());
    }
}
