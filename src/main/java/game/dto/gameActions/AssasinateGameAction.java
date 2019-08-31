package game.dto.gameActions;

public class AssasinateGameAction extends GameAction {
    private int killedPlayerId;

    public int getKilledPlayerId() {
        return killedPlayerId;
    }

    public void setKilledPlayerId(int killedPlayerId) {
        this.killedPlayerId = killedPlayerId;
    }
}
