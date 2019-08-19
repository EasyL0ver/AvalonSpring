package lobby;

import authentication.User;
import common.EventHandler;
import jdk.jfr.Event;
import lobby.dto.GameAddedLobbyInfo;
import lobby.dto.JoinGameLobbyMessage;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Component
public class MyGameLobby implements GameLobby {
    private final List<GameRoom> gameRooms;

    private EventHandler<GameAddedLobbyInfo> onGameAddedLobbyHandler;

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

    public void setOnGameAddedLobbyHandler(EventHandler<GameAddedLobbyInfo> onGameAddedLobbyHandler) {
        this.onGameAddedLobbyHandler = onGameAddedLobbyHandler;
    }

    private void PropagateRoomAddedInfo(GameRoom roomAdded){
        GameAddedLobbyInfo params = new GameAddedLobbyInfo();

        params.setRoomName(roomAdded.getGameName());
        params.setPlayerCount(1);
        params.setHostName(roomAdded.getHostName());


        if(onGameAddedLobbyHandler != null)
            onGameAddedLobbyHandler.Handle(params);
    }
}
