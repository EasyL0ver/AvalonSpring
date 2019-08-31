package game;

import authentication.User;
import common.Event;
import game.dto.gameActions.GameAction;

import java.util.List;
import java.util.UUID;

public class Player implements VisiblePlayer{

    private final Integer playerIndex;
    private final AvalonRole playerRole;
    private List<VisiblePlayer> identityInformation;
    private final User user;

    private final Event<GameAction> gameActionReceivedEvent = new Event<>();

    public Player(Integer playerIndex, AvalonRole playerRole, User user) {
        this.playerIndex = playerIndex;
        this.playerRole = playerRole;
        this.user = user;
    }

    public AvalonRole getPlayerRole() {
        return playerRole;
    }

    @Override
    public String getPlayerName() {
        return user.getUserName();
    }

    public UUID getUserUUID() {return user.getUserApiKey();}

    public Integer getPlayerId() {
        return playerIndex;
    }

    public Event<GameAction> getGameActionReceivedEvent(){
        return gameActionReceivedEvent;
    }

    public List<VisiblePlayer> getIdentityInformation() {
        return identityInformation;
    }

    public void setIdentityInformation(List<VisiblePlayer> identityInformation) {
        this.identityInformation = identityInformation;
    }
}
