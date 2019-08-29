var stompClient = null;
var usersInGame = [];

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

function stomp_connect(roomUUID) {
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);

        var playerListRefreshedUrl = "/topic/lobby/room/" + roomUUID + "/users";
        var gameAbortedUrl = "/topic/lobby/room/" + roomUUID + "/abort";
        var gameStartedUrl = "/topic/lobby/room/" + roomUUID + "/started/" + apk;

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
        stompClient.subscribe(gameStartedUrl, function (removedInfo) {
            console.log(removedInfo);
        });
    });
}


