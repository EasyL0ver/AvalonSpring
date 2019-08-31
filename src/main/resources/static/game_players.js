function updatePlayerView(){
    $("#playersList").empty();

    viewModel.players.forEach(function(player){

        var isEvil = viewModel.identityInfo != null && viewModel.identityInfo.contains(player.playerId);

        var htmlPlayer ='<div class="player" data-nickname=' + player.playerName + ' ' + 'data-id=' + player.playerId + '>' +
            '<div class="nickname">' + player.playerName + '</div>' +
            '<div class="isGood">' + isEvil +'</div>' +
            '</div>'

        $("#playersList").append(htmlPlayer);

    })

}

