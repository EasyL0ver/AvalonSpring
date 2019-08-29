var stompClient = null;
var lobbyGames = [];
var apk = null;

function updateGamesView() {
    console.log(lobbyGames);

    $(".gamelist").empty();
    lobbyGames.forEach(function (value) {
        console.log(value);

        var htmlElement = "<li>" +
                            "<span class=\"g-name\">" + value.roomName + "</span>" +
                            "<span class=\"g-players-count-element\">" +
                                "(<span class=\"g-players-count\">" + value.playerCount + "</span>/10)" +
                            "</span>" +
                            '<form action=/lobby/join-game method=post>' +
                                '<button class=g-join-btn> Dołącz </button>' +
                                '<input type="hidden" name="userApiKey" value=' + apk + '>' +
                                '<input type="hidden" name="joinedGameUUID" value=' + value.gameUUID + '>' +
                            "</form>" +
                          "</li>";

        $(".gamelist").append(htmlElement)
    })
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


