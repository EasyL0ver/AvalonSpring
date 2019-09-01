var stompClient = null;
var apiKey = null;
var gameUUID = null;
var viewModel = null;


function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    console.log("Disconnected");
}

function stomp_connect() {
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);

        var phaseChangedEventUrl = "/topic/game/user-specific/" + apiKey + "/phase-changed";
        var scoreChangedEventUrl = "/topic/game/user-specific/" + apiKey + "/score-changed";
        var gameEndedEventUrl = "/topic/game/user-specific/" + apiKey + "/game-ended";
        var voteResultEventUrl = "/topic/game/user-specific/" + apiKey + "/vote-result";
        var missionResultEventUrl = "/topic/game/user-specific/" + apiKey + "/mission-result";

        stompClient.subscribe(phaseChangedEventUrl, function (addedInfo) {
            viewModel.gamePhaseInfo = JSON.parse(addedInfo.body);
            console.log(viewModel);

            var phaseName = viewModel.gamePhaseInfo.gagamePhaseType;

            if(phaseName === "EvilWonReveal" || phaseName === "GoodWonAssassination"){
                //evil player reveal
                updatePlayerView();
            }

            setSecondsLeft(viewModel.gamePhaseInfo.timeoutSeconds)
            updateTeamSizeCounter();
            playersChangePhase();
            updateActionBar();
            updateTooltip();
        });

        stompClient.subscribe(scoreChangedEventUrl, function (addedInfo) {
            viewModel.scoreBoard = JSON.parse(addedInfo.body);
            console.log(viewModel);
            updateScoreBoard();
        });

        stompClient.subscribe(gameEndedEventUrl, function (addedInfo) {
            console.log(viewModel);
            popupGameOver(JSON.parse(addedInfo.body));
            var redirectBackToLobbyURL = window.location.origin + '/lobby?apiKey=' + apiKey;
            window.location.replace(redirectBackToLobbyURL);
        });

        stompClient.subscribe(voteResultEventUrl, function (addedInfo) {
            console.log(viewModel);
            popupVoteResult(JSON.parse(addedInfo.body));
        });

        stompClient.subscribe(missionResultEventUrl, function (addedInfo) {
            console.log(viewModel);
            popupMissionResult(JSON.parse(addedInfo.body));
        })
    })}
