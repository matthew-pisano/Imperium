package com.reactordevelopment.ImperiumLite.core.player;

import static com.reactordevelopment.ImperiumLite.activities.MainActivity.SUBJECT_INCOME;
import static com.reactordevelopment.ImperiumLite.activities.MainActivity.TRUCE_TIMER;
import static com.reactordevelopment.ImperiumLite.activities.MainActivity.formatInt;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.reactordevelopment.ImperiumLite.core.Achivements;
import com.reactordevelopment.ImperiumLite.components.Event;
import com.reactordevelopment.ImperiumLite.core.gameTypes.Game;
import com.reactordevelopment.ImperiumLite.R;
import com.reactordevelopment.ImperiumLite.core.mapping.Province;

import java.util.ArrayList;

public class Player extends Game {
    protected boolean human = true;
    protected boolean puppet = false;
    protected int id;
    protected int monetae;
    protected int stage;
    protected int selections;
    protected int conquers;
    protected double reinforcements;
    protected double infamy;
    protected Context context;
    protected Province[] tmpSelect;
    protected Province[] savedSelect;
    protected Province[] attackSelected;
    protected Province[] transportSelected;
    protected Province[] allOwned;
    protected ArrayList<Province> provinces;
    protected Province[] stationedIn;
    protected Province[] coreProvs;
    protected Province[] bordering;
    protected String[] nehiborTags;
    protected Province tempProvince;
    protected boolean canSpend;
    protected Nation nation;
    //protected int movesLeft = 0;
    protected Object[] history;
    protected ArrayList<String>[] diploList;
    protected ArrayList<TextView> nameTitles;
    protected ArrayList<String> recentWars;
    protected Province[] warBorders;
    //stats
    protected ArrayList<Integer> provincesStat;
    protected ArrayList<Double> infamyStat;
    protected ArrayList<Integer> reinforceStat;
    protected ArrayList<Integer> conquersStat;
    protected ArrayList<Integer> totalTroopsStat;
    protected ArrayList<Integer> continentsStat;
    protected ArrayList<Integer> monetaeStat;
    public Player(){}

    public Player(Context cont, int ident, boolean imperium, String tag){
        canSpend = true; //tells monetae and troops if they can be modded and not go into debt
        context = cont;
        id = ident;
        stage = -1;
        selections = 0;
        infamy = 0;
        conquers = 0;
        recentWars = new ArrayList<>(0);
        bordering = new Province[0];
        nehiborTags = new String[0];
        coreProvs = new Province[0];
        warBorders = new Province[0];
        diploList = new ArrayList[6]; //requests, allies, subjects, war, truce, overlord
        for(int i=0; i<diploList.length; i++) diploList[i] = new ArrayList(0);
        if(game != null) {
            Log.i("historyStuff", game.getTimeline() + game.getYear());
            nation = new Nation(tag, game.getTimeline(), game.getYear());

        }else
            nation = new Nation(tag, debugId.substring(0, 3), Integer.parseInt(debugId.substring(3)));
        savedSelect = new Province[2];
        tmpSelect = new Province[2];
        attackSelected = new Province[2];
        transportSelected = new Province[2];
        allOwned = new Province[0];
        /*nameTitle = new TextView(context);
        nameTitle.setTextColor(Color.BLACK);
        nameTitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 5);
        nameTitle.setText(nation.getName());
        nameTitle.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        getMapLayout().addView(nameTitle);*/
        //if(isHistorical()) movesLeft = 5;
        //else movesLeft = 1;
        try {
            if (imperium) {
                monetae = (int) (30 + (double) getGame().getMap().getList().length / getPlayerList().length);
            } else
                reinforcements = 30 + (double) getGame().getMap().getList().length / getPlayerList().length;
            tempProvince = getMap().getList()[0];
        }catch (NullPointerException e){ /*e.printStackTrace();*/Log.i("player", "nullmap"); }
        if(debug){
            tempProvince = null;
        }
        nameTitles = new ArrayList<>(0);
        provinces = new ArrayList<>(0);
        provincesStat = new ArrayList<>(0);
        infamyStat = new ArrayList<>(0);
        reinforceStat = new ArrayList<>(0);
        conquersStat = new ArrayList<>(0);
        totalTroopsStat = new ArrayList<>(0);
        continentsStat = new ArrayList<>(0);
        monetaeStat = new ArrayList<>(0);
    }

    public void turn(boolean overwriteStage){
        Log.i("Humanity", "TurnLog: "+getName()+" is human: "+isHuman()+", InSteup: "+inSetup);
        recentWars = new ArrayList<>(0);
        getAllStationedStacks();
        if(!inSetup)
            for(Province p : stationedIn)
                p.getStackFrom(getTag()).resetMoves();
        //if(isHistorical()) movesLeft = 5;
        //else movesLeft = 1;
        changeProvEnabled(isHuman() && !isPuppet());
        if(isHistorical()) {
            warVis(isHuman());
            alertVis(isHuman());
            turnMoveVis(isHuman() && !isPuppet());
        }
        if(stage > 0 && overwriteStage) setStage(0);
        calcAllOwned(true);
        expireTruce();
        refreshWars();
        clearAlerts();
        int i=0;
        for(String s : diploList[0]){
            addAlert(i, Integer.parseInt(s.substring(0, 2)), s.substring(2, 8), s.substring(8));
            i++;
        }
        if(getTurnNum()>1) {
            provincesStat.add(provinces.size());
            infamyStat.add(infamy);
            totalTroopsStat.add(getTotalTroops());
            if(imperium) reinforceStat.add((int) (reinforcements * 1000));
            else reinforceStat.add((int) (reinforcements));
            conquersStat.add(conquers);
            if(!imperium)continentsStat.add(getMap().ownedContinents(this));
            else monetaeStat.add(monetae);
        }
        ArrayList<Province> cores = new ArrayList<>(0);
        for(Province p : getMap().getList()) {
            if(p.getCore().equals(getTag()))
                cores.add(p);
        }
        coreProvs = cores.toArray(new Province[0]);
        if(isHuman()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() { updateAllOwners(); }});
        }
    }

    public String getTag(){return nation.getTag();}
    public String getName(){return nation.getName();}
    public int getFlag(){return nation.getFlag();}
    public int getColor(){return nation.getColor();}
    public double getOpsEfficiency(){return nation.getOpsEfficiency();}
    public double getTroopHardening(){return nation.getTroopHardening();}
    public double getWarChance(){return nation.getWarChance();}
    public Province[] getBordering(){return bordering;}
    public ArrayList<String>[] getDiplo(){return diploList;}
    public boolean isAllied(String tag){ return isRelation(1, tag); }
    public boolean hasSubject(String tag){ return isRelation(2, tag); }
    public boolean isHostile(String tag){ return isRelation(3, tag); }
    public boolean isTruce(String tag){ return isRelation(4, tag); }
    public boolean hasOverlord(String tag){ return isRelation(5, tag);}
    public boolean hasOverlord(){return diploList[5].size() == 1;}
    public String getOverlord(){
        if(diploList[5].size() == 1) return diploList[5].get(0);
        else return "#nn";
    }
    public ArrayList<String> getSubjects(){return diploList[2];}
    public String getWar(String memberTag){
        for(String s : diploList[3]){
            if(s.substring(0, 3).equals(memberTag) || s.substring(3, 6).equals(memberTag) || s.substring(6, 9).equals(memberTag))
                return s.substring(0, 6);
        }
        return "000000";
    }
    public boolean isRelation(int relation, String tag) {
        for (String s : diploList[relation])
            if (s.substring(s.length()-3).equals(tag)) return true;
        return false;
    }
    public int[] getRelations(String tag){
        int[] relations = new int[diploList.length];
        for(int i=1; i<diploList.length; i++)
            if(isRelation(i, tag)) relations[i] = 1;
        /*if(isRelation(2, tag)) return 2;
        if(isRelation(3, tag)) return 3;
        if(isRelation(4, tag)) return 4;
        if(isRelation(5, tag)) return 5;*/
        return relations;
    }
    public int getTruceEnd(String tag){
        Log.i("TruceEnd", getTag()+" with "+" "+tag+" list: "+diploList[4]);
        for(String s : diploList[4])
                if(s.substring(4).equals(tag))
                    try{return Integer.parseInt(s.substring(0, 4));}catch (Exception e){e.printStackTrace();}
        return -1;
    }
    public ArrayList<String> getRecentWars(){return recentWars;}
    public int getExtraDev(){return nation.getExtraDev();}
    //public int getMovesLeft(){return movesLeft;}
    public Nation getNation(){return nation;}
    public double getInfamy(){return infamy;}
    public int getConquests(){return conquers;}
    public int getStage(){return stage;}
    public Province getTempProvince(){return tempProvince;}
    public Province[] getTmpSelect(){ return tmpSelect; }
    public Province[] getSavedSelect(){ return savedSelect; }
    public Province[] getAttackSelected(){return attackSelected;}
    public Province[] getTransportSelected(){return transportSelected;}
    public boolean isHuman(){return human;}
    public boolean isPuppet(){return puppet;}
    public boolean isOwned(Province province){return province.getOwnerId() == id;}
    public int getId(){return id;}
    public double getTroops(){ return reinforcements; }
    public boolean canSpend(){return canSpend;}
    public Province[] getAllOwned(){return allOwned;}
    public Province[] getCoreProvs(){return coreProvs;}
    public boolean isAttackable(String tag){
        return !isAllied(tag) && !isTruce(tag) && !hasSubject(tag) && !hasOverlord();
    }
    public boolean isFriendly(String tag){
        return isAllied(tag) || hasSubject(tag) || hasOverlord(tag);
    }
    public void getAllStationedStacks() {
        ArrayList<Province> stationed = new ArrayList<>(0);
        for (Province p : getMap().getList()) {
            if (p.hasStackFrom(getTag()))
                stationed.add(p);
        }
        stationedIn = stationed.toArray(new Province[0]);
    }
    public void calcAllOwned(boolean updateTitle) {
        //Log.i("clacOwned", "in");
        provinces = new ArrayList<>(0);
        //Log.i("Maplen", ""+getMap().getList().length);
        for (int i = 0; i < getMap().getList().length; i++) {
            //Log.i("ownerId", ""+getMap().getList()[i].getOwnerId());
            if (getMap().getList()[i].getOwnerId() != -1) {
                //Log.i("ownerTag", getMap().getList()[i].getOwner().getTag());
                if (getMap().getList()[i].getOwner().getTag().equals(nation.getTag())) {
                    try {
                        provinces.add(getMap().getList()[i]);
                    } catch (ArrayIndexOutOfBoundsException e) { e.printStackTrace(); }
                }
            }
        }

        if (provinces.size() == 0) {
            allOwned = new Province[0];
            return;
        }
        Log.i("CalcAllOwned", "" + provinces.size() + " from " + getName());
        allOwned = provinces.toArray(new Province[0]);
        Log.i("CalcAllOwned", "" + allOwned.length + " from " + getName());
        //Log.i("ownedlen", ""+allOwned.length);
        if (allOwned.length > 0 && updateTitle) {
            pocketText();
        }

    }
    private void findNehiborTags(){
        ArrayList<String> nehibors = new ArrayList<>(0);
        for(Province p : bordering){
            if(p.getOwner() != null)
                if(!nehibors.contains(p.getOwner().getTag()))
                    nehibors.add(p.getOwner().getTag());
        }
        nehiborTags = nehibors.toArray(new String[0]);
    }
    public void scanBordering()throws ArrayIndexOutOfBoundsException{ //adds all bordering enemy provinces to a list
        Log.i("scanning", "");
        ArrayList<Province> borderingArray = new ArrayList<>(0);
        for (Province owned : getAllOwned())
            for (Province border : owned.getBordering())
                if(border.getOwnerId() != id) borderingArray.add(border);

        bordering = borderingArray.toArray(new Province[0]);
        if(diploList[1].size() > 0) warBorders();
        findNehiborTags();
    }
    protected void warBorders(){
        ArrayList<Province> bigBroder = new ArrayList<>(0);
        ArrayList<Player> allies = new ArrayList<>(0);
        for(String s : diploList[1])
            allies.add(playerFromTag(s));
        for(Player ally : allies){
            for(Province p : ally.getBordering())
                if(howSurrounded(p) != 0)
                    bigBroder.add(p);
        }
        warBorders = bigBroder.toArray(new Province[0]);
    }
    protected int howSurrounded(Province province){ //returns number of enemy troops that border an owned province
        int count = 0;
        for(Province test : province.getBordering()){
            if(test.getOwnerId() != id  && test.getOwner() != null)
                if(!isAllied(test.getOwner().getTag()))
                    count += test.getTroops();
        }
        return  count;
    }
    protected int howSurroundedBy(Province province, int owner){ //returns number of one enemy's troops that border an owned province
        int count = 0;
        for(Province test : province.getBordering()){
            if(test.getOwnerId() == owner)
                count += test.getTroops();
        }
        return  count;
    }
    public double reasonsToAcceptPeace(String enemy){
        if(isHuman()) return -1;
        playerFromTag(enemy).calcAllOwned(false);
        calcAllOwned(false);
        String warWithEnemy = attDefFromTag(enemy);
        if(!getWarsAsLeader().contains(warWithEnemy)) {
            if (playerFromTag(enemy).getCoreProvs().length < 15)
                return (getCoreProvs().length * .75) / getAllOwned().length -
                        (playerFromTag(enemy).getCoreProvs().length * .75) / playerFromTag(enemy).getAllOwned().length;
            else
                return (getCoreProvs().length - 20.0) / getAllOwned().length -
                        (playerFromTag(enemy).getCoreProvs().length - 20.0) / playerFromTag(enemy).getAllOwned().length;
        }else{
            String[] allies = getWarAllies(warWithEnemy);
            int totalLoss = 0;
            int totalProvs = 0;
            for(String s : allies) {
                playerFromTag(s).calcAllOwned(false);
                Log.i("Pleace Reasons",
                        "difference from "+ playerFromTag(s).getName()+": "+(playerFromTag(s).getCoreProvs().length - playerFromTag(s).getAllOwned().length));
                totalLoss += (playerFromTag(s).getCoreProvs().length - playerFromTag(s).getAllOwned().length);
                totalProvs += playerFromTag(s).getAllOwned().length;
            }
            return totalLoss / (double) totalProvs * 1.33;
        }
    }
    public int totalIncome(){
        double total = 0;
        for(Province p : getAllOwned())
            total += p.calcOutput();
        for(String tag : diploList[2])
            total += playerFromTag(tag).totalIncome() * SUBJECT_INCOME;
        if(hasOverlord()) total -= total*SUBJECT_INCOME;
        return (int) total+nation.getExtraDev();
    }
    public String attDefFromTag(String tag){
        for(String s : diploList[3]){
            if(s.substring(0, 3).equals(tag) || s.substring(3, 6).equals(tag)
                    || s.substring(6, 9).equals(tag))
                return s.substring(0, 6);
        }
        return "#nn#nn";
    }
    public String[] getWarAllies(String attDef){
        ArrayList<String> allies = new ArrayList<>(0);
        for(Player p : getPlayerList()) {
            for(String war : p.getDiplo()[3])
                if (war.substring(0, 6).equals(attDef) && !isHostile(war.substring(6, 9)) && !allies.contains(war.substring(6, 9)))
                    allies.add(war.substring(6, 9));
        }
        return allies.toArray(new String[0]);
    }
    public void peaceOutAll() {
        ArrayList<String> warTags = new ArrayList<>(0);
        for (String tag : diploList[3])
            if (!warTags.contains(tag.substring(0, 6)))
                warTags.add(tag.substring(0, 6));

        for (String tag : warTags) {
            String enemyTag = relationsFromWarTag(tag)[1];
            Log.i("PeaceRequest", getName()+" requested peace from "+playerFromTag(enemyTag).getName());
            playerFromTag(enemyTag).addRequestFrom(40, tag, getTag());
        }
    }
    public ArrayList<String> getWarsAsLeader(){
        ArrayList<String> wars = new ArrayList<>(0);
        for(String s : diploList[3])
            if(s.substring(0, 3).equals(getTag()) || s.substring(3, 6).equals(getTag()))
                wars.add(s.substring(0, 6));
        return wars;
    }
    public void printDiplo(){
        String list = "{";
        for(ArrayList<String> ar : diploList) {
            for (String s : ar) {
                if(s.length()!=0) list += s + ",";
                else list += "_,";
            }
            list += "|";
        }
        Log.i(getTag()+"diploOf: "+getName(), list);
    }
    public void remove(){
        Log.i("remove", getName());
        for(TextView t : nameTitles) {
            Log.i("remove", "in, " + t.getText());
            t.setText("");
            Log.i("remove", "in, " + t.getText());
            getMapLayout().removeView(t);
        }
        Log.i("remove", "setup: "+inSetup);
        if(!inSetup) {
            //remove wars
            ArrayList<String> checkedWars = new ArrayList<>(0);
            Log.i("remove", "in not setup");
            ArrayList<String> strings = diploList[3];
            for (int i = 0; i < strings.size(); i++) {
                String s = strings.get(i);
                Log.i("remove", "at:" + s);
                try {
                    if (!checkedWars.contains(s.substring(0, 6))) {
                        Log.i("RemoveFrom", s);
                        checkedWars.add(s.substring(0, 6));
                        removeFromWar(s.substring(0, 6), splitAttDef(s.substring(0, 6))[1]);
                    }
                }catch (Exception e){e.printStackTrace();}
            }
            //remove relations
            for(int i=1; i<diploList.length; i++){
                if(i==3)continue;
                for(int inDip=0; inDip<diploList[i].size(); inDip++){
                    String tag = diploList[i].get(inDip);
                    tag = tag.substring(tag.length()-3);
                    if(i==1){
                        playerFromTag(tag).removeAlly(getTag());
                        removeAlly(tag);
                    }if(i==2){
                        playerFromTag(tag).removeOverlord(getTag());
                        removeMinion(tag);
                    }if(i==4){
                        playerFromTag(tag).removeTruce(getTag());
                        removeTruce(tag);
                    }if(i==5){
                        playerFromTag(tag).removeMinion(getTag());
                        removeOverlord(tag);
                    }
                    inDip--;
                }
            }
        }
    }
    public ArrayList<ArrayList> getStats(){
        ArrayList<ArrayList> allStats = new ArrayList<>(0);
        allStats.add(provincesStat);allStats.add(totalTroopsStat);allStats.add(reinforceStat);allStats.add(infamyStat);allStats.add(conquersStat);
        if(!imperium)allStats.add(continentsStat);
        else allStats.add(monetaeStat);
        return allStats;
    }

    public void setMonetae(int set){monetae = set;}
    public void setId(int set){id = set;}
    public void setTempProvince(Province temp){tempProvince = temp;}
    public void setTroops(double set){ reinforcements = set; }
    public void setConquers(int set){ conquers = set; }
    public void setStage(int set){
        if(nation != null)Log.i("StageSet", ""+set+", name: "+getName());
        stage = set;
        if(stage > 0) selections = 2;
    }
    public void titleVis(boolean vis){
        for(TextView title : nameTitles) {
            if (vis) title.setVisibility(View.VISIBLE);
            else title.setVisibility(View.INVISIBLE);
        }
    }
    public void addRecentWar(String warTag){
        recentWars.add(warTag);
    }
    public void addRequestFrom(int type, String group, String from){
        Log.i("AddRequest", "to "+getTag()+" from "+from);
        if(!from.equals(getTag())) {
            if((""+type).length() == 1) type*=10;
            if(!diploList[0].contains(type + group + from)){
                Log.i("AddRequestt", "to "+getTag()+" from "+from);
                diploList[0].add(type + group + from);
            }
        }
    }
    public void removeRequest(int type, String group, String from){diploList[0].remove(type+group+from);}

    public void addAlly(final String tag){
        Log.i("Add Ally", getName()+" added "+tag);diploList[1].add(tag);
        Log.i("AllyStetup", ""+inSetup);
        if(isHuman() && !inSetup)
            runOnUiThread(new Runnable() {
            @Override
            public void run() { new Event(context, "A New Alliance", "We have entered into an alliance with "+playerFromTag(tag).getName(), new String[]{"A bulwark against our foes"}, R.drawable.dipround, "0"); }});
    }
    public void removeAlly(final String tag){
        Log.i("Remove Ally", getName()+" removed "+tag);diploList[1].remove(tag); addTruce(tag);
        if(isHuman() && !inSetup)
            runOnUiThread(new Runnable() {
                @Override
                public void run() { new Event(context, "An Ally Lost", "We have lost our alliance with "+playerFromTag(tag).getName(), new String[]{"Good riddance!"}, R.drawable.dipround, "0"); }});
    }

    public void addMinion(final String tag){
        diploList[2].add(tag);
        if(isHuman() && !inSetup)
            runOnUiThread(new Runnable() {
                @Override
                public void run() { new Event(context, "A New Subject", playerFromTag(tag).getName()+" has agreed to our overlordship", new String[]{"Our domain grows"}, R.drawable.dipround, "0"); }});
    }
    public void removeMinion(final String tag){
        diploList[2].remove(tag); addTruce(tag);
        if(isHuman() && !inSetup)
            runOnUiThread(new Runnable() {
                @Override
                public void run() { new Event(context, "A Subject has broken away", "The traitorous people of "+playerFromTag(tag).getName()+" have rejected our protection and influence", new String[]{"Good riddance!"}, R.drawable.dipround, "0"); }});
    }

    public void addOverlord(final String tag){
        diploList[5].add(tag);
        if(isHuman() && !inSetup)
            runOnUiThread(new Runnable() {
                @Override
                public void run() { new Event(context, "A New Protector", "The government of "+playerFromTag(tag).getName()+" has agreed to shield our nation from our foes", new String[]{"Security at last"}, R.drawable.dipround, "0"); }});
    }
    public void removeOverlord(final String tag){
        diploList[5].remove(tag); addTruce(tag);
        if(isHuman() && !inSetup)
            runOnUiThread(new Runnable() {
                @Override
                public void run() { new Event(context, "Freedom", "We have thrown off the chains of our oppressors!", new String[]{"The future is ours!"}, R.drawable.dipround, "0"); }});
    }
    public ArrayList<String> getAllMinions(){
        ArrayList<String> subs = new ArrayList<>(0);
        for(String sub : diploList[2]) {
            subs.add(sub);
            for (String subSub : playerFromTag(sub).getAllMinions())
                subs.add(subSub);
        }
        return subs;
    }
    public void addHostile(String tag, String attacker, String defender){ //only for top level overlords
        String tagAt = tag;
        while(playerFromTag(tagAt).hasOverlord()) tagAt = playerFromTag(tagAt).getDiplo()[5].get(0);
        while(playerFromTag(tagAt).hasOverlord()) tagAt = playerFromTag(tagAt).getDiplo()[5].get(0);
        tag = tagAt; //highest enemy
        ArrayList<String> enemies = playerFromTag(tag).getAllMinions();
        ArrayList<String> friends = getAllMinions();
        for(String ours : friends) {
            playerFromTag(ours).getDiplo()[3].add(attacker+defender+tag);
            Log.i("War", tag+" is now enemy of "+ours);
            for (String theirs : enemies) {
                playerFromTag(ours).getDiplo()[3].add(attacker+defender+theirs);
                Log.i("War", theirs + " is now enemy of " + ours);
            }
        }
        for (String theirs : enemies) {
            diploList[3].add(attacker+defender+theirs);
            Log.i("War", theirs+" is now enemy of "+getName());
        }
        diploList[3].add(attacker+defender+tag);
        Log.i("War", tag+" is now enemy of "+getName());
        if(isHuman() && !inSetup) {
            final String finalTag = tag;
            runOnUiThread(new Runnable() {
                @Override
                public void run() { new Event(context, "A New Enemy", "We have entered into conflict with the tyrants of"+playerFromTag(finalTag).getName(), new String[]{"Prepare for battle!"}, R.drawable.dipround, "0"); }});
        }
    }
    public void removeHostile(String tag, String attacker, String defender, boolean winningSide){
        Log.i("Remosve Hostile", getName()+winningSide);
        calcAllOwned(false);
        if(winningSide){
            ArrayList<String> enemies = playerFromTag(tag).getAllMinions();
            enemies.add(tag);
            for(Province p : allOwned){
                String oldOwner = p.getOwner().getTag();
                if(enemies.contains(oldOwner)) {
                    Log.i("ProvinceGain", p.getName()+" to "+getTag()+" from "+oldOwner);
                    p.modTroops(1, getTag());
                    p.updatePress(getId());
                    p.modTroops(-1, oldOwner);
                }
            }
        }else{
            for(Province p : allOwned)
                if(!p.getCore().equals(getTag())){
                    Log.i("ProvinceLoss", p.getName()+" to "+p.getCore()+" from "+getTag());
                    p.modTroops(-1, getTag());
                    p.updatePress(playerIdFromTag(p.getCore()));
                    p.modTroops(1, p.getCore());
                }
        }
        calcAllOwned(false);
        saveCores();
        removeHostile(tag, attacker, defender);
    }
    public void removeHostile(String tag, String attacker, String defender){
        String tagAt = tag;
        Log.i("REmovingHostile", tagAt);
        addTruce(tagAt);
        while(playerFromTag(tagAt).hasOverlord()) tagAt = playerFromTag(tagAt).getDiplo()[5].get(0);
        tag = tagAt; //highest enemy
        ArrayList<String> enemies = playerFromTag(tag).getAllMinions();
        ArrayList<String> friends = getAllMinions();
        for(String ours : friends) {
            playerFromTag(ours).getDiplo()[3].remove(attacker+defender+tag);
            playerFromTag(ours).addTruce(tag);
            Log.i("peace", tag+" is not enemy of "+ours);
            for (String theirs : enemies) {
                playerFromTag(ours).getDiplo()[3].remove(attacker+defender+theirs);
                playerFromTag(ours).addTruce(theirs);
                Log.i("Preace", theirs + " is not enemy of " + ours);
            }
        }
        for (String theirs : enemies) {
            diploList[3].remove(attacker+defender+theirs);
            addTruce(theirs);
            Log.i("peace", theirs+" is not enemy of "+getName());
        }
        diploList[3].remove(attacker+defender+tag);
        addTruce(tag);
        Log.i("peace", tag+" is not enemy of "+getName());
        if(isHuman() && !inSetup) {
            final String finalTag = tag;
            runOnUiThread(new Runnable() {
                @Override
                public void run() { new Event(context, "Peace!", "We have made peace with our former enemies in "+playerFromTag(finalTag).getName(), new String[]{"Let us move forward"}, R.drawable.dipround, "0"); }});
        }
    }
    public void addToWar(String attDef, String senderTag){
        saveCores();
        String enemyTag = attDef.substring(0, 3);
        String attackerTag = attDef.substring(0, 3);
        String defenderTag = attDef.substring(3);
        if(enemyTag.equals(senderTag)) {
            enemyTag = attDef.substring(3);
        }
        if(senderTag.equals(getTag())){
            addHostile(enemyTag, attackerTag, defenderTag);
            addRecentWar(attDef);

            for (String s : diploList[1])
                if(!playerFromTag(s).isTruce(enemyTag)) playerFromTag(s).addRequestFrom(3, attDef, getTag());
        }else{
            for(String tag : playerFromTag(senderTag).getDiplo()[3]){
                if(tag.substring(0, 6).equals(attDef)){
                    addHostile(tag.substring(6), attackerTag, defenderTag);
                    playerFromTag(tag.substring(6)).addHostile(getTag(), attackerTag, defenderTag);
                }
            }
        }
        addRecentWar(attDef);
    }
    public void removeFromWar(String attDef, String winLeader) {
        Log.i("removeFromWar", attDef+" winlead "+winLeader);
        for(Province p : coreProvs){
            if(isFriendly(p.getOwner().getTag())) {
                Log.i("ProvinceLoss2", p.getName()+" to "+getTag()+" from "+p.getOwner().getTag());
                p.modTroops(1, getTag());
                p.updatePress(getId());
            }
        }
        if (getTag().equals(attDef.substring(0, 3)) || getTag().equals(attDef.substring(3))) {
            Log.i("END OF War", attDef);
            for (Player p : getPlayerList()) {
                //p.printWars();
                Log.i("removeWar", p.getName()+"*************************************");
                for (int i = 0; i < p.getDiplo()[3].size(); i++) {
                    String s = p.getDiplo()[3].get(i);
                    Log.i("removeWar", s+" atIndec: "+i+" of "+p.getDiplo()[3].size());
                    if (s.substring(0, 6).equals(attDef)) {
                        Log.i("removeWar", "removed " + s);
                        //p.getDiplo()[3].remove(s);
                        Log.i("RemoveFromWar", "Hostility: "+winLeader+p.isHostile(winLeader));
                        p.printDiplo();
                        p.removeHostile(s.substring(6, 9), s.substring(0, 3), s.substring(3, 6), winLeader.equals(getTag()) || !p.isHostile(winLeader));
                        i = 0;
                    }
                }
                Log.i("removeWar", "++++++++++++");
                //p.printWars();
                removeWar(attDef.substring(0, 3), attDef.substring(3));
            }
        } else {
            if(!isHostile(attDef.substring(3))) {
                removeHostile(attDef.substring(0, 3), attDef.substring(0, 3), attDef.substring(3), false);
                playerFromTag(attDef.substring(0, 3)).removeHostile(getTag(), attDef.substring(0, 3), attDef.substring(3));
            }
            else {
                removeHostile(attDef.substring(3), attDef.substring(0, 3), attDef.substring(3), false);
                playerFromTag(attDef.substring(3)).removeHostile(getTag(), attDef.substring(0, 3), attDef.substring(3));
            }
            ArrayList<String> strings = diploList[3];
            for (int i = 0; i < strings.size(); i++) {
                String s = strings.get(i);
                Log.i("ally", s);
                if (s.substring(0, 6).equals(attDef)) {
                    removeHostile(s.substring(6, 9), s.substring(0, 3), s.substring(3, 6));
                    playerFromTag(s.substring(6, 9)).removeHostile(getTag(), s.substring(0, 3), s.substring(3, 6), true);
                    i = 0;
                }
            }
        }
        saveCores();
        printWars();
    }
    //allied, hostile
    public String[] splitAttDef(String attDef){
        if(isHostile(attDef.substring(0, 3)))
            return new String[]{attDef.substring(3), attDef.substring(0, 3)};
        else
            return new String[]{attDef.substring(0, 3), attDef.substring(3)};
    }
    public void printWars(){
        for (int i = 0; i < diploList[3].size(); i++) {
            String s = diploList[3].get(i);
            Log.i("Printwarsof: " + getName(), s);
        }
    }
    public void addTruce(String tag){
        Log.i("truce", "truce end: "+(getTurnNum()+TRUCE_TIMER));
        diploList[4].add(formatInt(getTurnNum()+TRUCE_TIMER*getPlayerList().length, 4)+tag);
    }
    public void addTruce(String tag, int turns){ diploList[4].add(formatInt(getTurnNum()+turns*getPlayerList().length, 4)+tag);}
    public void removeTruce(String tag){
        for (int i = 0; i < diploList[4].size(); i++) {
            String s = diploList[4].get(i);
            if (s.substring(4).equals(tag)) {
                diploList[4].remove(s);
                i--;
            }
        }
    }
    public void expireTruce(){
        for (int i = 0; i < diploList[4].size(); i++) {
            String s = diploList[4].get(i);
            if (Integer.parseInt(s.substring(0, 4)) <= getTurnNum()) {
                Log.i("Expire truec", getName()+" with "+s);
                diploList[4].remove(s);
                i--;
            }
        }
    }
    public String[] relationsFromWarTag(String attDef){
        String enemyTag = attDef.substring(0, 3);
        String friendTag = attDef.substring(3);
        if(!isHostile(enemyTag)) {
            enemyTag = attDef.substring(3);
            friendTag = attDef.substring(0, 3);
        }
        return new String[]{friendTag, enemyTag};
    }
    public void saveCores(){
        calcAllOwned(false);
        coreProvs = allOwned;
        for(Province p : allOwned)
            p.setCoreOwner(getTag());
    }
    public boolean willAlly(String tag) { return diploList[1].size() < 3; }
    public boolean willSubmitTo(String tag) { return playerFromTag(tag).totalIncome() > 10*totalIncome();}
    public void pocketText(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for(TextView title : nameTitles) getMapLayout().removeView(title);
                nameTitles = new ArrayList<>(0);
                ArrayList<Province> arr = new ArrayList<Province>(0);
                ArrayList<Province[]> pockets = new ArrayList<>(0);
                for(Province p : allOwned){
                    boolean cont = true;
                    for(Province[] pock : pockets) {
                        for (Province inPock : pock) {
                            if (inPock.getId() == p.getId()) cont = false;
                        }
                    }
                    if(!cont) continue;
                    arr = new ArrayList<Province>(0);
                    //Log.i("seedProv", p.getName());
                    arr.add(p);
                    pockets.add(borderRecur(arr).toArray(new Province[0]));
                }
                for(Province[] pocket : pockets){
                    calcText(pocket);
                    //Log.i("pocketProv", "Pocket:");
                    //for(Province p : pocket) Log.i("pocketProv", p.getName());
                }
                //Log.i("pocketProv", "Done");
                for(TextView title : nameTitles) getMapLayout().addView(title);
            }
        });
    }
    public ArrayList<Province> borderRecur(ArrayList<Province> seed){
        for(Province p : seed.get(seed.size()-1).getBordering()){
            if(p.getOwnerId() == id && !seed.contains(p)){
                seed.add(p);
                borderRecur(seed);
            }
        }
        return seed;
    }
    public void calcText(final Province[] list) {
        TextView nameTitle = new TextView(context);
        nameTitle.setTextColor(Color.BLACK);
        nameTitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 5);
        nameTitle.setText(nation.getName());
        if(scaling > 2) nameTitle.setVisibility(View.INVISIBLE);
        Point center = new Point(0, 0);
        for (Province p : list) {
            //Log.i("centor", "x: " + p.getCenter().x + ", y: " + p.getCenter().y);
            center.x += p.getCenter().x;
            center.y += p.getCenter().y;
        }
        center.x /= list.length;
        center.y /= list.length;

        Point furthest = center;
        float xDiff = Math.abs(furthest.x - center.x);
        float yDiff = Math.abs(furthest.y - center.y);
        for (Province p : list) {
            if (Math.abs(p.getCenter().x - center.x) > Math.abs(xDiff) && Math.abs(p.getCenter().y - center.y) > Math.abs(yDiff)) {
                furthest = p.getCenter();
                //Log.i("futhedr", p.getName() + ", " + furthest.x + ", " + furthest.y);
                xDiff = furthest.x - center.x;
                yDiff = furthest.y - center.y;
            }
        }
        float diffVector = (float) Math.sqrt(xDiff * xDiff + yDiff * yDiff);
        //Log.i("diffs", "" + xDiff + ", " + yDiff);
        float rot = (float) Math.toDegrees(Math.atan(yDiff / (xDiff + 1)));
        //Log.i("Rotat", "" + rot);
        nameTitle.animate().rotation(rot).setDuration(0);
        float typeSize = diffVector / (float) Math.sqrt(getName().length());
        if (typeSize < 5) typeSize = 5;
        else if (typeSize > 50) typeSize = 50;
        nameTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, typeSize);
        center.x -= 1.4 * typeSize * Math.sqrt(getName().length()) / 2;
        center.y -= 1.4 * typeSize / 2;
        //Log.i("www", "" + nameTitle.getMeasuredHeight() + ", " + nameTitle.getHeight() + ", " + typeSize);
        //Log.i("centorFin", "x: " + center.x + ", y: " + center.y);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.leftMargin = center.x;
        params.topMargin = center.y;
        nameTitle.setLayoutParams(params);
        nameTitles.add(nameTitle);
    }
    //public void modMovesLeft(int mod){movesLeft += mod;}
    public int modMonetae(int mod){
        if(mod != 0) Log.i("modMoney", ""+monetae+"moddedby"+mod);
        if(monetae + mod >= 0) {
            monetae += mod;
            canSpend = true;
        }else canSpend = false;
        return monetae;
    }
    public double modTroops(int num){
        reinforcements += num;
        return reinforcements;
    }
    public int select(int change){
        //Log.i("SelectionPlayer", "chanfe: "+change+", prevSelect: "+selections);
        if(selections + change >= 0) selections += change;
        if(selections > 2) selections = 2;
        return  selections;
    }
    public void refreshWars(){
        if(diploList[3].size() > 0 && isHuman()) {
            ArrayList<String> currentWars = new ArrayList<>(0);
            for(String hostile : diploList[3])
                if(!currentWars.contains(hostile.substring(0, 6)))
                    currentWars.add(hostile.substring(0, 6));
            for(String war : currentWars){
                Log.i("addWarsTurn", "added: "+war);
                addWar(war.substring(0, 3), war.substring(3, 6));
            }

        }
    }
    public void parseStats(ArrayList<ArrayList> stats){
        provincesStat = stats.get(0);totalTroopsStat = stats.get(1);reinforceStat = stats.get(2);infamyStat = stats.get(3);conquersStat = stats.get(4);
        if(imperium)monetaeStat = stats.get(5);
        else continentsStat = stats.get(5);
    }
    public boolean attackScan(){
        attackSelected = new Province[2];
        boolean friend = false;
        boolean foe = false;
        for(Province p : getMap().getList()) {
            //if(p.getOwnerId() == id) Log.i("attack", "owned");
            //if(p.isSelected()) Log.i("attack", "select");
            if(p != null) {
                if (p.getOwnerId() == id && p.isSelected() && p.getTroops() > 1 || p.hasGuestStackFrom(getTag())) { //may be buggy
                    friend = true;
                    //Log.i("attackScan", "Friend: " + p.getName());
                    attackSelected[0] = p;
                }

                if (p.getOwner() != null && isHistorical()) {
                    if ((p.getOwnerId() != id && p.getOwner().isHostile(getTag())) && p.isSelected()) {
                        foe = true;
                        //Log.i("attackScan", "Foe: " + p.getName());
                        attackSelected[1] = p;
                    }
                } else if (p.getOwnerId() != id && p.isSelected()) {
                    foe = true;
                    //Log.i("attackScan", "Foe: " + p.getName());
                    attackSelected[1] = p;
                }
            }
        }
        try {
            if (attackSelected[0] != null && attackSelected[1] != null) {
                savedSelect[0] = attackSelected[0];
                savedSelect[1] = attackSelected[1];
                Log.i("scanNames", attackSelected[0].getName()+", "+attackSelected[1].getName());
                Log.i("attackScan", "friend: " + friend + " foe: " + foe + " Borders: " + attackSelected[0].bordering(attackSelected[1]));
                // if (friend && foe && attackSelected[0].bordering(attackSelected[1]) && stage == 1)
                    //tmpSelect[0].showAim(tmpSelect[1]);

                Log.i("attackScan", "defenderOwner: " + attackSelected[1].getOwnerId() + ", atttckerTroops: " + attackSelected[0].getTroops());
                if (attackSelected[1].getOwnerId() == -1 && attackSelected[0].getTroops() <= 3){ Log.i("Attachscan", "flaas");return false;}
                return friend && foe && attackSelected[0].bordering(attackSelected[1]);
            }
            else if(savedSelect[0] != null && savedSelect[1] != null){
                savedSelect[0].hideAim();
                savedSelect[1].hideAim();
            }
        } catch (NullPointerException e){e.printStackTrace();return false;}
        //Log.i("attack", "bothnull");
        return false;
    }
    public boolean transportScan(){
        transportSelected = new Province[2];
        int friends = 0;
        Province[] list = getMap().getList();
        for(int i=0; i<list.length; i++) {
            if(list[i] != null) {
                if (list[i].getOwner() != null && isHistorical()) {
                    //Log.i("Tansporty", ""+list[i].getOwner().isAllied(getTag()));
                    if ((list[i].getOwnerId() == id || list[i].getOwner().isAllied(getTag())) && list[i].isSelected()) {
                        if (friends == 0) {
                            friends++;
                            transportSelected[0] = list[i];
                        }
                        if (friends == 1 && transportSelected[0].getId() != list[i].getId()) {
                            friends++;
                            transportSelected[1] = list[i];
                            //selections = 2;
                            //Log.i("Fren", "dos");
                        }
                    }
                } else {
                    if (list[i].getOwnerId() == id && list[i].isSelected()) {
                        if (friends == 0) {
                            friends++;
                            transportSelected[0] = list[i];
                        }
                        if (friends == 1 && transportSelected[0].getId() != list[i].getId()) {
                            friends++;
                            transportSelected[1] = list[i];
                            //Log.i("Fren", "dos");
                        }
                    }
                }
            }
        }
        if(friends == 2) runOnUiThread(new Runnable() {
            @Override
            public void run() { getAgain().setBackgroundResource(R.drawable.transport); }});
        if(transportSelected[0] != null) return friends == 2  && transportSelected[0].bordering(transportSelected[1]);
        return false;
    }
    public int[] attack(){
        Log.i("attack", "activated by: "+getName());
        int[] losses;
        int[] rolls = new int[6];
        rolls[5] = 0; //detects if attackscan returned true
        //stage = 1;
        if(attackScan()) {
            rolls[5] = 1;
            savedSelect = new Province[2];
            savedSelect[0] = attackSelected[0]; savedSelect[1] = attackSelected[1];
            losses = roll(savedSelect[0].getTroops(), savedSelect[1].getTroops());
            if (!imperium) {
                Log.i("attack", "scanned");
                savedSelect[0].attackMod(losses[0]);
                savedSelect[1].attackMod(losses[1]);
            }else {
                Log.i("attack", "scanned2");
                if (savedSelect[1].getTroops() == 0) //unowned province
                    savedSelect[0].modTroops(-1);
                else {
                    double hardeningRatio = savedSelect[1].getOwner().getTroopHardening()/getTroopHardening();
                    Log.i("HardeningRatio", getName()+": "+getTroopHardening()+", "+savedSelect[1].getOwner().getName()
                    +": "+savedSelect[1].getOwner().getTroopHardening());
                    double wipe1 = losses[0] * Math.pow(savedSelect[1].getTroops() / savedSelect[0].getTroops(), .33)*hardeningRatio;
                    double wipe2 = losses[1] * Math.pow(savedSelect[0].getTroops() / savedSelect[1].getTroops(), .33)/hardeningRatio;
                    if (wipe1 < -10) wipe1 = -10;
                    if (wipe2 < -10) wipe2 = -10;
                    Log.i("attack", "Losses: " + losses[0] + ", " + losses[1]);
                    Log.i("attack", "wipes: " + wipe1 + ", " + wipe2);
                    savedSelect[0].attackMod(wipe1);
                    savedSelect[1].attackMod(wipe2);
                }
                Log.i("attack", "troops: " + savedSelect[1].getTroops()+", "+savedSelect[0].getTroops());
            }
            for (int i = 0; i < 5; i++) rolls[i] = losses[i + 2];
            if (savedSelect[1].getTroops() == 0) {
                conquers++;
                calcInfamy();
                Log.i("attac", "plater: "+getTag()+", "+isHuman());
                if(isHuman()) {
                    if (losses[0] == 0) Achivements.getAchive("lucky");
                    if (getTag().equals("pap")) Achivements.getAchive("ricardo");
                }
                Log.i("defeated", "" + savedSelect[1].getName() + "'s been defeated");
                Player defeatedOwner = null;
                if(savedSelect[1].getOwnerId() != -1){
                    defeatedOwner = savedSelect[1].getOwner();
                    //savedSelect[1].getOwner().calcAllOwned();
                    //if(savedSelect[1].getOwner().getAllOwned().length == 0) getPlayerList()[defeatedOwner.getId()].remove();
                }
                if(isFriendly(savedSelect[1].getCore()) && isHostile(savedSelect[1].getOwner().getTag())) {
                    savedSelect[1].updatePress(playerIdFromTag(savedSelect[1].getCore()));
                    savedSelect[1].modTroops(1, savedSelect[1].getCore());
                }
                else
                    savedSelect[1].updatePress(savedSelect[0].getOwnerId());
                //calcAllOwned();

                
                if (savedSelect[0].getTroops() > 3) {
                    savedSelect[0].modTroops(-3);
                    savedSelect[1].modTroops(3);
                    if(/*isHuman()*/true) {
                        final Player finalDefeatedOwner = defeatedOwner;
                        runOnUiThread(new Runnable() {
                            @Override public void run() {
                                startTransport();
                                if(finalDefeatedOwner != null) {
                                    getPlayerList()[finalDefeatedOwner.getId()].calcAllOwned(false);
                                    Log.i("defeated", "" + finalDefeatedOwner.getName() + " has " + finalDefeatedOwner.getAllOwned().length + " provinces");
                                    if (finalDefeatedOwner.getAllOwned().length == 0) {
                                        getPlayerList()[finalDefeatedOwner.getId()].remove();
                                        for (int i = 0; i < getMapLayout().getChildCount(); i++) {
                                            if (("" + getMapLayout().getChildAt(i).getClass()).equals("class android.widget.TextView")) {
                                                if (((TextView) getMapLayout().getChildAt(i)).getText().equals(finalDefeatedOwner.getName()))
                                                    ((TextView) getMapLayout().getChildAt(i)).setText("");
                                            }
                                        }
                                    }
                                }
                            }});
                    }
                } else if (savedSelect[0].getTroops() == 3) {
                    savedSelect[0].modTroops(-2);
                    savedSelect[1].modTroops(2);
                } else if (savedSelect[0].getTroops() == 2) {
                    savedSelect[0].modTroops(-1);
                    savedSelect[1].modTroops(1);
                }
            }
            if(savedSelect[0] != null && savedSelect[1] != null) {
                if (savedSelect[1].getOwnerId() != -1) savedSelect[1].getOwner().calcAllOwned(true);
                if (savedSelect[0].getOwnerId() != -1) savedSelect[0].getOwner().calcAllOwned(true);
            }
        }
        return rolls;
    }
    public void saveSelected(){
        savedSelect = tmpSelect;
    }
    public void transport(int num){
        Log.i("transportingFrom: "+getTag(), "troops: " + num);
        Log.i("transporting", "prohgress: " + getSlideProgress());
        if(transportScan()) {
            if(getSlideProgress() < 50) {
                //transportSelected[0].modTroops(num, getTag());
                //transportSelected[1].modTroops(-num, getTag());
                if(!transportSelected[1].transportTo(num, transportSelected[0]))
                    transportSelected[0].transportTo(num, transportSelected[1]);
            }
            else {
                //transportSelected[0].modTroops(-num, getTag());
                //transportSelected[1].modTroops(num, getTag());
                if(!transportSelected[0].transportTo(num, transportSelected[1]))
                    transportSelected[1].transportTo(num, transportSelected[0]);
                Log.i("transportying", "endPort");
            }
            transportSelected[0].updateOwner();
            transportSelected[1].updateOwner();
        }
        Log.i("transporting", "inPlayerEnded");
        endTransport();
        endAttack();
    }

    //potected
    public int getTotalTroops(){
        int total = 0;
        final ArrayList<Province> save = provinces;
        try{
            for(Province p : save) total += p.getTroops();
        }catch (Exception e){e.printStackTrace();}
        return total;
    }
    public int getFreeTroops(){
        int total = 0;
        final ArrayList<Province> save = provinces;
        try{
            for(Province p : save) total += p.getTroops();
        }catch (Exception e){e.printStackTrace();}
        return total-save.size();
    }
    protected void calcInfamy(){
        if(infamy < 2 && getTurnNum() > 0) infamy = (double)conquers / getTurnNum() * getPlayerList().length;
        if(infamy > 2) infamy = 2;
        Log.i("infame", "inf: "+infamy+", conq: "+conquers+", trn: "+getTurnNum());
    }
    protected void bubbleSort(int[] a) {
        boolean sorted = false;
        int temp;
        while(!sorted) {
            sorted = true;
            for (int i = 0; i < a.length - 1; i++) {
                if (a[i] > a[i+1]) {
                    temp = a[i];
                    a[i] = a[i+1];
                    a[i+1] = temp;
                    sorted = false;
                }
            }
        }
    }
    protected int[] roll(double men1, double men2) {
        int [] aftermath = new int[7];
        int [] aRolls = new int[3];
        int [] dRolls = new int[2];
        int fortified = 0;
        int aLoss = 0;
        int dLoss = 0;
        int temp;
        if(men1 > 0 && men2 > 0) {
            aRolls[0] = (int)(Math.random()*6+1);
            dRolls[0] = (int)(Math.random()*6+1);
            if (men1 >= 3)
                aRolls[1] = (int) (Math.random() * 6 + 1);
            if (men2 > 2)
                dRolls[1] = (int) (Math.random() * 6 + 1);
            if (men1 > 3)
                aRolls[2] = (int) (Math.random() * 6 + 1);
        }
        //Log.i("rolls", "aRolls2: " + aRolls[2]);
        //Log.i("rolls", "aRolls1: " + aRolls[1]);
        //Log.i("rolls", "aRolls0: " + aRolls[0]);
        bubbleSort(aRolls);
        bubbleSort(dRolls);
        /*temp = aRolls[0];

        aRolls[2] = temp;*/
        temp = dRolls[0];
        if(attackSelected[1].getFortLevel() > 1) fortified = 1;
        if(!isHistorical()) {
            aRolls[0] = aRolls[2] + (int) infamy;
            if (attackSelected[1].getOwnerId() != -1)
                dRolls[0] = dRolls[1] + (int) attackSelected[1].getOwner().getInfamy() + fortified;
            dRolls[1] = temp + (int) (attackSelected[1].getFortLevel() * Math.random()) / attackSelected[1].getFortLevel();
        }
        else{
            aRolls[0] = aRolls[2];
            if (attackSelected[1].getOwnerId() != -1)
                dRolls[0] = dRolls[1] + fortified;
            dRolls[1] = temp + (int) (attackSelected[1].getFortLevel() * Math.random()) / attackSelected[1].getFortLevel();
        }
        if(aRolls[0] > dRolls[0]) dLoss--;
        else if(aRolls[0] < dRolls[0]) aLoss--;
        else if(men1 > 0 && men2 > 0) aLoss--;
        if(aRolls[1] != 0 && dRolls[1] != 0){
            if(aRolls[1] > dRolls[1]) dLoss--;
            else if(aRolls[1] < dRolls[1]) aLoss--;
            else aLoss--;
        }
        aftermath[0] = aLoss;
        aftermath[1] = dLoss;
        aftermath[2] = aRolls[0];
        aftermath[3] = aRolls[1];
        aftermath[4] = aRolls[2];
        aftermath[5] = dRolls[0];
        aftermath[6] = dRolls[1];
        if(imperium) {
            attackSelected[0].modDevastation(-.03 * aLoss);
            attackSelected[1].modDevastation(-.03 * dLoss);
        }
        return aftermath;
    }

    public int collectiveTroops(){
        int collective = (int)getFreeTroops();
        for(String tag : diploList[1]){
            collective += (int) playerFromTag(tag).getFreeTroops();
        }
        return collective;
    }
}