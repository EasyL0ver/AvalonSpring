var secondsLeft = 0;
var timerInterval = setInterval(secondElapsedCallback, 1000)
var enabled = false;


function secondElapsedCallback() {
    secondsLeft = secondsLeft - 1;
    if(enabled)
        $('#timer span').text(formatTimer(Math.max(secondsLeft, 0)));
}

function formatTimer(seconds) {
    var timerMinutes = Math.floor(seconds / 60);
    var timerSeconds = seconds % 60;

    if(timerMinutes <= 9)
        timerMinutes = '0'+timerMinutes;

    if(timerSeconds <= 9)
        timerSeconds = '0'+timerSeconds;

    return timerMinutes + ':' + timerSeconds;
}

function setSecondsLeft(seconds){
    enabled =true;
    secondsLeft = seconds;
}