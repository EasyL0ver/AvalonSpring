package game.dto;

import game.AvalonRole;
import game.VisiblePlayer;

import java.util.List;

public class CompleteGameState {
    public List<VisiblePlayer> otherPlayers;
    public List<Integer> identityInfo;
    public AvalonRole role;
    public Integer myId;

    public GamePhaseInfo gamePhaseInfo;

}
