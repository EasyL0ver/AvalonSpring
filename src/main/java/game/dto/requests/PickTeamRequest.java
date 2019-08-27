package game.dto.requests;

public class PickTeamRequest implements Request {
    public Integer teamSize;
    public String requestName;
    public Integer timeout;

    public PickTeamRequest(Integer teamSize, Integer timeout){
        this.teamSize = teamSize;
        this.requestName = "pick-team";
        this.timeout = timeout;
    }
}
