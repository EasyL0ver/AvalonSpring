package lobby;

import authentication.User;
import common.Event;
import lobby.gameroom.GameRoom;
import lobby.gameroom.MyGameRoom;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class MyGameLobby implements GameLobby {
    //todo storing gameroom uuid in two places!
    private final Map<UUID, GameRoom> gameRooms;

    private final Event<GameRoom> gameAddedEvent = new Event<>();
    private final Event<GameRoom> gameStartedEvent = new Event<>();
    private final Event<GameRoom> gameAbortedEvent = new Event<>();

    public MyGameLobby() {
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

        return gameRoom;
    }

    @Override
    public void startGame(GameRoom room) {
        gameRooms.remove(room.getGameRoomUUID());
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
