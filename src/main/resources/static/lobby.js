var stompClient = null;
var lobbyGames = [];


function connect(api_key) {
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        authorize(api_key)
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/lobby/added', function (addedInfo) {
            onGameAdded(addedInfo)
        });
        stompClient.subscribe('/topic/lobby/removed', function (removedInfo) {
            onGameRemoved(JSON.parse(removedInfo.body))
        });
    });
}

function onGameRemoved(removedUUID) {
    console.log("game removed");
    console.log(removedUUID);

    lobbyGames = lobbyGames.filter(function (lobbyGame) {
        return lobbyGame.gameUUID !== removedUUID;
    });

    updateGamesView();
}

function onGameAdded(addedParams) {
    console.log("game added");
    console.log(addedParams);

    lobbyGames.push(JSON.parse(addedParams.body));

    updateGamesView();
}

function updateGamesView() {
    console.log(lobbyGames)

    console.log($("#greetings"));

    var new_table_body = document.createElement("tbody")
    new_table_body.id = "greetings";

    console.log(new_table_body)

    lobbyGames.forEach(function(lobbyGame){
        new_table_body.append(lobbyGame.roomName)
    })

    var tableBody = document.getElementById("greetings")
    var parentNode = tableBody.parentNode

    parentNode.replaceChild(new_table_body, tableBody)

}

function authorize(api_key){
    console.log(api_key)
    var jsonObject = JSON.stringify({'userApiKey': api_key});
    console.log(jsonObject)
    stompClient.send("/app/lobbyreg", {}, jsonObject);
}

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

