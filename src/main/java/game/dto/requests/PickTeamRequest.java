package game.dto.requests;

public class PickTeamRequest implements Request {
    public Integer teamSize;
    public String requestName;

    public PickTeamRequest(Integer teamSize){
        this.teamSize = teamSize;
    }


}
