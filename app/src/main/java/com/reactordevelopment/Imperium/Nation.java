package com.reactordevelopment.Imperium;

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
    }
    public String getName(){return name;}
    public int getFlag(){return flag;}
    public String getTag(){return tag;}
    public int getColor(){return color;}
    public int getExtraDev(){return extraDev;}
    public double getOpsEfficiency(){return opsEfficiency;}
    public double getTroopHardening(){return troopHardening;}
    private Object[] getValues(String type){
        if(type.equals("#nn")) return new Object[]{"Player None", R.drawable.noflag, Color.argb(ALPHA, 0, 0, 0), 1.0, 1.0};
        if(type.equals("#00")) return new Object[]{"Blue", R.drawable.p00, Color.argb(ALPHA, 36, 177, 201), 1.0, 1.0};
        if(type.equals("#01")) return new Object[]{"Red", R.drawable.p01, Color.argb(ALPHA, 255, 0, 0), 1.0, 1.0};
        if(type.equals("#02")) return new Object[]{"Green", R.drawable.p02, Color.argb(ALPHA, 0, 255, 0), 1.0, 1.0};
        if(type.equals("#03")) return new Object[]{"Purple", R.drawable.p03, Color.argb(ALPHA, 191, 0, 255), 1.0, 1.0};
        //if(type.equals("Pol")) return new Object[]{"Poland", R.drawable.select, Color.argb(ALPHA, 255, 122, 222), 1.0, 1.0};
        //if(type.equals("nov")) return new Object[]{"Novgorod", R.drawable.attack, Color.argb(ALPHA, 59, 134, 24), 1.0, 1.0};

        if(type.equals("rom")) return new Object[]{"Imperial Rome", R.drawable.rom, Color.argb(ALPHA, 216, 24, 24), .8, 1.2};
        if(type.equals("tce")) return new Object[]{"Thrace", R.drawable.tce, Color.argb(ALPHA, 91, 216, 24), 1.0, .8};
        if(type.equals("par")) return new Object[]{"Parthia", R.drawable.par, Color.argb(ALPHA, 78, 145, 208), .7, 1.1};
        if(type.equals("che")) return new Object[]{"Cherusci Tribe", R.drawable.che, Color.argb(ALPHA, 78, 208, 112), 1.0, .8};
        if(type.equals("van")) return new Object[]{"Vandals", R.drawable.van, Color.argb(ALPHA, 200, 216, 24), 1.0, .8};
        if(type.equals("dac")) return new Object[]{"Dacians", R.drawable.dac, Color.argb(ALPHA, 159, 48, 207), 1.0, .8};
        if(type.equals("sar")) return new Object[]{"Sarmatians", R.drawable.sar, Color.argb(ALPHA, 207, 48, 109), 1.0, .8};
        if(type.equals("ala")) return new Object[]{"Alans", R.drawable.ala, Color.argb(ALPHA, 122, 207, 48), 1.0, .8};
        if(type.equals("nab")) return new Object[]{"Nabatea", R.drawable.nab, Color.argb(ALPHA, 222, 115, 33), 1.0, .8};
        if(type.equals("scy")) return new Object[]{"Scythians", R.drawable.scy, Color.argb(ALPHA, 55, 225, 137), .8, .8};
        if(type.equals("toc")) return new Object[]{"Tochaians", R.drawable.toc, Color.argb(ALPHA, 55, 78, 225), 1.0, .8};

        if(type.equals("pic")) return new Object[]{"Picts", R.drawable.pic, Color.argb(ALPHA, 220, 195, 80), 1.0, .8};
        if(type.equals("bzn")) return new Object[]{"Byzantine Empire", R.drawable.bzn, Color.argb(ALPHA, 132, 56, 220), .9, 1.2};
        if(type.equals("fnk")) return new Object[]{"Franks", R.drawable.fnk, Color.argb(ALPHA, 45, 130, 205), 1.0, .85};
        if(type.equals("bgn")) return new Object[]{"Burgundians", R.drawable.bgn, Color.argb(ALPHA, 128, 0, 32), 1.0, .8};
        if(type.equals("vis")) return new Object[]{"Visigoths", R.drawable.vis, Color.argb(ALPHA, 12, 146, 23), 1.0, .8};
        if(type.equals("hun")) return new Object[]{"Huns", R.drawable.hun, Color.argb(ALPHA, 189, 174, 15), 1.0, .9};
        if(type.equals("sas")) return new Object[]{"Sassanid", R.drawable.sas, Color.argb(ALPHA, 15, 189, 79), .85, 1.2};
        if(type.equals("gas")) return new Object[]{"Ghassanids", R.drawable.gas, Color.argb(ALPHA, 79, 196, 185), 1.0, 1.0};
        if(type.equals("lak")) return new Object[]{"Lakhmids", R.drawable.lak, Color.argb(ALPHA, 208, 114, 67), 1.0, 1.0};
        if(type.equals("slv")) return new Object[]{"Slavs", R.drawable.slv, Color.argb(ALPHA, 200, 52, 30), .7, 1.0};

        if(type.equals("syg")) return new Object[]{"Syagirus", R.drawable.syg, Color.argb(ALPHA, 215, 185, 35), 1.0, .9};
        if(type.equals("bri")) return new Object[]{"Brittany", R.drawable.bri, Color.argb(ALPHA, 149, 178, 183), 1.0, 1.1};
        if(type.equals("sbi")) return new Object[]{"Suebi", R.drawable.sbi, Color.argb(ALPHA, 230, 125, 20), 1.0, 1.0};
        if(type.equals("lom")) return new Object[]{"Lombards", R.drawable.lom, Color.argb(ALPHA, 205, 100, 86), 1.0, .8};
        if(type.equals("ost")) return new Object[]{"Ostrogoths", R.drawable.ost, Color.argb(ALPHA, 10, 60, 118), 1.0, .8};
        if(type.equals("sax")) return new Object[]{"Saxons", R.drawable.sax, Color.argb(ALPHA, 190, 158, 111), 1.0, .87};
        if(type.equals("btn")) return new Object[]{"Brittons", R.drawable.btn, Color.argb(ALPHA, 218, 75, 47), 1.0, 1.0};
        if(type.equals("odo")) return new Object[]{"Odoacer", R.drawable.odo, Color.argb(ALPHA, 176, 194, 122), 1.0, 1.1};
        if(type.equals("hph")) return new Object[]{"Hephtalites", R.drawable.hph, Color.argb(ALPHA, 255, 158, 61), .7, 1.0};

        if(type.equals("neu")) return new Object[]{"Neustria", R.drawable.neu, Color.argb(ALPHA, 0, 13, 189), 1.0, 1.0};
        if(type.equals("ata")) return new Object[]{"Austrasia", R.drawable.ata, Color.argb(ALPHA, 103, 199, 0), 1.0, 1.0};
        if(type.equals("eag")) return new Object[]{"East Anglia", R.drawable.eag, Color.argb(ALPHA, 113, 218, 189), 1.0, 1.0};
        if(type.equals("yrk")) return new Object[]{"York", R.drawable.yrk, Color.argb(ALPHA, 198, 190, 238), 1.0, 1.0};
        if(type.equals("wha")) return new Object[]{"Whales", R.drawable.wha, Color.argb(ALPHA, 224, 60, 31), 1.0, 1.0};
        if(type.equals("wes")) return new Object[]{"Wessex", R.drawable.wes, Color.argb(ALPHA, 200, 69, 217), 1.0, 1.0};
        if(type.equals("fri")) return new Object[]{"Frisians", R.drawable.fri, Color.argb(ALPHA, 169, 66, 35), 1.0, 1.0};
        if(type.equals("pom")) return new Object[]{"Pomeranians", R.drawable.pom, Color.argb(ALPHA, 23, 173, 0), .9, .9};
        if(type.equals("pol")) return new Object[]{"Polans", R.drawable.pol, Color.argb(ALPHA, 255, 92, 105), .9, 1.0};
        if(type.equals("blt")) return new Object[]{"Balts", R.drawable.blt, Color.argb(ALPHA, 163, 255, 177), .8, .9};
        if(type.equals("bav")) return new Object[]{"Bavarians", R.drawable.bav, Color.argb(ALPHA, 85, 211, 190), 1.0, 1.0};
        if(type.equals("bul")) return new Object[]{"Bulgaria", R.drawable.bul, Color.argb(ALPHA, 255, 87, 41), .8, .9};
        if(type.equals("kha")) return new Object[]{"Khazars", R.drawable.kha, Color.argb(ALPHA, 173, 211, 230), .8, 1.0};
        if(type.equals("cph")) return new Object[]{"Caliphate", R.drawable.cph, Color.argb(ALPHA, 30, 159, 52), 1.12, 1.1};
        if(type.equals("bne")) return new Object[]{"Benevento", R.drawable.bne, Color.argb(ALPHA, 102, 255, 133), 1.0, 1.0};
        if(type.equals("dan")) return new Object[]{"Danes", R.drawable.dan, Color.argb(ALPHA, 255, 82, 108), 1.0, 1.0};
        if(type.equals("ava")) return new Object[]{"Avars", R.drawable.ava, Color.argb(ALPHA, 255, 41, 41), .8, 1.0};

        if(type.equals("cba")) return new Object[]{"Emirate of Cordoba", R.drawable.cba, Color.argb(ALPHA, 62, 208, 167), 1.0, 1.0};
        if(type.equals("mer")) return new Object[]{"Mercia", R.drawable.mer, Color.argb(ALPHA, 38, 45, 232), 1.0, 1.0};
        if(type.equals("num")) return new Object[]{"Northumbria", R.drawable.num, Color.argb(ALPHA, 242, 233, 110), 1.0, 1.0};
        if(type.equals("isd")) return new Object[]{"Idrisid Emirate", R.drawable.isd, Color.argb(ALPHA, 195, 246, 147), 1.0, 1.0};
        if(type.equals("tah")) return new Object[]{"Tahert", R.drawable.tah, Color.argb(ALPHA, 196, 114, 243), 1.0, 1.0};
        if(type.equals("pap")) return new Object[]{"Papal States", R.drawable.pap, Color.argb(ALPHA, 255, 242, 219), 1.3, 1.0};
        if(type.equals("len")) return new Object[]{"Leinster", R.drawable.len, Color.argb(ALPHA, 15, 184, 0), 1.0, 1.0};
        if(type.equals("cnn")) return new Object[]{"Connacht", R.drawable.cnn, Color.argb(ALPHA, 0, 101, 184), 1.0, 1.0};
        if(type.equals("uls")) return new Object[]{"Ulster", R.drawable.uls, Color.argb(ALPHA, 252, 248, 49), 1.0, 1.0};
        if(type.equals("des")) return new Object[]{"Desmond", R.drawable.des, Color.argb(ALPHA,71, 46, 46), 1.0, 1.0};
        if(type.equals("mns")) return new Object[]{"Munster", R.drawable.mns, Color.argb(ALPHA, 47, 182, 202), 1.0, 1.0};
        if(type.equals("asd")) return new Object[]{"Abbasid Caliphate", R.drawable.asd, Color.argb(ALPHA, 238, 32, 32), .7, 1.2};
        if(type.equals("atr")) return new Object[]{"Asturias", R.drawable.atr, Color.argb(ALPHA, 32, 228, 238), 1.0, 1.0};

        if(type.equals("eng")) return new Object[]{"England", R.drawable.eng, Color.argb(ALPHA, 246, 49, 49), 1.0, 1.0};
        if(type.equals("sct")) return new Object[]{"Scotland", R.drawable.sct, Color.argb(ALPHA, 240, 180, 0), 1.0, 1.0};
        if(type.equals("fra")) return new Object[]{"France", R.drawable.fra, Color.argb(ALPHA, 36, 20, 255), 1.0, 1.2};
        if(type.equals("leo")) return new Object[]{"Leon", R.drawable.leo, Color.argb(ALPHA, 0, 184, 230), 1.0, 1.0};
        if(type.equals("pam")) return new Object[]{"Pamplona", R.drawable.pam, Color.argb(ALPHA, 20, 169, 42), 1.0, 1.0};
        if(type.equals("hre")) return new Object[]{"Holy Roman Empire", R.drawable.hre, Color.argb(ALPHA, 163, 195, 157), .8, 1.1};
        if(type.equals("Pol")) return new Object[]{"Poland", R.drawable.pol2, Color.argb(ALPHA, 255, 112, 146), 1.0, 1.2};
        if(type.equals("fat")) return new Object[]{"Fatimids", R.drawable.fat, Color.argb(ALPHA, 61, 255, 61), 1.0, 1.0};
        if(type.equals("sal")) return new Object[]{"Salerno", R.drawable.sal, Color.argb(ALPHA, 205, 255, 26), 1.0, 1.0};
        if(type.equals("sel")) return new Object[]{"Seljiq Turks", R.drawable.sel, Color.argb(ALPHA, 25, 204, 127), .86, 1.0};
        if(type.equals("mos")) return new Object[]{"Mosul", R.drawable.mos, Color.argb(ALPHA, 188, 188, 92), 1.0, 1.0};
        if(type.equals("den")) return new Object[]{"Denmark", R.drawable.den, Color.argb(ALPHA, 231, 64, 64), 1.0, 1.0};
        if(type.equals("swe")) return new Object[]{"Sweden", R.drawable.swe, Color.argb(ALPHA, 46, 117, 250), 1.0, 1.0};
        if(type.equals("nor")) return new Object[]{"Norway", R.drawable.nor, Color.argb(ALPHA, 185, 117, 110), 1.0, 1.0};
        if(type.equals("nov")) return new Object[]{"Novgorod", R.drawable.nov, Color.argb(ALPHA, 84, 148, 81), 1.0, 1.15};
        if(type.equals("vla")) return new Object[]{"Vladimir", R.drawable.vla, Color.argb(ALPHA, 204, 102, 102), 1.0, 1.0};
        if(type.equals("plo")) return new Object[]{"Polotsk", R.drawable.plo, Color.argb(ALPHA, 101, 205, 139), 1.0, 1.0};
        if(type.equals("smo")) return new Object[]{"Smolensk", R.drawable.smo, Color.argb(ALPHA, 215, 132, 171), 1.0, 1.0};
        if(type.equals("cng")) return new Object[]{"Chernigov", R.drawable.cng, Color.argb(ALPHA, 245, 238, 56), 1.0, 1.0};
        if(type.equals("ryz")) return new Object[]{"Ryzan", R.drawable.ryz, Color.argb(ALPHA, 138, 165, 136), 1.0, 1.0};
        if(type.equals("vol")) return new Object[]{"Volhynia", R.drawable.vol, Color.argb(ALPHA, 232, 186, 186), 1.0, 1.0};
        if(type.equals("kev")) return new Object[]{"Kiev", R.drawable.kev, Color.argb(ALPHA, 83, 198, 104), 1.0, 1.0};
        if(type.equals("gal")) return new Object[]{"Galacia", R.drawable.gal, Color.argb(ALPHA, 210, 121, 154), 1.0, 1.0};
        if(type.equals("hng")) return new Object[]{"Hungary", R.drawable.hng, Color.argb(ALPHA, 210, 152, 121), 1.0, 1.0};
        if(type.equals("cro")) return new Object[]{"Croatia", R.drawable.cro, Color.argb(ALPHA, 121, 210, 163), 1.0, 1.0};
        if(type.equals("pec")) return new Object[]{"Pecheneg", R.drawable.pec, Color.argb(ALPHA, 187, 166, 201), 1.0, 1.0};
        if(type.equals("cmn")) return new Object[]{"Cuman", R.drawable.cmn, Color.argb(ALPHA, 255, 112, 112), 1.0, 1.0};
        if(type.equals("geo")) return new Object[]{"Georgia", R.drawable.geo, Color.argb(ALPHA, 189, 224, 220), 1.0, 1.0};
        if(type.equals("zir")) return new Object[]{"Zirid", R.drawable.zir, Color.argb(ALPHA, 169, 244, 202), 1.0, 1.0};
        if(type.equals("sra")) return new Object[]{"Saragossa", R.drawable.sra, Color.argb(ALPHA, 239, 178, 128), 1.0, 1.0};

        if(type.equals("mgl")) return new Object[]{"Mongols", R.drawable.mgl, Color.argb(ALPHA, 39, 180, 75), .4, 1.0};
        if(type.equals("por")) return new Object[]{"Portugal", R.drawable.por, Color.argb(ALPHA, 35, 169, 48), 1.0, 1.0};
        if(type.equals("cas")) return new Object[]{"Castile", R.drawable.cas, Color.argb(ALPHA, 227, 210, 59), 1.0, 1.0};
        if(type.equals("ara")) return new Object[]{"Aragon", R.drawable.ara, Color.argb(ALPHA, 207, 110, 160), 1.0, 1.0};
        if(type.equals("gra")) return new Object[]{"Granada", R.drawable.gra, Color.argb(ALPHA, 233, 220, 170), 1.0, 1.0};
        if(type.equals("alm")) return new Object[]{"Almohad", R.drawable.alm, Color.argb(ALPHA, 122, 150, 179), 1.0, 1.0};
        if(type.equals("tle")) return new Object[]{"Tlemcen", R.drawable.tle, Color.argb(ALPHA, 102, 214, 197), 1.0, 1.0};
        if(type.equals("haf")) return new Object[]{"Hafsid", R.drawable.haf, Color.argb(ALPHA, 21, 158, 32), 1.0, 1.0};
        if(type.equals("ayy")) return new Object[]{"Ayyubid", R.drawable.ayy, Color.argb(ALPHA, 154, 229, 56), 1.0, 1.0};
        if(type.equals("lat")) return new Object[]{"Latin Empire", R.drawable.lat, Color.argb(ALPHA, 237, 120, 120), 1.0, 1.0};
        if(type.equals("nic")) return new Object[]{"Nicaea", R.drawable.nic, Color.argb(ALPHA, 237, 120, 228), 1.0, 1.1};
        if(type.equals("ach")) return new Object[]{"Achaea", R.drawable.ach, Color.argb(ALPHA, 165, 158, 199), 1.0, 1.0};
        if(type.equals("ven")) return new Object[]{"Venice", R.drawable.ven, Color.argb(ALPHA, 119, 223, 178), 1.0, 1.0};
        if(type.equals("epi")) return new Object[]{"Epirus", R.drawable.epi, Color.argb(ALPHA, 223, 166, 119), 1.0, 1.0};
        if(type.equals("srb")) return new Object[]{"Serbia", R.drawable.srb, Color.argb(ALPHA, 162, 127, 87), 1.0, 1.0};
        if(type.equals("teu")) return new Object[]{"Teutonic Kinghts", R.drawable.teu, Color.argb(ALPHA, 122, 148, 125), 1.0, 1.0};
        if(type.equals("lit")) return new Object[]{"Lithuania", R.drawable.lit, Color.argb(ALPHA, 199, 102, 149), 1.0, 1.0};
        if(type.equals("pis")) return new Object[]{"Pisa", R.drawable.pis, Color.argb(ALPHA, 199, 174, 102), 1.0, 1.0};

        if(type.equals("mar")) return new Object[]{"Marinid", R.drawable.mar, Color.argb(ALPHA, 210, 234, 31), 1.0, 1.0};
        if(type.equals("sav")) return new Object[]{"Savoy", R.drawable.sav, Color.argb(ALPHA, 255, 163, 163), 1.0, 1.0};
        if(type.equals("mil")) return new Object[]{"Milan", R.drawable.mil, Color.argb(ALPHA, 92, 255, 105), 1.0, 1.0};
        if(type.equals("ast")) return new Object[]{"Austria", R.drawable.ast, Color.argb(ALPHA, 255, 240, 240), 1.0, 1.0};
        if(type.equals("boh")) return new Object[]{"Bohemia", R.drawable.boh, Color.argb(ALPHA, 205, 203, 91), 1.0, 1.0};
        if(type.equals("bos")) return new Object[]{"Bosnia", R.drawable.bos, Color.argb(ALPHA, 225, 207, 137), 1.0, 1.0};
        if(type.equals("alb")) return new Object[]{"Albania", R.drawable.alb, Color.argb(ALPHA, 182, 47, 47), 1.0, 1.0};
        if(type.equals("mol")) return new Object[]{"Moldavia", R.drawable.mol, Color.argb(ALPHA, 158, 182, 47), 1.0, 1.0};
        if(type.equals("liv")) return new Object[]{"Livonian Order", R.drawable.liv, Color.argb(ALPHA, 182, 47, 72), 1.0, 1.0};
        if(type.equals("msk")) return new Object[]{"Moscovy", R.drawable.msk, Color.argb(ALPHA, 209, 196, 21), 1.0, 1.0};
        if(type.equals("gdn")) return new Object[]{"Golden Horde", R.drawable.gdn, Color.argb(ALPHA, 255, 240, 36), .8, 1.0};
        if(type.equals("nog")) return new Object[]{"Nogai", R.drawable.nog, Color.argb(ALPHA, 36, 255, 142), .8, 1.0};
        if(type.equals("kar")) return new Object[]{"Karaman", R.drawable.kar, Color.argb(ALPHA, 134, 234, 207), 1.0, 1.0};
        if(type.equals("ott")) return new Object[]{"Ottoman", R.drawable.ott, Color.argb(ALPHA, 93, 234, 77), 1.0, 1.25};
        if(type.equals("mam")) return new Object[]{"Mamluks", R.drawable.mam, Color.argb(ALPHA, 239, 214, 123), 1.0, 1.15};
        if(type.equals("qqu")) return new Object[]{"Qara Qoyunlu", R.drawable.qqu, Color.argb(ALPHA, 191, 123, 239), 1.0, 1.0};
        if(type.equals("aqu")) return new Object[]{"Aq Qoyunlu", R.drawable.aqu, Color.argb(ALPHA, 122, 118, 234), 1.0, 1.0};
        if(type.equals("azz")) return new Object[]{"Anazzah", R.drawable.azz, Color.argb(ALPHA, 168, 125, 26), 1.0, 1.0};
        if(type.equals("dje")) return new Object[]{"Djerid", R.drawable.dje, Color.argb(ALPHA, 26, 168, 118), 1.0, 1.0};
        if(type.equals("fez")) return new Object[]{"Fezzan", R.drawable.fez, Color.argb(ALPHA, 65, 225, 151), 1.0, 1.0};
        if(type.equals("tim")) return new Object[]{"Timmurids", R.drawable.tim, Color.argb(ALPHA, 255, 41, 41), .7, 1.1};
        if(type.equals("gen")) return new Object[]{"Genoa", R.drawable.gen, Color.argb(ALPHA, 247, 255, 97), 1.0, 1.0};
        if(type.equals("wal")) return new Object[]{"Wallacia", R.drawable.wal, Color.argb(ALPHA, 206, 195, 146), 1.0, 1.0};
        if(type.equals("cri")) return new Object[]{"Crimea", R.drawable.cri, Color.argb(ALPHA, 116, 236, 192), 1.0, 1.0};
        if(type.equals("Kha")) return new Object[]{"Kazan", R.drawable.kha, Color.argb(ALPHA, 173, 211, 230), 1.0, 1.0};
        if(type.equals("Bav")) return new Object[]{"Bavaria", R.drawable.bav, Color.argb(ALPHA, 85, 211, 190), 1.0, 1.0};
        if(type.equals("Pom")) return new Object[]{"Pomerania", R.drawable.pom2, Color.argb(ALPHA, 23, 173, 0), 1.0, 1.0};
        if(type.equals("bra")) return new Object[]{"Brandenburg", R.drawable.bra, Color.argb(ALPHA, 148, 149, 111), 1.0, 1.3};
        if(type.equals("hes")) return new Object[]{"Hesse", R.drawable.hes, Color.argb(ALPHA, 131, 201, 206), 1.0, 1.0};
        if(type.equals("Sax")) return new Object[]{"Saxony", R.drawable.sax2, Color.argb(ALPHA, 190, 158, 111), 1.0, 1.0};
        if(type.equals("Bgn")) return new Object[]{"Burgundy", R.drawable.bgn, Color.argb(ALPHA, 128, 0, 32), 1.0, 1.0};

        if(type.equals("spa")) return new Object[]{"Spain", R.drawable.spa, Color.argb(ALPHA, 255, 233, 31), 1.0, 1.1};
        if(type.equals("atk")) return new Object[]{"Astrakan", R.drawable.atk, Color.argb(ALPHA, 113, 173, 171), 1.0, 1.0};
        if(type.equals("wat")) return new Object[]{"Wattasid", R.drawable.wat, Color.argb(ALPHA, 47, 147, 122), 1.0, 1.0};
        if(type.equals("plc")) return new Object[]{"Commonwealth", R.drawable.plc, Color.argb(ALPHA, 255, 87, 123), .9, 1.3};
        if(type.equals("per")) return new Object[]{"Persia", R.drawable.per, Color.argb(ALPHA, 193, 227, 114), 1.0, .9};
        if(type.equals("swi")) return new Object[]{"Swizerland", R.drawable.swi, Color.argb(ALPHA, 184, 153, 97), 1.0, 1.0};

        if(type.equals("gbr")) return new Object[]{"Great Britian", R.drawable.gbr, Color.argb(ALPHA, 204, 0, 0), 1.0, 1.0};
        if(type.equals("net")) return new Object[]{"Netherlands", R.drawable.net, Color.argb(ALPHA, 245, 126, 0), 1.0, 1.0};
        if(type.equals("rus")) return new Object[]{"Russia", R.drawable.rus, Color.argb(ALPHA, 13, 153, 0), .4, .7};
        if(type.equals("saa")) return new Object[]{"Saadi", R.drawable.mar, Color.argb(ALPHA, 0, 163, 46), 1.0, 1.0};
        if(type.equals("lor")) return new Object[]{"Lorraine", R.drawable.lor, Color.argb(ALPHA, 255, 245, 107), 1.0, 1.0};
        if(type.equals("tuc")) return new Object[]{"Tuscany", R.drawable.tuc, Color.argb(ALPHA, 144, 154, 218), 1.0, 1.0};

        if(type.equals("pru")) return new Object[]{"Prussia", R.drawable.pru, Color.argb(ALPHA, 122, 123, 91), 1.1, 1.4};
        if(type.equals("han")) return new Object[]{"Hanover", R.drawable.han, Color.argb(ALPHA, 142, 195, 19), 1.0, 1.0};
        if(type.equals("mec")) return new Object[]{"Mecklenburg", R.drawable.mec, Color.argb(ALPHA, 19, 195, 104), 1.0, 1.0};
        if(type.equals("pie")) return new Object[]{"Piedmont-Sardinia", R.drawable.pie, Color.argb(ALPHA, 19, 195, 60), 1.0, 1.17};
        if(type.equals("mor")) return new Object[]{"Morocco", R.drawable.mar, Color.argb(ALPHA, 195, 104, 19), 1.0, 1.0};
        if(type.equals("nap")) return new Object[]{"Naples", R.drawable.nap, Color.argb(ALPHA, 195, 42, 203), 1.0, 1.0};
        if(type.equals("aze")) return new Object[]{"Azerbijan", R.drawable.aze, Color.argb(ALPHA, 203, 42, 85), 1.0, 1.0};
        if(type.equals("zan")) return new Object[]{"Zand", R.drawable.zan, Color.argb(ALPHA, 152, 203, 42), 1.0, 1.0};

        if(type.equals("rhi")) return new Object[]{"Rhine Confederation", R.drawable.rhi, Color.argb(ALPHA, 105, 179, 191), 1.1, 1.0};
        if(type.equals("bad")) return new Object[]{"Baden", R.drawable.bad, Color.argb(ALPHA, 218, 37, 37), 1.0, 1.0};
        if(type.equals("fre")) return new Object[]{"French Empire", R.drawable.fra2, Color.argb(ALPHA, 71, 145, 225), 1.3, 1.4};

        if(type.equals("two")) return new Object[]{"Two Sicilies", R.drawable.two, Color.argb(ALPHA, 180, 231, 136), 1.0, 1.0};

        if(type.equals("Fra")) return new Object[]{"France", R.drawable.fra2, Color.argb(ALPHA, 103, 92, 255), .85, 1.1};
        if(type.equals("ita")) return new Object[]{"Italy", R.drawable.ita, Color.argb(ALPHA, 161, 255, 138), 1.0, 1.0};
        if(type.equals("ger")) return new Object[]{"Germany", R.drawable.ger, Color.argb(ALPHA, 163, 199, 176), 1.2, 1.3};
        if(type.equals("bel")) return new Object[]{"Belgum", R.drawable.bel, Color.argb(ALPHA, 197, 145, 43), 1.0, 1.0};
        if(type.equals("ahe")) return new Object[]{"Austria-Hungary", R.drawable.ahe, Color.argb(ALPHA, 250, 245, 234), 1.0, 1.1};
        if(type.equals("gre")) return new Object[]{"Greece", R.drawable.gre, Color.argb(ALPHA, 140, 87, 224), 1.0, 1.0};
        if(type.equals("rmn")) return new Object[]{"Romania", R.drawable.rmn, Color.argb(ALPHA, 158, 213, 103), 1.0, 1.0};
        if(type.equals("Bul")) return new Object[]{"Bulgaria", R.drawable.bul2, Color.argb(ALPHA, 255, 87, 41), 1.0, 1.0};
        if(type.equals("Nor")) return new Object[]{"Norway", R.drawable.nor2, Color.argb(ALPHA, 185, 117, 110), 1.0, 1.0};

        if(type.equals("ire")) return new Object[]{"Ireland", R.drawable.ire, Color.argb(ALPHA, 113, 182, 84), 1.0, 1.0};
        if(type.equals("Ita")) return new Object[]{"Italy", R.drawable.ita2, Color.argb(ALPHA, 79, 232, 79), 1.3, 1.1};
        if(type.equals("yug")) return new Object[]{"Yugoslavia", R.drawable.yug, Color.argb(ALPHA, 0, 154, 250), 1.0, 1.0};
        if(type.equals("Atr")) return new Object[]{"Austria", R.drawable.atr2, Color.argb(ALPHA, 241, 250, 234), 1.0, 1.0};
        if(type.equals("Hng")) return new Object[]{"Hungary", R.drawable.hng2, Color.argb(ALPHA, 210, 152, 121), 1.0, 1.0};
        if(type.equals("POl")) return new Object[]{"Poland", R.drawable.pol3, Color.argb(ALPHA, 255, 112, 146), 1.0, 1.2};
        if(type.equals("Ger")) return new Object[]{"Germany", R.drawable.ger2, Color.argb(ALPHA, 144, 187, 160), 1.2, 1.1};
        if(type.equals("Lit")) return new Object[]{"Lithuania", R.drawable.lit2, Color.argb(ALPHA, 199, 102, 149), 1.0, 1.0};
        if(type.equals("ltv")) return new Object[]{"Latvia", R.drawable.ltv, Color.argb(ALPHA, 220, 221, 162), 1.0, 1.0};
        if(type.equals("est")) return new Object[]{"Estonia", R.drawable.est, Color.argb(ALPHA, 156, 231, 151), 1.0, 1.0};
        if(type.equals("sov")) return new Object[]{"Soviet Union", R.drawable.sov, Color.argb(ALPHA, 141, 32, 32), .5, .8};
        if(type.equals("fin")) return new Object[]{"Finland", R.drawable.fin, Color.argb(ALPHA, 244, 235, 210), 1.0, 1.0};
        if(type.equals("trk")) return new Object[]{"Turkey", R.drawable.trk, Color.argb(ALPHA, 111, 218, 103), 1.0, 1.0};
        if(type.equals("cze")) return new Object[]{"Czechoslovakia", R.drawable.cze, Color.argb(ALPHA, 88, 213, 161), 1.0, 1.0};
        if(type.equals("ira")) return new Object[]{"Iran", R.drawable.ira, Color.argb(ALPHA, 27, 222, 138), 1.0, 1.0};
        if(type.equals("Spa")) return new Object[]{"Spain", R.drawable.spa2, Color.argb(ALPHA, 224, 202, 0), 1.0, 1.1};

        if(type.equals("SPa")) return new Object[]{"Spain", R.drawable.spa3, Color.argb(ALPHA, 224, 202, 0), 1.1, 1.2};
        if(type.equals("rei")) return new Object[]{"Third Reich", R.drawable.rei, Color.argb(ALPHA, 84, 84, 84), 1.3, 1.4};

        if(type.equals("ITa")) return new Object[]{"Italy", R.drawable.ita3, Color.argb(ALPHA, 96, 235, 115), 1.0, 1.0};
        if(type.equals("wgr")) return new Object[]{"West Germany", R.drawable.wgr, Color.argb(ALPHA, 126, 206, 190), 1.0, 1.0};
        if(type.equals("egr")) return new Object[]{"East Germany", R.drawable.egr, Color.argb(ALPHA, 206, 126, 126), .9, 1.0};
        if(type.equals("alg")) return new Object[]{"Algeria", R.drawable.alg, Color.argb(ALPHA, 26, 213, 114), 1.0, 1.0};
        if(type.equals("tun")) return new Object[]{"Tanisia", R.drawable.tun, Color.argb(ALPHA, 234, 201, 83), 1.0, 1.0};
        if(type.equals("lib")) return new Object[]{"Libya", R.drawable.lib, Color.argb(ALPHA, 223, 170, 171), 1.0, 1.0};
        if(type.equals("egy")) return new Object[]{"Egypt", R.drawable.egy, Color.argb(ALPHA, 218, 251, 141), 1.0, 1.0};
        if(type.equals("isr")) return new Object[]{"Isreal", R.drawable.isr, Color.argb(ALPHA, 141, 160, 251), 1.0, 1.2};
        if(type.equals("sau")) return new Object[]{"Saudi Arabia", R.drawable.sau, Color.argb(ALPHA, 191, 64, 64), 1.0, 1.0};
        if(type.equals("syr")) return new Object[]{"Syria", R.drawable.syr, Color.argb(ALPHA, 156, 199, 0), 1.0, 1.0};
        if(type.equals("irq")) return new Object[]{"Iraq", R.drawable.irq, Color.argb(ALPHA, 0, 199, 159), 1.0, 1.0};

        if(type.equals("Rus")) return new Object[]{"Russia", R.drawable.rus2, Color.argb(ALPHA, 13, 153, 0), .6, .7};
        if(type.equals("SPA")) return new Object[]{"Spain", R.drawable.spa4, Color.argb(ALPHA, 224, 202, 0), 1.0, 1.0};
        if(type.equals("Srb")) return new Object[]{"Serbia", R.drawable.srb2, Color.argb(ALPHA, 162, 127, 87), 1.0, 1.0};
        if(type.equals("slo")) return new Object[]{"Slovenia", R.drawable.slo, Color.argb(ALPHA, 255, 102, 229), 1.0, 1.0};
        if(type.equals("Cro")) return new Object[]{"Croatia", R.drawable.cro2, Color.argb(ALPHA, 121, 210, 163), 1.0, 1.0};
        if(type.equals("Bos")) return new Object[]{"Bosnia", R.drawable.bos2, Color.argb(ALPHA, 225, 207, 137), 1.0, 1.0};
        if(type.equals("mac")) return new Object[]{"Macedonia", R.drawable.mac, Color.argb(ALPHA, 144, 157, 47), 1.0, 1.0};
        if(type.equals("Mol")) return new Object[]{"Moldova", R.drawable.mol2, Color.argb(ALPHA, 158, 182, 47), 1.0, 1.0};
        if(type.equals("ukr")) return new Object[]{"Ukraine", R.drawable.ukr, Color.argb(ALPHA, 49, 180, 128), 1.0, 1.0};
        if(type.equals("bru")) return new Object[]{"Belarus", R.drawable.bru, Color.argb(ALPHA, 219, 123, 123), 1.0, 1.0};
        if(type.equals("GEr")) return new Object[]{"Germany", R.drawable.ger2, Color.argb(ALPHA, 156, 196, 167), 1.0, 1.2};
        if(type.equals("slk")) return new Object[]{"Slovakia", R.drawable.slk, Color.argb(ALPHA, 118, 164, 234), 1.0, 1.0};
        if(type.equals("Geo")) return new Object[]{"Georgia", R.drawable.geo, Color.argb(ALPHA, 189, 224, 220), 1.0, 1.0};
        if(type.equals("kaz")) return new Object[]{"Kazakhstan", R.drawable.kaz, Color.argb(ALPHA, 73, 222, 43), 1.0, 1.0};



        if(type.equals("aqt")) return new Object[]{"Aquitania", R.drawable.aqt, Color.argb(ALPHA, 239, 147, 98), 1.0, 1.0};
        if(type.equals("lug")) return new Object[]{"Lugdunensis", R.drawable.lug, Color.argb(ALPHA, 98, 239, 180), 1.0, 1.0};
        if(type.equals("his")) return new Object[]{"Hispania", R.drawable.his, Color.argb(ALPHA, 239, 98, 164), 1.0, 1.0};

        if(type.equals("orl")) return new Object[]{"Orleans", R.drawable.orl, Color.argb(ALPHA, 140, 98, 239), 1.0, 1.1};
        if(type.equals("gcn")) return new Object[]{"Gascons", R.drawable.gcn, Color.argb(ALPHA, 222, 232, 33), 1.0, 1.1};
        if(type.equals("can")) return new Object[]{"Cantabria", R.drawable.can, Color.argb(ALPHA, 232, 33, 73), 1.0, 1.0};

        if(type.equals("ovi")) return new Object[]{"Oviedo", R.drawable.ovi, Color.argb(ALPHA, 33, 232, 136), 1.0, 1.0};

        if(type.equals("Nov")) return new Object[]{"Novgorod", R.drawable.nov2, Color.argb(ALPHA, 84, 148, 81), 1.0, 1.2};

        return new Object[]{"[Not Found]", R.drawable.noflag, Color.argb(ALPHA, 0, 0, 0), 1.0, 1.0};
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
