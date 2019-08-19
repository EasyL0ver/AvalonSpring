package lobby;

import authentication.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MyGameRoom implements GameRoom {
    private final User gameHost;
    private final UUID gameUUID;
    private final List<User> gameUsers;
    private final String gameName;

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
        //todo notification
    }

    @Override
    public UUID getGameRoomUUID() {
        return gameUUID;
    }

    @Override
    public List<User> getUsersInGame() {
        return gameUsers;
    }

    public String getGameName() {
        return gameName;
    }

    @Override
    public String getHostName() {
        return gameHost.getUserName();
    }
}
