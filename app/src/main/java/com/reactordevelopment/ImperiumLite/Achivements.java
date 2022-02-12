package com.reactordevelopment.ImperiumLite;

import android.content.SharedPreferences;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static com.reactordevelopment.ImperiumLite.MainActivity.SAVE_FORM;
import static com.reactordevelopment.ImperiumLite.MainActivity.achives;


public class Achivements extends Game {
    //private static final String ACHIVE_FILE = achiveFile();
    public static final String[] ACHIVE_TAGS = new String[]{"remove", "space", "winged", "ground", "unvasion",
            "moors", "tracts", "justinian", "waves", "badnito", "goodnito", "off", "woahh", "hedgemon", "reconned",
            "zeroTo", "heroTo", "kylie", "lucky", "ctrlZ", "ricardo", "roads", "reconquistus", "khans", "deus", "student",
            "private", "portals", "steamroll", "realThird", "notice", "defenestrate", "mercs", "tables", /*"rough", "zone",*/
            "reich", "bismark", "ashes", "gauls"};
    //private static final String[] ACHIVE_TAGS = tagsFromFile();
    private static SharedPreferences.Editor edit = achives.edit();;
    private static InputStream stream;
    {
        try { stream = getAssets().open("sacredTexts/achives.txt");
        } catch (IOException e) { e.printStackTrace(); }
    }

    private static Province[] provs;
    private static Player current;
    private static String tag;

    public static void scanCriteria(){
        //final String[] HORDES = {"Kha, nog, cri, gdn"};
        ArrayList<String> hordes = new ArrayList<>(Collections.singletonList("Kha, nog, cri, gdn"));
        current = getCurrentPlayer();
        Log.i("DEbugAchive", game.getTimeline()+game.getYear());
        int year = game.getYear();
        String timeline = game.getTimeline();
        provs = current.getAllOwned();
        tag = current.getTag();
        getAchive("kylie");
        if (getMap().getId() == 2) {
            if (timeline.equals("alp")) {
                edit.putBoolean("DEBUG", true);
                Log.i("DEbugAchive", "done");
                if (tag.equals("srb")) {
                    if (scanProvFor(186) && playerByTag("ott").getAllOwned().length == 0) {
                        getAchive("remove");
                    }
                }
                if (tag.equals("pol") || tag.equals("Pol") || tag.equals("POl") || tag.equals("plc")) {
                    if (current.totalIncome() == 300) {
                        getAchive("space");
                    }
                    if (year == 1618 && current.getFreeTroops() >= 300) {
                        getAchive("winged");
                    }
                }
                if (tag.equals("nov")) {
                    if (year == 1445 && playerByTag("msk").getAllOwned().length == 0 && scanProvFor(146)) {
                        getAchive("ground");
                    }
                }
                if (tag.equals("eng")) {
                    if (year == 1066 && scanProvFor(new int[]{30, 31, 38, 39, 29, 32, 27, 28, 36, 34, 35, 37, 41})) {
                        getAchive("unvasion");
                    }
                }
                if (tag.equals("gra")) {
                    if (year == 1248 && playerByTag("cas").getAllOwned().length == 0
                            && playerByTag("por").getAllOwned().length == 0 && playerByTag("ara").getAllOwned().length == 0
                            && scanProvFor(new int[]{18, 22, 26})) {
                        getAchive("moors");
                    }
                }
                if (tag.equals("sct")) {
                    if (playerByTag("eng").getAllOwned().length == 0 && playerByTag("gbr").getAllOwned().length == 0) {
                        getAchive("tracts");
                    }
                }
                if (tag.equals("bzn")) {
                    if (year >= 802 && scanProvFor(new int[]{61, 32, 22, 12, 186})) {
                        getAchive("justinian");
                    }
                    if (year == 1445 && scanProvFor(new int[]{89, 94, 92, 93, 188, 187, 189, 190, 195, 191, 192, 194, 196, 197, 199, 198})
                            && scanProvFor(new int[]{186, 91, 90}, "ott")) {
                        getAchive("off");
                    }
                    if (year >= 396 && playerByTag("rom").getAllOwned().length == 0) {
                        getAchive("student");
                    }
                }
                if (tag.equals("gbr")) {
                    if (year == 1823 && scanProvFor(new int[]{39, 38, 31, 30, 29, 27, 23, 17, 18, 19, 20})) {
                        getAchive("waves");
                    }
                }
                if (tag.equals("Ita")) {
                    if (year == 1931) {
                        getAchive("badnito");
                        Log.i("AchiveTile", "badnito");
                        if (scanProvFor(new int[]{32, 61, 22, 186})) {
                            getAchive("goodnito");
                        }
                    }
                }
                if (tag.equals("rus") || tag.equals("msk")) {
                    if (scanProvFor(new int[]{61}) && getMap().getList()[145].modDevelopment(0) >= 20) {
                        getAchive("roads");
                    }
                }
                if (tag.equals("rom")) {
                    if (year == 477 && scanProvFor(new int[]{65, 63, 64, 62, 61, 48, 67, 49, 42}) && playerByTag("odo").getAllOwned().length == 0) {
                        getAchive("reconquistus");
                    }
                }
                if (hordes.contains(tag)) {
                    int count = 0;
                    for (String str : hordes) {
                        if (!str.equals(tag)) {
                            if (playerByTag(str).getAllOwned().length == 0)
                                count++;
                        }
                    }
                    if (count == hordes.size() - 1)
                        getAchive("khans");
                }
                if (tag.equals("lat")) {
                    if (year == 1248 && scanProvFor(224) && playerByTag("nic").getAllOwned().length == 0 && playerByTag("ach").getAllOwned().length == 0) {
                        getAchive("deus");
                    }
                }
                if (tag.equals("num")) {
                    if (scanProvFor(new int[]{1, 2, 3, 4, 5, 6, 7, 9, 8, 10, 11, 12, 13, 14, 15, 16})) {
                        getAchive("private");
                    }
                }
                if (tag.equals("odo")) {
                    if (year == 477 && scanProvFor(new int[]{61, 62, 91, 186, 235, 194})
                            && playerByTag("rom").getAllOwned().length == 0 && playerByTag("bzn").getAllOwned().length == 0) {
                        getAchive("realThird");
                    }
                }

                if (tag.equals("azz")) {
                    if (scanProvFor(new int[]{235, 236, 238, 239, 240, 241, 242, 247, 246, 248, 245, 254, 253, 252, 251, 256, 259, 258})) {
                        getAchive("notice");
                    }
                }
                if (tag.equals("hes")) {
                    if (scanProvFor(12)) {
                        getAchive("mercs");
                    }
                }
                if (tag.equals("Atr")) {
                    if (year == 1931 && scanProvFor(59) && playerByTag("Ger").getAllOwned().length == 0) {
                        getAchive("tables");
                    }
                }
                if (year != 0) {
                    if (mostOwned() == current.getId())
                        if (extractProvinceCount(game.getTimeline(), year, getCurrentPlayer().getTag()) <= 5)
                            getAchive("zeroTo");
                    if (current.getPlayerList().length <= 5)
                        if (extractMostOwned(game.getTimeline(), year).equals(current.getTag()))
                            getAchive("heroTo");
                    if (scanProvFor(new int[]{12, 32, 22, 59, 61, 146, 186, 235, 159})) {
                        getAchive("portals");
                    }
                }
            }
            if (timeline.equals("vir")) {
                if(ownsBestCity(15))
                    getAchive("rough");
                if(quarantine())
                    getAchive("zone");
            }
            if (timeline.equals("kai")) {
                if(tag.equals("ger"))
                if(current.totalIncome() >= 400)
                    getAchive("reich");
                if(scanProvFor(new int[]{32, 70, 77, 53, 74}) && playerByTag("Fra").getAllOwned().length == 0
                        && playerByTag("cze").getAllOwned().length == 0 && playerByTag("POl").getAllOwned().length == 0
                     && playerByTag("Hng").getAllOwned().length == 0  && playerByTag("Cro").getAllOwned().length == 0)
                    getAchive("bismark");
            }
            if (timeline.equals("rom")) {
                if(tag.equals("his")){
                    if(scanProvFor(61) && playerByTag("lug").getAllOwned().length == 0
                            && playerByTag("aqt").getAllOwned().length == 0 && playerByTag("rom").getAllOwned().length == 0)
                        getAchive("ashes");
                }
                if(tag.equals("lug")){
                    if(scanProvFor(61) && playerByTag("his").getAllOwned().length == 0
                            && playerByTag("aqt").getAllOwned().length == 0 && playerByTag("rom").getAllOwned().length == 0)
                        getAchive("ashes");
                }
                if(tag.equals("aqt")){
                    if(scanProvFor(61) && playerByTag("lug").getAllOwned().length == 0
                            && playerByTag("his").getAllOwned().length == 0 && playerByTag("rom").getAllOwned().length == 0)
                        getAchive("ashes");
                }
                if(tag.equals("fnk")){
                    if(playerByTag("lug").getAllOwned().length == 0
                            && playerByTag("aqt").getAllOwned().length == 0)
                        getAchive("gauls");
                }
            }
        }
        if (getMap().getId() == 2 || getMap().getId() == 1) {
            if (current.totalIncome() > getMap().totalDev() / 2) {
                getAchive("woahh");
            }
            if (provs.length == getMap().getList().length) {
                getAchive("hedgemon");
            }
            if (mostTroopCheck()) {
                getAchive("reconned");
            }
        }
        //edit.commit();
    }
    /*private static String achiveFile(){
        String achiveString = "";
        byte[] achiveBytes = new byte[0];
        try {
            int size = stream.available();
            achiveBytes = new byte[size];
            stream.read(achiveBytes);
            stream.close();
        } catch (IOException e) { e.printStackTrace(); }
        return new String(achiveBytes);

    }*/
    public static Object[] infoFromTag(String tag){
        /*int colonIndex = ACHIVE_FILE.indexOf(':', ACHIVE_FILE.indexOf(tag));
        int quoteIndex = ACHIVE_FILE.indexOf('"', ACHIVE_FILE.indexOf(tag));
        int barIndex = ACHIVE_FILE.indexOf('|', ACHIVE_FILE.indexOf(tag));
        String title = ACHIVE_FILE.substring(colonIndex+1, quoteIndex);
        String description = ACHIVE_FILE.substring(quoteIndex+1, barIndex);
        return new Object[]{title, description};*/
        if(tag.equals("remove"))
            return new Object[] {"Remove Kebab", "As Serbia, make sure the Ottomans don't exist while holding Constantinople", R.drawable.kebab, new int[]{186}};
        if(tag.equals("space"))
            return new Object[] {"Poland Can Into Space", "Reach 300 Development As Poland Or The Commonwealth", R.drawable.space, new int[]{}};
        if(tag.equals("winged"))
            return new Object[] {"Winged Hussars", "Starting in 1618 as the Commonwealth, have over 300 legions", R.drawable.winged, new int[]{}};
        if(tag.equals("ground"))
            return new Object[] {"I Have The High Ground", "Starting in 1445 as Novgorod, eliminate Muscovy and own Moskva", R.drawable.ground, new int[]{146}};
        if(tag.equals("unvasion"))
            return new Object[] {"The Norman Un-vasion", "Starting in 1066 as England, own all French provinces", R.drawable.unvasion, new int[]{30, 31, 38, 39, 29, 32, 27, 28, 36, 34, 35, 37, 41}};
        if(tag.equals("moors"))
            return new Object[] {"More Moors", "Starting in 1248 as Granada, eliminate Portugal, Castile, and Aragon while owning Porto, Madrid, and Zargoza", R.drawable.moors, new int[]{18, 22, 26}};
        if(tag.equals("tracts"))
            return new Object[] {"Huge Tracts of Land", "Eliminate England as Scotland", R.drawable.tracts, new int[]{}};
        if(tag.equals("justinian"))
            return new Object[] {"Justinian's Justinian", "As the Byzantines after 800 AD, re-conquer Rome, Tours, Madrid, and London while owning Constantinople", R.drawable.justinian, new int[]{61, 32, 22, 12, 186}};
        if(tag.equals("waves"))
            return new Object[] {"Britania Rules The Waves", "As Great Britain in 1823, own the Atlantic coastlines of Portugal, Spain, and France", R.drawable.waves, new int[]{39, 38, 31, 30, 29, 27, 23, 17, 18, 19, 20}};
        if(tag.equals("badnito"))
            return new Object[] {"rOmAn eMpIre", "Literally just exist as Fascist Italy in 1931, even Benito can manage that...right?", R.drawable.badnito, new int[]{}};
        if(tag.equals("goodnito"))
            return new Object[] {"Not All Talk", "As Fascist Italy in 1931, own Rome, Tours, Madrid, and Constantinople", R.drawable.goodnito, new int[]{32, 61, 22, 186}};
        if(tag.equals("off"))
            return new Object[] {"Something's...Off", "Starting as Byzantium in 1445, swap territory with the Ottomans", R.drawable.off, new int[]{89, 94, 92, 93, 188, 187, 189, 190, 195, 191, 192, 194, 196, 197, 199, 198}};
        if(tag.equals("woahh"))
            return new Object[] {"Woahh, We're (Almost) Half Way There", "Own half of all the development on the map", R.drawable.woahh, new int[]{}};
        if(tag.equals("hedgemon"))
            return new Object[] {"Hedgemon Europa", "Conquer and colonize every province on the map", R.drawable.hedgemon, new int[]{}};
        if(tag.equals("reconned"))
            return new Object[] {"A Force to be Reckoned With", "Command the most legions in the game", R.drawable.reconned, new int[]{}};
        if(tag.equals("zeroTo"))
            return new Object[] {"Zero To Hero", "Starting with 5 or less provinces, grow your domain to be the largest in the game", R.drawable.zeroto, new int[]{}};
        if(tag.equals("heroTo"))
            return new Object[] {"Hero To Zero", "Starting with the most provinces in the game, whittle yourself down to 5", R.drawable.heroto, new int[]{}};
        if(tag.equals("kylie"))
            return new Object[] {"Burma Or Myanmar?", "Neither Burma nor Myanmar exists", R.drawable.kylie, new int[]{}};
        if(tag.equals("lucky"))
            return new Object[] {"Luck of The Draw", "Win a battle without suffering any losses", R.drawable.lucky, new int[]{}};
        if(tag.equals("ctrlZ"))
            return new Object[] {"Tactical Repositioning", "Retreat from a battle where you are outnumbered more that 3 to 1", R.drawable.ctrlz, new int[]{}};
        if(tag.equals("ricardo"))
            return new Object[] {"By The Grace Of Father Ricardo", "Win a battle as the Pope", R.drawable.ricardo, new int[]{}};
        if(tag.equals("roads"))
            return new Object[] {"All Roads Lead To Moscow", "Own Rome as Muscovy or Russia while Moskva has over 20 development", R.drawable.roads, new int[]{61}};
        if(tag.equals("reconquistus"))
            return new Object[] {"Reconquistus", "Retake all of Italy as Rome and eliminate Odoacer in 477", R.drawable.reconquistus, new int[]{65, 63, 64, 62, 61, 48, 67, 49, 42}};
        if(tag.equals("khans"))
            return new Object[] {"Legacy Of The Khans", "As any eastern horde in 1445, eliminate all others", R.drawable.khans, new int[]{}};
        if(tag.equals("deus"))
            return new Object[] {"Deus Vult!", "AS the Latin Empire in 1248, eliminate all other Byzantine remnants and own Jerusalem", R.drawable.deus, new int[]{224}};
        if(tag.equals("student"))
            return new Object[] {"The Student Has Become The Master", "As Byzantium in 396, eliminate Rome", R.drawable.student, new int[]{}};
        if(tag.equals("private"))
            return new Object[] {"Private Island", "Own all of the British Isles as Northumbria", R.drawable.island, new int[]{1, 2, 3, 4, 5, 6, 7, 9, 8, 10, 11, 12, 13, 14, 15, 16}};
        if(tag.equals("portals"))
            return new Object[] {"Now You're Thinking With Portals", "Own London, Tours, Madrid, Berlin, Rome, Moskva, Constantinople, Cairo, and Kazan while they are not connected to any other part of your territory", R.drawable.portals, new int[]{12, 32, 22, 59, 61, 146, 186, 235, 159}};
        if(tag.equals("steamroll"))
            return new Object[] {"Steamroll", "As Rome in 17 eliminate all other players before turn 30", R.drawable.steamroll, new int[]{}};
        if(tag.equals("realThird"))
            return new Object[] {"The Real Third Rome", "As Odoacer in 477, conquer Rome, Napoli, Athens, Constantinople, Ankara, and Cairo while Rome and Byzantium don't exist", R.drawable.realthird, new int[]{61, 62, 91, 186, 235, 194}};
        if(tag.equals("notice"))
            return new Object[] {"Notice Me!", "Conquer all of the North Aftrican coast as Anazzah", R.drawable.notice, new int[]{235, 236, 238, 239, 240, 241, 242, 247, 246, 248, 245, 254, 253, 252, 251, 256, 259, 258}};
        if(tag.equals("defenestrate"))
            return new Object[] {"Defenestrate This!", "Eliminate both Austria and The HRE as Bohemia in 1618", R.drawable.defenestrate, new int[]{}};
        if(tag.equals("mercs"))
            return new Object[] {"Who's The Mercenary Now", "Own London as Hesse", R.drawable.mercs, new int[]{12}};
        if(tag.equals("tables"))
            return new Object[] {"How The Turn Tables", "Eliminate Germany as Austria in 1931 and own Berlin", R.drawable.tables, new int[]{59}};
        //if(tag.equals("rough"))
            //return new Object[] {"A Diamond In The Rough", "Own the highest developed province in the game with at least 15 development and no devastation", R.drawable.rough, new int[]{}};
        //if(tag.equals("zone"))
            //return new Object[] {"Quarantine Zone", "Have a level 5 fort on a province with over .7 devastation", R.drawable.quarantine, new int[]{12}};
        if(tag.equals("reich"))
            return new Object[] {"A True Third Reich", "As The German Empire in the Kaiser Gloria timeline, have an income of at least 500 per turn", R.drawable.realthird, new int[]{12}};
        if(tag.equals("bismark"))
            return new Object[] {"The Bismark", "As Germany in the Kaiser Gloria timeline, destroy France, Poland, Czechoslovakia, Hungary, and Croatia while owning Tours, Berlin, Prague, Warzaw, and Hungary", R.drawable.bismark, new int[]{32, 70, 77, 53, 74}};
        if(tag.equals("ashes"))
            return new Object[] {"E Pluribus Unum", "As one of the Roman generals' empires in the Imperio Eternum timeline, eliminate Rome and the others while owning Rome", R.drawable.ashes, new int[]{12}};
        if(tag.equals("gauls"))
            return new Object[] {"Gaul For Gauls", "Eliminate Lugdunensis and Aquitania as the Franks in the Imperio Eternum timeline", R.drawable.gauls, new int[]{}};
        return new Object[] {"", "", R.drawable.blank, new int[]{}};
    }
    /*private static String[] tagsFromFile(){
        ArrayList<String> tagList = new ArrayList<>(0);
        for(int i=0; i<ACHIVE_FILE.length(); i++){
            if(ACHIVE_FILE.charAt(i) == '|'){
                tagList.add(ACHIVE_FILE.substring(i+1, ACHIVE_FILE.indexOf(':', i)));
            }
        }
        return tagList.toArray(new String[0]);
    }*/
    private static int extractProvinceCount(String timeline, int year, String tag){
       String loadString = new SaveBooter().loadFileString(getMap().getMapFilePath(), timeline, year);
       int count = 0;
       for(int i=0; i<loadString.length()-5; i++){
           if(loadString.substring(i, i+4).equals("<"+tag))
               count++;
       }
       return count-1;
    }
    private static String extractMostOwned(String timeline, int year){
        String loadString = new SaveBooter().loadFileString(getMap().getMapFilePath(), timeline, year);
        int pStrLen = SAVE_FORM[8] + SAVE_FORM[1] + SAVE_FORM[5] + SAVE_FORM[6] + SAVE_FORM[7] + 1;
        int init = 8;
        /*
        int playerLen = Integer.parseInt(loadString.substring(init + pStrLen*getMap().getList().length, init + pStrLen*getMap().getList().length+3));*/
        int plStrLen = 3 + 1 + 1 + SAVE_FORM[2] + SAVE_FORM[3] + SAVE_FORM[4];
        int start = init + pStrLen*getMap().getList().length+3;
        int maxProvs = 0;
        String mostTag = "";
        for(int i=0; i<loadString.length(); i++){
            int provs = extractProvinceCount(timeline, year, loadString.substring(start+plStrLen*i, start+plStrLen*i+3));
            if(provs > maxProvs){
                maxProvs = provs;
                mostTag = loadString.substring(start+plStrLen*i, start+plStrLen*i+3);
            }
        }
        return mostTag;
    }
    private static boolean mostTroopCheck(){
        int id = -1;
        double troops = 0;
        for(Player p : getStaticPlayerList()){
            if(p.getFreeTroops() > troops){
                troops = p.getFreeTroops();
                id = p.getId();
            }
        }
        return id == getCurrentPlayer().getId();
    }
    private static boolean quarantine(){
        for(Province p : provs){
            if(p.getFortLevel() == 5 && p.modDevastation(0) >- .7)
                return true;
        }
        return false;
    }
    private static boolean ownsBestCity(int minDev){
        int max = 0;
        Province potential = null;
        for(Province p : getMap().getList()){
            if(p.modDevelopment(0) > max && p.modDevelopment(0) > minDev) {
                potential = p;
                max = (int)p.modDevelopment(0);
            }
        }
        if(potential != null) return  potential.getOwnerId() == getCurrentPlayer().getId() && potential.modDevastation(0) < .1;
        return false;
    }
    public static void getAchive(String tag){
        //achiveDrop(tag);
        Log.i("AchiveGet", tag+": "+achives.getBoolean(tag, false));
        if(!achives.getBoolean(tag, false)) {
            edit.putBoolean(tag, true);
            achiveDrop(tag);
        }
        edit.commit();
    }
    public static void initAchives(){
        SharedPreferences.Editor edit = achives.edit();
        for(String s : ACHIVE_TAGS)
            if(!achives.contains(s))
                edit.putBoolean(s, false);
        edit.commit();
    }
    private static boolean scanProvFor(int id){
        for(Province p : provs)
            if(p.getId() == id)
                return true;
        return false;
    }
    private static boolean scanProvFor(int[] ids){
        int count = 0;
        for(int id : ids) {
            for (Province p : provs)
                if (p.getId() == id)
                    count++;
        }
        return count == ids.length;
    }
    private static boolean scanProvFor(int[] ids, String tag){
        int count = 0;
        Province[] provinces = playerByTag(tag).getAllOwned();
        for(int id : ids) {
            for (Province p : provinces)
                if (p.getId() == id)
                    count++;
        }
        return count == ids.length;
    }
    private static Player playerByTag(String tag){
        for(Player p : getStaticPlayerList())
            if(p.getTag().equals(tag))
                return p;
        return new Player(GameActivity.context, 0, true, "");
    }
}

