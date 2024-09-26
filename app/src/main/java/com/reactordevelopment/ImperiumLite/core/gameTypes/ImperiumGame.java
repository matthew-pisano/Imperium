package com.reactordevelopment.ImperiumLite.core.gameTypes;

import com.reactordevelopment.ImperiumLite.core.mapping.Map;
import com.reactordevelopment.ImperiumLite.core.player.Player;

public class ImperiumGame extends Game {
    
    public ImperiumGame(int[] playerTypes, Map map, boolean debug) {
        super(playerTypes, map, GameMode.IMPERIUM, debug);
    }
    
    protected void givePlayerTurnResources() {
        Player currPlayer = players[currPlayerIdx];
        double turnIncome = currPlayer.totalIncome() + currPlayer.getInfamy() + map.hegemonyBonus(currPlayer.getAllOwned());
        currPlayer.modMonetae((int) turnIncome);
    }
}
