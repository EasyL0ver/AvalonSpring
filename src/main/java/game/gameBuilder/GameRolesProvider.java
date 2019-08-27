package game.gameBuilder;

import common.AdditionalRules;
import game.AvalonRole;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameRolesProvider {

    private Integer evilSlotsLeft;
    private Integer playerSlotsLeft;

    private final AdditionalRules additionalRules;
    private final List<AvalonRole> outputList;

    public GameRolesProvider(Integer playerCount, Integer evilPlayers, AdditionalRules additionalRules){
        this.playerSlotsLeft = playerCount;
        this.evilSlotsLeft = evilPlayers;

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
}
