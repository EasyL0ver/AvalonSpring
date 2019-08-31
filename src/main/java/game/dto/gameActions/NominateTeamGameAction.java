package game.dto.gameActions;

import game.dto.gameActions.GameAction;

import java.util.List;

public class NominateTeamGameAction extends GameAction {
    private List<Integer> nominatedPlayers;

    public List<Integer> getNominatedPlayers() {
        return nominatedPlayers;
    }

    public void setNominatedPlayers(List<Integer> nominatedPlayers) {
        this.nominatedPlayers = nominatedPlayers;
    }
}
