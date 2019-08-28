package game.dto;

import game.AvalonRole;
import game.VisiblePlayer;
import game.dto.responses.ScoreBoard;

import java.util.List;

public class CompleteGameState {
    public List<VisiblePlayer> otherPlayers;
    public List<Integer> identityInfo;
    public AvalonRole role;
    public Integer myId;
    public GamePhaseInfo gamePhaseInfo;
    public ScoreBoard scoreBoard;
}
