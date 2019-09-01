function updateScoreBoard(){
    $('#good-score-counter').text(viewModel.scoreBoard.goodScore);
    $('#evil-score-counter').text(viewModel.scoreBoard.evilScore);
    $('.voting-unpassed').text(viewModel.scoreBoard.failedAttempts);
}

function updateTeamSizeCounter(){
    if(viewModel.gamePhaseInfo == null)
    {
        $('span.mission-number').text(" ");
        return;
    }

    var count = viewModel.gamePhaseInfo.teamSize;
    $('span.mission-number').text(count);
}