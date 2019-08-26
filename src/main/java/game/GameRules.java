package game;

//todo implement
public class GameRules {

    private final Integer playersInGame;

    public GameRules(Integer playersInGame) {
        this.playersInGame = playersInGame;
    }

    public Integer GetTeamSize(Integer round){
        return 2;
    }

    public Integer GetAllowedFails(Integer round){
        return 1;

    }

}
