package game;

import authentication.User;

import java.util.List;

public class Player implements VisiblePlayer{

    private final Integer playerIndex;
    private final AvalonRole playerRole;
    private List<VisiblePlayer> identityInformation;
    private final User user;

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
        return null;
    }

    @Override
    public Integer getPlayerId() {
        return null;
    }

    public void notify(GameNotification notification){

    }

    public List<VisiblePlayer> getIdentityInformation() {
        return identityInformation;
    }

    public void setIdentityInformation(List<VisiblePlayer> identityInformation) {
        this.identityInformation = identityInformation;
    }
}
