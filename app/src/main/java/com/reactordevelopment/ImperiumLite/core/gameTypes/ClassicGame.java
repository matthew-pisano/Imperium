package com.reactordevelopment.ImperiumLite.core.gameTypes;

import com.reactordevelopment.ImperiumLite.core.mapping.Map;
import com.reactordevelopment.ImperiumLite.core.player.Player;

public class ClassicGame extends Game {

    private int turnStartStage;

    public ClassicGame(int[] playerTypes, Map map, boolean debug) {
        super(playerTypes, map, GameMode.CLASSIC, debug);
        turnStartStage = -1;
    }

    protected int getTurnStartStage(){ return turnStartStage; }
    
    protected void givePlayerTurnResources() {
        Player currPlayer = players[currPlayerIdx];
        double turnTroops = 3 + map.bonuses(currPlayer) + currPlayer.getInfamy() + map.hegemonyBonus(currPlayer.getAllOwned());
        currPlayer.modTroops((int) (turnTroops));
    }
}
