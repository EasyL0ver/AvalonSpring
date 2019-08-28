package game;

import game.dto.CompleteGameState;
import game.dto.PlayerInfo;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GameStateBuilder {

    CompleteGameState Build(Game game, Player player){
        List<VisiblePlayer> playerInfo = game.getPlayerCollection().getPlayerList().values()
                .stream()
                .filter(p -> !p.getPlayerId().equals(player.getPlayerId()))
                .map(p -> new PlayerInfo(p.getPlayerName(), p.getPlayerId())
                ).collect(Collectors.toList());


        CompleteGameState gameState = new CompleteGameState();

        gameState.role = player.getPlayerRole();
        gameState.otherPlayers = playerInfo;

        gameState.identityInfo = player.getIdentityInformation()
                .stream().map(VisiblePlayer::getPlayerId).collect(Collectors.toList());

        gameState.myId = player.getPlayerId();

        gameState.gamePhaseInfo = game.getGamePhaseInfo();
        gameState.scoreBoard = game.getScore();

        return gameState;
    }

}
