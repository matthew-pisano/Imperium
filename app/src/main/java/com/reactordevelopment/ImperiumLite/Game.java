package com.reactordevelopment.ImperiumLite;

import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import static com.reactordevelopment.ImperiumLite.MainActivity.*;
import static android.graphics.Color.*;

public class Game extends GameActivity implements java.io.Serializable {
    //controllers
    private static ImageButton change;
    private static ImageButton again;
    private static ImageButton retreat;
    private static ImageButton annihilate;
    //rolls
    private static ImageView rollsCover;
    private static ImageView attackerBackround;
    private static ImageView defenderBackround;
    private static ImageView attacker;
    private static ImageView defender;
    private static TextView attackerTroops;
    private static TextView defenderTroops;
    private static TextView aDie1;
    private static TextView aDie2;
    private static TextView aDie3;
    private static TextView dDie1;
    private static TextView dDie2;
    private static ImageView defeated;
    private static ImageView defeated2;
    //slide
    private static ImageView slideCover;
    private static ImageView sliderImage;
    private static SeekBar slider;
    private static TextView slideTroops;
    //status
    private static ImageView statusCover;
    private static TextView status;
    //misc
    private static RelativeLayout mapLayout;
    private static ConstraintLayout winLayout;
    private static int year;
    private boolean[] ais;
    private static int numPlayers;
    private static ArrayList<ArrayList<ArrayList>> allStats;
    private transient Context context;
    protected static boolean imperium;
    protected static int mapMode;
    protected static Player focusPlayer = null;
    protected static double maxDev;
    protected static double maxTroops;
    private Player wiener;
    private int gameId;
    private static int currentPlayer;
    private static int slideValue;
    private static int slideProgress;
    private static int turnNum;
    private static boolean transporting;
    private static boolean attacking;
    private static Player[] players; //start attack non static
    private static Map map;
    //private int year = 0;
    private String timeline = "";
    public static boolean debug;
    private MediaPlayer deusMedia;
    private boolean deusPlaying = false;
    private long lastUpdate = 0;
    private static boolean pulsing;
    private double pulseCount = 0.2;
    private double pulseChange = .05;
    private static int[] pulseProvs = new int[]{0};
    public boolean inSetup;
    private int lastPlayerId;

    public Game(){}
    public Game(Context context, int numPlayers, int imperium, Object[] history){ //loadGame
        inSetup = true;
        this.imperium = imperium==1;
        Log.i("initilize", ""+imperium);
        this.context = context;
        gameId = (int)(Math.random()*10000);
        gameAt = gameId;
        Log.i("GameId", ""+gameId+", gameAt: "+gameAt);
        turnNum = 1;
        pulsing = false;
        timeline = (String)history[0];
        if(!((String)history[1]).equals(""))year = Integer.parseInt((String)history[1]);
        else year = 0;
        transporting = false;
        currentPlayer = 0;
        attacking = false;
        slideValue = 0;
        mapMode = 1;
        deusMedia = new MediaPlayer();
        this.debug = DEBUG;
        players = new Player[numPlayers];
        lastPlayerId = -1;
        assignCtrls();
    }
    public Game(Context context, boolean[] ais, int imperium, Object[] history){ //newGame
        this(context, ais.length, imperium, history);
        this.ais = ais; //overwritten ais
    }
    public Game(Context context, boolean debug, Object[] history){ //debugGame
        this(context, 2, 1, history); //debug players length
        this.debug = debug;
        this.ais = new boolean[numPlayers]; //overwritten ais
    }
    public void postNew(){ //called after new game
        touched();
        initRolls();
        slide();
        update();
        endAttack();
        endTransport();
        if(!debug) addPlayers();
        else players = debugPlayers;
        //players[0].turn();
        //if(debug) ownerFromTag();
        changeProvEnabled(players[0].isHuman());
        setPlayerInfo(getCurrentPlayer().getName());
        changeNationAt();
        if(!debug) change.setVisibility(View.INVISIBLE);
        updateMaxDev();
        mapLayout = getMapLayout();
        //map.logContinents();
        getCurrentPlayer().turn(false);
        winStuff();
        inSetup = false;
    }
    public void postLoad(){ //called after loaded game
        this.ais = reverseAis();
        touched();
        initRolls();
        slide();
        update();
        endAttack();
        endTransport();
        updateMaxDev();
        if(debug && NEW_DEBUG_SAVE) overwritePlayers();
        loadOwnerFromTag();
        playerTitles();
        statusCover.setBackgroundResource(R.drawable.statusbar);
        //players[0].turn();
        if(getCurrentPlayer().getStage() == -1 && !DEBUG) change.setVisibility(View.INVISIBLE);
        setPlayerInfo(getCurrentPlayer().getName());
        changeProvEnabled(getCurrentPlayer().isHuman());
        changeNationAt();
        updateAllOwners(); //friend or foe
        if(getCurrentPlayer().getStage() == 1) change.setBackgroundResource(R.drawable.endattack);
        if(getCurrentPlayer().getStage() == 2) change.setBackgroundResource(R.drawable.endtransport);
        turnMoveVis(getCurrentPlayer().isHuman() && isHistorical() && !timeView);
        updateMapMode(1);
        //map.logContinents();
        initialCores();
        getCurrentPlayer().turn(false);
        winStuff();
        inSetup = false;
    }
    //public
    public static boolean isHistorical(){return year != 0;}
    public int getYear(){return year;}
    public String getTimeline(){return timeline;}
    public int getGameId(){return gameId;}
    public void setYear(int year){this.year = year;}
    public void setTimeline(String timeline) { this.timeline = timeline; }

    public Object[] getHistory(){return new Object[]{timeline, ""+year};}
    public Player getFocusPlayer(){return focusPlayer;}
    public int getMapMode(){return mapMode;}
    public static Player getCurrentPlayer(){return players[currentPlayer];}
    public static int getPlayerNum(){return currentPlayer;}
    public boolean getImperium(){return imperium;}
    public int getLastPlayerId(){return lastPlayerId;}
    public int getTurnNum(){return turnNum;}
    public Player[] getPlayerList(){return players;}
    public static Player[] getStaticPlayerList(){return players;}
    public static Map getMap() { return map; }
    public int getSlideProgress(){return slideProgress;}
    public Player getWiener(){return wiener;}
    public ArrayList<ArrayList<ArrayList>> getAllStats(){
        allStats = new ArrayList<>(0);
        for(int i=0; i<players.length; i++) allStats.add(players[i].getStats());
        return allStats;
    }
    public void setLastPlayerId(int id){Log.i("LastAi", "Now: "+id+", was: "+ lastPlayerId); lastPlayerId = id;}
    public static void setFocusPlayer(Player p){focusPlayer = p;}
    public void setMap(Map set){map = set; if(map == null) Log.i("map", "nulllllll");}
    public void updateMapMode(int set){ Log.i("InSetup", ""+inSetup);mapMode = set; updateAllOverlays(); }
    public void setTurnNum(int set){turnNum = set;}
    public void setCurrentPlayer(int set){currentPlayer = set;}
    public void setPlayerLength(int len){
        Log.i("removing", "in");
        for(Player p : players){
            p.remove();
        }
        players = new Player[len];
    }
    public static void changeProvEnabled(boolean set){provEnabled = set;}
    public void updateMaxDev(){
        for(int i=0; i<map.getList().length; i++)
            if(map.getList()[i].modDevelopment(0) > maxDev) maxDev = map.getList()[i].modDevelopment(0);
    }
    public void updateAllOverlays(){
        Log.i("UpdatedOverlays", "");
        runOnUiThread(new Runnable() {
            @Override
            public void run() { for(int i = 0; i<map.getList().length; i++) map.getList()[i].updateOverlays(); }
        });
        if(mapMode == 8 && focusPlayer != null){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        for (int i = focusPlayer.getDiplo().length-1; i > 0; i--) {
                            for (String s : focusPlayer.getDiplo()[i]) {
                                //Player target = playerFromTag(s);
                                focusPlayer.printDiplo();
                                if (i == 1) {
                                    Log.i("Fromtag", s);
                                    Player target = playerFromTag(s);
                                    highlightPlayer(target, ALLY_COLOR);
                                } //allies
                                else if (i == 2) {
                                    Player target = playerFromTag(s);
                                    highlightPlayer(target, SUBJECT_COLOR);
                                } //subject
                                else if (i == 4) {
                                    Player target = playerFromTag(s.substring(s.length()-3));
                                    highlightPlayer(target, TRUCE_COLOR);
                                } //truce
                                else if (i == 3) {
                                    Player target = playerFromTag(s.substring(6));
                                    highlightPlayer(target, RED);
                                } //war
                                else if (i == 5) {
                                    Player target = playerFromTag(s);
                                    highlightPlayer(target, OVERLORD_COLOR);
                                } //overlord
                            }
                        }
                        highlightPlayer(focusPlayer, SELF_COLOR);
                    }catch(ArrayIndexOutOfBoundsException e){e.printStackTrace(); focusPlayer = null;}
                }
            });
        }
    }
    private void updateDevastation(){
        for(Province p : map.getList()) p.modDevastation(-.003);
    }
    private void updateProvinces(){
        for(Province p : map.getList())
           p.updateOwner();
    }
    public void playerTitles(){
        for(Player p : players) p.turn(false);
    }
    public void updateTitles(){
        for(Player p : players)
            p.pocketText();
    }
    public void haltAis(){
        for(Player p : players)
            if(!p.isHuman())
                p.halt();
    }
    public void removePlayer(int id){
        //players[id].setId(-1);
        players[id].remove();
        Log.i("Player Remove", "Yeeted: "+id);
    }
    public void addPlayers(){
        for(int i=0; i<ais.length; i++) {
            Log.i("addPlayers", "Ai: "+ais[i]);
            if(ais[i]) players[i] = new Ai(context, i, AI_STYLE, imperium, "#0"+i);
            else players[i] = new Player(context, i, imperium, "#0"+i);
            Log.i("addPlayers", "added player"+i+", "+players[i].isHuman());
        }
    }
    public void changePlayer(boolean adding){
        clearAlerts();
        clearWarPortals();
        getCurrentPlayer().printDiplo();
        String prevTag = getCurrentPlayer().getTag();
        Log.i("inchangeplayer", "in, "+turnNum+", "+currentPlayer);
        if(getCurrentPlayer().isHuman() && getCurrentPlayer().getAllOwned().length > 0 && !DEBUG && year != 1)Achivements.scanCriteria();
        players[currentPlayer].calcAllOwned(false);
        if(players[currentPlayer].getAllOwned().length == 0 && turnNum > players.length){
            getCurrentPlayer().peaceOutAll();
            removePlayer(currentPlayer);
        }
        do{
            if (currentPlayer < players.length - 1) currentPlayer++;
            else currentPlayer = 0;
            players[currentPlayer].calcAllOwned(false);
        }while (players[currentPlayer].getAllOwned().length == 0 && turnNum > players.length);
        Log.i("inchangeplayer", "in2 "+players[currentPlayer].getAllOwned().length+", "+players[currentPlayer].getName());
        maxTroops = map.maxTroops();
        Log.i("inchangeplayer", "in3");
        if(adding && !imperium) {
            getCurrentPlayer().modTroops(3 + map.bonuses(getCurrentPlayer()) + (int) getCurrentPlayer().getInfamy() + map.hegemonyBonus(getCurrentPlayer().getAllOwned()));
        }else if(adding && imperium){
            getCurrentPlayer().modMonetae(getCurrentPlayer().totalIncome() + (int) getCurrentPlayer().getInfamy() + map.hegemonyBonus(getCurrentPlayer().getAllOwned()));
            //getCurrentPlayer().modTroops(getCurrentPlayer().totalIncome());
        }else if(adding && isHistorical()){
            getCurrentPlayer().modMonetae((int) (getCurrentPlayer().totalIncome()*getCurrentPlayer().getOpsEfficiency() + (int) getCurrentPlayer().getInfamy()));
            //getCurrentPlayer().modTroops(getCurrentPlayer().totalIncome());
        }
        Log.i("inchangeplayer", "in4");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateAllOwners(); //friend or foe
                chickenDinner();
                if(debug)setPlayerInfo(getCurrentPlayer().getName());
                Log.i("playerStage", ""+getCurrentPlayer().getStage()+", "+currentPlayer);
                change.setBackgroundResource(R.drawable.endplacement);
                endTransport();
                /*if(getCurrentPlayer().getId() == 0) statusCover.setBackgroundResource(R.drawable.statusblue);
                if(getCurrentPlayer().getId() == 1) statusCover.setBackgroundResource(R.drawable.statusred);
                if(getCurrentPlayer().getId() == 2) statusCover.setBackgroundResource(R.drawable.statusgreen);
                if(getCurrentPlayer().getId() == 3) statusCover.setBackgroundResource(R.drawable.statuspurple);*/
                changeNationAt();
                changeAllUISelection(false);
            }
        });
        //for(Player p : players) p.calcAllOwned();
        if(!adding) getCurrentPlayer().setStage(-1);
        else getCurrentPlayer().setStage(0);
        Log.i("playerStage", ""+getCurrentPlayer().getStage()+", "+currentPlayer);
        turnNum++;
        Log.i("Turn Num", "" + turnNum);
        saveAllProvinces();
        map.decayAll();
        updateTitles();
        getCurrentPlayer().printDiplo();
        getCurrentPlayer().turn(true);
    }
    public void jumpToPlayer(String tag){
        for(int i=0; i<players.length; i++){
            Player p = players[i];
            if((p.getTotalTroops() > 0 || turnNum < players.length) && p.getTag().equals(tag))
                currentPlayer = i;
        }
        updateAllOwners();
        if(debug)setPlayerInfo(getCurrentPlayer().getName());
        maxTroops = map.maxTroops();
        getCurrentPlayer().setStage(-1);
        turnNum++;
        saveAllProvinces();
        changeNationAt();
        map.decayAll();
        getCurrentPlayer().turn(false);
    }
    public static int mostOwned(){
        int most = 0;
        int mostId = -1;
        for(Player p : players){
            if(p.getAllOwned().length > most){
                most = p.getAllOwned().length;
                mostId = p.getId();
            }
        }
        return mostId;
    }
    public void updateAllOwners(){
        for(Province p : map.getList()) p.updateOwner();
    }
    public void enablePulse(int[] ids){
        stopPulse();
        pulseProvs = ids;
        pulsing = true;
    }
    public void stopPulse(){
        if(pulsing) updateAllOverlays();
        pulsing = false;
    }
    private void highlight(){
        for(int i : pulseProvs){
            for(Province p : map.getList()){
                if(p.getId() == i) p.pulse(Color.argb(255, 189, 148, 242), pulseCount);
            }
        }
    }
    public void highlightPlayer(Player player, int color){
        if(player == null) return;
        for(Province p : map.getList())
            if(p.getOwnerId() == player.getId())
                p.setColor(color);
    }
    private void initialCores(){
        for(Province p : map.getList()){
            if(p.getCore().equals("#nn") && p.getOwnerId() != -1)
                p.setCoreOwner(p.getOwner().getTag());
        }
    }
    //presses province with owner recorded in the loadTag
    public void loadOwnerFromTag(){
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
        save += formatInt(currentPlayer, 3);
        //save += aisToString();

        if(imperium) save += ""+1;
        if(!imperium) save += ""+0;
        getAllStats();
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

    public static void startTransport(){
        boolean humanity = getCurrentPlayer().isHuman();
        defeated.setVisibility(View.INVISIBLE);
        defeated2.setVisibility(View.INVISIBLE);
        rollsCover.setVisibility(View.INVISIBLE);
        attackerBackround.setVisibility(View.INVISIBLE);
        defenderBackround.setVisibility(View.INVISIBLE);
        if(getCurrentPlayer().isHuman())slideTroops.setVisibility(View.VISIBLE);
        attacker.setVisibility(View.INVISIBLE);
        defender.setVisibility(View.INVISIBLE);
        attackerTroops.setVisibility(View.INVISIBLE);
        defenderTroops.setVisibility(View.INVISIBLE);
        change.setVisibility(View.INVISIBLE);
        annihilate.setVisibility(View.INVISIBLE);
        if(humanity){
            again.setVisibility(View.VISIBLE);
            retreat.setVisibility(View.VISIBLE);
            slider.setVisibility(View.VISIBLE);
            slideCover.setVisibility(View.VISIBLE);
            sliderImage.setVisibility(View.VISIBLE);
        }
        //for(Province p : map.getList()) p.hideAim();
        slider.setProgress(50);
        sliderImage.setX(50);
        again.setBackgroundResource(R.drawable.transport);
        retreat.setEnabled(true);
        retreat.setBackgroundResource(R.drawable.done);
        changeProvEnabled(false);
        transporting = true;
        attacking = false;
        for(Province p : map.getList()) p.updateOwner(); //friend or foe
        Log.i("Transport", "Start");
    }
    public static void endTransport(){
        Log.i("transport", "ending");
        slider.setVisibility(View.INVISIBLE);
        slideTroops.setText("");
        again.setVisibility(View.INVISIBLE);
        slideTroops.setVisibility(View.INVISIBLE);
        retreat.setVisibility(View.INVISIBLE);
        slideCover.setVisibility(View.INVISIBLE);
        if(getCurrentPlayer() != null)
            if(getCurrentPlayer().isHuman())changeProvEnabled(true);
        sliderImage.setVisibility(View.INVISIBLE);
        aDie1.setVisibility(View.INVISIBLE);
        aDie2.setVisibility(View.INVISIBLE);
        aDie3.setVisibility(View.INVISIBLE);
        dDie1.setVisibility(View.INVISIBLE);
        dDie2.setVisibility(View.INVISIBLE);
        if(!isHistorical())change.setVisibility(View.VISIBLE);
        transporting = false;
    }
    private static void startAttackBase(){
        again.setBackgroundColor(LTGRAY);
        again.setBackgroundResource(R.drawable.attack);
        retreat.setBackgroundColor(LTGRAY);
        retreat.setBackgroundResource(R.drawable.retreat);
        annihilate.setVisibility(View.VISIBLE);
        changeProvEnabled(false);
        attackerBackround.setVisibility(View.VISIBLE);
        defenderBackround.setVisibility(View.VISIBLE);
        attacker.setVisibility(View.VISIBLE);
        defender.setVisibility(View.VISIBLE);
        slideTroops.setVisibility(View.INVISIBLE);
        attackerTroops.setVisibility(View.VISIBLE);
        defenderTroops.setVisibility(View.VISIBLE);
        defeated.setVisibility(View.VISIBLE);
        defeated2.setVisibility(View.VISIBLE);
        again.setVisibility(View.VISIBLE);
        retreat.setVisibility(View.VISIBLE);
        change.setVisibility(View.INVISIBLE);
        aDie1.setVisibility(View.VISIBLE);
        aDie2.setVisibility(View.VISIBLE);
        aDie3.setVisibility(View.VISIBLE);
        dDie1.setVisibility(View.VISIBLE);
        dDie2.setVisibility(View.VISIBLE);
        rollsCover.setVisibility(View.VISIBLE);
        annihilate.setVisibility(View.VISIBLE);
        attacking = true;
        attacker.setBackgroundResource(getCurrentPlayer().getFlag());

    }
    public static void startAttack(){
        startAttackBase();
        if(getCurrentPlayer().getSavedSelect()[1].getOwnerId() != -1)
            defender.setBackgroundResource(getCurrentPlayer().getAttackSelected()[1].getOwner().getFlag());
        else defender.setBackgroundResource(R.drawable.noflag);
    }
    public static void startAiAttack(Province defenderP){
        startAttackBase();
        change.setVisibility(View.INVISIBLE);
        again.setVisibility(View.INVISIBLE);
        retreat.setVisibility(View.INVISIBLE);
        annihilate.setVisibility(View.INVISIBLE);
        if(defenderP != null) {
            if(defenderP.getOwnerId() != -1)
                defender.setBackgroundResource(defenderP.getOwner().getFlag());
            else defender.setBackgroundResource(R.drawable.noflag);
        }
    }
    public static void endAttack(){
        again.setBackgroundColor(TRANSPARENT);
        again.setVisibility(View.INVISIBLE);
        retreat.setBackgroundColor(TRANSPARENT);
        retreat.setVisibility(View.INVISIBLE);
        annihilate.setVisibility(View.INVISIBLE);
        slideTroops.setText("");
        if(getCurrentPlayer() != null)
            if(getCurrentPlayer().isHuman())changeProvEnabled(true);
        attackerBackround.setVisibility(View.INVISIBLE);
        defenderBackround.setVisibility(View.INVISIBLE);
        attacker.setVisibility(View.INVISIBLE);
        defender.setVisibility(View.INVISIBLE);
        attackerTroops.setVisibility(View.INVISIBLE);
        defenderTroops.setVisibility(View.INVISIBLE);
        defeated.setVisibility(View.INVISIBLE);
        defeated2.setVisibility(View.INVISIBLE);
        aDie1.setVisibility(View.INVISIBLE);
        aDie2.setVisibility(View.INVISIBLE);
        aDie3.setVisibility(View.INVISIBLE);
        dDie1.setVisibility(View.INVISIBLE);
        dDie2.setVisibility(View.INVISIBLE);
        rollsCover.setVisibility(View.INVISIBLE);
        if(!isHistorical())change.setVisibility(View.VISIBLE);
        annihilate.setVisibility(View.INVISIBLE);
        //for(Province p : map.getList()) p.hideAim();
        attacking = false;
    }
    public void changer() {
        Log.i("jumpText", getJumpText());
        if(!getJumpText().equals("") && debug){
            jumpToPlayer(getJumpText());
            setJumpText("");
            return;
        }
        if(debug){
            changePlayer(false);
            return;
        }
        changeAllSelection(false);
        Log.i("Changer", "press"+getCurrentPlayer().getStage()+", name: "+getCurrentPlayer().getName());
        if (!imperium && getCurrentPlayer().getStage() == 0 && getCurrentPlayer().getTroops() == 0) {
            getCurrentPlayer().setStage(1); Log.i("changeStage", "place-1");
            change.setBackgroundResource(R.drawable.endattack);
        } else if (imperium && getCurrentPlayer().getStage() == 0) {
            getCurrentPlayer().setStage(1); Log.i("changeStage", "place0");
            change.setBackgroundResource(R.drawable.endattackdown);
            change.postDelayed(new Runnable() {
                @Override
                public void run() {
                    change.setBackgroundResource(R.drawable.endattack);
                }
            }, 300);
        } else if (getCurrentPlayer().getStage() == 1) {
            getCurrentPlayer().setStage(2); Log.i("changeStage", "place1");
            change.setBackgroundResource(R.drawable.endtransport);
            changeAllUISelection(false);
        } else if (getCurrentPlayer().getStage() == 2) {
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
            changePlayer(false);
            return;
        }
        changeAllSelection(false);
        Log.i("Changer", "press"+getCurrentPlayer().getStage()+"id:"+currentPlayer);
        /*if (!imperium && getCurrentPlayer().getStage() == 0 && getCurrentPlayer().getTroops() == 0) {
            getCurrentPlayer().setStage(1); Log.i("changeStage", "place-1");
            change.setBackgroundResource(R.drawable.endattack);
            undo.setVisibility(View.INVISIBLE);
        } else if (imperium && getCurrentPlayer().getStage() == 0) {
            getCurrentPlayer().setStage(1); Log.i("changeStage", "place0");
            change.setBackgroundResource(R.drawable.endattackdown);
            change.postDelayed(new Runnable() {
                @Override
                public void run() {
                    change.setBackgroundResource(R.drawable.endattack);
                }
            }, 300);
            undo.setVisibility(View.INVISIBLE);
        }*/if (getCurrentPlayer().getStage() == 1) {
            getCurrentPlayer().setStage(0); Log.i("changerevStage", "place1");
            //change.setBackgroundResource(R.drawable.endtransport);
            changeAllUISelection(false);
        } else if (getCurrentPlayer().getStage() == 2) {
            //change.setBackgroundResource(R.drawable.endplacement);
            changeAllUISelection(false);
            getCurrentPlayer().setStage(1);
            //toStageZero();
        }
    }

    public void again() {
        Log.i("again", "presses");
        rollOut(new int[6]);

        if (getCurrentPlayer().getStage() == 1 && !transporting) {
            rollOut(getCurrentPlayer().attack());
            again.setBackgroundResource(R.drawable.attackdown);
            again.postDelayed(new Runnable() {
                @Override
                public void run() {
                    again.setBackgroundResource(R.drawable.attack);
                }
            }, 500);
        } else if (getCurrentPlayer().getStage() == 1 && transporting /*&& ran*/) {
            Log.i("transporting", "againEnded");
            again.setBackgroundResource(R.drawable.transportdown);
            try{getCurrentPlayer().transport(slideValue);}catch (NullPointerException e){e.printStackTrace();}
            again.setBackgroundResource(R.drawable.transport);
        }
        if (getCurrentPlayer().getStage() == 2 && !isHistorical()) {
            again.setBackgroundResource(R.drawable.transportdown);
            change.setBackgroundResource(R.drawable.endplacement);
            getCurrentPlayer().transport(slideValue);
            toStageZero();
        }
        else if(getCurrentPlayer().getStage() == 2) {
            getCurrentPlayer().transport(slideValue);
            endTransport();
            changeAllUISelection(false);
        }
    }
    public void retreat() {
        try {
            if(getCurrentPlayer().attackSelected[1] != null && getCurrentPlayer().attackSelected[0] != null)
                if (getCurrentPlayer().attackSelected[1].getTroops() / (getCurrentPlayer().attackSelected[0].getTroops() + 1) > 3)
                    Achivements.getAchive("ctrlZ");
        }catch (NullPointerException e){e.printStackTrace();}
        changeAllUISelection(false);
        //getCurrentPlayer().select(2);
        retreat.setBackgroundResource(R.drawable.retreatdown);
        if (getCurrentPlayer().getStage() == 1 && transporting) endTransport();
        else if (getCurrentPlayer().getStage() == 1) endAttack();
    }
    public void annihilate() {
        annihilate.setBackgroundResource(R.drawable.annihilatedown);
        try {
            while (getCurrentPlayer().getAttackSelected()[0].getTroops() > 1 && getCurrentPlayer().getAttackSelected()[1].getTroops() > 1)
                rollOut(getCurrentPlayer().attack());
        }catch(NullPointerException e){e.printStackTrace();}
    }
    public void undo() {
        Province temp = getCurrentPlayer().getTempProvince();
        if (!imperium && temp.getLastUndoable().equals("t")) {
            if (temp.getTroops() > temp.getSavedTroops()) {
                temp.modTroops(-1);
                getCurrentPlayer().modTroops(1);
            }
        }
        if (imperium && temp.getLastUndoable().equals("t")) {
            if (temp.getTroops() > temp.getSavedTroops()) {
                temp.modTroops(-1);
                getCurrentPlayer().modMonetae(1*MONETAE_TO_TROOPS);
            }
        }
        if (temp.getLastUndoable().equals("d")) {
            if (temp.modDevelopment(0) > temp.getSavedDevelopment()) {
                temp.modDevelopment(temp.getSavedDevelopment() - temp.modDevelopment(0));
                getCurrentPlayer().modMonetae(10 * temp.getLastDevelops());
            }
        }
    }
    //private
    private void assignCtrls(){
        again = getAgain();
        change = getChange();
        retreat = getRetreat();
        annihilate = getAnnihilate();
    }
    private void winStuff(){
        winLayout = getWinLayout();
        winLayout.setVisibility(View.INVISIBLE);
        winLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.i("Winxoo", "zoom");
                winLayout.animate().y(-screenWidth).setDuration(1000);
                return false;
            }
        });
    }
    private void update(){
        status = getStatus();
        statusCover = getStatusCover();
        statusCover.setBackgroundResource(R.drawable.statusbar);
        //status.setTextSize(TypedValue.COMPLEX_UNIT_IN,.1f*screenWidth/480f);
        Thread lookingThread = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        if(getGameAt() != gameId) {
                            Log.i("GameHalt", "");
                            Thread.sleep(10000000);
                        }
                        Thread.sleep(500);
                        try {
                            if (getPlayerList().length != 0 && !inSetup) {
                                if (getCurrentPlayer() != null && wiener == null) {
                                    final boolean transportScan = getCurrentPlayer().transportScan();
                                    final boolean attackScan = getCurrentPlayer().attackScan();
                                    //if(!transportScan) {
                                    //if(getCurrentPlayer().isHuman()) Achivements.scanCriteria();
                                    if (DEBUG)
                                        getCurrentPlayer().setMonetae(getCurrentPlayer().totalIncome());

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (attackScan && getCurrentPlayer().getStage() == 1 && !attacking) {
                                                if (getCurrentPlayer().isHuman()) startAttack();
                                            } else if (!attackScan && getCurrentPlayer().getStage() == 1 && attacking)
                                                endAttack();
                                            //}
                                            if (getCurrentPlayer().getStage() == 2) {
                                                if (transportScan && !transporting)
                                                    startTransport();
                                                else if (!transportScan && transporting)
                                                    endTransport();
                                            }
                                            if (attackScan) {
                                                try {
                                                    if (!imperium) {
                                                        attackerTroops.setText("" + (int) getCurrentPlayer().getSavedSelect()[0].getTroops());
                                                        defenderTroops.setText("" + (int) getCurrentPlayer().getSavedSelect()[1].getTroops());
                                                    } else {
                                                        attackerTroops.setText("" + (int) (getCurrentPlayer().getSavedSelect()[0].getTroops() * 10) / 10.0);
                                                        defenderTroops.setText("" + (int) (getCurrentPlayer().getSavedSelect()[1].getTroops() * 10) / 10.0);
                                                    }
                                                } catch (NullPointerException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                            if (attacking)
                                                again.setBackgroundResource(R.drawable.attack);
                                            String statusText = "Stage: " + getCurrentPlayer().getStage();
                                            if (!imperium)
                                                statusText += ", Reinforcements: " + (int) getCurrentPlayer().getTroops();
                                            else
                                                statusText += ", Monetae: " + getCurrentPlayer().modMonetae(0)
                                                        + ", Development: " + getCurrentPlayer().totalIncome();
                                            statusText += ", Infamy: " + (int) (getCurrentPlayer().getInfamy() * 100) / 100.0;
                                            status.setText(statusText);

                                            inBounds();
                                            if (pulsing) {
                                                if (pulseCount > .75 || pulseCount < 0.1)
                                                    pulseChange = -pulseChange;
                                                pulseCount += pulseChange;
                                                highlight();
                                            }
                                            updateProvinces();
                                            //slider.bringToFront();

                                            deusVult();
                                        }
                                    });
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };lookingThread.start();

    }
    private void deusVult(){
        float x = getMapLayout().getX();
        float y = getMapLayout().getY();
        //Log.i("mapdeus", x+", "+y);
        //Log.i("bools", (x < -1850)+", "+(x > -1975)+", "+(y < -1255)+", "+(y > -1355)+", "+(scaling > 9.6)+", "+(map.getId() == 2));
        if(x < -1850 && x > -1975 && y < -1255 && y > -1355 && scaling > 9.6 && map.getId() == 2){
            if(!deusPlaying) {
                deusMedia = MediaPlayer.create(context, R.raw.deus);
                deusMedia.setLooping(true);
                deusMedia.start();
                deusPlaying = true;
                Log.i("deus", "start");
            }
            Log.i("deus", "vult");
        }else if(deusPlaying){
            Log.i("deus", "end");
            deusMedia.stop();
            deusMedia.release();
            deusMedia = new MediaPlayer();
            deusPlaying = false;
        }
    }
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
        /*if(map.allOwned(players[0])) {
            wiener = players[0];
            winCover.setBackgroundResource(R.drawable.winnerblue);
        }
        else if(map.allOwned(players[1])) {
            wiener = players[1];
            winCover.setBackgroundResource(R.drawable.winnerred);
        }
        if(players.length >= 3){
            if(map.allOwned(players[2])) {
                wiener = players[2];
                winCover.setBackgroundResource(R.drawable.winnergreen);
            }
        }
        if(players.length >= 4){
            if(map.allOwned(players[3])) {
                wiener = players[3];
                winCover.setBackgroundResource(R.drawable.winnerpurple);
            }
        }
        if(wiener != null) {
            changeAllSelection(false);
            getCurrentPlayer().setStage(42);
            winCover.setVisibility(View.VISIBLE);
        }*/
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
        getGame().setLastPlayerId(getCurrentPlayer().getId());
        changePlayer(true);
        //undo.setVisibility(View.VISIBLE);
        //again.setBackgroundColor(TRANSPARENT);
        again.setVisibility(View.INVISIBLE);
        change.setBackgroundResource(R.drawable.endplacement);
        endTransport();
        changeAllUISelection(false);
        updateDevastation();
    }


    private void slide(){
        slider = getSlider();
        sliderImage = getSliderImage();
        slideTroops = getSlideTroops();
        slideCover = getSlideCover();
        slideProgress = 0;
        sliderImage.setX(50);
        slider.setProgress(50);
        sliderImage.setVisibility(View.INVISIBLE);
        Log.i("slide", "created");
        slider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                try {
                    getCurrentPlayer().transportScan();
                    slideProgress = progress;
                    sliderImage.setX(progress/100f*slider.getWidth());
                    sliderImage.bringToFront();
                    if (getCurrentPlayer().getStage() == 1) {
                        if (progress < 50) {
                            slideValue = (int) ((getCurrentPlayer().getSavedSelect()[1].getTroopsFrom(getCurrentPlayer().getTag()) - 3) * (1 - progress / 50.0));
                            slideTroops.setText(slideValue + " troops to " + getCurrentPlayer().getSavedSelect()[0].getName());
                        }
                        if (progress >= 50) {
                            slideValue = (int) ((getCurrentPlayer().getSavedSelect()[0].getTroopsFrom(getCurrentPlayer().getTag()) - 1) * (progress - 50) / 50.0);
                            slideTroops.setText(slideValue + " troops to " + getCurrentPlayer().getSavedSelect()[1].getName());
                        }
                    }
                    if (getCurrentPlayer().getStage() == 2) {
                        int friendly = 0;
                        getCurrentPlayer().saveSelected();
                        try {
                            if (progress < 50) {
                                if (getCurrentPlayer().getTransportSelected()[1].getOwnerId() != getCurrentPlayer().getId())
                                    friendly = 1;
                                slideValue = (int) ((getCurrentPlayer().getTransportSelected()[1].getTroopsFrom(getCurrentPlayer().getTag()) - 1 + friendly) * (1 - progress / 50.0));
                                slideTroops.setText(slideValue + " troops to " + getCurrentPlayer().getTransportSelected()[0].getName());
                                if(getCurrentPlayer().getTransportSelected()[1].getStackFrom(getCurrentPlayer().getTag()).getMovesLeft() == 0) {
                                    slideTroops.setText("These legions have already been moved this turn");
                                    slideValue = 0;
                                }
                            }
                            if (progress >= 50) {
                                if (getCurrentPlayer().getTransportSelected()[0].getOwnerId() != getCurrentPlayer().getId())
                                    friendly = 1;
                                slideValue = (int) ((getCurrentPlayer().getTransportSelected()[0].getTroopsFrom(getCurrentPlayer().getTag()) - 1 + friendly) * (progress - 50) / 50.0);
                                slideTroops.setText(slideValue + " troops to " + getCurrentPlayer().getTransportSelected()[1].getName());
                                if(getCurrentPlayer().getTransportSelected()[0].getStackFrom(getCurrentPlayer().getTag()).getMovesLeft() == 0) {
                                    slideTroops.setText("These legions have already been moved this turn");
                                    slideValue = 0;
                                }
                            }
                        }catch (NullPointerException e){
                            e.printStackTrace();
                            slideValue = 0;
                        }
                    }
                }catch (Exception e){e.printStackTrace();}
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }
    private void initRolls(){
        attackerBackround = getAttackerBackround();
        defenderBackround = getDefenderBackround();
        attacker = getAttacker();
        defender = getDefender();
        attackerTroops = getAttackerTroops();
        defenderTroops = getDefenderTroops();
        rollsCover = getRollsCover();
        aDie1 = getaDie1();
        aDie2 = getaDie2();
        aDie3 = getaDie3();
        dDie1 = getdDie1();
        dDie2 = getdDie2();
        defeated = getDefeated();
        defeated2 = getDefeated2();
        defeated.setBackgroundResource(R.drawable.defeated);
        defeated2.setBackgroundResource(R.drawable.defeated);
        aDie1.setText("");
        aDie2.setText("");
        aDie3.setText("");
        dDie1.setText("");
        dDie2.setText("");
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
    private void saveAllProvinces(){
        for (int i=0; i<map.getList().length; i++) {
            getMap().getList()[i].saveTroops();
            getMap().getList()[i].saveDevelopment();
        }
    }
    private String aisToString(){
        String list = "";
        for(int i=0; i<ais.length; i++){
            if(ais[i]) list += "1";
            else list += "0";
        }return list;
    }
    private boolean[] reverseAis(){
        boolean[] ais = new boolean[players.length];
        for(int i=0; i<ais.length; i++){
            if(players[i].isHuman()) ais[i] = false;
            else ais[i] = true;
        }return ais;
    }
}