package game.gameBuilder;

import authentication.User;
import game.*;
import lobby.gameroom.GameRoom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameBuilder {
    private final GameRolesProvider rolesProvider;

    public GameBuilder(GameRolesProvider rolesProvider) {
        this.rolesProvider = rolesProvider;
    }

    public Game Build(GameRoom gameRoom){
        List<User> usersInGame = gameRoom.getUsersInGame();
        List<AvalonRole> shuffledRoles = rolesProvider.GetShuffledRoles(usersInGame.size(), gameRoom.getAdditionalRules());

        Collections.shuffle(usersInGame);
        List<Player> gamePlayers = PopulatePlayerList(usersInGame, shuffledRoles);

        PlayerCollection gamePlayerCollection = new PlayerCollection(gamePlayers, 0);







    }

    private List<Player> PopulatePlayerList(List<User> usersInGame, List<AvalonRole> shuffledRoles) {
        List<Player> gamePlayers = new ArrayList<>();

        for(int i = 0 ; i < usersInGame.size() ; i++){
            User currentUser = usersInGame.get(i);
            AvalonRole currentRole = shuffledRoles.get(i);

            gamePlayers.add(new Player(i,currentRole,currentUser));
        }

        return gamePlayers;
    }

    private void AddIdentityInformation(PlayerCollection playerCollection){
        //in normal game rules there should be only one
        List<VisiblePlayer> merlinPlayers = playerCollection.getPlayersWithRoles(AvalonRole.Merin);
        List<VisiblePlayer> evilPlayers = playerCollection.getPlayersWithRoles(AvalonRole.Assasin, AvalonRole.RegularEvil);

        //todo kurwa
        for(Player merlinPlayer : merlinPlayers) {
            merlinPlayer.setIdentityInformation((List<VisiblePlayer>) evilPlayers);

        }



    }
}
