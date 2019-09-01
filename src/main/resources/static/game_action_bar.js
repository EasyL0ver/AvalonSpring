function acceptChoice(){
    var pickedPlayers = $('.player.active')

    if(pickedPlayers.length != viewModel.gamePhaseInfo.teamSize)
    {
        alert('Nieodpowiednia liczba graczy w drużynie, wybierz: ' + viewModel.gamePhaseInfo.teamSize + ' graczy');
        return;
    }


    var ids = $.map($('.player.active'), function(el) {
        return $(el).data('id')
    });

    nominateTeam(ids);
}

function acceptAssasinate(){
    var pickedPlayers = $('.player.active')

    if(pickedPlayers.length != 1)
    {
        alert('Musisz wytypować dokładnie jednego Merlina!');
        return;
    }

    var ids = $.map($('.player.active'), function(el) {
        return $(el).data('id')
    });

    assasinate(ids[0]);
}


function nominateTeam(nominatedPlayersIds){
    var payload = {'nominatedPlayers': nominatedPlayersIds
        , 'playerId' : viewModel.myId
        , 'userApiKey' : apiKey
        , 'gameUUID' : gameUUID
        , 'currentGamePhase' : 1};

    console.log(payload);

    stompClient.send("/app/game/action/nominate", {}, JSON.stringify(payload));
}

function voteTeam(result, phase){
    var payload = {'vote': result
        , 'playerId' : viewModel.myId
        , 'userApiKey' : apiKey
        , 'gameUUID' : gameUUID
        , 'currentGamePhase' : phase};

    console.log(payload);

    stompClient.send("/app/game/action/vote", {}, JSON.stringify(payload));
}


function assasinate(playerId){
    var payload = {'killedPlayerId;': playerId
        , 'playerId' : viewModel.myId
        , 'userApiKey' : apiKey
        , 'gameUUID' : gameUUID
        , 'currentGamePhase' : 5};

    console.log(payload);

    stompClient.send("/app/game/action/assassinate", {}, JSON.stringify(payload));
}

function updateActionBar(){
    if(viewModel.gamePhaseInfo == null)
        return;

    var body = $('#decisions')

    body.empty();

    var gamePhase = viewModel.gamePhaseInfo.gamePhaseType;

    if(gamePhase == "PickTeam"){
        if(viewModel.gamePhaseInfo.playerPickingTeamId == viewModel.myId)
            body.append(acceptHtml())
    }else if(gamePhase == "VoteTeam"){
        body.append(teamVoteHtml());
    }else if(gamePhase == "Mission"){
        viewModel.gamePhaseInfo.currentTeam.forEach(function (id) {
            if(id == viewModel.myId)
                body.append(missionHtml());
        })
    }else if(gamePhase == "EvilWonReveal"){

    }else if(gamePhase == "GoodWonAssassination"){
        if(viewModel.role == "Assassin")
            body.append(acceptAssassinateHtml())
    }
}

function acceptHtml(){
    return  '<div class="cards-pair"><button id="akcept" onclick="acceptChoice()">Akceptuj wybór drużyny</button></div>'
}

function acceptAssassinateHtml(){
    return  '<div class="cards-pair"><button id="akcept" onclick="acceptAssasinate()">Akceptuj wybór merlina</button></div>'
}

function teamVoteHtml(){
    return  '<div class="cards-pair"><img src="/zgoda.png" id="zgoda" onclick="voteTeam(true, 2)"/><img src="/sprzeciw.png" id="sprzeciw" onclick="voteTeam(false, 2)"/></div>'
}

function missionHtml(){
    return  '<div class="cards-pair"><img src="/sukces.png" id="sukces" onclick="voteTeam(true, 3)"/><img src="/porazka.png" id="porazka" onclick="voteTeam(false, 3)"/></div>'

}
