var pickablePlayers = 0;

function updatePlayerView(){
    $("#playersList").empty();

    viewModel.players.forEach(function(player){
        var additionalInfo = "Nieznany";
        var evil = isEvil(player.playerId);
        var knowsEvil =  (viewModel.role == "Merlin" || viewModel.role =="Assassin" || viewModel.role == "RegularEvil");

        if(evil)
            additionalInfo = 'Zły';
        if(!evil && knowsEvil)
            additionalInfo = 'Dobry';

        var htmlPlayer ='<div class="player" data-nickname="' + player.playerName + '" ' + 'data-id="' + player.playerId + '">' +
        '<div class="nickname">' + player.playerName + '</div>' +
        '<div class="isGood">' + additionalInfo +'</div>' +
        '</div>'

        $("#playersList").append(htmlPlayer);
    })

}

function isEvil(playerId){
    if(viewModel.identityInfo == null)
        return false;

    var evil = false;
    viewModel.identityInfo.forEach(function(evilId){
        if(evilId === playerId)
            evil = true;
    });

    if(viewModel.gamePhaseInfo == null)
        return evil;

    if(viewModel.gamePhaseInfo.knownEvilPlayers != null)
        viewModel.gamePhaseInfo.knownEvilPlayers.forEach(function (evilPlayer) {
            if(playerId === evilPlayer)
                evil = true
        })

    return evil;
}


function playersChangePhase(){
    $('.player.active').removeClass('active');

    if(viewModel.gamePhaseInfo == null)
        return;

    if(viewModel.gamePhaseInfo.gamePhaseType === "PickTeam" && viewModel.gamePhaseInfo.playerPickingTeamId === viewModel.myId){
        pickablePlayers = viewModel.gamePhaseInfo.teamSize;
        return;
    }

    if(viewModel.gamePhaseInfo.gamePhaseType === "GoodWonAssassination" && viewModel.role === "Assassin"){
        pickablePlayers = 1;
        return;
    }

    if(viewModel.gamePhaseInfo.currentTeam == null){
        pickablePlayers = 0;
        return;
    }

    var highlightedPlayersId = viewModel.gamePhaseInfo.currentTeam;

    highlightedPlayersId.forEach(function(id){
        $('.player[data-id="' + id +'"]').addClass('active');
    });

    pickablePlayers = 0;

}

