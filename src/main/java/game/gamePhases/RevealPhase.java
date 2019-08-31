package game.gamePhases;

import common.EventHandler;
import game.AvalonRole;
import game.GamePhase;
import game.Player;
import game.PlayerCollection;
import game.communication.OutgoingGameCommunicationAPI;
import game.dto.gameActions.AssasinateGameAction;
import game.dto.gameActions.GameAction;
import game.dto.notifications.GamePhaseInfo;
import game.dto.GamePhaseType;
import game.exceptions.PhaseFailedException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Game phase in which according to the score results evil players are revealed and given chance to
 * assassinate Merlin player
 */
public class RevealPhase implements GamePhase<Boolean> {
    /**
     * Score outcome of the game, true is won by good false is won by evil
     */
    private final Boolean outcome;
    private final PlayerCollection playerCollection;
    private final GamePhaseType gamePhaseType;
    private final OutgoingGameCommunicationAPI communicationAPI;
    private final List<Integer> evilPlayersIds;
    private final Long pollOffsetMillis = 300L;

    private final EventHandler<GameAction> gameActionEventHandler = new EventHandler<GameAction>() {
        @Override
        public void Handle(GameAction params) {
            if(!(params instanceof AssasinateGameAction))
                return;

            assassinResponse = (AssasinateGameAction) params;
        }
    };

    private AssasinateGameAction assassinResponse = null;

    public RevealPhase(Boolean outcome, PlayerCollection playerCollection, OutgoingGameCommunicationAPI communicationAPI) {
        this.outcome = outcome;
        this.playerCollection = playerCollection;
        this.communicationAPI = communicationAPI;

        if(outcome)
            gamePhaseType = GamePhaseType.GoodWonAssassination;
        else
            gamePhaseType = GamePhaseType.EvilWonReveal;

        evilPlayersIds = playerCollection.getPlayersWithRoles(AvalonRole.Assassin, AvalonRole.RegularEvil).stream().map(Player::getPlayerId).collect(Collectors.toList());
    }

    /**
     * evil players are revealed and depending on an outcome evil players are given chance to assassinate merlin
     * @return final result of the game
     * @throws PhaseFailedException
     * @throws InterruptedException
     */
    @Override
    public Boolean resolve() throws PhaseFailedException, InterruptedException {
        List<Player> assasins = playerCollection.getPlayersWithRoles(AvalonRole.Assassin);
        if(outcome){
            for(Player assassin : assasins)
                assassin.getGameActionReceivedEvent().AttachHandler(gameActionEventHandler);
        }

        communicationAPI.AnnouncePhaseChanged(playerCollection.getPlayerList().values(), getGamePhaseChangedInfo());

        if (outcome) {
            Long timeElapsedMillis = 0L;

            while(assassinResponse == null){

                Thread.sleep(pollOffsetMillis);
                timeElapsedMillis += pollOffsetMillis;

                if(timeElapsedMillis >= 60 * 1000){
                    communicationAPI.NotifyGameOver(playerCollection.getPlayerList().values(), true);
                    return true;
                }

            }

            for(Player assassin : assasins)
                assassin.getGameActionReceivedEvent().DetachHandler(gameActionEventHandler);

            boolean merlinHit = playerCollection.getPlayersWithRoles(AvalonRole.Merlin).stream().filter(m -> m.getPlayerId() == assassinResponse.getKilledPlayerId()).count() >= 1;
            communicationAPI.NotifyGameOver(playerCollection.getPlayerList().values(), !merlinHit);
            return merlinHit;
        }else{

            communicationAPI.NotifyGameOver(playerCollection.getPlayerList().values(), false);
            return false;
        }

    }

    @Override
    public GamePhaseInfo getGamePhaseChangedInfo() {
        return new GamePhaseInfo(gamePhaseType, 60, null, null ,null, evilPlayersIds);
    }
}
