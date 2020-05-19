package com.reactordevelopment.ImperiumLite;

import android.graphics.Color;

import java.io.Serializable;

public class Nation implements Serializable {
    private String name;
    private int flag;
    private String tag;
    private int color;
    private int extraDev;
    private double opsEfficiency;
    private double troopHardening;
    private double warChance;

    private static final int ALPHA = 200;
    /*public static final int PLAYER_BLUE = Color.argb(ALPHA, 36, 177, 201);
    public static final int PLAYER_RED = Color.argb(ALPHA, 255, 0, 0);
    public static final int PLAYER_GREEN = Color.argb(ALPHA, 0, 255, 0);
    public static final int PLAYER_PURPLE = Color.argb(ALPHA, 191, 0, 255);*/
    public Nation(String tag, String timeline, int year){
        this.tag = tag;
        Object[] list = getValues(tag);
        Object[] byPeriod = byPeriod(tag, timeline, year);
        name = (String)list[0];
        flag = (Integer) list[1];
        color = (Integer)list[2];
        extraDev = (int)byPeriod[0];
        opsEfficiency = (Double) list[3];
        troopHardening = (Double) list[4];
        warChance = (Double) list[5];
    }
    public String getName(){return name;}
    public int getFlag(){return flag;}
    public String getTag(){return tag;}
    public int getColor(){return color;}
    public int getExtraDev(){return extraDev;}
    public double getOpsEfficiency(){return opsEfficiency;}
    public double getTroopHardening(){return troopHardening;}
    public double getWarChance(){return warChance;}
    private Object[] getValues(String type){
        if(type.equals("#nn")) return makeTraits("Player None", R.drawable.noflag, Color.argb(ALPHA, 0, 0, 0), 1.0, 1.0, 0.4);
        if(type.equals("#00")) return makeTraits("Blue", R.drawable.p00, Color.argb(ALPHA, 36, 177, 201), 1.0, 1.0, 0.4);
        if(type.equals("#01")) return makeTraits("Red", R.drawable.p01, Color.argb(ALPHA, 255, 0, 0), 1.0, 1.0, 0.4);
        if(type.equals("#02")) return makeTraits("Green", R.drawable.p02, Color.argb(ALPHA, 0, 255, 0), 1.0, 1.0, 0.4);
        if(type.equals("#03")) return makeTraits("Purple", R.drawable.p03, Color.argb(ALPHA, 191, 0, 255), 1.0, 1.0, 0.4);
        //if(type.equals("Pol")) return makeTraits("Poland", R.drawable.select, Color.argb(ALPHA, 255, 122, 222), 1.0, 1.0, 0.4);
        //if(type.equals("nov")) return makeTraits("Novgorod", R.drawable.attack, Color.argb(ALPHA, 59, 134, 24), 1.0, 1.0, 0.4);

        if(type.equals("rom")) return makeTraits("Imperial Rome", R.drawable.rom, Color.argb(ALPHA, 216, 24, 24), 0.8, 1.2, 0.4);
        if(type.equals("tce")) return makeTraits("Thrace", R.drawable.tce, Color.argb(ALPHA, 91, 216, 24), 1.0, 0.8, 0.6);
        if(type.equals("par")) return makeTraits("Parthia", R.drawable.par, Color.argb(ALPHA, 78, 145, 208), 0.7, 1.1, 0.6);
        if(type.equals("che")) return makeTraits("Cherusci Tribe", R.drawable.che, Color.argb(ALPHA, 78, 208, 112), 1.0, 0.8, 0.6);
        if(type.equals("van")) return makeTraits("Vandals", R.drawable.van, Color.argb(ALPHA, 200, 216, 24), 1.0, 0.8, 0.6);
        if(type.equals("dac")) return makeTraits("Dacians", R.drawable.dac, Color.argb(ALPHA, 159, 48, 207), 1.0, 0.8, 0.6);
        if(type.equals("sar")) return makeTraits("Sarmatians", R.drawable.sar, Color.argb(ALPHA, 207, 48, 109), 1.0, 0.8, 0.6);
        if(type.equals("ala")) return makeTraits("Alans", R.drawable.ala, Color.argb(ALPHA, 122, 207, 48), 1.0, 0.8, 0.6);
        if(type.equals("nab")) return makeTraits("Nabatea", R.drawable.nab, Color.argb(ALPHA, 222, 115, 33), 1.0, 0.8, 0.6);
        if(type.equals("scy")) return makeTraits("Scythians", R.drawable.scy, Color.argb(ALPHA, 55, 225, 137), 0.8, 0.8, 0.6);
        if(type.equals("toc")) return makeTraits("Tochaians", R.drawable.toc, Color.argb(ALPHA, 55, 78, 225), 1.0, 0.8, 0.6);

        if(type.equals("pic")) return makeTraits("Picts", R.drawable.pic, Color.argb(ALPHA, 220, 195, 80), 1.0, 0.8, 0.6);
        if(type.equals("bzn")) return makeTraits("Byzantine Empire", R.drawable.bzn, Color.argb(ALPHA, 132, 56, 220), 0.9, 1.2, 0.2);
        if(type.equals("fnk")) return makeTraits("Franks", R.drawable.fnk, Color.argb(ALPHA, 45, 130, 205), 1.0, 0.85, 0.4);
        if(type.equals("bgn")) return makeTraits("Burgundians", R.drawable.bgn, Color.argb(ALPHA, 128, 0, 32), 1.0, 0.8, 0.4);
        if(type.equals("vis")) return makeTraits("Visigoths", R.drawable.vis, Color.argb(ALPHA, 12, 146, 23), 1.0, 0.8, 0.4);
        if(type.equals("hun")) return makeTraits("Huns", R.drawable.hun, Color.argb(ALPHA, 189, 174, 15), 1.0, 0.9, 0.4);
        if(type.equals("sas")) return makeTraits("Sassanid", R.drawable.sas, Color.argb(ALPHA, 15, 189, 79), 0.85, 1.2, 0.4);
        if(type.equals("gas")) return makeTraits("Ghassanids", R.drawable.gas, Color.argb(ALPHA, 79, 196, 185), 1.0, 1.0, 0.4);
        if(type.equals("lak")) return makeTraits("Lakhmids", R.drawable.lak, Color.argb(ALPHA, 208, 114, 67), 1.0, 1.0, 0.4);
        if(type.equals("slv")) return makeTraits("Slavs", R.drawable.slv, Color.argb(ALPHA, 200, 52, 30), 0.7, 1.0, 0.4);

        if(type.equals("syg")) return makeTraits("Syagirus", R.drawable.syg, Color.argb(ALPHA, 215, 185, 35), 1.0, 0.9, 0.6);
        if(type.equals("bri")) return makeTraits("Brittany", R.drawable.bri, Color.argb(ALPHA, 149, 178, 183), 1.0, 1.1, 0.6);
        if(type.equals("sbi")) return makeTraits("Suebi", R.drawable.sbi, Color.argb(ALPHA, 230, 125, 20), 1.0, 1.0, 0.6);
        if(type.equals("lom")) return makeTraits("Lombards", R.drawable.lom, Color.argb(ALPHA, 205, 100, 86), 1.0, 0.8, 0.6);
        if(type.equals("ost")) return makeTraits("Ostrogoths", R.drawable.ost, Color.argb(ALPHA, 10, 60, 118), 1.0, 0.8, 0.6);
        if(type.equals("sax")) return makeTraits("Saxons", R.drawable.sax, Color.argb(ALPHA, 190, 158, 111), 1.0, 0.87, 0.6);
        if(type.equals("btn")) return makeTraits("Brittons", R.drawable.btn, Color.argb(ALPHA, 218, 75, 47), 1.0, 1.0, 0.6);
        if(type.equals("odo")) return makeTraits("Odoacer", R.drawable.odo, Color.argb(ALPHA, 176, 194, 122), 1.0, 1.1, 0.6);
        if(type.equals("hph")) return makeTraits("Hephtalites", R.drawable.hph, Color.argb(ALPHA, 255, 158, 61), 0.7, 1.0, 0.6);

        if(type.equals("neu")) return makeTraits("Neustria", R.drawable.neu, Color.argb(ALPHA, 0, 13, 189), 1.0, 1.0, 0.6);
        if(type.equals("ata")) return makeTraits("Austrasia", R.drawable.ata, Color.argb(ALPHA, 103, 199, 0), 1.0, 1.0, 0.6);
        if(type.equals("eag")) return makeTraits("East Anglia", R.drawable.eag, Color.argb(ALPHA, 113, 218, 189), 1.0, 1.0, 0.6);
        if(type.equals("yrk")) return makeTraits("York", R.drawable.yrk, Color.argb(ALPHA, 198, 190, 238), 1.0, 1.0, 0.6);
        if(type.equals("wha")) return makeTraits("Whales", R.drawable.wha, Color.argb(ALPHA, 224, 60, 31), 1.0, 1.0, 0.6);
        if(type.equals("wes")) return makeTraits("Wessex", R.drawable.wes, Color.argb(ALPHA, 200, 69, 217), 1.0, 1.0, 0.6);
        if(type.equals("fri")) return makeTraits("Frisians", R.drawable.fri, Color.argb(ALPHA, 169, 66, 35), 1.0, 1.0, 0.6);
        if(type.equals("pom")) return makeTraits("Pomeranians", R.drawable.pom, Color.argb(ALPHA, 23, 173, 0), 0.9, 0.9, 0.6);
        if(type.equals("pol")) return makeTraits("Polans", R.drawable.pol, Color.argb(ALPHA, 255, 92, 105), 0.9, 1.0, 0.6);
        if(type.equals("blt")) return makeTraits("Balts", R.drawable.blt, Color.argb(ALPHA, 163, 255, 177), 0.8, 0.9, 0.6);
        if(type.equals("bav")) return makeTraits("Bavarians", R.drawable.bav, Color.argb(ALPHA, 85, 211, 190), 1.0, 1.0, 0.6);
        if(type.equals("bul")) return makeTraits("Bulgaria", R.drawable.bul, Color.argb(ALPHA, 255, 87, 41), 0.8, 0.9, 0.6);
        if(type.equals("kha")) return makeTraits("Khazars", R.drawable.kha, Color.argb(ALPHA, 173, 211, 230), 0.8, 1.0, 0.6);
        if(type.equals("cph")) return makeTraits("Caliphate", R.drawable.cph, Color.argb(ALPHA, 30, 159, 52), 1.12, 1.1, 0.6);
        if(type.equals("bne")) return makeTraits("Benevento", R.drawable.bne, Color.argb(ALPHA, 102, 255, 133), 1.0, 1.0, 0.6);
        if(type.equals("dan")) return makeTraits("Danes", R.drawable.dan, Color.argb(ALPHA, 255, 82, 108), 1.0, 1.0, 0.6);
        if(type.equals("ava")) return makeTraits("Avars", R.drawable.ava, Color.argb(ALPHA, 255, 41, 41), 0.8, 1.0, 0.6);

        if(type.equals("cba")) return makeTraits("Emirate of Cordoba", R.drawable.cba, Color.argb(ALPHA, 62, 208, 167), 1.0, 1.0, 0.4);
        if(type.equals("mer")) return makeTraits("Mercia", R.drawable.mer, Color.argb(ALPHA, 38, 45, 232), 1.0, 1.0, 0.4);
        if(type.equals("num")) return makeTraits("Northumbria", R.drawable.num, Color.argb(ALPHA, 242, 233, 110), 1.0, 1.0, 0.4);
        if(type.equals("isd")) return makeTraits("Idrisid Emirate", R.drawable.isd, Color.argb(ALPHA, 195, 246, 147), 1.0, 1.0, 0.4);
        if(type.equals("tah")) return makeTraits("Tahert", R.drawable.tah, Color.argb(ALPHA, 196, 114, 243), 1.0, 1.0, 0.4);
        if(type.equals("pap")) return makeTraits("Papal States", R.drawable.pap, Color.argb(ALPHA, 255, 242, 219), 1.3, 1.0, 0.4);
        if(type.equals("len")) return makeTraits("Leinster", R.drawable.len, Color.argb(ALPHA, 15, 184, 0), 1.0, 1.0, 0.4);
        if(type.equals("cnn")) return makeTraits("Connacht", R.drawable.cnn, Color.argb(ALPHA, 0, 101, 184), 1.0, 1.0, 0.4);
        if(type.equals("uls")) return makeTraits("Ulster", R.drawable.uls, Color.argb(ALPHA, 252, 248, 49), 1.0, 1.0, 0.4);
        if(type.equals("des")) return makeTraits("Desmond", R.drawable.des, Color.argb(ALPHA, 71, 46, 46), 1.0, 1.0, 0.4);
        if(type.equals("mns")) return makeTraits("Munster", R.drawable.mns, Color.argb(ALPHA, 47, 182, 202), 1.0, 1.0, 0.4);
        if(type.equals("asd")) return makeTraits("Abbasid Caliphate", R.drawable.asd, Color.argb(ALPHA, 238, 32, 32), .7, 1.2, 0.4);
        if(type.equals("atr")) return makeTraits("Asturias", R.drawable.atr, Color.argb(ALPHA, 32, 228, 238), 1.0, 1.0, 0.4);

        if(type.equals("eng")) return makeTraits("England", R.drawable.eng, Color.argb(ALPHA, 246, 49, 49), 1.0, 1.0, 0.4);
        if(type.equals("sct")) return makeTraits("Scotland", R.drawable.sct, Color.argb(ALPHA, 240, 180, 0), 1.0, 1.0, 0.4);
        if(type.equals("fra")) return makeTraits("France", R.drawable.fra, Color.argb(ALPHA, 36, 20, 255), 1.0, 1.2, 0.5);
        if(type.equals("leo")) return makeTraits("Leon", R.drawable.leo, Color.argb(ALPHA, 0, 184, 230), 1.0, 1.0, 0.4);
        if(type.equals("pam")) return makeTraits("Pamplona", R.drawable.pam, Color.argb(ALPHA, 20, 169, 42), 1.0, 1.0, 0.4);
        if(type.equals("hre")) return makeTraits("Holy Roman Empire", R.drawable.hre, Color.argb(ALPHA, 163, 195, 157), .8, 1.1, 0.2);
        if(type.equals("Pol")) return makeTraits("Poland", R.drawable.pol2, Color.argb(ALPHA, 255, 112, 146), 1.0, 1.2, 0.4);
        if(type.equals("fat")) return makeTraits("Fatimids", R.drawable.fat, Color.argb(ALPHA, 61, 255, 61), 1.0, 1.0, 0.4);
        if(type.equals("sal")) return makeTraits("Salerno", R.drawable.sal, Color.argb(ALPHA, 205, 255, 26), 1.0, 1.0, 0.4);
        if(type.equals("sel")) return makeTraits("Seljiq Turks", R.drawable.sel, Color.argb(ALPHA, 25, 204, 127), .86, 1.0, 0.5);
        if(type.equals("mos")) return makeTraits("Mosul", R.drawable.mos, Color.argb(ALPHA, 188, 188, 92), 1.0, 1.0, 0.4);
        if(type.equals("den")) return makeTraits("Denmark", R.drawable.den, Color.argb(ALPHA, 231, 64, 64), 1.0, 1.0, 0.4);
        if(type.equals("swe")) return makeTraits("Sweden", R.drawable.swe, Color.argb(ALPHA, 46, 117, 250), 1.0, 1.0, 0.4);
        if(type.equals("nor")) return makeTraits("Norway", R.drawable.nor, Color.argb(ALPHA, 185, 117, 110), 1.0, 1.0, 0.4);
        if(type.equals("nov")) return makeTraits("Novgorod", R.drawable.nov, Color.argb(ALPHA, 84, 148, 81), 1.0, 1.15, 0.2);
        if(type.equals("vla")) return makeTraits("Vladimir", R.drawable.vla, Color.argb(ALPHA, 204, 102, 102), 1.0, 1.0, 0.4);
        if(type.equals("plo")) return makeTraits("Polotsk", R.drawable.plo, Color.argb(ALPHA, 101, 205, 139), 1.0, 1.0, 0.4);
        if(type.equals("smo")) return makeTraits("Smolensk", R.drawable.smo, Color.argb(ALPHA, 215, 132, 171), 1.0, 1.0, 0.4);
        if(type.equals("cng")) return makeTraits("Chernigov", R.drawable.cng, Color.argb(ALPHA, 245, 238, 56), 1.0, 1.0, 0.4);
        if(type.equals("ryz")) return makeTraits("Ryzan", R.drawable.ryz, Color.argb(ALPHA, 138, 165, 136), 1.0, 1.0, 0.4);
        if(type.equals("vol")) return makeTraits("Volhynia", R.drawable.vol, Color.argb(ALPHA, 232, 186, 186), 1.0, 1.0, 0.4);
        if(type.equals("kev")) return makeTraits("Kiev", R.drawable.kev, Color.argb(ALPHA, 83, 198, 104), 1.0, 1.0, 0.4);
        if(type.equals("gal")) return makeTraits("Galacia", R.drawable.gal, Color.argb(ALPHA, 210, 121, 154), 1.0, 1.0, 0.4);
        if(type.equals("hng")) return makeTraits("Hungary", R.drawable.hng, Color.argb(ALPHA, 210, 152, 121), 1.0, 1.0, 0.4);
        if(type.equals("cro")) return makeTraits("Croatia", R.drawable.cro, Color.argb(ALPHA, 121, 210, 163), 1.0, 1.0, 0.4);
        if(type.equals("pec")) return makeTraits("Pecheneg", R.drawable.pec, Color.argb(ALPHA, 187, 166, 201), 1.0, 1.0, 0.4);
        if(type.equals("cmn")) return makeTraits("Cuman", R.drawable.cmn, Color.argb(ALPHA, 255, 112, 112), 1.0, 1.0, 0.4);
        if(type.equals("geo")) return makeTraits("Georgia", R.drawable.geo, Color.argb(ALPHA, 189, 224, 220), 1.0, 1.0, 0.4);
        if(type.equals("zir")) return makeTraits("Zirid", R.drawable.zir, Color.argb(ALPHA, 169, 244, 202), 1.0, 1.0, 0.4);
        if(type.equals("sra")) return makeTraits("Saragossa", R.drawable.sra, Color.argb(ALPHA, 239, 178, 128), 1.0, 1.0, 0.4);

        if(type.equals("mgl")) return makeTraits("Mongols", R.drawable.mgl, Color.argb(ALPHA, 39, 180, 75), .4, 1.0, 0.2);
        if(type.equals("por")) return makeTraits("Portugal", R.drawable.por, Color.argb(ALPHA, 35, 169, 48), 1.0, 1.0, 0.4);
        if(type.equals("cas")) return makeTraits("Castile", R.drawable.cas, Color.argb(ALPHA, 227, 210, 59), 1.0, 1.0, 0.5);
        if(type.equals("ara")) return makeTraits("Aragon", R.drawable.ara, Color.argb(ALPHA, 207, 110, 160), 1.0, 1.0, 0.4);
        if(type.equals("gra")) return makeTraits("Granada", R.drawable.gra, Color.argb(ALPHA, 233, 220, 170), 1.0, 1.0, 0.4);
        if(type.equals("alm")) return makeTraits("Almohad", R.drawable.alm, Color.argb(ALPHA, 122, 150, 179), 1.0, 1.0, 0.4);
        if(type.equals("tle")) return makeTraits("Tlemcen", R.drawable.tle, Color.argb(ALPHA, 102, 214, 197), 1.0, 1.0, 0.4);
        if(type.equals("haf")) return makeTraits("Hafsid", R.drawable.haf, Color.argb(ALPHA, 21, 158, 32), 1.0, 1.0, 0.4);
        if(type.equals("ayy")) return makeTraits("Ayyubid", R.drawable.ayy, Color.argb(ALPHA, 154, 229, 56), 1.0, 1.0, 0.4);
        if(type.equals("lat")) return makeTraits("Latin Empire", R.drawable.lat, Color.argb(ALPHA, 237, 120, 120), 1.0, 1.0, 0.4);
        if(type.equals("nic")) return makeTraits("Nicaea", R.drawable.nic, Color.argb(ALPHA, 237, 120, 228), 1.0, 1.1, 0.4);
        if(type.equals("ach")) return makeTraits("Achaea", R.drawable.ach, Color.argb(ALPHA, 165, 158, 199), 1.0, 1.0, 0.4);
        if(type.equals("ven")) return makeTraits("Venice", R.drawable.ven, Color.argb(ALPHA, 119, 223, 178), 1.0, 1.0, 0.4);
        if(type.equals("epi")) return makeTraits("Epirus", R.drawable.epi, Color.argb(ALPHA, 223, 166, 119), 1.0, 1.0, 0.4);
        if(type.equals("srb")) return makeTraits("Serbia", R.drawable.srb, Color.argb(ALPHA, 162, 127, 87), 1.0, 1.0, 0.4);
        if(type.equals("teu")) return makeTraits("Teutonic Kinghts", R.drawable.teu, Color.argb(ALPHA, 122, 148, 125), 1.0, 1.0, 0.4);
        if(type.equals("lit")) return makeTraits("Lithuania", R.drawable.lit, Color.argb(ALPHA, 199, 102, 149), 1.0, 1.0, 0.4);
        if(type.equals("pis")) return makeTraits("Pisa", R.drawable.pis, Color.argb(ALPHA, 199, 174, 102), 1.0, 1.0, 0.4);

        if(type.equals("mar")) return makeTraits("Marinid", R.drawable.mar, Color.argb(ALPHA, 210, 234, 31), 1.0, 1.0, 0.4);
        if(type.equals("sav")) return makeTraits("Savoy", R.drawable.sav, Color.argb(ALPHA, 255, 163, 163), 1.0, 1.0, 0.4);
        if(type.equals("mil")) return makeTraits("Milan", R.drawable.mil, Color.argb(ALPHA, 92, 255, 105), 1.0, 1.0, 0.4);
        if(type.equals("ast")) return makeTraits("Austria", R.drawable.ast, Color.argb(ALPHA, 255, 240, 240), 1.0, 1.0, 0.45);
        if(type.equals("boh")) return makeTraits("Bohemia", R.drawable.boh, Color.argb(ALPHA, 205, 203, 91), 1.0, 1.0, 0.5);
        if(type.equals("bos")) return makeTraits("Bosnia", R.drawable.bos, Color.argb(ALPHA, 225, 207, 137), 1.0, 1.0, 0.4);
        if(type.equals("alb")) return makeTraits("Albania", R.drawable.alb, Color.argb(ALPHA, 182, 47, 47), 1.0, 1.0, 0.4);
        if(type.equals("mol")) return makeTraits("Moldavia", R.drawable.mol, Color.argb(ALPHA, 158, 182, 47), 1.0, 1.0, 0.4);
        if(type.equals("liv")) return makeTraits("Livonian Order", R.drawable.liv, Color.argb(ALPHA, 182, 47, 72), 1.0, 1.0, 0.4);
        if(type.equals("msk")) return makeTraits("Moscovy", R.drawable.msk, Color.argb(ALPHA, 209, 196, 21), 1.0, 1.0, 0.6);
        if(type.equals("gdn")) return makeTraits("Golden Horde", R.drawable.gdn, Color.argb(ALPHA, 255, 240, 36), .8, 1.0, 0.4);
        if(type.equals("nog")) return makeTraits("Nogai", R.drawable.nog, Color.argb(ALPHA, 36, 255, 142), .8, 1.0, 0.4);
        if(type.equals("kar")) return makeTraits("Karaman", R.drawable.kar, Color.argb(ALPHA, 134, 234, 207), 1.0, 1.0, 0.4);
        if(type.equals("ott")) return makeTraits("Ottoman", R.drawable.ott, Color.argb(ALPHA, 93, 234, 77), 1.0, 1.25, 0.6);
        if(type.equals("mam")) return makeTraits("Mamluks", R.drawable.mam, Color.argb(ALPHA, 239, 214, 123), 1.0, 1.15, 0.4);
        if(type.equals("qqu")) return makeTraits("Qara Qoyunlu", R.drawable.qqu, Color.argb(ALPHA, 191, 123, 239), 1.0, 1.0, 0.4);
        if(type.equals("aqu")) return makeTraits("Aq Qoyunlu", R.drawable.aqu, Color.argb(ALPHA, 122, 118, 234), 1.0, 1.0, 0.4);
        if(type.equals("azz")) return makeTraits("Anazzah", R.drawable.azz, Color.argb(ALPHA, 168, 125, 26), 1.0, 1.0, 0.4);
        if(type.equals("dje")) return makeTraits("Djerid", R.drawable.dje, Color.argb(ALPHA, 26, 168, 118), 1.0, 1.0, 0.4);
        if(type.equals("fez")) return makeTraits("Fezzan", R.drawable.fez, Color.argb(ALPHA, 65, 225, 151), 1.0, 1.0, 0.4);
        if(type.equals("tim")) return makeTraits("Timmurids", R.drawable.tim, Color.argb(ALPHA, 255, 41, 41), .7, 1.1, 0.4);
        if(type.equals("gen")) return makeTraits("Genoa", R.drawable.gen, Color.argb(ALPHA, 247, 255, 97), 1.0, 1.0, 0.4);
        if(type.equals("wal")) return makeTraits("Wallacia", R.drawable.wal, Color.argb(ALPHA, 206, 195, 146), 1.0, 1.0, 0.4);
        if(type.equals("cri")) return makeTraits("Crimea", R.drawable.cri, Color.argb(ALPHA, 116, 236, 192), 1.0, 1.0, 0.4);
        if(type.equals("Kha")) return makeTraits("Kazan", R.drawable.kha, Color.argb(ALPHA, 173, 211, 230), 1.0, 1.0, 0.4);
        if(type.equals("Bav")) return makeTraits("Bavaria", R.drawable.bav, Color.argb(ALPHA, 85, 211, 190), 1.0, 1.0, 0.4);
        if(type.equals("Pom")) return makeTraits("Pomerania", R.drawable.pom2, Color.argb(ALPHA, 23, 173, 0), 1.0, 1.0, 0.4);
        if(type.equals("bra")) return makeTraits("Brandenburg", R.drawable.bra, Color.argb(ALPHA, 148, 149, 111), 1.0, 1.3, 0.4);
        if(type.equals("hes")) return makeTraits("Hesse", R.drawable.hes, Color.argb(ALPHA, 131, 201, 206), 1.0, 1.0, 0.4);
        if(type.equals("Sax")) return makeTraits("Saxony", R.drawable.sax2, Color.argb(ALPHA, 190, 158, 111), 1.0, 1.0, 0.4);
        if(type.equals("Bgn")) return makeTraits("Burgundy", R.drawable.bgn, Color.argb(ALPHA, 128, 0, 32), 1.0, 1.0, 0.4);

        if(type.equals("spa")) return makeTraits("Spain", R.drawable.spa, Color.argb(ALPHA, 255, 233, 31), 1.0, 1.1, 0.4);
        if(type.equals("atk")) return makeTraits("Astrakan", R.drawable.atk, Color.argb(ALPHA, 113, 173, 171), 1.0, 1.0, 0.4);
        if(type.equals("wat")) return makeTraits("Wattasid", R.drawable.wat, Color.argb(ALPHA, 47, 147, 122), 1.0, 1.0, 0.4);
        if(type.equals("plc")) return makeTraits("Commonwealth", R.drawable.plc, Color.argb(ALPHA, 255, 87, 123), .9, 1.3, 0.5);
        if(type.equals("per")) return makeTraits("Persia", R.drawable.per, Color.argb(ALPHA, 193, 227, 114), 1.0, .9, 0.4);
        if(type.equals("swi")) return makeTraits("Swizerland", R.drawable.swi, Color.argb(ALPHA, 184, 153, 97), 1.0, 1.0, 0.1);

        if(type.equals("gbr")) return makeTraits("Great Britian", R.drawable.gbr, Color.argb(ALPHA, 204, 0, 0), 1.0, 1.0, 0.4);
        if(type.equals("net")) return makeTraits("Netherlands", R.drawable.net, Color.argb(ALPHA, 245, 126, 0), 1.0, 1.0, 0.4);
        if(type.equals("rus")) return makeTraits("Russia", R.drawable.rus, Color.argb(ALPHA, 13, 153, 0), .4, .7, 0.6);
        if(type.equals("saa")) return makeTraits("Saadi", R.drawable.mar, Color.argb(ALPHA, 0, 163, 46), 1.0, 1.0, 0.4);
        if(type.equals("lor")) return makeTraits("Lorraine", R.drawable.lor, Color.argb(ALPHA, 255, 245, 107), 1.0, 1.0, 0.4);
        if(type.equals("tuc")) return makeTraits("Tuscany", R.drawable.tuc, Color.argb(ALPHA, 144, 154, 218), 1.0, 1.0, 0.4);

        if(type.equals("pru")) return makeTraits("Prussia", R.drawable.pru, Color.argb(ALPHA, 122, 123, 91), 1.1, 1.4, 0.5);
        if(type.equals("han")) return makeTraits("Hanover", R.drawable.han, Color.argb(ALPHA, 142, 195, 19), 1.0, 1.0, 0.4);
        if(type.equals("mec")) return makeTraits("Mecklenburg", R.drawable.mec, Color.argb(ALPHA, 19, 195, 104), 1.0, 1.0, 0.4);
        if(type.equals("pie")) return makeTraits("Piedmont-Sardinia", R.drawable.pie, Color.argb(ALPHA, 19, 195, 60), 1.0, 1.17, 0.5);
        if(type.equals("mor")) return makeTraits("Morocco", R.drawable.mar, Color.argb(ALPHA, 195, 104, 19), 1.0, 1.0, 0.4);
        if(type.equals("nap")) return makeTraits("Naples", R.drawable.nap, Color.argb(ALPHA, 195, 42, 203), 1.0, 1.0, 0.4);
        if(type.equals("aze")) return makeTraits("Azerbijan", R.drawable.aze, Color.argb(ALPHA, 203, 42, 85), 1.0, 1.0, 0.4);
        if(type.equals("zan")) return makeTraits("Zand", R.drawable.zan, Color.argb(ALPHA, 152, 203, 42), 1.0, 1.0, 0.4);

        if(type.equals("rhi")) return makeTraits("Rhine Confederation", R.drawable.rhi, Color.argb(ALPHA, 105, 179, 191), 1.1, 1.0, 0.4);
        if(type.equals("bad")) return makeTraits("Baden", R.drawable.bad, Color.argb(ALPHA, 218, 37, 37), 1.0, 1.0, 0.4);
        if(type.equals("fre")) return makeTraits("French Empire", R.drawable.fra2, Color.argb(ALPHA, 71, 145, 225), 1.3, 1.4, 0.7);

        if(type.equals("two")) return makeTraits("Two Sicilies", R.drawable.two, Color.argb(ALPHA, 180, 231, 136), 1.0, 1.0, 0.4);

        if(type.equals("Fra")) return makeTraits("France", R.drawable.fra2, Color.argb(ALPHA, 103, 92, 255), .85, 1.1, 0.3);
        if(type.equals("ita")) return makeTraits("Italy", R.drawable.ita, Color.argb(ALPHA, 161, 255, 138), 1.0, 1.0, 0.3);
        if(type.equals("ger")) return makeTraits("Germany", R.drawable.ger, Color.argb(ALPHA, 163, 199, 176), 1.2, 1.3, 0.3);
        if(type.equals("bel")) return makeTraits("Belgum", R.drawable.bel, Color.argb(ALPHA, 197, 145, 43), 1.0, 1.0, 0.3);
        if(type.equals("ahe")) return makeTraits("Austria-Hungary", R.drawable.ahe, Color.argb(ALPHA, 250, 245, 234), 1.0, 1.1, 0.3);
        if(type.equals("gre")) return makeTraits("Greece", R.drawable.gre, Color.argb(ALPHA, 140, 87, 224), 1.0, 1.0, 0.3);
        if(type.equals("rmn")) return makeTraits("Romania", R.drawable.rmn, Color.argb(ALPHA, 158, 213, 103), 1.0, 1.0, 0.3);
        if(type.equals("Bul")) return makeTraits("Bulgaria", R.drawable.bul2, Color.argb(ALPHA, 255, 87, 41), 1.0, 1.0, 0.3);
        if(type.equals("Nor")) return makeTraits("Norway", R.drawable.nor2, Color.argb(ALPHA, 185, 117, 110), 1.0, 1.0, 0.3);

        if(type.equals("ire")) return makeTraits("Ireland", R.drawable.ire, Color.argb(ALPHA, 113, 182, 84), 1.0, 1.0, 0.3);
        if(type.equals("Ita")) return makeTraits("Italy", R.drawable.ita2, Color.argb(ALPHA, 79, 232, 79), 1.3, 1.1, 0.3);
        if(type.equals("yug")) return makeTraits("Yugoslavia", R.drawable.yug, Color.argb(ALPHA, 0, 154, 250), 1.0, 1.0, 0.3);
        if(type.equals("Atr")) return makeTraits("Austria", R.drawable.atr2, Color.argb(ALPHA, 241, 250, 234), 1.0, 1.0, 0.3);
        if(type.equals("Hng")) return makeTraits("Hungary", R.drawable.hng2, Color.argb(ALPHA, 210, 152, 121), 1.0, 1.0, 0.3);
        if(type.equals("POl")) return makeTraits("Poland", R.drawable.pol3, Color.argb(ALPHA, 255, 112, 146), 1.0, 1.2, 0.3);
        if(type.equals("Ger")) return makeTraits("Germany", R.drawable.ger2, Color.argb(ALPHA, 144, 187, 160), 1.2, 1.1, 0.3);
        if(type.equals("Lit")) return makeTraits("Lithuania", R.drawable.lit2, Color.argb(ALPHA, 199, 102, 149), 1.0, 1.0, 0.3);
        if(type.equals("ltv")) return makeTraits("Latvia", R.drawable.ltv, Color.argb(ALPHA, 220, 221, 162), 1.0, 1.0, 0.3);
        if(type.equals("est")) return makeTraits("Estonia", R.drawable.est, Color.argb(ALPHA, 156, 231, 151), 1.0, 1.0, 0.3);
        if(type.equals("sov")) return makeTraits("Soviet Union", R.drawable.sov, Color.argb(ALPHA, 141, 32, 32), .5, .8, 0.3);
        if(type.equals("fin")) return makeTraits("Finland", R.drawable.fin, Color.argb(ALPHA, 244, 235, 210), 1.0, 1.0, 0.3);
        if(type.equals("trk")) return makeTraits("Turkey", R.drawable.trk, Color.argb(ALPHA, 111, 218, 103), 1.0, 1.0, 0.3);
        if(type.equals("cze")) return makeTraits("Czechoslovakia", R.drawable.cze, Color.argb(ALPHA, 88, 213, 161), 1.0, 1.0, 0.3);
        if(type.equals("ira")) return makeTraits("Iran", R.drawable.ira, Color.argb(ALPHA, 27, 222, 138), 1.0, 1.0, 0.3);
        if(type.equals("Spa")) return makeTraits("Spain", R.drawable.spa2, Color.argb(ALPHA, 224, 202, 0), 1.0, 1.1, 0.3);

        if(type.equals("SPa")) return makeTraits("Spain", R.drawable.spa3, Color.argb(ALPHA, 224, 202, 0), 1.1, 1.2, 0.3);
        if(type.equals("rei")) return makeTraits("Third Reich", R.drawable.rei, Color.argb(ALPHA, 84, 84, 84), 1.3, 1.4, 0.5);

        if(type.equals("ITa")) return makeTraits("Italy", R.drawable.ita3, Color.argb(ALPHA, 96, 235, 115), 1.0, 1.0, 0.3);
        if(type.equals("wgr")) return makeTraits("West Germany", R.drawable.wgr, Color.argb(ALPHA, 126, 206, 190), 1.0, 1.0, 0.3);
        if(type.equals("egr")) return makeTraits("East Germany", R.drawable.egr, Color.argb(ALPHA, 206, 126, 126), .9, 1.0, 0.3);
        if(type.equals("alg")) return makeTraits("Algeria", R.drawable.alg, Color.argb(ALPHA, 26, 213, 114), 1.0, 1.0, 0.3);
        if(type.equals("tun")) return makeTraits("Tanisia", R.drawable.tun, Color.argb(ALPHA, 234, 201, 83), 1.0, 1.0, 0.3);
        if(type.equals("lib")) return makeTraits("Libya", R.drawable.lib, Color.argb(ALPHA, 223, 170, 171), 1.0, 1.0, 0.3);
        if(type.equals("egy")) return makeTraits("Egypt", R.drawable.egy, Color.argb(ALPHA, 218, 251, 141), 1.0, 1.0, 0.3);
        if(type.equals("isr")) return makeTraits("Isreal", R.drawable.isr, Color.argb(ALPHA, 141, 160, 251), 1.0, 1.2, 0.3);
        if(type.equals("sau")) return makeTraits("Saudi Arabia", R.drawable.sau, Color.argb(ALPHA, 191, 64, 64), 1.0, 1.0, 0.3);
        if(type.equals("syr")) return makeTraits("Syria", R.drawable.syr, Color.argb(ALPHA, 156, 199, 0), 1.0, 1.0, 0.3);
        if(type.equals("irq")) return makeTraits("Iraq", R.drawable.irq, Color.argb(ALPHA, 0, 199, 159), 1.0, 1.0, 0.3);

        if(type.equals("Rus")) return makeTraits("Russia", R.drawable.rus2, Color.argb(ALPHA, 13, 153, 0), .6, .7, 0.1);
        if(type.equals("SPA")) return makeTraits("Spain", R.drawable.spa4, Color.argb(ALPHA, 224, 202, 0), 1.0, 1.0, 0.1);
        if(type.equals("Srb")) return makeTraits("Serbia", R.drawable.srb2, Color.argb(ALPHA, 162, 127, 87), 1.0, 1.0, 0.1);
        if(type.equals("slo")) return makeTraits("Slovenia", R.drawable.slo, Color.argb(ALPHA, 255, 102, 229), 1.0, 1.0, 0.1);
        if(type.equals("Cro")) return makeTraits("Croatia", R.drawable.cro2, Color.argb(ALPHA, 121, 210, 163), 1.0, 1.0, 0.1);
        if(type.equals("Bos")) return makeTraits("Bosnia", R.drawable.bos2, Color.argb(ALPHA, 225, 207, 137), 1.0, 1.0, 0.1);
        if(type.equals("mac")) return makeTraits("Macedonia", R.drawable.mac, Color.argb(ALPHA, 144, 157, 47), 1.0, 1.0, 0.1);
        if(type.equals("Mol")) return makeTraits("Moldova", R.drawable.mol2, Color.argb(ALPHA, 158, 182, 47), 1.0, 1.0, 0.1);
        if(type.equals("ukr")) return makeTraits("Ukraine", R.drawable.ukr, Color.argb(ALPHA, 49, 180, 128), 1.0, 1.0, 0.1);
        if(type.equals("bru")) return makeTraits("Belarus", R.drawable.bru, Color.argb(ALPHA, 219, 123, 123), 1.0, 1.0, 0.1);
        if(type.equals("GEr")) return makeTraits("Germany", R.drawable.ger2, Color.argb(ALPHA, 156, 196, 167), 1.0, 1.2, 0.1);
        if(type.equals("slk")) return makeTraits("Slovakia", R.drawable.slk, Color.argb(ALPHA, 118, 164, 234), 1.0, 1.0, 0.1);
        if(type.equals("Geo")) return makeTraits("Georgia", R.drawable.geo, Color.argb(ALPHA, 189, 224, 220), 1.0, 1.0, 0.1);
        if(type.equals("kaz")) return makeTraits("Kazakhstan", R.drawable.kaz, Color.argb(ALPHA, 73, 222, 43), 1.0, 1.0, 0.1);



        if(type.equals("aqt")) return makeTraits("Aquitania", R.drawable.aqt, Color.argb(ALPHA, 239, 147, 98), 1.0, 1.0, 0.4);
        if(type.equals("lug")) return makeTraits("Lugdunensis", R.drawable.lug, Color.argb(ALPHA, 98, 239, 180), 1.0, 1.0, 0.4);
        if(type.equals("his")) return makeTraits("Hispania", R.drawable.his, Color.argb(ALPHA, 239, 98, 164), 1.0, 1.0, 0.4);

        if(type.equals("orl")) return makeTraits("Orleans", R.drawable.orl, Color.argb(ALPHA, 140, 98, 239), 1.0, 1.1, 0.4);
        if(type.equals("gcn")) return makeTraits("Gascons", R.drawable.gcn, Color.argb(ALPHA, 222, 232, 33), 1.0, 1.1, 0.4);
        if(type.equals("can")) return makeTraits("Cantabria", R.drawable.can, Color.argb(ALPHA, 232, 33, 73), 1.0, 1.0, 0.4);

        if(type.equals("ovi")) return makeTraits("Oviedo", R.drawable.ovi, Color.argb(ALPHA, 33, 232, 136), 1.0, 1.0, 0.4);

        if(type.equals("Nov")) return makeTraits("Novgorod", R.drawable.nov2, Color.argb(ALPHA, 84, 148, 81), 1.0, 1.2, 0.4);

        return makeTraits("[Not Found]", R.drawable.noflag, Color.argb(ALPHA, 0, 0, 0), 1.0, 1.0, 0.4);
    }
    private Object[] makeTraits(String name, int flag, int color, double opsEff, double harden, double war){
        return new Object[]{name, flag, color, opsEff, harden, war};
    }
    private Object[] byPeriod(String tag, String timeline, int year){
        if(timeline.equals("alp")) {
            if (year == 17) {
                if (tag.equals("par")) return new Object[]{50};
                if (tag.equals("toc")) return new Object[]{20};
            }
            if (year == 396) {
                if (tag.equals("sas")) return new Object[]{55};
            }
            if (year == 477) {
                if (tag.equals("sas")) return new Object[]{45};
                if (tag.equals("hep")) return new Object[]{20};
            }
            if (year == 642) {
                if (tag.equals("sas")) return new Object[]{50};
                if (tag.equals("cph")) return new Object[]{30};
            }
            if (year == 802) {
                if (tag.equals("asd")) return new Object[]{70};
            }
            if (year == 1066) {
                if (tag.equals("fat")) return new Object[]{20};
                if (tag.equals("sel")) return new Object[]{25};
            }
            if (year == 1248) {
                if (tag.equals("ayy")) return new Object[]{50};
                if (tag.equals("mgl")) return new Object[]{80}; //nerfed for obvious reasons
            }
            if (year == 1445) {
                if (tag.equals("mam")) return new Object[]{25};
                if (tag.equals("tim")) return new Object[]{50};
            }
            if (year == 1532) {
                if (tag.equals("ott")) return new Object[]{25};
                if (tag.equals("per")) return new Object[]{35};
                if (tag.equals("spa")) return new Object[]{22};
                if (tag.equals("por")) return new Object[]{18};
                if (tag.equals("gbr")) return new Object[]{7};
                if (tag.equals("fra")) return new Object[]{8};
            }
            if (year == 1618) {
                if (tag.equals("ott")) return new Object[]{30};
                if (tag.equals("per")) return new Object[]{27};
                if (tag.equals("rus")) return new Object[]{55};
                if (tag.equals("spa")) return new Object[]{45};
                if (tag.equals("por")) return new Object[]{28};
                if (tag.equals("gbr")) return new Object[]{20};
                if (tag.equals("fra")) return new Object[]{25};
                if (tag.equals("net")) return new Object[]{6};
            }
            if (year == 1756) {
                if (tag.equals("ott")) return new Object[]{30};
                if (tag.equals("zan")) return new Object[]{15};
                if (tag.equals("rus")) return new Object[]{143};
                if (tag.equals("spa")) return new Object[]{135};
                if (tag.equals("por")) return new Object[]{79};
                if (tag.equals("gbr")) return new Object[]{83};
                if (tag.equals("fra")) return new Object[]{91};
                if (tag.equals("net")) return new Object[]{38};
            }
            if (year == 1823) {
                if (tag.equals("ott")) return new Object[]{50};
                if (tag.equals("per")) return new Object[]{15};
                if (tag.equals("rus")) return new Object[]{163};
                if (tag.equals("spa")) return new Object[]{106};
                if (tag.equals("por")) return new Object[]{42};
                if (tag.equals("gbr")) return new Object[]{143};
                if (tag.equals("fra")) return new Object[]{31};
                if (tag.equals("net")) return new Object[]{43};
            }
            if (year == 1914) {
                if (tag.equals("ott")) return new Object[]{38};
                if (tag.equals("per")) return new Object[]{13};
                if (tag.equals("rus")) return new Object[]{173};
                if (tag.equals("spa")) return new Object[]{36};
                if (tag.equals("por")) return new Object[]{32};
                if (tag.equals("gbr")) return new Object[]{163};
                if (tag.equals("Fra")) return new Object[]{103};
                if (tag.equals("net")) return new Object[]{63};
                if (tag.equals("ger")) return new Object[]{53};
                if (tag.equals("ita")) return new Object[]{33};
            }
            if (year == 1931) {
                if (tag.equals("per")) return new Object[]{13};
                if (tag.equals("sov")) return new Object[]{163};
                if (tag.equals("Spa")) return new Object[]{33};
                if (tag.equals("por")) return new Object[]{28};
                if (tag.equals("gbr")) return new Object[]{163};
                if (tag.equals("Fra")) return new Object[]{113};
                if (tag.equals("net")) return new Object[]{66};
                if (tag.equals("Ita")) return new Object[]{43};
            }
            if (year == 1939) {
                if (tag.equals("per")) return new Object[]{13};
                if (tag.equals("sov")) return new Object[]{163};
                if (tag.equals("SPa")) return new Object[]{33};
                if (tag.equals("por")) return new Object[]{28};
                if (tag.equals("gbr")) return new Object[]{163};
                if (tag.equals("Fra")) return new Object[]{113};
                if (tag.equals("net")) return new Object[]{66};
                if (tag.equals("Ita")) return new Object[]{57};
            }
            if (year == 1966) {
                if (tag.equals("per")) return new Object[]{13};
                if (tag.equals("sov")) return new Object[]{167};
                if (tag.equals("SPa")) return new Object[]{17};
                if (tag.equals("por")) return new Object[]{22};
                if (tag.equals("gbr")) return new Object[]{43};
                if (tag.equals("Fra")) return new Object[]{31};
                if (tag.equals("net")) return new Object[]{16};
                if (tag.equals("ITa")) return new Object[]{6};
            }
            if (year == 2020) {
                if (tag.equals("ira")) return new Object[]{18};
                if (tag.equals("Rus")) return new Object[]{177};
                if (tag.equals("SPA")) return new Object[]{7};
                if (tag.equals("por")) return new Object[]{5};
                if (tag.equals("gbr")) return new Object[]{10};
                if (tag.equals("Fra")) return new Object[]{7};
                if (tag.equals("net")) return new Object[]{6};
            }
        }
        if(timeline.equals("rom")){
            if (year == 414) {
                if (tag.equals("sas")) return new Object[]{45};
                if (tag.equals("hep")) return new Object[]{20};
            }
            if (year == 631) {
                if (tag.equals("sas")) return new Object[]{50};
                if (tag.equals("cph")) return new Object[]{30};
            }
            if (year == 794) {
                if (tag.equals("asd")) return new Object[]{70};
            }
        }
        if(timeline.equals("kai")){
            if (year == 1917) {
                if (tag.equals("per")) return new Object[]{13};
                if (tag.equals("rus")) return new Object[]{153};
                if (tag.equals("spa")) return new Object[]{36};
                if (tag.equals("por")) return new Object[]{32};
                if (tag.equals("gbr")) return new Object[]{163};
                if (tag.equals("Fra")) return new Object[]{103};
                if (tag.equals("net")) return new Object[]{63};
                if (tag.equals("ger")) return new Object[]{83};
                if (tag.equals("ita")) return new Object[]{33};
            }
        }
        if(timeline.equals("vir")){
            if (year == 2020) {
                if (tag.equals("ira")) return new Object[]{9};
                if (tag.equals("Rus")) return new Object[]{136};
                if (tag.equals("gbr")) return new Object[]{3};
                if (tag.equals("Fra")) return new Object[]{2};
                if (tag.equals("net")) return new Object[]{6};
            }
        }
        return new Object[]{0};
    }
}
