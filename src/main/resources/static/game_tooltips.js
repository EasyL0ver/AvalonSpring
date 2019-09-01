function updateTooltip(){
    if(viewModel.gamePhaseInfo == null)
        return;

    var gamePhase = viewModel.gamePhaseInfo.gamePhaseType;
    var toolTip = null;

    if(gamePhase == "PickTeam"){
        toolTip = createPickTeamToolTip();
    }else if(gamePhase == "VoteTeam"){
        toolTip = "Zagłosuj na wybraną drużynę";
    }else if(gamePhase == "Mission"){
        toolTip = createMissionToolTip();
    }else if(gamePhase == "EvilWonReveal"){
        toolTip = "Poplecznicy Mordreda zwyciężyli";
    }else if(gamePhase == "GoodWonAssassination"){
        toolTip = assassinationToolTip();
    }

    $('.console').text(toolTip)
}

function getPlayerName(id){
    var playerNickname = $('.player[data-id="' + id +'"]').data('nickname');
    return playerNickname;
}


function createPickTeamToolTip(){
    var teamSize = viewModel.gamePhaseInfo.teamSize;
    var playerPickingId = viewModel.gamePhaseInfo.playerPickingTeamId

    if(playerPickingId== viewModel.myId)
        return 'Wybierz drużynę ' + teamSize + ' graczy!';

    return getPlayerName(playerPickingId)+ ' wybiera drużyne';
}


function createMissionToolTip(){
    var currentTeamIds = viewModel.gamePhaseInfo.currentTeam;

    currentTeamIds.forEach(function (id) {
        if(id == viewModel.myId)
            return 'Weź udział w misji';
    });

    return 'Podświetleni gracze biorą udział w misji';
}

function assassinationToolTip(){
    if(viewModel.role == "Assassin")
        return 'Zidentyfikuj merlina aby wygrać grę';

    return 'Poplecznicy mordreda identyfikują merlina';
}


