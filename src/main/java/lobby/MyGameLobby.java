package lobby;

import authentication.User;
import common.Event;
import lobby.dto.GameAddedLobbyInfo;
import lobby.gameroom.GameRoom;
import lobby.gameroom.MyGameRoom;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class MyGameLobby implements GameLobby {
    private final List<GameRoom> gameRooms;
    private final Event<GameAddedLobbyInfo> gameAddedEvent = new Event<>();

    public MyGameLobby() {
        this.gameRooms = new ArrayList<>();
    }

    @Override
    public List<GameRoom> getLobbyGames() {
        return gameRooms;
    }

    @Override
    public GameRoom createGameRoom(User userCreatingGame, String gameRoomName) {
        GameRoom gameRoom = new MyGameRoom(userCreatingGame, UUID.randomUUID(), gameRoomName);

        gameRooms.add(gameRoom);

        PropagateRoomAddedInfo(gameRoom);

        return gameRoom;
    }

    @Override
    public Event<GameAddedLobbyInfo> getGameAddedEvent() {
        return gameAddedEvent;
    }


    private void PropagateRoomAddedInfo(GameRoom roomAdded){
        GameAddedLobbyInfo params = new GameAddedLobbyInfo();

        params.setRoomName(roomAdded.getGameName());
        params.setPlayerCount(1);
        params.setHostName(roomAdded.getHostName());

        gameAddedEvent.Invoke(params);
    }
}
