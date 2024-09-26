package com.reactordevelopment.ImperiumLite.core.gameTypes;

import com.reactordevelopment.ImperiumLite.core.mapping.Map;
import com.reactordevelopment.ImperiumLite.core.player.Player;

public class HistoricalGame extends Game {
    private final String timeline;
    private final int year;

    public HistoricalGame(int[] playerTypes, Map map, String timeline, int year, boolean debug) {
        super(playerTypes, map, GameMode.HISTORICAL, debug);
        this.timeline = timeline;
        this.year = year;
    }

    public String getTimeline(){return timeline;}
    public int getYear(){return year;}

    protected void givePlayerTurnResources() {
        Player currPlayer = players[currPlayerIdx];
        double turnIncome = currPlayer.totalIncome() * currPlayer.getOpsEfficiency() + currPlayer.getInfamy();
        currPlayer.modMonetae((int) (turnIncome));
    }
}
