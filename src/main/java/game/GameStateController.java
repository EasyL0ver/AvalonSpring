package game;

import game.dto.CompleteGameState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class GameStateController {

    private final GameManager gameManager;

    @Autowired
    public GameStateController(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @GetMapping("/game/state")
    public CompleteGameState GetGameState(String apiKey, String gameUUID) throws Exception {
        UUID actualGameUUID = UUID.fromString(gameUUID);
        UUID actualApiKey = UUID.fromString(apiKey);

        return gameManager.loadCompleteGameState(actualApiKey, actualGameUUID);
    }


}
