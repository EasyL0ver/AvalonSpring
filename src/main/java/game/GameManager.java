package game;

import game.dto.CompleteGameState;
import game.exceptions.PhaseFailedException;
import game.gameBuilder.GameBuilder;
import lobby.gameroom.GameRoom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class GameManager {
    private final GameBuilder gameBuilder;
    private final Map<UUID, Game> gameMap;
    private final GameStateBuilder gameStateBuilder;

    @Autowired
    public GameManager(GameBuilder gameBuilder, GameStateBuilder gameStateBuilder){
        this.gameBuilder = gameBuilder;
        this.gameStateBuilder = gameStateBuilder;

        gameMap = new ConcurrentHashMap<>();
    }

    public void startGame(GameRoom room){
        Game game = gameBuilder.Build(room);

        gameMap.put(room.getGameRoomUUID(), game);

        new Thread(() -> {
            try {
                game.Start();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (PhaseFailedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public Map<UUID, Game> getGameMap(){
        return gameMap;
    }

    public CompleteGameState loadCompleteGameState(UUID playerApiKey, UUID gameRoomUUID) throws Exception {
        if(!gameMap.containsKey(gameRoomUUID))
            throw new Exception("game not found");

        Game game = gameMap.get(gameRoomUUID);

        PlayerCollection playerCollection = game.getPlayerCollection();

        if(!playerCollection.getPlayerList().containsKey(playerApiKey))
            throw new Exception("player not present in the game");

        Player player = playerCollection.getPlayerList().get(playerApiKey);


        return gameStateBuilder.Build(game, player);
    }

}
