package game.communication;

import game.Player;
import game.dto.GamePhaseInfo;
import game.dto.MissionResult;
import game.dto.TeamVoteResult;
import game.dto.responses.ScoreBoard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
public class OutgoingGameCommunicationAPI {

    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public OutgoingGameCommunicationAPI(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public void AnnouncePhaseChanged(Collection<Player> players, GamePhaseInfo info){
        for(Player player : players)
            simpMessagingTemplate.convertAndSend("/topic/game/user-specific/" + player.getUserUUID() + "/phase-changed", info);
    }

    public void NotifyScoreChanged(Collection<Player> players, ScoreBoard scoreBoard){
        for(Player player : players)
            simpMessagingTemplate.convertAndSend("/topic/game/user-specific/" + player.getUserUUID() + "/score-changed", scoreBoard);
    }

    public void NotifyGameOver(Collection<Player> players, Boolean result){
        for(Player player : players)
            simpMessagingTemplate.convertAndSend("/topic/game/user-specific/" + player.getUserUUID() + "/game-ended", result);
    }

    public void NotifyTeamVoteResult(Collection<Player> players, TeamVoteResult result){
        for(Player player : players)
            simpMessagingTemplate.convertAndSend("/topic/game/user-specific/" + player.getUserUUID() + "/vote-result", result);
    }

    public void NotifyMissionResult(Collection<Player> players, MissionResult result){
        for(Player player : players)
            simpMessagingTemplate.convertAndSend("/topic/game/user-specific/" + player.getUserUUID() + "/mission-result", result);
    }


}
