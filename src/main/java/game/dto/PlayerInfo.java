package game.dto;

import game.VisiblePlayer;

public class PlayerInfo implements VisiblePlayer {

    private final String name;
    private final Integer id;

    public PlayerInfo(String name, Integer id){

        this.name = name;
        this.id = id;
    }

    @Override
    public String getPlayerName() {
        return name;
    }

    @Override
    public Integer getPlayerId() {
        return id;
    }
}
