var stompClient = null;


function connect(api_key) {
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        authorize(api_key)
        console.log('Connected: ' + frame);
        //stompClient.subscribe('/topic/greetings', function (greeting) {
         //   showGreeting(JSON.parse(greeting.body).content);
        //});
    });
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

