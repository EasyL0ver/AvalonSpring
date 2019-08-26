var stompClient = null;
var usersInGame = [];

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
var Initialize = function initialize(roomUUID) {
    var client = new HttpClient();
    //todo relative url
    client.get('http://localhost:9090/lobby/room/all?gameRoomUUID=' + roomUUID, function (response) {
        console.log("response");
        console.log(response);

        var responses = JSON.parse(response)

        responses.forEach(function (response) {
            usersInGame.push(response)
        });

        console.log(usersInGame)

        updateGamesView();
        stomp_connect(roomUUID)
    });
};

function createJoinGameForm(lobbyGame){
    var joinGameForm = document.createElement("form");
    joinGameForm.action = "/lobby/join_game";
    joinGameForm.method = "POST";

    var buttonInput = document.createElement("input");
    buttonInput.type="submit";
    buttonInput.value="Join";

    var hiddenApiKeyInput = document.createElement("input");
    hiddenApiKeyInput.type="hidden";
    hiddenApiKeyInput.name="userApiKey";
    hiddenApiKeyInput.value=apk;

    var hiddenGameUUidInput = document.createElement("input");
    hiddenGameUUidInput.type="hidden";
    hiddenGameUUidInput.name="joinedGameUUID";
    hiddenGameUUidInput.value=lobbyGame.gameUUID;

    joinGameForm.appendChild(buttonInput);
    joinGameForm.appendChild(hiddenGameUUidInput);
    joinGameForm.appendChild(hiddenApiKeyInput);

    return joinGameForm;
}

function createLobbyTable(usersInGame){
    var new_table_body = document.createElement("tbody");
    new_table_body.id = "greetings";

    usersInGame.forEach(function(lobbyGame){
        var row = document.createElement("tr");
        var name_cell = document.createElement("td");
        name_cell.innerHTML = lobbyGame;

        row.appendChild(name_cell);

        new_table_body.appendChild(row)
    });

    return new_table_body
}

function updateGamesView() {

    var new_table_body = createLobbyTable(usersInGame)
    var tableBody = document.getElementById("greetings")
    var parentNode = tableBody.parentNode

    console.log(new_table_body)
    parentNode.replaceChild(new_table_body, tableBody)
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    console.log("Disconnected");
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

function stomp_connect(roomUUID) {
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);

        var playerListRefreshedUrl = "/topic/lobby/room/" + roomUUID + "/users";
        var gameAbortedUrl = "/topic/lobby/room/" + roomUUID + "/abort"

        console.log(playerListRefreshedUrl)
        console.log(gameAbortedUrl)

        stompClient.subscribe(playerListRefreshedUrl, function (playerList) {
            console.log("updated");
            console.log(playerList);

            usersInGame = JSON.parse(playerList.body);
            updateGamesView();
        });
        stompClient.subscribe(gameAbortedUrl, function (removedInfo) {
            console.log(removedInfo);
        });
    });
}


