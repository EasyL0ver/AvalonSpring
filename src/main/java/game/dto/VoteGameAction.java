package game.dto;

public class VoteGameAction extends GameAction {
    private boolean vote;

    public boolean isVote() {
        return vote;
    }

    public void setVote(boolean vote) {
        this.vote = vote;
    }
}
