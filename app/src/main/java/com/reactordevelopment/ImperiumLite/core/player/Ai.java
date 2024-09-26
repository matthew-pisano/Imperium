package com.reactordevelopment.ImperiumLite.core.player;

import static com.reactordevelopment.ImperiumLite.activities.MainActivity.MONETAE_TO_TROOPS;

import android.content.SharedPreferences;
import android.util.Log;

import com.reactordevelopment.ImperiumLite.core.mapping.Continent;
import com.reactordevelopment.ImperiumLite.core.mapping.Province;

import java.util.ArrayList;

public class Ai extends Player {
    public static final int BASIC = 0;
    public static final int MONGOL = 1;
    public static final int ROMAN = 2;
    public static final int HAN = 3;
    private static final int MONGOL_LIMIT = 30;
    private static final int MAX_TURNS = 5000;
    private static long lastRun;

    private int gameId;
    private int aiAttackSpeed = 200;
    private int style;
    private boolean executing;

    private Province threat;
    private Continent interestContinent;
    private Province interestProvince;
    private boolean attacked;
    private int id;
    private Thread control;
    private boolean halted;
    private static boolean killSwitch;
    private SharedPreferences prefs;

    public Ai(int ident, int style, boolean imperium, String tag) {
        super(ident, imperium, tag);
        id = ident;
        human = false;
        puppet = false;
        killSwitch = false;
        gameId = getGameId();
        killSwitch = false;
        executing = false;
        this.style = style;
        setStage(-1);
        halted = false;
        lastRun = System.currentTimeMillis();
        interestProvince = null;
        prefs = cont.getSharedPreferences("vars", 0);
        absoluteControl();
        scanBordering();
    }

    private void absoluteControl() {
        control = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        //Log.i("KillSwitch", "killed "+Ai.this.getName()+" "+killSwitch);
                        if(killSwitch) break;
                        /*if(halted) {
                            Log.i("AiHalt", getNation().getName()+", gameAt: "+gameAt+", "+gameId);
                            Thread.sleep(10000000);
                        }*/
                        Thread.sleep(500);
                        if (getPlayerList().length != 0 && getGame() != null && System.currentTimeMillis() > lastRun + prefs.getInt("turnSpeed", 500)) {
                            if (getCurrPlayer() != null) {
                                if (getCurrPlayer().getId() == id && !executing && !loneSurvivor() && getTurnNum() < MAX_TURNS) {
                                    Log.i("Timing", "" + (System.currentTimeMillis() - lastRun) + ", " + prefs.getInt("turnSpeed", 500));
                                    if (getAllOwned().length == 0|| id == getGame().getLastPlayerId()){
                                        if(isHistorical() || getTurnNum() > getPlayerList().length) {
                                            Log.i("Ai cont", "Skipped: " + getNation().getName() + ", " + getAllOwned().length + ", " + getGame().getLastPlayerId() + ", " + id+", tunrnum: "+getTurnNum());
                                            if(stage >=0) startNextTurn(true);
                                            else startNextTurn(false);
                                            continue;
                                        }
                                    }
                                    if (!getCurrPlayer().isHuman()) {
                                        Log.i("Logic", "Running: " + id+", "+getNation().getName());
                                        lastRun = System.currentTimeMillis();
                                        try {runLogic(); /*getGame().setLastPlayerId(id);*/} catch (Exception e) { e.printStackTrace(); }
                                        Log.i("Logic", "Done: " + id+", "+getNation().getName());
                                    }
                                }
                            }
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        control.start();
    }
    public void turn(boolean runAi){
        super.turn(runAi);
        /*
        Log.i("Logic", "Running: " + id+", "+getName()+", Enabled: "+runAi);
        if(runAi) {
            ExecutorService executorService = Executors.newCachedThreadPool();
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    runLogic();
                }
            });

        }
        Log.i("Logic", "Done: " + id+", "+getName());*/
    }
    public static void halt(){
        /*halted = true;*/
        Log.i("AiKill", "killed");
        killSwitch = true;
    }
    //Tools
    private void pressProvince(final Province province) {
        Log.i("AiProvince", "presed");
        try {
            province.doClick();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void testPress(final Province province, final String tag) {
        try { province.doClick(); } catch (NullPointerException e) {
            Log.i("AiProvince", tag);
            e.printStackTrace();
        }
    }

    private void pressChange(final String at) {
        boolean save = executing;
        Log.i("AiChange", "perssed" + at);
        Log.i("AiChange", "" + save);
        if (save) runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.i("ChangeId", getCurrPlayer().getId() + ", Ai:" + id);
                if (getCurrPlayer().getId() == id) changer();
            }
        });
    }

    private void overridePressChange() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.i("AiChange", "perssed");
                changer();
            }
        });
    }

    private void pressAgain() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.i("AiAgain", "perssed");
                again();
            }
        });
    }
    private void pressRetreat() { retreat(); }
    private void pressAnnihilate() { annihilate(); }


    private boolean loneSurvivor() { //checks of there is only one player left
        int count = 0;
        try{
            for (int i = 0; i < getPlayerList().length; i++)
                if (getPlayerList()[i].getAllOwned().length == 0)
                    count++;
        }catch (NullPointerException e){
            return true;
        }

        return count == getPlayerList().length - 1 && getTurnNum() > 10;
    }

    private Province[] threatenedEntrances(Continent c) {
        ArrayList<Province> threatened = new ArrayList<>(0);
        for (Province p : c.getList())
            for (Province border : p.getBordering())
                if (border.getContinentId() != c.getId() && border.getOwnerId() != id) {
                    threatened.add(p);
                    break;
                }
        return threatened.toArray(new Province[0]);
    }


    public boolean calcPeaceOutAll() {
        if(coreProvs.length < 15)
            return allOwned.length < coreProvs.length * .75;
        else
            return allOwned.length < coreProvs.length - 20;
    }



    public boolean calcAcceptPeace(String enemy) {
        double reasons = reasonsToAcceptPeace(enemy);
        Log.i("Reasons to peace with "+enemy, ""+reasons);
        return reasons >= 1;
    }

    public boolean willJoinAgainst(String enemyTag, String friendFrom) {
        return playerFromTag(friendFrom).collectiveTroops() > playerFromTag(enemyTag).collectiveTroops() * .75;
    }

    private void searchAndDeclareWar() {
        Log.i("searchanddeclare", "" + nehiborTags.length);
        for (String tag : nehiborTags) {
            if (diploList[3].size() == 0 && isAttackable(tag)) {
                Log.i("collectivetroops", "out" + collectiveTroops() + ", theirs" + (playerFromTag(tag).collectiveTroops() * .75));
                if (collectiveTroops() > playerFromTag(tag).collectiveTroops() * .75) {
                    Log.i("*************WAR***",
                            getName() + "(" + collectiveTroops() + " troops) has declared war on " + playerFromTag(tag).getName() + "(" + playerFromTag(tag).collectiveTroops() + " troops)");
                    for(String s : diploList[1])
                        if(playerFromTag(tag).isAllied(s)){
                            removeAlly(s);
                            playerFromTag(s).removeAlly(getTag());
                        }
                    addToWar(getTag() + tag, getTag());
                    playerFromTag(tag).addToWar(getTag() + tag, tag);
                }
            }
        }
    }


    private void respondToRequests() {
        ArrayList<String> strings = diploList[0];
        for (int i = 0; i < strings.size(); i++) {
            String request = strings.get(i);
            Log.i("Requests", "Index: "+i+", Request: "+request);
            if (request.substring(0, 1).equals("1")) {
                if (willAlly(request.substring(7)))
                    requestChoice(Integer.parseInt(request.substring(0, 2)), request.substring(2, 8), request.substring(8));
            }
            if (request.substring(0, 1).equals("2")) {
                if(willSubmitTo(request.substring(7)))
                    requestChoice(Integer.parseInt(request.substring(0, 2)), request.substring(2, 8), request.substring(8));
            }
            if (request.substring(0, 1).equals("3")) {
                String war = request.substring(2, 8);
                String from = request.substring(8);
                if (willJoinAgainst(relationsFromWarTag(war)[1], from))
                    requestChoice(Integer.parseInt(request.substring(0, 2)), war, from);
            }
            if (request.substring(0, 1).equals("4")) {
                if (!request.substring(8).equals("#nn")) {
                    boolean peace = calcAcceptPeace((request.substring(8)));
                    Log.i("calcPeace", ""+peace);
                    if (peace)
                        requestChoice(Integer.parseInt(request.substring(0, 2)), request.substring(2, 8), request.substring(8));
                }
            }
        }
    }

    private Province[] calcTransport() {
        getAllStationedStacks();
        double max = 0;
        Player toPlayer = null;
        Province fromProv = null;
        Province nextProv = null;
        Province toProv = null;
        for (String s : diploList[3]) {
            double free = playerFromTag(s.substring(6)).getFreeTroops();
            if (free > max) {
                max = free;
                toPlayer = playerFromTag(s.substring(6));
            }
        }
        ArrayList<Province> borderWith = new ArrayList<>(0);
        for (Province p : warBorders)
            if (p.borders(toPlayer.getTag()))
                borderWith.add(p);

        if (borderWith.size() > 0) {
            toProv = borderWith.get((int) (Math.random() * borderWith.size()));
            max = 0;
            for (Province p : stationedIn) {
                if(p.getStackFrom(getTag()).getMovesLeft() == 0) continue;
                double troops = p.getTroopsFrom(getTag());
                Object[] pathResults = pathTo(toProv, p);
                int provsAway = (int) pathResults[0];
                if (troops / provsAway > max) {
                    max = troops / provsAway;
                    fromProv = p;
                    nextProv = (Province) pathResults[1];
                }
            }
        }
        return new Province[]{nextProv, fromProv};
    }

    private void transport(Province[] provs) {
        setStage(2);
        if (provs[0] != null && provs[1] != null) {
            double transport = provs[1].getTroopsFrom(getTag());
            //provs[1].modTroops(-transport, getTag());
            //provs[0].modTroops(transport, getTag());
            provs[1].transportTo(transport, provs[0]);
        }
        Log.i("AiTransport", "" + getStage());
        pressChange("t");
        Log.i("AiTransport2", "" + getStage());
    }

    private Object[] pathTo(Province target, Province owned) {
        int targetX = target.getX();
        int targetY = target.getY();
        int xAt = owned.getX();
        int yAt = owned.getY();
        Province nextProv = null;
        Province provAt = owned;
        double vector = Math.sqrt((targetX - xAt) * (targetX - xAt) + (targetY - yAt) * (targetY - yAt));
        int loops = 0;
        while (provAt.getId() == target.getId()) {
            loops++;
            if (loops > 50) break;
            for (Province border : provAt.getBordering()) {
                if (border.getId() != id) continue;
                int testX = border.getX();
                int testY = border.getY();
                double testVector = Math.sqrt((targetX - testX) * (targetX - testX) + (targetY - testY) * (targetY - testY));
                if (testVector < vector) {
                    vector = testVector;
                    provAt = border;
                    if (nextProv == null) nextProv = border;
                    break;
                }
            }
        }
        return new Object[]{loops, nextProv};
    }

    private Province pathToOnce(Province target, Province owned) {
        int targetX = target.getX();
        int targetY = target.getY();
        int xAt = owned.getX();
        int yAt = owned.getY();
        Province provAt = owned;
        double vector = Math.sqrt((targetX - xAt) * (targetX - xAt) + (targetY - yAt) * (targetY - yAt));
        for (Province border : provAt.getBordering()) {
            if (border.getId() != id) continue;
            int testX = border.getX();
            int testY = border.getY();
            double testVector = Math.sqrt((targetX - testX) * (targetX - testX) + (targetY - testY) * (targetY - testY));
            if (testVector < vector) {
                vector = testVector;
                provAt = border;
                return border;
            }
        }
        return owned;
    }

    //Logic
    private void runLogic() { //executes turn stage logic in stage order
        executing = true;
        //changeProvEnabled(false);
        Log.i("AiTurn", "aiturn: " + id + ", Stage: " + getStage() + "-----------------------------------------------------turn: " + getTurnNum());
        if (getStage() == 2){
            Log.i("Skipped2", "AtStage2");
            startNextTurn(true);
        }
        scanBordering();
        Log.i("AiTurn", "inLogic");
        if (getStage() == -1) setupStage();
        Log.i("AiTurn", "new Stage? " + getStage());
        if (getStage() == 0 || getStage() == 1) {
            if (isHistorical()) {
                double warCahnce = Math.random();
                Log.i("WarChance", "rand: "+warCahnce+", fromNat: "+(getWarChance()));
                if (warCahnce < getWarChance()) searchAndDeclareWar();
            }

            int randAttacks = (int) (2 * Math.random()) + 2;
            if (style == BASIC) {
                if (getStage() == 0) {
                    basicPlaceStage();
                    pressChange("0");
                }
                if (getStage() == 1) {
                    baseAttackStage();
                    pressChange("1");
                }
            }

            if (style == ROMAN) {
                if (getStage() == 0) threat = romanPlaceStage();
                if (getStage() == 1) {
                    for (double i = 0; i < randAttacks; i++)
                        if (!romanAttackStage()) i -= .3;
                }
            }
            if (isHistorical()) {
                respondToRequests();
                if (calcPeaceOutAll()) peaceOutAll();
            }
            getAllStationedStacks();
            for(Province p : stationedIn){
                if (diploList[3].size() == 0) transportStage();
                else try{transport(calcTransport());}catch (Exception e){
                    e.printStackTrace();
                    setStage(2);
                    pressChange("t");
                }
            }
        }
        /*if(getStage() == 2 || allOwned.length == 0){
           changePlayer(true);
        }*/
        executing = false;
    }

    private void setupStage() {
        Log.i("AiStaging", "setup");
        double max = 0;
        Province bestP = null;
        if (!getMap().allTaken()) {
            for (Player player : getPlayerList())
                for (Continent cont : getMap().getContinents())
                    if (cont.hasIn(player.getAllOwned()) == cont.getList().length - 2)
                        for (Province prov : cont.getList())
                            if (prov.getOwnerId() == -1) {
                                testPress(prov, "1st setup");
                                return;
                            }

            for (Province p : getMap().getList()) {
                double clusterBias = 0;
                for(Province border : p.getBordering())
                    if(border.getOwnerId() == id) clusterBias = 1;
                if (p.getInterest()+clusterBias > max && p.getOwnerId() == -1) {
                    Log.i("AiStaging", "imterested");
                    max = p.getInterest()+clusterBias;
                    bestP = p; //assigns unowned province with highest total interest
                }
            }
        } else {
            max = 0;
            //Log.i("taken", "allTaken");
            for (int i = 0; i < getMap().getList().length; i++) {
                Province test = getMap().getList()[i];
                int rand = (int) (Math.random() * 5 + 16);
                if ((test.getInterest() - test.getTroops() / rand) > max && test.getOwnerId() == id) {
                    max = getMap().getList()[i].getInterest();
                    bestP = getMap().getList()[i]; //assigns province with highest combined interest with less than a number of troops
                }
            }
        }
        Log.i("Setup", "pressed: " + bestP.getName());
        testPress(bestP, "2nd setup");
    }
    //Place

    private Province basePlaceStage() { //chooses best provinces to place troops
        try {
            Log.i("AiStaging", "place");
            long start = System.currentTimeMillis();
            double max = 0;
            Province bestP = null;
            boolean condition;
            if (!imperium) condition = getTroops() > 2;
            else condition = modMonetae(0) > 2 * MONETAE_TO_TROOPS;
            while (condition) {
                long loopStart = System.currentTimeMillis();
                Log.i("place", "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                Continent bestC = null; //looks for best continent that ai has a foothold on
                for (int i = 0; i < getMap().getContinents().length; i++) {
                    Continent test = getMap().getContinents()[i];
                    if (test.hasIn(getAllOwned()) >= test.getList().length / 2 && test.getInterest() > max && !test.hasComplete(getAllOwned())) {
                        max = test.getInterest();
                        bestC = test;
                        Log.i("bestC", "has " + bestC.hasIn(getAllOwned()) + " of " + bestC.getList().length + " in " + bestC.getName());
                    }
                }
                if (bestC != null) { //assigns best province from continent
                    Log.i("place", "bestC");
                    double min = Integer.MAX_VALUE;
                    Province borderP = null;
                    for (Province border : bestC.getList())
                        if (border.getOwnerId() != id && border.getTroops() < min) {
                            min = border.getTroops();
                            borderP = border;
                        }
                    if (borderP != null) {
                        for (Province owned : borderP.getBordering())
                            if (owned.getOwnerId() == id) bestP = owned;
                    } else Log.i("place", "it was nuillllkjhgfdl");
                }
                max = 0;
                if (bestC == null || bestP == null) { //if no best province or continent is found
                    Log.i("place", "inOrNull");
                    long orNullStart = System.currentTimeMillis();
                    for (Province test : getMap().getList()) { //places troop in most threatened province
                        if ((howSurrounded(test) / (test.getTroops() + 1) * test.getInterest() > max && test.getOwnerId() == id)) {
                            max = howSurrounded(test) / (test.getTroops() + 1) * test.getInterest();
                            bestP = test;
                        }
                    }
                    Log.i("orNullDElay", ""+(System.currentTimeMillis()-orNullStart));
                }
                Log.i("InLoopDElay1", ""+(System.currentTimeMillis()-loopStart));
                if (bestP != null)
                    if (imperium && howSurrounded(bestP) == 0) {
                        for (Province border : bordering)
                            if (border.getOwnerId() == -1)
                                for (Province owned : border.getBordering())
                                    if (owned.getOwnerId() == id && owned.getTroops() < 15)
                                        bestP = owned;
                    }
                if (bestP == null /*&& getAllOwned().length > 1*/) { //randomly places troop
                    Log.i("place", "randomPlace");
                    int rand = (int) ((getAllOwned().length - 1) * Math.random());
                    bestP = getCurrPlayer().getAllOwned()[rand];
                    //Log.i("place", "name: "+bestP.getName()+", id: "+getId()+", owner: " +bestP.getOwnerId()+", rnd: " + rand);
                }
                //pressProvince(bestP);
                testPress(bestP, "Its a me!");
                Log.i("InLoopDElay2", ""+(System.currentTimeMillis()-loopStart));
            /*if(getStage() == 0) testPress(bestP, "Its a me!");
            else if(getStage() == 1){
                bestP.modTroops(modMonetae(0)/MONETAE_TO_TROOPS);
                modMonetae(-modMonetae(0)+2*MONETAE_TO_TROOPS);
            }*/
                if (bestP != null)
                    Log.i("place", "+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++  Presses:" + bestP.getName() + "Stage:" + getStage() + "money:" + modMonetae(0));
                if (getStage() != 0) break;
                if (bestP == null) break;
                if (!imperium) condition = getTroops() > 2;
                else condition = modMonetae(0) > 2 * MONETAE_TO_TROOPS;
                Log.i("LoopDElay", ""+(System.currentTimeMillis()-loopStart));
            }
            Log.i("PlaceDelay", ""+(System.currentTimeMillis()-start));
            return bestP;
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    private void basicPlaceStage() {
        Province best = basePlaceStage();
        if (!imperium) while (getTroops() > 0) { //randomly places remaining troops
            int rand = (int) ((getAllOwned().length - 1) * Math.random());
            testPress(getAllOwned()[rand], "basic 1");
        }
        else while (modMonetae(0) > 0) { //randomly places remaining troops
            int rand = (int) ((getAllOwned().length - 1) * Math.random());
            testPress(getAllOwned()[rand], "basic 2");
        }
    }

    private void mongolPlaceStage() {
        if (getTotalTroops() < MONGOL_LIMIT) {
            if (interestProvince == null)
                interestProvince = getAllOwned()[(int) (Math.random() * getAllOwned().length)];
            while (getTroops() > 0) { //picks and places random province as base of expansion
                for (int i = 0; i < interestProvince.getBordering().length; i++) {
                    if (interestProvince.getBordering()[i].getOwnerId() == id)
                        testPress(interestProvince.getBordering()[i], "mongolpress");
                }
            }
        } else {
            basePlaceStage();
        }
        pressChange("3");
    }

    private Province romanPlaceStage() {
        basePlaceStage();
        if (!imperium)
            for (Province border : bordering) //places troops on border of partially owned continent
                if (border != null)
                    if (border.getContinent().completedBy() != null)
                        for (Province owned : border.getBordering())
                            if (owned.getOwnerId() == id && howSurrounded(owned) > 0) {
                                while (getTroops() > 0) testPress(owned, "prepare press");
                                Log.i("Roman", "Roman at:" + owned.getName());
                                pressChange("4");
                                setStage(1);
                                return border;
                            }
        Log.i("inRoman", "in");
        pressChange("5");
        setStage(1);
        return null;
    }

    private void hanPlaceStage() {
        Province bestP = basePlaceStage();
        if (!imperium) while (getTroops() > 0) testPress(bestP, "hanpress1");
        else while (modMonetae(0) > 0) testPress(bestP, "hanpress2");
        pressChange("6");
    }
    //Attack

    private boolean baseAttackStage() {
        Log.i("AiStaging", "attack");
        double max = 0;
        boolean validAttack = true;
        Continent bestC = interestContinent;
        if (bestC != null) {
            for (Continent test : getMap().getContinents()) { //assigns to continent that is alrady partially owned
                if (test.hasIn(getAllOwned()) >= test.getList().length / 2.0 - 1 && test.getInterest() > max && !test.hasComplete(getAllOwned())) {
                    //Log.i("bestC", "the best arouound: " + bestC.getName());
                    max = test.getInterest();
                    bestC = test;
                }
            }
        }
        if (bestC != null) {
            Log.i("bestC", "the best arouound: " + bestC.getName());
            for (Province target : bestC.getList()) { //attacks all available provinces in a continent
                if (target.getOwnerId() != id) {
                    Log.i("attackStage", "foe: " + target.getName());
                    for (Province owned : target.getBordering()) {
                        Log.i("attackStage", "foeBorder: " + owned.getName());
                        if (owned.getOwnerId() == id && owned.getTroops() > target.getTroops()) {
                            validAttack = aiAttack(owned, target, .8)[0];
                        }
                    }
                }
            }
        }
        if (imperium) {
            Province bestp = null;
            max = 0;
            for (Province target : bordering) {
                double threatened = howSurroundedBy(target, id) / target.getTroops();
                if (threatened > max) {
                    max = threatened;
                    bestp = target;
                }
            }
            if (bestp != null) {
                for (Province attacker : bestp.getBordering())
                    if (attacker.getOwnerId() == id && attacker.getTroops() > 4)
                        validAttack = aiAttack(attacker, bestp, 1)[0];
            }
        }
        //if(!attacked){ //attacks all provinces on border to keep troop number from escalating
        int controlller = 0;
        Log.i("attackStage", "inNotAttacked");
        for (Province owned : getAllOwned()) {
            if (owned.getTroops() > 15) {
                for (Province enemy : owned.getBordering()) {
                    if (enemy.getOwnerId() != id) {
                        controlller ++;
                        if(controlller > 2 ) return false;
                        Log.i("attackStage", "populationControlStart");
                        validAttack = aiAttack(owned, enemy, .8)[0];
                        Log.i("attackStage", "populationControlEnd");
                    }
                }
            }
        }
        //}
        return validAttack;
    }

    private void mongolAttackStage() {
        if (getTotalTroops() < MONGOL_LIMIT)
            baseAttackStage();
        else if (interestProvince != null) { //expands outward until it is unable to
            boolean exhausted = false;
            while (!exhausted) { // attacks along a string of provinces
                for (int i = 0; i < interestProvince.getBordering().length; i++) {
                    if (interestProvince.getBordering()[i].getOwnerId() != id) {
                        if (aiAttack(interestProvince, interestProvince.getBordering()[i], .2)[1]) {
                            interestProvince = interestProvince.getBordering()[i];
                            break;
                        }
                    }
                    exhausted = true;
                }
            }
        }
        pressChange("7");
    }

    private boolean romanAttackStage() {
        if (!baseAttackStage()) return false;

        if (threat != null) {
            double max = 0;
            Province start = null; //attempts to breach enemy controlled continent and then take over
            for (Province owned : threat.getBordering()) {
                if (owned.getTroops() > max && owned.getOwnerId() == id) {
                    max = owned.getTroops();
                    start = owned;
                }
            }
            if (aiAttack(start, threat, .9)[1] && !imperium) ;
            interestContinent = threat.getContinent();
        }
        pressChange("8");
        return true;
    }

    private void hanAttackStage() {
        baseAttackStage();
        pressChange("9");
    }

    private boolean[] aiAttack(final Province attacker, final Province defender, double perDiff /*ratio of attacking troops to defending troops*/) {
        boolean[] status = new boolean[2]; //valid, victory
        if (isHistorical()) {
            if (defender.getOwner() != null)
                if (!isHostile(defender.getOwner().getTag()))
                    return new boolean[]{false, false};
        }
        //boolean defendedDed = false;
        Log.i("aiAttack", "ran");
        if (getCurrPlayer().getId() == id) {
            changeAllSelection(false);
            attacker.setSelected(true);
            defender.setSelected(true);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for(Province p : getMap().getList()) p.showSelection(false);
                    attacker.showSelection(true);
                    defender.showSelection(true);
                }
            });
        }

        attackSelected[0] = attacker;
        attackSelected[1] = defender;
        Log.i("aiAttack", " Attacker selected: " + attacker.isSelected() + ", Id: " + attacker.getId() + ", Troops: " + attacker.getTroops());
        Log.i("aiAttack", " Devender selected: " + defender.isSelected() + ", Id: " + defender.getId() + ", Troops: " + defender.getTroops());
        while (attacker.getTroops() / defender.getTroops() > perDiff && attacker.getTroops() > 1) {
            attackAnimation(defender, attacker);
            Log.i("aiAttack", "again");
            if (!attacked) return new boolean[]{false, false};
            if (defender.getOwnerId() == getId()) { //was transportscan()
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        attacker.hideAim();
                        endAttack();
                    }
                });
                scanBordering();
                Log.i("aiAttack", "battleWonBroke");
                getSlider().setProgress(50);
                double transprot = attacker.getTroops() - 1;
                attacker.modTroops(-transprot);
                defender.modTroops(transprot);
                //pressAgain();
                attacked = false;
                return new boolean[]{false, true};
            }
        }
        return new boolean[]{true, false};
    }

    private void attackAnimation(final Province defender, final Province attacker) {
        if (defender.getOwnerId() == -1) {
            try {
                attacked = rollOut(attack());
            } catch (NullPointerException e) { e.printStackTrace(); }
            try {
                control.sleep(aiAttackSpeed);
            } catch (InterruptedException e) { e.printStackTrace(); }
            //ui thread was here too
        } else {
            if (defender.getOwner().isHuman()) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        startAiAttack(defender);
                    }});
                attacked = rollOut(attack());
                try {
                    control.sleep(prefs.getInt("attackSpeed", 2000));
                } catch (InterruptedException e) { e.printStackTrace(); }
                /*runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        attacker.hideAim();
                        endAttack();
                    }
                });*/
            } else {
                try {
                    attacked = rollOut(attack());
                } catch (NullPointerException e) { e.printStackTrace(); }
            }
            try {
                control.sleep(aiAttackSpeed);
            } catch (InterruptedException e) { e.printStackTrace(); }
            /*runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    attacker.hideAim();
                    endAttack();
                }
            });*/
        }
    }

    private void transportStage() {
        Log.i("AiStaging", "================================================================transport");
        setStage(2);
        double maxTroops = 0;
        Province from = null;
        Province to = null;
        Log.i("AiTransport", "Treanted entrances");
        for (Continent c : getMap().getContinents())
            if (c.allOwned(getCurrPlayer())) {
                Province[] threatened = threatenedEntrances(c);
                for (Province entrance : threatened)
                    for (Province owned : entrance.getBordering())
                        if (owned.getTroops() > maxTroops) {
                            maxTroops = owned.getTroops();
                            from = owned;
                            to = entrance;
                        }
            }

        if (to == null) {
            Log.i("AiTransport", "Treanted prov");
            double borderThreat = 0;
            for (Province test : getAllOwned()) {
                if(test.getStackFrom(getTag()) != null)
                    if(test.getStackFrom(getTag()).getMovesLeft() == 0) continue;
                if (howSurrounded(test) == 0) {
                    int testGrad = 0;
                    for (Province threatened : test.getBordering()) {
                        testGrad += howSurrounded(threatened);
                        if (test.getTroops() * testGrad > borderThreat) {
                            borderThreat = test.getTroops() * testGrad;
                            from = test;
                        }
                    }
                }
            }
            maxTroops = 0;
            if (from != null) {
                Log.i("AiTransport", "bestpnotNull");
                Log.i("AiTransport", "From: "+from.getName());
                for (Province border : from.getBordering())
                    if (border.getTroops() > maxTroops && border.getId() != from.getId()) {
                        maxTroops = (int) border.getTroops();
                        to = border;
                    }
            }
        }
        if(from != null && to != null) {
            Log.i("AiTransport", "To: "+to.getName());
            double transport = from.getTroops() - 1;
            //from.modTroops(-transport);
            //to.modTroops(transport);
            from.transportTo(transport, to);
            Log.i("AiTransport", "Transported "+transport+" troops from "+from.getName()+" to "+to.getName());

        }

        Log.i("AiTransport", "" + getStage());
        pressChange("t");
        Log.i("AiTransport2", "" + getStage());
    }

}
