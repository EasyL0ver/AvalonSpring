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
    });
}

function showGreeting(message) {
    lobbyGames.append(message);

    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}

function onGameAdded(addedParams) {
    console.log("game added");
    console.log(addedParams);

    var parsed = JSON.parse(addedParams.body);
    var stringrep = parsed.roomName + "hosted by: " + parsed.hostName

    console.log(stringrep);

    $("#greetings").append("<tr><td>" + stringrep + "</td></tr>");
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

