package game;

import game.communication.OutgoingGameCommunicationAPI;
import game.dto.notifications.GamePhaseInfo;
import game.dto.notifications.MissionResult;
import game.dto.notifications.TeamVoteResult;
import game.dto.notifications.ScoreBoard;
import game.exceptions.GameOverException;
import game.exceptions.PhaseFailedException;
import game.gameBuilder.GamePhaseFactory;

import java.util.Map;


public class Game {
    private final PlayerCollection playerCollection;
    private final ScoreTracker scoreTracker;
    private final GamePhaseFactory gamePhaseFactory;
    private final GameRulesProvider gameRulesProvider;
    private final OutgoingGameCommunicationAPI communicationAPI;

    private Integer round = 0;
    private GamePhaseInfo currentGamePhase;

    public Game(PlayerCollection playerCollection, ScoreTracker scoreTracker, GamePhaseFactory gamePhaseFactory, GameRulesProvider provider, OutgoingGameCommunicationAPI communicationAPI) {
        this.playerCollection = playerCollection;
        this.scoreTracker = scoreTracker;
        this.gamePhaseFactory = gamePhaseFactory;
        this.gameRulesProvider = provider;
        this.communicationAPI = communicationAPI;
    }

    public ScoreBoard getScore(){
        return scoreTracker.getScoreBoard();
    }

    public GamePhaseInfo getGamePhaseInfo(){
        return currentGamePhase;
    }

    public PlayerCollection getPlayerCollection(){
        return playerCollection;
    }

    void Start() throws InterruptedException, PhaseFailedException {

        Thread.sleep(1000*5);

        try {
            while(true){

                PlayerTeam playerTeam = chooseTeam();

                GamePhase<Map<Integer, Boolean>> missionPhase = gamePhaseFactory.BuildMissionPhase(playerCollection, playerTeam);

                currentGamePhase = missionPhase.getGamePhaseChangedInfo();

                Map<Integer, Boolean> missionResult = missionPhase.resolve();
                Boolean outcome = gameRulesProvider.getMissionVoteResultStrategy(round, playerCollection.GetPlayerCount()).resolveVoteResult(missionResult.values());

                NotifyMissionResult(missionResult, outcome);

                if(outcome)
                    scoreTracker.incrementGood();
                else
                    scoreTracker.incrementEvil();

                round++;
                playerCollection.activePlayerMoveNext();
            }
        } catch (GameOverException e) {
            GamePhase<Boolean> revealPhase = gamePhaseFactory.BuildRevealPhase(playerCollection, e.getOutcome());
            Boolean result = revealPhase.resolve();

        } catch (PhaseFailedException e) {
            e.printStackTrace();
        }
    }

    private PlayerTeam proposeTeam() throws GameOverException, InterruptedException {
        PlayerTeam proposedTeam = null;

        while(proposedTeam == null){
            try{
                GamePhase<PlayerTeam> pickTeamGamePhase = gamePhaseFactory.BuildPickingPhase(playerCollection, round);
                currentGamePhase = pickTeamGamePhase.getGamePhaseChangedInfo();
                proposedTeam = pickTeamGamePhase.resolve();
            }catch (PhaseFailedException e){
                playerCollection.activePlayerMoveNext();
                scoreTracker.reportFailure();
            }
        }

        return proposedTeam;
    }

    private PlayerTeam chooseTeam() throws GameOverException, InterruptedException, PhaseFailedException {
        PlayerTeam decidedTeam = null;

        while(decidedTeam == null){
            PlayerTeam proposedTeam = proposeTeam();
            GamePhase<Map<Integer, Boolean>> voteOnTeamPhase = gamePhaseFactory.BuildVotePhase(playerCollection, proposedTeam);
            currentGamePhase = voteOnTeamPhase.getGamePhaseChangedInfo();
            Map<Integer, Boolean> outcome = voteOnTeamPhase.resolve();

            NotifyVoteResult(outcome);

            if(gameRulesProvider.getTeamVoteResultStrategy().resolveVoteResult(outcome.values()))
                decidedTeam = proposedTeam;
            else{
                scoreTracker.reportFailure();
                playerCollection.activePlayerMoveNext();
            }
        }
        return decidedTeam;
    }

    private void NotifyMissionResult(Map<Integer, Boolean> result, Boolean outcome){
        MissionResult missionResult = new MissionResult();
        missionResult.outcome = outcome;
        missionResult.fails = (int) result.values().stream().filter(p -> !p).count();
        missionResult.successes = (int) result.values().stream().filter(p -> p).count();

        communicationAPI.NotifyMissionResult(playerCollection.getPlayerList().values(), missionResult);
    }

    private void NotifyVoteResult(Map<Integer, Boolean> result){
        TeamVoteResult teamVoteResult = new TeamVoteResult();
        teamVoteResult.voteResults = result;

        communicationAPI.NotifyTeamVoteResult(playerCollection.getPlayerList().values(), teamVoteResult);
    }
}
