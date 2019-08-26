package lobby;

import authentication.User;
import authentication.UserFactory;
import common.Event;
import game.GameManager;
import lobby.gameroom.GameRoom;
import lobby.gameroom.MyGameRoom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class MyGameLobby implements GameLobby {
    //todo storing gameroom uuid in two places!
    private final Map<UUID, GameRoom> gameRooms;
    private final GameManager gameManager;

    private final Event<GameRoom> gameAddedEvent = new Event<>();
    private final Event<GameRoom> gameStartedEvent = new Event<>();
    private final Event<GameRoom> gameAbortedEvent = new Event<>();

    @Autowired
    public MyGameLobby(GameManager gameManager) {
        this.gameManager = gameManager;
        this.gameRooms = new HashMap<>();
    }

    @Override
    public Map<UUID, GameRoom> getLobbyGames() {
        return gameRooms;
    }

    @Override
    public GameRoom createGameRoom(User userCreatingGame, String gameRoomName) {
        UUID gameUUID = UUID.randomUUID();
        GameRoom gameRoom = new MyGameRoom(userCreatingGame, gameUUID, gameRoomName);

        gameRooms.put(gameUUID, gameRoom);

        gameAddedEvent.Invoke(gameRoom);

        //todo for testing
        AddTestUsers(gameRoom);


        return gameRoom;
    }

    private void AddTestUsers(GameRoom gameRoom) {
        User u1 = new User() {
            private UUID u = UUID.randomUUID();
            @Override
            public UUID getUserApiKey() {
                return u;
            }

            @Override
            public String getUserName() {
                return u.toString();
            }
        };
        gameRoom.join(u1);

        User u2 = new User() {
            private UUID u = UUID.randomUUID();
            @Override
            public UUID getUserApiKey() {
                return u;
            }

            @Override
            public String getUserName() {
                return u.toString();
            }
        };
        gameRoom.join(u2);
    }

    @Override
    public void startGame(GameRoom room) {
        gameRooms.remove(room.getGameRoomUUID());

        gameManager.startGame(room);

        //todo redirect
        gameStartedEvent.Invoke(room);
    }

    @Override
    public void abortGame(GameRoom room) {
        gameRooms.remove(room.getGameRoomUUID());
        gameAbortedEvent.Invoke(room);
    }

    @Override
    public Event<GameRoom> getGameAddedEvent() {
        return gameAddedEvent;
    }

    @Override
    public Event<GameRoom> getGameStartedEvent() {
        return gameStartedEvent;
    }

    @Override
    public Event<GameRoom> getGameAbortedEvent() {
        return gameAbortedEvent;
    }
}
