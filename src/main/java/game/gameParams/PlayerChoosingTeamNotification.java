package game.gameParams;

import game.GameNotification;
import game.VisiblePlayer;

public class PlayerChoosingTeamNotification implements GameNotification {
    private final VisiblePlayer playerChoosing;

    public PlayerChoosingTeamNotification(VisiblePlayer playerChoosing) {
        this.playerChoosing = playerChoosing;
    }
}
