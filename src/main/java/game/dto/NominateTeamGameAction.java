package game.dto;

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
