var stompClient = null;
var apiKey = null;
var gameUUID = null;
var viewModel = null;



var HttpClient = function() {
    this.get = function(aUrl, aCallback) {
        var anHttpRequest = new XMLHttpRequest();
        anHttpRequest.onreadystatechange = function() {
            if (anHttpRequest.readyState == 4 && anHttpRequest.status == 200)
                aCallback(anHttpRequest.responseText);
        };

        anHttpRequest.open( "GET", aUrl, true );
        anHttpRequest.send( null );
    }
};

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    console.log("Disconnected");
}

var Initialize = function initialize() {
    var client = new HttpClient();
    //todo relative url
    //todo pass api key in body
    client.get('http://localhost:9090/game/state?gameUUID=' + gameUUID + '&apiKey=' + apiKey, function (response) {
        console.log("response");
        console.log(response);

        viewModel = JSON.parse(response);
        console.log(viewModel);

        stomp_connect();
    });
};

function stomp_connect() {
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);

        var phaseChangedEventUrl = "/topic/game/user-specific/" + apiKey + "/phase-changed";
        var scoreChangedEventUrl = "/topic/game/user-specific/" + apiKey + "/score-changed";
        var gameEndedEventUrl = "/topic/game/user-specific/" + apiKey + "/game-ended";

        stompClient.subscribe(phaseChangedEventUrl, function (addedInfo) {
            viewModel.gamePhaseInfo = JSON.parse(addedInfo.body);
            console.log(viewModel);

            //todo for testing

            if(viewModel.gamePhaseInfo.gamePhaseType == "PickTeam")
            {
                var testAnswer = [0];
                nominateTeam(testAnswer);
            }
            if(viewModel.gamePhaseInfo.gamePhaseType == "VoteTeam")
            {
                voteTeam(true,2);
            }

            if(viewModel.gamePhaseInfo.gamePhaseType == "Mission")
            {
                voteTeam(true,3);
            }

            if(viewModel.gamePhaseInfo.gamePhaseType == "GoodWonAssassination")
            {
                assasinate(0);
            }

        });

        stompClient.subscribe(scoreChangedEventUrl, function (addedInfo) {
            viewModel.scoreBoard = JSON.parse(addedInfo.body);
            console.log(viewModel);
        });

        stompClient.subscribe(gameEndedEventUrl, function (addedInfo) {
            viewModel.scoreBoard = JSON.parse(addedInfo.body);
            console.log(viewModel);
        });
    })}

function nominateTeam(nominatedPlayersIds){
    var payload = {'nominatedPlayers': nominatedPlayersIds
        , 'playerId' : viewModel.myId
        , 'userApiKey' : apiKey
        , 'gameUUID' : gameUUID
        , 'currentGamePhase' : 1};

    console.log(payload);

    stompClient.send("/app/game/action/nominate", {}, JSON.stringify(payload));
}

function voteTeam(result, phase){
    var payload = {'vote': result
        , 'playerId' : viewModel.myId
        , 'userApiKey' : apiKey
        , 'gameUUID' : gameUUID
        , 'currentGamePhase' : phase};

    console.log(payload);

    stompClient.send("/app/game/action/vote", {}, JSON.stringify(payload));
}


function assasinate(playerId){
    var payload = {'killedPlayerId;': playerId
        , 'playerId' : viewModel.myId
        , 'userApiKey' : apiKey
        , 'gameUUID' : gameUUID
        , 'currentGamePhase' : 5};

    console.log(payload);

    stompClient.send("/app/game/action/assassinate", {}, JSON.stringify(payload));
}