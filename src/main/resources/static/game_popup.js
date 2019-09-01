function popupVoteResult(voteResult){
    console.log('vote result');
    console.log(voteResult);
    var voteResultMap = voteResult.voteResults;
    var succesCount = 0;
    var failCount = 0;

    var a = 'Wynik głosowania!\n\n';
    for (var key in voteResultMap){
        if(!voteResultMap.hasOwnProperty(key))
            continue;

        var playerName = $('.player[data-id="' + key +'"]').data('nickname');
        var playerVote = voteResultMap[key];

        if(playerVote)
            succesCount++;
        else
            failCount++;

        var voteString = "Zgoda";
        if(!playerVote)
            voteString = "Sprzeciw";

        a = a + playerName + ': ' + voteString + '\n';
    }

    a = a + "Liczba zgód: " + succesCount + " Liczba sprzeciwów: " + failCount + '\n';
    var result = succesCount > failCount;

    if(result)
        a = a + "Wynik głosowania: Pozytywny";
    else
        a = a + "Wynik głosowania: Negatywny";


    alert(a);
}

function popupMissionResult(missionResult){
    var succesMessage = 'Misja zakończona sukcesem\n\n';

    if(!missionResult.outcome)
        succesMessage = "Misja zakończona niepowodzeniem\n\n"

    alert(succesMessage + 'Sukcesy: ' + missionResult.successes +  '\n Porażki: ' + missionResult.fails)
}

function popupGameOver(gameOverResult){
    if(gameOverResult)
        alert("Koniec gry \n\nDrużyna dobrych zwyciężyła");
    else
        alert("Koniec gry \n\nDrużyna złych zwyciężyła");
}

