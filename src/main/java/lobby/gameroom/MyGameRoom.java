package lobby.gameroom;

import authentication.User;
import common.AdditionalRules;
import common.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MyGameRoom implements GameRoom {
    private final User gameHost;
    private final UUID gameUUID;
    private final List<User> gameUsers;
    private final String gameName;

    private final Event<GameRoom> gameAbortedEvent = new Event<>();
    private final Event<GameRoom> playerListChangedEvent = new Event<>();

    public MyGameRoom(User gameHost, UUID gameRoomUUID, String gameName){
        this.gameHost = gameHost;
        this.gameUUID = gameRoomUUID;
        this.gameName = gameName;

        gameUsers = new ArrayList<>();
        gameUsers.add(gameHost);
    }

    @Override
    public void join(User user) {
        gameUsers.add(user);
        playerListChangedEvent.Invoke(this);
    }

    @Override
    public void leave(User user) {
        gameUsers.remove(user);

        if(user == gameHost){
            gameAbortedEvent.Invoke(this);
        }else {
            playerListChangedEvent.Invoke(this);
        }
    }

    @Override
    public UUID getGameRoomUUID() {
        return gameUUID;
    }

    @Override
    public List<User> getUsersInGame() {
        return gameUsers;
    }

    @Override
    public Event<GameRoom> getGameAbortedEvent() {
        return gameAbortedEvent;
    }

    @Override
    public Event<GameRoom> getPlayerListChangedEvent() {
        return playerListChangedEvent;
    }

    public String getGameName() {
        return gameName;
    }

    @Override
    public AdditionalRules getAdditionalRules() {
        return new AdditionalRules();
    }

    @Override
    public User getHost() { return gameHost; }
}
