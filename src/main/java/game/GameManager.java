package game;

import common.EventHandler;
import lobby.GameLobby;
import lobby.gameroom.GameRoom;
import org.springframework.beans.factory.annotation.Autowired;

public class GameManager {

    private final GameLobby gameLobby;

    @Autowired
    public GameManager(GameLobby gameLobby) {
        this.gameLobby = gameLobby;

        gameLobby.getGameStartedEvent().AttachHandler(new EventHandler<GameRoom>() {
            @Override
            public void Handle(GameRoom params) {

            }
        });
    }
}
