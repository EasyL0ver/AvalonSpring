var stompClient = null;
var usersInGame = [];

function updateGamesView() {
    console.log(usersInGame);

    $(".players-list").empty();
    usersInGame.forEach(function (value) {
        console.log(value);

        var htmlElement = '<li>' +
                              '<span class="p-name">' + value + '</span>' +
                                  '<span class="p-winrate"> 50% </span>' +
                              '</span>' +
                          '</li>';

        $(".players-list").append(htmlElement)
    })
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

        console.log(playerListRefreshedUrl);
        console.log(gameAbortedUrl);

        stompClient.subscribe(playerListRefreshedUrl, function (playerList) {
            console.log("updated");
            console.log(playerList);

            usersInGame = JSON.parse(playerList.body);
            updateGamesView();
        });
        stompClient.subscribe(gameAbortedUrl, function (removedInfo) {
            var redirectBackToLobbyURL = window.location.origin + '/lobby?apiKey=' + apk;
            window.location.replace(redirectBackToLobbyURL);
        });
        stompClient.subscribe(gameStartedUrl, function (removedInfo) {
            var startGameRedirectURL = window.location.origin + '/game?apiKey=' + apk + '&gameUUID=' + roomUUID;
            window.location.replace(startGameRedirectURL);
        });
    });
}


