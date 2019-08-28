package game;

import game.dto.CompleteGameState;
import lobby.gameroom.GameRoom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Controller
public class GameController {

    private final GameManager gameManager;

    @Autowired
    public GameController(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @GetMapping("/game")
    public String goToGame(
            @ModelAttribute("apiKey") UUID apiKey
            , @ModelAttribute("gameUUID") UUID gameUUID
            , Model model) throws Exception {

        model.addAttribute("room_uuid", gameUUID);
        model.addAttribute("api_key", apiKey);
        model.addAttribute("state", gameManager.loadCompleteGameState(apiKey, gameUUID));

        return "game";
    }
}
