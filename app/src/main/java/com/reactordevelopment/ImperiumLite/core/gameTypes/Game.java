package com.reactordevelopment.ImperiumLite.core.gameTypes;

import static com.reactordevelopment.ImperiumLite.activities.MainActivity.SAVE_FORM;
import static com.reactordevelopment.ImperiumLite.activities.MainActivity.SAVE_VERSION;
import static com.reactordevelopment.ImperiumLite.activities.MainActivity.formatDouble;
import static com.reactordevelopment.ImperiumLite.activities.MainActivity.formatInt;
import static com.reactordevelopment.ImperiumLite.activities.MainActivity.screenHeight;
import static com.reactordevelopment.ImperiumLite.activities.MainActivity.screenWidth;

import android.graphics.Color;
import android.util.Log;
import android.view.View;

import com.reactordevelopment.ImperiumLite.R;
import com.reactordevelopment.ImperiumLite.core.Achivements;
import com.reactordevelopment.ImperiumLite.core.mapping.Map;
import com.reactordevelopment.ImperiumLite.core.mapping.Province;
import com.reactordevelopment.ImperiumLite.core.mapping.TroopStack;
import com.reactordevelopment.ImperiumLite.core.player.Ai;
import com.reactordevelopment.ImperiumLite.core.player.Player;
import com.reactordevelopment.ImperiumLite.core.player.PlayerStats;

import java.util.ArrayList;


public class Game {
    //controllers
//    private static ImageButton change;
//    private static ImageButton again;
//    private static ImageButton retreat;
//    private static ImageButton annihilate;
//    //rolls
//    private static ImageView rollsCover;
//    private static ImageView attackerBackround;
//    private static ImageView defenderBackround;
//    private static ImageView attacker;
//    private static ImageView defender;
//    private static TextView attackerTroops;
//    private static TextView defenderTroops;
//    private static TextView aDie1;
//    private static TextView aDie2;
//    private static TextView aDie3;
//    private static TextView dDie1;
//    private static TextView dDie2;
//    private static ImageView defeated;
//    private static ImageView defeated2;
//    //slide
//    private static ImageView slideCover;
//    private static ImageView sliderImage;
//    private static SeekBar slider;
//    private static TextView slideTroops;
//    //status
//    private static ImageView statusCover;
//    private static TextView status;
//    //misc
//    private static RelativeLayout mapLayout;
//    private static ConstraintLayout winLayout;
    protected final GameMode gameMode;
    protected final PlayerStats[] allPlayerStats;
    //private transient Context context;
    protected MapMode currentMapMode;
    protected Player focusPlayer = null;
    protected double maxDev;
    protected double maxTroops;
    protected int currPlayerIdx;
//    private int slideValue;
//    private int slideProgress;
    protected int turnNum;
    protected Player[] players; //start attack non static
    protected final Map map;
    protected boolean debug;

    protected Game(int[] playerTypes, Map map, GameMode gameMode, boolean debug){ //loadGame
        this.players = buildPlayers(playerTypes);
        this.allPlayerStats = new PlayerStats[players.length];
        this.map = map;
        this.gameMode = gameMode;
        turnNum = 1;
        currPlayerIdx = 0;
        currentMapMode = MapMode.PLAYER;
        this.debug = debug;
        loadProvinceOwners();
    }

    //public
    public Player getFocusPlayer(){ return focusPlayer; }
    public MapMode getCurrentMapMode(){ return currentMapMode; }
    public GameMode getGameMode(){ return gameMode; }
    public Player getCurrPlayer(){ return players[currPlayerIdx]; }
    public int getTurnNum(){ return turnNum; }
    public Player[] getPlayerList(){ return players; }
    public Map getMap() { return map; }
    public PlayerStats[] getAllPlayerStats(){ return allPlayerStats; }
    public void setMapMode(MapMode set){ currentMapMode = set; }

    public Player[] buildPlayers(int[] playerTypes){
        Player[] players = new Player[playerTypes.length];
        for(int i = 0; i< playerTypes.length; i++) {
            if(playerTypes[i] == 1) players[i] = new Ai(i, Ai.ROMAN, gameMode != GameMode.CLASSIC, "#0"+i);
            else players[i] = new Player(i, gameMode != GameMode.CLASSIC, "#0"+i);
        }
        return players;
    }

    protected void givePlayerTurnResources() {}
    protected int getTurnStartStage(){ return 0; }

    public void startNextTurn(){
        turnNum++;
        // Loop over defeated players until the next alive player is found
        do {
            if (currPlayerIdx < players.length - 1) currPlayerIdx++;
            else currPlayerIdx = 0;
        } while (players[currPlayerIdx].isDefeated());
        Player currPlayer = players[currPlayerIdx];

        map.decayAll();
        map.updateDevastation();

        givePlayerTurnResources();
        currPlayer.setStage(getTurnStartStage());
        currPlayer.turn(true);
    }


    //presses province with owner recorded in the loadTag
    public void loadProvinceOwners(){
        //Log.i("OwnerTag", "In");
        for(Province p : map.getList()){
            //Log.i("OwnerTag", p.getName()+", "+p.getLoadTag());
            for(Player pl : getPlayerList()) {
                if (p.getLoadTag().equals(pl.getTag())) {
                    //Log.i("OwnerTag", pl.getTag());
                    p.updatePress(pl.getId());
                }
            }
            if(p.getOwnerId() == -1) p.modTroops(-p.getTroops());
        }
    }
    public int playerIdFromTag(String tag){
        for(Player p : players)
            if(p.getTag().equals(tag))
                return p.getId();
        return -1;
    }
    public Player playerFromTag(String tag){
        for(Player p : players)
            if(p.getTag().equals(tag))
                return p;
        return null;
        //return players[playerIdFromTag(tag)];
    }
    public void changeAllSelection(boolean set){
        Log.i("selection", "changeAll");
        for(Province p : map.getList()) p.setSelected(set);
    }

    public void changeAllUISelection(boolean set){
        Log.i("selection", "changeAll");
        for(Province p : map.getList()) p.setAndShowSelection(set);
    }
    public String saveString(){
        String save = "";
        save += formatDouble(SAVE_VERSION, 4);
        save += "" + map.getId();
        save += formatInt(turnNum, SAVE_FORM[0]);

        for(int i=0; i<map.getList().length; i++){
            Province pAt = map.getList()[i];
            save += "<";
            if(pAt.getOwnerId() != -1) save += pAt.getOwner().getTag();
            else save += "#nn";
            save += pAt.getCore();
            save += formatDouble(pAt.modDevelopment(0), SAVE_FORM[5]);
            save += formatDouble(pAt.modDevastation(0), SAVE_FORM[6]);
            save += formatDouble(pAt.getAttrition(), SAVE_FORM[7]);
            save += pAt.getFortLevel(); //1 len
            save += "(";
            for(TroopStack ts : pAt.getTroopStacks()){
                save += ts.getOwnerTag() + formatDouble(ts.getTroops(), 5) + ts.getMovesLeft() + ",";
            }
            save += ")";
        }
        save += "!";
        int start = save.length();
        save += formatInt(players.length, SAVE_FORM[8]);
        for(int i=0; i<players.length; i++){
            save += "<";
            Player plAt = players[i];
            save += plAt.getTag(); //3
            if(!plAt.isHuman() || debug) save += "1";
            else save += "0";
            if(debug) save += "1";
            else save += plAt.getStage();
            save += formatDouble(plAt.getTroops(), SAVE_FORM[2]);
            save += formatInt(plAt.modMonetae(0), SAVE_FORM[3]);
            save += formatInt(plAt.getConquests(), SAVE_FORM[4]);
            save += "(";
            try {
                for (int dip = 0; dip < plAt.getDiplo().length; dip++) {
                    for (String s : plAt.getDiplo()[dip])
                        save += s;
                    save += ",";
                }
            }catch (NullPointerException e){e.printStackTrace();}
            save += ")";
        }
        Log.i("playerSave", save.substring(start));
        save += "!";
        save += formatInt(currPlayerIdx, 3);
        //save += aisToString();

        if(imperium) save += ""+1;
        if(!imperium) save += ""+0;
        getAllPlayerStats();
        save += "|";
        if(year != 0) save += "["+timeline+","+year+"]";
        else save += "[,]";
        save += allStats.get(0).size();
        try {
            for (ArrayList<ArrayList> playerStats : allStats) {
                //Log.i("StatSave", "Player");
                for (ArrayList statGroup : playerStats) {
                    //Log.i("StatSave", "Stat");
                    save += "{";
                    for (Object stat : statGroup) {
                        //Log.i("StatSave", ""+stat);
                        if (stat.getClass() == Integer.class)
                            save += formatInt((Integer) (stat), 5);
                        else save += formatDouble((Double) (stat), 5);
                    }
                }
                save += "|";
            }
        }catch (Exception e){e.printStackTrace();}
        Log.i("FullStats", save.substring(save.indexOf("{")));
        for(int i=0; i<save.length()-1; i++){
            if(save.substring(i, i+2).equals("-1"))
                save = save.substring(0,i) + "n" + save.substring(i+2);
        }
        Log.i("gameSave", "saved: " + save);
        return save;
    }

    public void changer() {
        Log.i("jumpText", getJumpText());
        if(!getJumpText().equals("") && debug){
            jumpToPlayer(getJumpText());
            setJumpText("");
            return;
        }
        if(debug){
            startNextTurn(false);
            return;
        }
        changeAllSelection(false);
        Log.i("Changer", "press"+ getCurrPlayer().getStage()+", name: "+ getCurrPlayer().getName());
        if (!imperium && getCurrPlayer().getStage() == 0 && getCurrPlayer().getTroops() == 0) {
            getCurrPlayer().setStage(1); Log.i("changeStage", "place-1");
            change.setBackgroundResource(R.drawable.endattack);
        } else if (imperium && getCurrPlayer().getStage() == 0) {
            getCurrPlayer().setStage(1); Log.i("changeStage", "place0");
            change.setBackgroundResource(R.drawable.endattackdown);
            change.postDelayed(new Runnable() {
                @Override
                public void run() {
                    change.setBackgroundResource(R.drawable.endattack);
                }
            }, 300);
        } else if (getCurrPlayer().getStage() == 1) {
            getCurrPlayer().setStage(2); Log.i("changeStage", "place1");
            change.setBackgroundResource(R.drawable.endtransport);
            changeAllUISelection(false);
        } else if (getCurrPlayer().getStage() == 2) {
            change.setBackgroundResource(R.drawable.endplacement);
            toStageZero();
        }
    }
    public void changerRev() {
        Log.i("jumpText", getJumpText());
        if(!getJumpText().equals("") && debug){
            jumpToPlayer(getJumpText());
            setJumpText("");
            return;
        }
        if(debug){
            startNextTurn(false);
            return;
        }
        changeAllSelection(false);
        Log.i("Changer", "press"+ getCurrPlayer().getStage()+"id:"+ currPlayerIdx);
        if (getCurrPlayer().getStage() == 1) {
            getCurrPlayer().setStage(0); Log.i("changerevStage", "place1");
            changeAllUISelection(false);
        } else if (getCurrPlayer().getStage() == 2) {
            changeAllUISelection(false);
            getCurrPlayer().setStage(1);
        }
    }

    public void again() {
        Log.i("again", "presses");
        rollOut(new int[6]);

        if (getCurrPlayer().getStage() == 1 && !transporting) {
            rollOut(getCurrPlayer().attack());
            again.setBackgroundResource(R.drawable.attackdown);
            again.postDelayed(new Runnable() {
                @Override
                public void run() {
                    again.setBackgroundResource(R.drawable.attack);
                }
            }, 500);
        } else if (getCurrPlayer().getStage() == 1 && transporting /*&& ran*/) {
            Log.i("transporting", "againEnded");
            again.setBackgroundResource(R.drawable.transportdown);
            try{
                getCurrPlayer().transport(slideValue);}catch (NullPointerException e){e.printStackTrace();}
            again.setBackgroundResource(R.drawable.transport);
        }
        if (getCurrPlayer().getStage() == 2 && !isHistorical()) {
            again.setBackgroundResource(R.drawable.transportdown);
            change.setBackgroundResource(R.drawable.endplacement);
            getCurrPlayer().transport(slideValue);
            toStageZero();
        }
        else if(getCurrPlayer().getStage() == 2) {
            getCurrPlayer().transport(slideValue);
            endTransport();
            changeAllUISelection(false);
        }
    }
    public void retreat() {
        try {
            if(getCurrPlayer().attackSelected[1] != null && getCurrPlayer().attackSelected[0] != null)
                if (getCurrPlayer().attackSelected[1].getTroops() / (getCurrPlayer().attackSelected[0].getTroops() + 1) > 3)
                    Achivements.getAchive("ctrlZ");
        }catch (NullPointerException e){e.printStackTrace();}
        changeAllUISelection(false);
        //getCurrentPlayer().select(2);
        retreat.setBackgroundResource(R.drawable.retreatdown);
        if (getCurrPlayer().getStage() == 1 && transporting) endTransport();
        else if (getCurrPlayer().getStage() == 1) endAttack();
    }
    public void annihilate() {
        annihilate.setBackgroundResource(R.drawable.annihilatedown);
        try {
            while (getCurrPlayer().getAttackSelected()[0].getTroops() > 1 && getCurrPlayer().getAttackSelected()[1].getTroops() > 1)
                rollOut(getCurrPlayer().attack());
        }catch(NullPointerException e){e.printStackTrace();}
    }
    //private
    private void chickenDinner() {
        Player winner = calcWiener();
        if (winner != null) {
            wiener = winner;
            Log.i("weinner", ""+winner.getName());
            winLayout.setVisibility(View.VISIBLE);
            setWinFlag(winner);
            changeAllUISelection(false);
            for(Player p : players)
                p.setStage(9);
            if(winner.isHuman() && turnNum <= 30 && year == 17 && timeline.equals("alp"))
                Achivements.getAchive("steamroll");
        }
    }
    private void overwritePlayers(){
        ArrayList<Player> decimate = new ArrayList<>(0);
        for(Player loaded : players) {
            boolean found = false;
            for (Player debugP : debugPlayers)
                if(loaded.getTag().equals(debugP.getTag()))
                    found = true;
        if(!found) decimate.add(loaded);
        }
        for(Player remove : decimate)
            for(Province p : map.getList())
                if(p.getOwnerId() == remove.getId()) {
                    p.updatePress(-1);
                    p.modTroops(-p.getTroops());
                }
        players = debugPlayers;
    }
    private Player calcWiener(){
        int count = 0;
        Player potential = null;
        for(Player p : players) {
            if (p.getAllOwned().length == 0) {
                try{p.calcAllOwned(false);}catch(Exception e){};
                if (p.getAllOwned().length == 0) count++;
            }
            else potential = p;
            Log.i("playerowned", ""+p.getAllOwned().length+", "+p.getId());
        }
        Log.i("CountWeiner", ""+count);
        if(count == players.length-1 && turnNum > players.length+1)
            return potential;
        return null;
    }
    protected void toStageZero(){
        Log.i("switchinh", "tostagezero");
        slideValue = 0;
        getGame().setLastPlayerId(getCurrPlayer().getId());
        startNextTurn(true);
        //undo.setVisibility(View.VISIBLE);
        //again.setBackgroundColor(TRANSPARENT);
        again.setVisibility(View.INVISIBLE);
        change.setBackgroundResource(R.drawable.endplacement);
        endTransport();
        changeAllUISelection(false);
        updateDevastation();
    }

    protected boolean rollOut(final int[] rolls){
        Log.i("rolls", "rolled");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                aDie1.setText("" + rolls[0]);
                aDie2.setText("" + rolls[1]);
                aDie3.setText("" + rolls[2]);
                dDie1.setText("" + rolls[3]);
                dDie2.setText("" + rolls[4]);
                if(rolls[0] > rolls[3]){
                    defeated.setRotation(0);
                    defeated.setBackgroundResource(R.drawable.defeated);
                }
                else if(rolls[0] < rolls[3]){
                    defeated.setRotation(180);
                    defeated.setBackgroundResource(R.drawable.defeated);
                }
                else defeated.setBackgroundResource(R.drawable.tie);

                if(rolls[1] > rolls[4]){
                    defeated2.setRotation(0);
                    defeated2.setBackgroundResource(R.drawable.defeated);
                }
                else if(rolls[1] < rolls[4]){
                    defeated2.setRotation(180);
                    defeated2.setBackgroundResource(R.drawable.defeated);
                }
                else defeated2.setBackgroundResource(R.drawable.tie);
            }});
        return rolls[5] == 1;
    }
    private void inBounds(){
        //Log.i("map", ""+getMapLayout().getX()+", "+getMapLayout().getY());
        double vector = Math.sqrt(Math.pow(getMapLayout().getX(), 2) + Math.pow(getMapLayout().getY(), 2));
        if(getMapLayout().getScaleX() < 1.3f)
            getMapLayout().animate().x(screenHeight/2-getMapLayout().getMeasuredWidth()/2)
                    .y(screenWidth/2-getMapLayout().getMeasuredHeight()/2)
                    .setDuration((long) (2000 * Math.pow(getMapLayout().getScaleX(), 2) / Math.sqrt(vector)));
    }

    private String aisToString(){
        String list = "";
        for(int i = 0; i< playerTypes.length; i++){
            list += playerTypes[i];
        }return list;
    }
    private int[] arrFromPlayerType(){
        int[] types = new int[players.length];
        for(int i=0; i<types.length; i++){
            if(players[i].isHuman()) types[i] = 0;
            if(!players[i].isPuppet()) types[i] = 1;
            else types[i] = 2;
        }return types;
    }


    public enum GameMode {
        CLASSIC(0), IMPERIUM(1), HISTORICAL(2);

        private final int value;
        GameMode(int value) { this.value = value; }
        public int getValue() { return value; }
    }

    public enum MapMode {
        TERRAIN, PLAYER, CONTINENT, DEVELOPMENT, ATTRITION, DEVASTATION, FORT_LEVEL, TROOPS, DIPLOMATIC, CORES
    }

//    public void enablePulse(int[] ids){
//        stopPulse();
//        pulseProvs = ids;
//        pulsing = true;
//    }
//    public void stopPulse(){
//        if(pulsing) updateAllOverlays();
//        pulsing = false;
//    }
//    private void highlight(){
//        for(int i : pulseProvs){
//            for(Province p : map.getList()){
//                if(p.getId() == i) p.pulse(Color.argb(255, 189, 148, 242), pulseCount);
//            }
//        }
//    }
//    public void highlightPlayer(Player player, int color){
//        if(player == null) return;
//        for(Province p : map.getList())
//            if(p.getOwnerId() == player.getId())
//                p.setColor(color);
//    }
//    public void jumpToPlayer(String tag){
//        for(int i=0; i<players.length; i++){
//            Player p = players[i];
//            if((p.getTotalTroops() > 0 || turnNum < players.length) && p.getTag().equals(tag))
//                currPlayerIdx = i;
//        }
//        updateAllOwners();
//        if(debug)setPlayerInfo(getCurrPlayer().getName());
//        maxTroops = map.maxTroops();
//        getCurrPlayer().setStage(-1);
//        turnNum++;
//        saveAllProvinces();
//        changeNationAt();
//        map.decayAll();
//        getCurrPlayer().turn(false);
//    }
//    public void changePlayer(boolean adding){
//        clearAlerts();
//        clearWarPortals();
//        getCurrentPlayer().printDiplo();
//        String prevTag = getCurrentPlayer().getTag();
//        Log.i("inchangeplayer", "in, "+turnNum+", "+currentPlayer);
//        if(getCurrentPlayer().isHuman() && getCurrentPlayer().getAllOwned().length > 0 && !debugingOn && year != 1)
//            Achivements.scanCriteria();
//        players[currentPlayer].calcAllOwned(false);
//        if(players[currentPlayer].getAllOwned().length == 0 && turnNum > players.length){
//            getCurrentPlayer().peaceOutAll();
//            defeatPlayer(currentPlayer);
//        }
//        do{
//            if (currentPlayer < players.length - 1) currentPlayer++;
//            else currentPlayer = 0;
//            players[currentPlayer].calcAllOwned(false);
//        }while (players[currentPlayer].getAllOwned().length == 0 && turnNum > players.length);
//        Log.i("inchangeplayer", "in2 "+players[currentPlayer].getAllOwned().length+", "+players[currentPlayer].getName());
//        maxTroops = map.maxTroops();
//        Log.i("inchangeplayer", "in3");
//        if(adding && !imperium) {
//            getCurrentPlayer().modTroops(3 + map.bonuses(getCurrentPlayer()) + (int) getCurrentPlayer().getInfamy() + map.hegemonyBonus(getCurrentPlayer().getAllOwned()));
//        }else if(adding && imperium){
//            getCurrentPlayer().modMonetae(getCurrentPlayer().totalIncome() + (int) getCurrentPlayer().getInfamy() + map.hegemonyBonus(getCurrentPlayer().getAllOwned()));
//            //getCurrentPlayer().modTroops(getCurrentPlayer().totalIncome());
//        }else if(adding && isHistorical()){
//            getCurrentPlayer().modMonetae((int) (getCurrentPlayer().totalIncome()*getCurrentPlayer().getOpsEfficiency() + (int) getCurrentPlayer().getInfamy()));
//            //getCurrentPlayer().modTroops(getCurrentPlayer().totalIncome());
//        }
//        Log.i("inchangeplayer", "in4");
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                updateAllOwners(); //friend or foe
//                chickenDinner();
//                if(debug)setPlayerInfo(getCurrentPlayer().getName());
//                Log.i("playerStage", ""+getCurrentPlayer().getStage()+", "+currentPlayer);
//                change.setBackgroundResource(R.drawable.endplacement);
//                endTransport();
//                /*if(getCurrentPlayer().getId() == 0) statusCover.setBackgroundResource(R.drawable.statusblue);
//                if(getCurrentPlayer().getId() == 1) statusCover.setBackgroundResource(R.drawable.statusred);
//                if(getCurrentPlayer().getId() == 2) statusCover.setBackgroundResource(R.drawable.statusgreen);
//                if(getCurrentPlayer().getId() == 3) statusCover.setBackgroundResource(R.drawable.statuspurple);*/
//                changeNationAt();
//                changeAllUISelection(false);
//            }
//        });
//        //for(Player p : players) p.calcAllOwned();
//        if(!adding) getCurrentPlayer().setStage(-1);
//        else getCurrentPlayer().setStage(0);
//        Log.i("playerStage", ""+getCurrentPlayer().getStage()+", "+currentPlayer);
//        turnNum++;
//        Log.i("Turn Num", "" + turnNum);
//        saveAllProvinces();
//        map.decayAll();
//        updateTitles();
//        getCurrentPlayer().printDiplo();
//        getCurrentPlayer().turn(true);
//    }
//    public void updateAllOverlays(){
//        Log.i("UpdatedOverlays", "");
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() { for(int i = 0; i<map.getList().length; i++) map.getList()[i].updateOverlays(); }
//        });
//        if(mapMode == 8 && focusPlayer != null){
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        for (int i = focusPlayer.getDiplo().length-1; i > 0; i--) {
//                            for (String s : focusPlayer.getDiplo()[i]) {
//                                //Player target = playerFromTag(s);
//                                focusPlayer.printDiplo();
//                                if (i == 1) {
//                                    Log.i("Fromtag", s);
//                                    Player target = playerFromTag(s);
//                                    highlightPlayer(target, ALLY_COLOR);
//                                } //allies
//                                else if (i == 2) {
//                                    Player target = playerFromTag(s);
//                                    highlightPlayer(target, SUBJECT_COLOR);
//                                } //subject
//                                else if (i == 4) {
//                                    Player target = playerFromTag(s.substring(s.length()-3));
//                                    highlightPlayer(target, TRUCE_COLOR);
//                                } //truce
//                                else if (i == 3) {
//                                    Player target = playerFromTag(s.substring(6));
//                                    highlightPlayer(target, RED);
//                                } //war
//                                else if (i == 5) {
//                                    Player target = playerFromTag(s);
//                                    highlightPlayer(target, OVERLORD_COLOR);
//                                } //overlord
//                            }
//                        }
//                        highlightPlayer(focusPlayer, SELF_COLOR);
//                    }catch(ArrayIndexOutOfBoundsException e){e.printStackTrace(); focusPlayer = null;}
//                }
//            });
//        }
//    }

    //    private void update(){
//        status = getStatus();
//        statusCover = getStatusCover();
//        statusCover.setBackgroundResource(R.drawable.statusbar);
//        Thread lookingThread = new Thread() {
//
//            @Override
//            public void run() {
//                try {
//                    while (!isInterrupted()) {
//                        if(getGameAt() != gameId) {
//                            Log.i("GameHalt", "");
//                            Thread.sleep(10000000);
//                        }
//                        Thread.sleep(500);
//                        try {
//                            if (getPlayerList().length != 0 && !inSetup) {
//                                if (getCurrentPlayer() != null && wiener == null) {
//                                    final boolean transportScan = getCurrentPlayer().transportScan();
//                                    final boolean attackScan = getCurrentPlayer().attackScan();
//                                    if (debugingOn)
//                                        getCurrentPlayer().setMonetae(getCurrentPlayer().totalIncome());
//
//                                    runOnUiThread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            if (attackScan && getCurrentPlayer().getStage() == 1 && !attacking) {
//                                                if (getCurrentPlayer().isHuman()) startAttack();
//                                            } else if (!attackScan && getCurrentPlayer().getStage() == 1 && attacking)
//                                                endAttack();
//                                            //}
//                                            if (getCurrentPlayer().getStage() == 2) {
//                                                if (transportScan && !transporting)
//                                                    startTransport();
//                                                else if (!transportScan && transporting)
//                                                    endTransport();
//                                            }
//                                            if (attackScan) {
//                                                try {
//                                                    if (!imperium) {
//                                                        attackerTroops.setText("" + (int) getCurrentPlayer().getSavedSelect()[0].getTroops());
//                                                        defenderTroops.setText("" + (int) getCurrentPlayer().getSavedSelect()[1].getTroops());
//                                                    } else {
//                                                        attackerTroops.setText("" + (int) (getCurrentPlayer().getSavedSelect()[0].getTroops() * 10) / 10.0);
//                                                        defenderTroops.setText("" + (int) (getCurrentPlayer().getSavedSelect()[1].getTroops() * 10) / 10.0);
//                                                    }
//                                                } catch (NullPointerException e) {
//                                                    e.printStackTrace();
//                                                }
//                                            }
//                                            if (attacking) again.setBackgroundResource(R.drawable.attack);
//                                            changeStageIcon(getCurrentPlayer().getStage());
//                                            String statusText = /*"Stage: " + getCurrentPlayer().getStage();*/ "";
//                                            if (!imperium) statusText += ", Reinforcements: " + (int) getCurrentPlayer().getTroops();
//                                            else
//                                                statusText += "Monetae: " + getCurrentPlayer().modMonetae(0)
//                                                        + ", Development: " + getCurrentPlayer().totalIncome();
//                                            statusText += ", Infamy: " + (int) (getCurrentPlayer().getInfamy() * 100) / 100.0;
//                                            status.setText(statusText);
//
//                                            inBounds();
//                                            if (pulsing) {
//                                                if (pulseCount > .75 || pulseCount < 0.1)
//                                                    pulseChange = -pulseChange;
//                                                pulseCount += pulseChange;
//                                                highlight();
//                                            }
//                                            updateProvinces();
//                                            deusVult();
//                                        }
//                                    });
//                                }
//                            }
//                        } catch (Exception e) { e.printStackTrace(); }
//                    }
//                } catch (InterruptedException e) { e.printStackTrace(); }
//            }
//        };lookingThread.start();
//    }

//    public static void startTransport(){
//        boolean humanity = getCurrentPlayer().isHuman();
//        defeated.setVisibility(View.INVISIBLE);
//        defeated2.setVisibility(View.INVISIBLE);
//        rollsCover.setVisibility(View.INVISIBLE);
//        attackerBackround.setVisibility(View.INVISIBLE);
//        defenderBackround.setVisibility(View.INVISIBLE);
//        if(getCurrentPlayer().isHuman())slideTroops.setVisibility(View.VISIBLE);
//        attacker.setVisibility(View.INVISIBLE);
//        defender.setVisibility(View.INVISIBLE);
//        attackerTroops.setVisibility(View.INVISIBLE);
//        defenderTroops.setVisibility(View.INVISIBLE);
//        change.setVisibility(View.INVISIBLE);
//        annihilate.setVisibility(View.INVISIBLE);
//        if(humanity){
//            again.setVisibility(View.VISIBLE);
//            retreat.setVisibility(View.VISIBLE);
//            slider.setVisibility(View.VISIBLE);
//            slideCover.setVisibility(View.VISIBLE);
//            sliderImage.setVisibility(View.VISIBLE);
//        }
//        //for(Province p : map.getList()) p.hideAim();
//        slider.setProgress(50);
//        sliderImage.setX(50);
//        again.setBackgroundResource(R.drawable.transport);
//        retreat.setEnabled(true);
//        retreat.setBackgroundResource(R.drawable.done);
//        changeProvEnabled(false);
//        transporting = true;
//        attacking = false;
//        for(Province p : map.getList()) p.updateOwner(); //friend or foe
//        Log.i("Transport", "Start");
//    }
//    public static void endTransport(){
//        Log.i("transport", "ending");
//        slider.setVisibility(View.INVISIBLE);
//        slideTroops.setText("");
//        again.setVisibility(View.INVISIBLE);
//        slideTroops.setVisibility(View.INVISIBLE);
//        retreat.setVisibility(View.INVISIBLE);
//        slideCover.setVisibility(View.INVISIBLE);
//        if(getCurrentPlayer() != null)
//            if(getCurrentPlayer().isHuman() && !getCurrentPlayer().isPuppet())changeProvEnabled(true);
//        sliderImage.setVisibility(View.INVISIBLE);
//        aDie1.setVisibility(View.INVISIBLE);
//        aDie2.setVisibility(View.INVISIBLE);
//        aDie3.setVisibility(View.INVISIBLE);
//        dDie1.setVisibility(View.INVISIBLE);
//        dDie2.setVisibility(View.INVISIBLE);
//        if(!isHistorical())change.setVisibility(View.VISIBLE);
//        transporting = false;
//    }
//    private static void startAttackBase(){
//        again.setBackgroundColor(LTGRAY);
//        again.setBackgroundResource(R.drawable.attack);
//        retreat.setBackgroundColor(LTGRAY);
//        retreat.setBackgroundResource(R.drawable.retreat);
//        annihilate.setVisibility(View.VISIBLE);
//        changeProvEnabled(false);
//        attackerBackround.setVisibility(View.VISIBLE);
//        defenderBackround.setVisibility(View.VISIBLE);
//        attacker.setVisibility(View.VISIBLE);
//        defender.setVisibility(View.VISIBLE);
//        slideTroops.setVisibility(View.INVISIBLE);
//        attackerTroops.setVisibility(View.VISIBLE);
//        defenderTroops.setVisibility(View.VISIBLE);
//        defeated.setVisibility(View.VISIBLE);
//        defeated2.setVisibility(View.VISIBLE);
//        again.setVisibility(View.VISIBLE);
//        retreat.setVisibility(View.VISIBLE);
//        change.setVisibility(View.INVISIBLE);
//        aDie1.setVisibility(View.VISIBLE);
//        aDie2.setVisibility(View.VISIBLE);
//        aDie3.setVisibility(View.VISIBLE);
//        dDie1.setVisibility(View.VISIBLE);
//        dDie2.setVisibility(View.VISIBLE);
//        rollsCover.setVisibility(View.VISIBLE);
//        annihilate.setVisibility(View.VISIBLE);
//        attacking = true;
//        attacker.setBackgroundResource(getCurrentPlayer().getFlag());
//
//    }
//    public static void startAttack(){
//        startAttackBase();
//        if(getCurrentPlayer().getSavedSelect()[1].getOwnerId() != -1)
//            defender.setBackgroundResource(getCurrentPlayer().getAttackSelected()[1].getOwner().getFlag());
//        else defender.setBackgroundResource(R.drawable.noflag);
//    }
//    public static void startAiAttack(Province defenderP){
//        startAttackBase();
//        change.setVisibility(View.INVISIBLE);
//        again.setVisibility(View.INVISIBLE);
//        retreat.setVisibility(View.INVISIBLE);
//        annihilate.setVisibility(View.INVISIBLE);
//        if(defenderP != null) {
//            if(defenderP.getOwnerId() != -1)
//                defender.setBackgroundResource(defenderP.getOwner().getFlag());
//            else defender.setBackgroundResource(R.drawable.noflag);
//        }
//    }
//    public static void endAttack(){
//        again.setBackgroundColor(TRANSPARENT);
//        again.setVisibility(View.INVISIBLE);
//        retreat.setBackgroundColor(TRANSPARENT);
//        retreat.setVisibility(View.INVISIBLE);
//        annihilate.setVisibility(View.INVISIBLE);
//        slideTroops.setText("");
//        if(getCurrentPlayer() != null)
//            if(getCurrentPlayer().isHuman() && !getCurrentPlayer().isPuppet())changeProvEnabled(true);
//        attackerBackround.setVisibility(View.INVISIBLE);
//        defenderBackround.setVisibility(View.INVISIBLE);
//        attacker.setVisibility(View.INVISIBLE);
//        defender.setVisibility(View.INVISIBLE);
//        attackerTroops.setVisibility(View.INVISIBLE);
//        defenderTroops.setVisibility(View.INVISIBLE);
//        defeated.setVisibility(View.INVISIBLE);
//        defeated2.setVisibility(View.INVISIBLE);
//        aDie1.setVisibility(View.INVISIBLE);
//        aDie2.setVisibility(View.INVISIBLE);
//        aDie3.setVisibility(View.INVISIBLE);
//        dDie1.setVisibility(View.INVISIBLE);
//        dDie2.setVisibility(View.INVISIBLE);
//        rollsCover.setVisibility(View.INVISIBLE);
//        if(!isHistorical())change.setVisibility(View.VISIBLE);
//        annihilate.setVisibility(View.INVISIBLE);
//        //for(Province p : map.getList()) p.hideAim();
//        attacking = false;
//    }

//    private void winStuff(){
//        winLayout = getWinLayout();
//        winLayout.setVisibility(View.INVISIBLE);
//        winLayout.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                Log.i("Winxoo", "zoom");
//                winLayout.animate().y(-screenWidth).setDuration(1000);
//                return false;
//            }
//        });
//    }
}