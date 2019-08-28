var stompClient = null;
var lobbyGames = [];

function createJoinGameForm(lobbyGame){
    var joinGameForm = document.createElement("form");
    joinGameForm.action = "/lobby/join-game";
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

function createLobbyTable(lobbyGames){
    var new_table_body = document.createElement("tbody");
    new_table_body.id = "greetings";

    lobbyGames.forEach(function(lobbyGame){
        var row = document.createElement("tr");
        var name_cell = document.createElement("td");
        name_cell.innerHTML = lobbyGame.roomName;

        var other_cell = document.createElement("td");
        other_cell.appendChild(createJoinGameForm(lobbyGame));

        row.appendChild(name_cell);
        row.appendChild(other_cell);

        new_table_body.appendChild(row)
    });

    return new_table_body
}

function updateGamesView() {
    console.log(lobbyGames);
    console.log($("#greetings"));

    var new_table_body = createLobbyTable(lobbyGames)
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

function stomp_connect() {
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/lobby/added', function (addedInfo) {
            onGameAdded(addedInfo)
        });
        stompClient.subscribe('/topic/lobby/removed', function (removedInfo) {
            onGameRemoved(JSON.parse(removedInfo.body))
        });
    })};


