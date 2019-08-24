package lobby;

import authentication.UserService;
import common.EventHandler;
import lobby.dto.GameAddedLobbyInfo;
import lobby.gameroom.GameRoom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class LobbyInformationController{
    private SimpMessagingTemplate simpMessagingTemplate;
    private GameLobby gameLobby;

    @GetMapping("/lobby/all")
    public Collection<GameAddedLobbyInfo> gameRoomList() {
        List<GameAddedLobbyInfo> added = gameLobby.getLobbyGames().values().stream().map(this::Map).collect(Collectors.toList());
        return added;
    }

    @Autowired
    public LobbyInformationController(SimpMessagingTemplate simpMessagingTemplate, GameLobby gameLobby) {
        this.gameLobby = gameLobby;
        this.simpMessagingTemplate = simpMessagingTemplate;

        EventHandler<GameRoom> gameAddedHandler = new EventHandler<GameRoom>() {
            @Override
            public void Handle(GameRoom addedRoom) {
                GameAddedLobbyInfo params = Map(addedRoom);
                simpMessagingTemplate.convertAndSend("/topic/lobby/added", params);
            }
        };

        EventHandler<GameRoom> gameRemovedHandler = new EventHandler<GameRoom>() {
            @Override
            public void Handle(GameRoom params) {
                simpMessagingTemplate.convertAndSend("/topic/lobby/removed", params.getGameRoomUUID());
            }
        };

        gameLobby.getGameAddedEvent().AttachHandler(gameAddedHandler);
        gameLobby.getGameAbortedEvent().AttachHandler(gameRemovedHandler);
        gameLobby.getGameStartedEvent().AttachHandler(gameRemovedHandler);
    }

    private GameAddedLobbyInfo Map(GameRoom addedRoom){
        GameAddedLobbyInfo params = new GameAddedLobbyInfo();

        params.setRoomName(addedRoom.getGameName());
        params.setPlayerCount(addedRoom.getUsersInGame().size());
        params.setHostName(addedRoom.getHost().getUserName());
        params.setGameUUID(addedRoom.getGameRoomUUID());

        return params;
    }
}
