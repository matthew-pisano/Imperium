package com.reactordevelopment.ImperiumLite.core.player;

import java.util.ArrayList;


public class PlayerStats {
    private final ArrayList<PlayerTurnStats> statSeries;

    public PlayerStats() {
        this.statSeries = new ArrayList<>();
    }

    public ArrayList<PlayerTurnStats> getStatSeries(){ return statSeries; }
    public void addTurnStats(int provincesOwned, int playersConquered, int troops, int infamy, int monetae, int continentsOwned){
        statSeries.add(new PlayerTurnStats(provincesOwned, playersConquered, troops, infamy, monetae, continentsOwned));
    }
    public void addTurnStats(PlayerTurnStats pts){ statSeries.add(pts); }

    private static PlayerStats deserialize(String data) {
        PlayerStats pStats = new PlayerStats();
        for (String s : data.split(";"))
            pStats.addTurnStats(PlayerTurnStats.deserialize(s));
        return pStats;
    }

    public String serialize(){
        StringBuilder sb = new StringBuilder();
        for(PlayerTurnStats pts : statSeries)
            sb.append(pts.serialize()).append(";");
        return sb.toString();
    }

    public static class PlayerTurnStats {
        private final int provincesOwned;
        private final int playersConquered;
        private final int troops;
        private final int infamy;
        private final int monetae;
        private final int continentsOwned;

        public PlayerTurnStats(int provincesOwned, int playersConquered, int troops, int infamy, int monetae, int continentsOwned){
            this.provincesOwned = provincesOwned;
            this.playersConquered = playersConquered;
            this.troops = troops;
            this.infamy = infamy;
            this.monetae = monetae;
            this.continentsOwned = continentsOwned;
        }

        public int getProvincesOwned(){return provincesOwned;}
        public int getPlayersConquered(){return playersConquered;}
        public int getTroops(){return troops;}
        public int getInfamy(){return infamy;}
        public int getMonetae(){return monetae;}
        public int getContinentsOwned(){return continentsOwned;}

        public static PlayerTurnStats deserialize(String data){
            String[] stats = data.split(",");
            return new PlayerTurnStats(Integer.parseInt(stats[0]), Integer.parseInt(stats[1]), Integer.parseInt(stats[2]),
                    Integer.parseInt(stats[3]), Integer.parseInt(stats[4]), Integer.parseInt(stats[5]));
        }
        public String serialize(){
            return provincesOwned + "," + playersConquered + "," + troops + "," + infamy + "," + monetae + "," + continentsOwned;
        }
    }
}