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

var Initialize = function initialize(roomUUID, api_key) {
    var client = new HttpClient();
    //todo relative url
    //todo pass api key in body
    client.get('http://localhost:9090/game/state?gameUUID=' + roomUUID + '&apiKey=' + api_key, function (response) {
        console.log("response");
        console.log(response);

        var parsedResponse = JSON.parse(response);

        console.log(parsedResponse)


        //updateGamesView();
        stomp_connect(roomUUID, api_key)
    });
};


function stomp_connect(roomUUID, api_key) {
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe("/topic/game/user-specific/" + api_key, function (addedInfo) {
            console.log(addedInfo)
        });
        stompClient.subscribe('/topic/lobby/removed', function (removedInfo) {
            onGameRemoved(JSON.parse(removedInfo.body))
        });
    })}