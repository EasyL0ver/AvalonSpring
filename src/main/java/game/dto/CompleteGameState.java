package game.dto;

import game.AvalonRole;
import game.VisiblePlayer;
import game.dto.notifications.GamePhaseInfo;
import game.dto.notifications.ScoreBoard;

import java.util.List;

public class CompleteGameState {
    public List<VisiblePlayer> players;
    public List<Integer> identityInfo;
    public AvalonRole role;
    public Integer myId;
    public GamePhaseInfo gamePhaseInfo;
    public ScoreBoard scoreBoard;
}
