package game.gameBuilder;

import authentication.User;
import game.*;
import game.notifications.PlayerInfo;
import lobby.gameroom.GameRoom;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class GameBuilder {

    public Game Build(GameRoom gameRoom){
        List<User> usersInGame = gameRoom.getUsersInGame();
        GameRolesProvider gameRolesProvider = new GameRolesProvider(usersInGame.size(), gameRoom.getAdditionalRules());
        List<AvalonRole> shuffledRoles = gameRolesProvider.GetShuffledRoles();

        Collections.shuffle(usersInGame);
        Map<UUID, Player> gamePlayers = PopulatePlayerList(usersInGame, shuffledRoles);

        PlayerCollection gamePlayerCollection = new PlayerCollection(gamePlayers, 0);
        GameRules gameRules = new GameRules(usersInGame.size());
        ScoreTracker scoreTracker = new ScoreTracker(3, 5);

        AddIdentityInformation(gamePlayerCollection);

        return new Game(gamePlayerCollection, scoreTracker, gameRules);
    }

    private Map<UUID, Player> PopulatePlayerList(List<User> usersInGame, List<AvalonRole> shuffledRoles) {
        Map<UUID, Player> gamePlayers = new HashMap<>();

        for(int i = 0 ; i < usersInGame.size() ; i++){
            User currentUser = usersInGame.get(i);
            AvalonRole currentRole = shuffledRoles.get(i);

            gamePlayers.put(currentUser.getUserApiKey(), new Player(i,currentRole,currentUser));
        }

        return gamePlayers;
    }

    private void AddIdentityInformation(PlayerCollection playerCollection){
        List<Player> playersWhoKnowEvil = playerCollection.getPlayersWithRoles(AvalonRole.Merin, AvalonRole.RegularEvil, AvalonRole.Assasin);
        List<Player> evilPlayers = playerCollection.getPlayersWithRoles(AvalonRole.Assasin, AvalonRole.RegularEvil);

        List<VisiblePlayer> evilPlayerInfo = evilPlayers.stream().map(player -> new PlayerInfo(player.getPlayerName(), player.getPlayerId())).collect(Collectors.toList());

        for(Player player : playersWhoKnowEvil){
            player.setIdentityInformation(evilPlayerInfo);
        }
    }
}
