package game.gameBuilder;

import common.AdditionalRules;
import game.AvalonRole;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameRolesProvider {

    private Integer evilSlotsLeft = 0;
    private Integer playerSlotsLeft = 0;

    private final AdditionalRules additionalRules;
    private final List<AvalonRole> outputList;

    public GameRolesProvider(Integer playerCount, AdditionalRules additionalRules){
        this.playerSlotsLeft = playerCount;
        this.evilSlotsLeft = evilPlayersInGame(playerCount);

        outputList = new ArrayList<>();
        this.additionalRules = additionalRules;
    }

    List<AvalonRole> GetShuffledRoles(){

        AddMerlin();
        AddAssassin();
        FillWithRegularEvil();
        FillWithRegularGood();

        Collections.shuffle(outputList);

        return outputList;
    }

    private void AddMerlin(){
        playerSlotsLeft--;
        outputList.add(AvalonRole.Merin);
    }

    private void AddAssassin(){
        playerSlotsLeft--;
        evilSlotsLeft--;
        outputList.add(AvalonRole.Assasin);
    }

    private void FillWithRegularEvil(){
        for(int i = 0; i < evilSlotsLeft ; i++)
        {
            outputList.add(AvalonRole.RegularEvil);
            evilSlotsLeft--;
            playerSlotsLeft--;
        }
    }

    private void FillWithRegularGood(){
        for(int i = 0; i < playerSlotsLeft ; i++)
        {
            outputList.add(AvalonRole.RegularGood);
            playerSlotsLeft--;
        }
    }


    private Integer evilPlayersInGame(Integer count){
        Integer val = 0;
        switch (count){
            case 1:
            case 2:
            case 3:
            case 4:
                val =  1;
                break;
            case 5:
            case 6:
            case 7:
                val =  2;
                break;
            case 8:
            case 9:
                val =  3;
                break;
            case 10:
                val =  4;
                break;
        }

        return val;
    }



}
