package com.reactordevelopment.ImperiumLite.MappedActivities;

import static com.reactordevelopment.ImperiumLite.MainActivity.AUTO_SAVE_ID;
import static com.reactordevelopment.ImperiumLite.MainActivity.BASE_TEXT_SCALE;
import static com.reactordevelopment.ImperiumLite.MainActivity.SAVE_PATH;
import static com.reactordevelopment.ImperiumLite.MainActivity.backwardMusic;
import static com.reactordevelopment.ImperiumLite.MainActivity.formatInt;
import static com.reactordevelopment.ImperiumLite.MainActivity.forwardMusic;
import static com.reactordevelopment.ImperiumLite.MainActivity.getSongTitle;
import static com.reactordevelopment.ImperiumLite.MainActivity.inchWidth;
import static com.reactordevelopment.ImperiumLite.MainActivity.onBuild;
import static com.reactordevelopment.ImperiumLite.MainActivity.pauseMusic;
import static com.reactordevelopment.ImperiumLite.MainActivity.playMusic;
import static com.reactordevelopment.ImperiumLite.MainActivity.screenHeight;
import static com.reactordevelopment.ImperiumLite.MainActivity.screenWidth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.util.TypedValue;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.reactordevelopment.ImperiumLite.AchivementActivity;
import com.reactordevelopment.ImperiumLite.Achivements;
import com.reactordevelopment.ImperiumLite.Ai;
import com.reactordevelopment.ImperiumLite.AlertBanner;
import com.reactordevelopment.ImperiumLite.Game;
import com.reactordevelopment.ImperiumLite.MainActivity;
import com.reactordevelopment.ImperiumLite.OptionsActivity;
import com.reactordevelopment.ImperiumLite.Player;
import com.reactordevelopment.ImperiumLite.Province;
import com.reactordevelopment.ImperiumLite.R;
import com.reactordevelopment.ImperiumLite.StatsActivity;
import com.reactordevelopment.ImperiumLite.WarPortal;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameActivity extends MappedActivity {

    //savePopup
    private static ImageButton saveOK;
    private static ImageButton saveCancel;
    private static EditText saveInput;
    private static ConstraintLayout saveMaker;
    //navbar
    public static ImageButton handle;
    private static ImageButton saver;
    private static ImageButton optioner;
    private static ImageButton quitter;
    private static ImageButton stater;
    public static ConstraintLayout navBar;
    //controllers
    private static ImageButton change;
    private static ImageButton again;
    private static ImageButton retreat;
    private static ImageButton annihilate;
    private static ImageButton turnBack;
    private static ImageButton turnForward;
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
    private static ImageView stageCover;
    private static TextView status;
    //Achives
    private static ConstraintLayout achiveDrop;
    private static ImageView achiveImage;
    private static TextView achiveTitle;
    private ImageButton toAchives;
    //music
    private boolean musicOpen;
    private static TextView musicTitle;
    //diplo
    private String diploTag = "";
    private ImageButton closeDiplo;
    private ImageButton backDiplo;
    private ImageView flagDiplo;
    private TextView textDiplo;
    private ImageButton warDiplo;
    private ImageButton allyDiplo;
    private ImageButton subDiplo;
    private ImageView dipProg;
    private ImageView dipProgCover;
    private ImageView dipType;
    private ImageView dipLogo;
    private ConstraintLayout diploLayout;
    //alerts
    private static LinearLayout alertLayout;
    private static ArrayList<AlertBanner> alerts;
    //atWar
    private static ArrayList<WarPortal> warPortals;
    private static LinearLayout portalHolder;
    private static ImageButton atWar;
    private static ImageView atWarCover;
    private static ImageView warAttacker;
    private static ImageView warDefender;
    //diplomacy popup
    private static ConstraintLayout dipPopLayout;
    private static ImageButton dipPopConfirm;
    private static ImageButton dipPopSurrender;
    private static ImageButton dipPopCancel;
    private static ImageButton dipPopFlag;
    private static ImageView dipPopTitle;
    private static TextView dipPopText;
    private static TextView popReasons;
    private static ImageView popProgress;
    private static ImageView popProgCover;
    //win layout
    private static ImageView winnerFlag;
    private static ImageView winCover;
    private static ConstraintLayout winLayout;
    //loading screen
    protected static TextView loadText;

    public static ArrayList<ArrayList<ArrayList>> statsBundle;

    private static String saveString;

    private static SharedPreferences vars;
    //loading screens
    public static final Integer[] LOAD_ROUNDS = {R.drawable.loadhussars, R.drawable.loadnuke, R.drawable.loadplan, R.drawable.loaddisplay, R.drawable.loaddoge, R.drawable.loadnevsky, R.drawable.loadwillhelm, R.drawable.loadcurie, R.drawable.loadjoan};
    //debug
    private static EditText jumpTo;
    public static final String DEBUG_MAP = "europeMap/";
    public static final int DEBUG_MAP_ID = 2;
    //public static final String[] DEBUG_NATIONS = {};
    //*17*/public static final String[] DEBUG_NATIONS = {"rom", "par", "tce", "che", "van", "dac", "sar", "ala", "nab", "scy", "toc"};
    //*376*/public static final String[] DEBUG_NATIONS = {"rom", "bzn", "vis", "pic", "van", "bgn", "fnk", "ala", "hun", "sas", "slv", "gas", "lak"};
    //*477*/public static final String[] DEBUG_NATIONS = {"rom", "bzn", "btn", "bri", "fnk", "bgn", "vis", "syg", "sbi", "sas", "lom", "gas", "lak", "van", "odo", "sax", "ala", "hph", "pic", "slv", "ost"};
    //*642*/public static final String[] DEBUG_NATIONS = {"neu", "ata", "eag", "yrk", "wha", "wes", "fri", "pom", "pol", "blt", "bav", "bul", "kha", "cph", "bne", "dan", "bzn", "pic", "vis", "bri", "lom", "ava", "slv", "sas", "sax"};
    //*802*/ public static final String[] DEBUG_NATIONS = {"atr", "cba", "fnk", "isd", "tah", "pap", "mer", "num", "len", "cnn", "uls", "mns", "wha", "wes", "pic", "pol", "ava", "pom", "blt", "dan", "slv", "kha", "bul", "bne", "bzn", "bri", "asd"};
    //*1066*/ public static final String[] DEBUG_NATIONS = {"cba", "fra", "leo", "pam", "sra", "eng", "wha", "sct", "len", "cnn", "mns", "uls", "hre", "pap", "zir", "bne", "sal", "cro", "hng", "Pol", "pom", "vol", "bzn", "gal", "pec", "plo", "kev", "cng", "smo", "nov", "vla", "ryz", "cmn", "geo", "mos", "sel", "den", "swe", "nor", "fat"};
    //*1248*/ public static final String[] DEBUG_NATIONS = {"cas", "fra", "por", "ara", "gra", "eng", "wha", "sct", "alm", "cnn", "mns", "uls", "hre", "pap", "tle", "haf", "pis", "srb", "hng", "Pol", "teu", "lit", "lat", "mgl", "bul", "plo", "nic", "ach", "ven", "nov", "epi", "ayy", "asd", "den", "swe", "nor"};
    //*1445*/ public static final String[] DEBUG_NATIONS = {"cas", "fra", "por", "ara", "nap", "gra", "eng", "sct", "mar", "cnn", "mns", "uls", "hre", "pap", "tle", "haf", "gen", "srb", "hng", "Pol", "teu", "lit", "liv", "bos", "wal", "msk", "alb", "mol", "kar", "ven", "boh", "ast", "nov", "mam", "tim", "qqu", "aqu", "geo", "cri", "gdn", "ott", "nog", "sav", "mil", "azz", "Kha", "Bav", "fez", "dje", "pis", "den", "swe", "bzn", "Pom", "bra", "hes", "Sax", "Bgn"};
    //*1532*/ public static final String[] DEBUG_NATIONS = {"spa", "fra", "por", "eng", "sct", "wat", "hre", "pap", "tle", "haf", "gen", "plc", "liv", "msk", "ven", "ast", "per", "atk", "geo", "swi", "ott", "nog", "sav", "mil", "azz", "Kha", "Bav", "pis", "den", "swe", "Pom", "bra", "hes", "Sax"};
    //*1618*/ public static final String[] DEBUG_NATIONS = {"spa", "fra", "por", "gbr", "saa", "hre", "pap", "gen", "plc", "rus", "tuc", "ven", "ast", "per", "geo", "swi", "ott", "nog", "sav", "mil", "Bav", "den", "swe", "Pom", "bra", "hes", "Sax", "net", "lor"};
    //*1756*/ public static final String[] DEBUG_NATIONS = {"spa", "fra", "por", "gbr", "mor", "hre", "pap", "gen", "plc", "rus", "tuc", "ven", "ast", "per", "geo", "swi", "ott", "aze", "pie", "mil", "Bav", "den", "swe", "zan", "pru", "hes", "Sax", "net", "lor", "han", "mec"};
    //*1811*/ public static final String[] DEBUG_NATIONS = {"spa", "fre", "por", "gbr", "mor", "rus", "ita", "ast", "per", "geo", "swi", "ott", "pie", "Bav", "den", "swe", "pru", "rhi", "Sax", "bad", "nap", "POl"};
    //*1823*/ public static final String[] DEBUG_NATIONS = {"spa", "fra", "por", "gbr", "mor", "hre", "pap", "rus", "tuc", "ast", "per", "geo", "swi", "ott", "pie", "Bav", "den", "swe", "pru", "hes", "Sax", "net", "lor", "han", "mec", "two"};
    //*1914*/ public static final String[] DEBUG_NATIONS = {"spa", "Fra", "por", "gbr", "ger", "ita", "rus", "ahe", "per", "swi", "ott", "den", "swe", "Nor", "net", "bel", "bos", "srb", "rmn", "Bul", "alb", "gre", "cze"};
    //*1931*/ public static final String[] DEBUG_NATIONS = {"Spa", "Fra", "por", "gbr", "ire", "Ger", "Ita", "sov", "Atr", "Hng", "per", "swi", "Nor", "trk", "den", "swe", "fin", "net", "bel", "yug", "POl", "rmn", "Bul", "alb", "gre", "Lit", "ltv", "est", "cze"};
    //*1939*/ public static final String[] DEBUG_NATIONS = {"SPa", "Fra", "por", "gbr", "ire", "rei", "Ita", "sov", "Hng", "ira", "swi", "trk", "den", "swe", "Nor", "fin", "net", "bel", "yug", "POl", "rmn", "Bul", "alb", "gre", "Lit", "ltv", "est", "cze"};
    //*1966*/ public static final String[] DEBUG_NATIONS = {"SPa", "Fra", "por", "gbr", "ire", "mor", "wgr", "egr", "ITa", "sov", "Atr", "Hng", "ira", "swi", "trk", "den", "swe", "Nor", "fin", "net", "bel", "yug", "POl", "rmn", "Bul", "alb", "gre", "cze", "alg", "tun", "lib", "egy", "isr", "sau", "syr", "irq"};
    //*2020*/ public static final String[] DEBUG_NATIONS = {"SPA", "Fra", "por", "gbr", "ire", "Geo", "mor", "GEr", "ITa", "Rus", "Hng", "ira", "Atr", "swi", "trk", "den", "swe", "Nor", "fin", "net", "bel", "Srb", "POl", "rmn", "Bul", "alb", "gre", "Lit", "ltv", "est", "cze", "alg", "tun", "lib", "egy", "isr", "sau", "syr", "irq", "Bos", "slo", "slk", "Cro", "mac", "Mol", "ukr", "bru", "kaz"};

    //*Rom414*/public static final String[] DEBUG_NATIONS = {"rom", "bzn", "vis", "pic", "van", "bgn", "fnk", "ala", "hun", "sas", "slv", "gas", "lak", "aqt", "lug", "his", "sbi"};
    //*Rom631*/public static final String[] DEBUG_NATIONS = {"neu", "ata", "eag", "yrk", "wha", "wes", "fri", "pom", "pol", "blt", "bav", "bul", "kha", "cph", "bne", "dan", "bzn", "pic", "vis", "bri", "lom", "ava", "slv", "sas", "sax", "orl", "gcn", "rom", "atr", "can"};
    //*Rom794*/ public static final String[] DEBUG_NATIONS = {"atr", "cba", "fnk", "isd", "tah", "mer", "num", "len", "cnn", "uls", "mns", "wha", "wes", "pic", "pol", "ava", "pom", "blt", "dan", "slv", "kha", "bzn", "bri", "asd", "ovi", "gcn"};

    //*Kai1917*/ public static final String[] DEBUG_NATIONS = {"spa", "Fra", "por", "gbr", "ger", "ita", "rus", "per", "swi", "trk", "den", "swe", "Nor", "net", "bel", "srb", "rmn", "Bul", "alb", "gre", "cze", "Hng", "bos", "cro", "POl", "fin"};
    //*Kai1930*/ public static final String[] DEBUG_NATIONS = {"spa", "Fra", "fsu", "por", "gbr", "ger", "ita", "rus", "per", "swi", "trk", "den", "swe", "Nor", "net", "bel", "srb", "rmn", "Bul", "alb", "gre", "cze", "Hng", "bos", "cro", "POl", "fin"};

    //*2023*/ public static final String[] DEBUG_NATIONS = {"SPA", "Fra", "por", "gbr", "ire", "Geo", "mor", "GEr", "ITa", "Rus", "Hng", "ira", "Atr", "swi", "trk", "den", "swe", "Nor", "fin", "net", "bel", "Srb", "POl", "rmn", "Bul", "alb", "gre", "Lit", "ltv", "est", "cze", "alg", "tun", "lib", "egy", "isr", "sau", "syr", "irq", "Bos", "slo", "slk", "Cro", "mac", "Mol", "ukr", "bru", "kaz", "Nov"};
    //public static final String DEBUG_TIMELINE = "kai";
    public static final String debugId = "kai1930";
    public static String[] debugNations;

    protected static Player[] debugPlayers;

    public static ConstraintLayout getWinLayout(){return winLayout;}
    public static ImageView getStatusCover(){return statusCover;}
    public static ImageView getAttackerBackround(){return attackerBackround;}
    public static ImageView getDefenderBackround(){return defenderBackround;}
    public static ImageView getAttacker(){return attacker;}
    public static ImageView getDefender(){return defender;}
    public static ImageView getSliderImage(){return sliderImage;}
    public static ImageView getDefeated(){return defeated;}
    public static ImageView getDefeated2(){return defeated2;}
    public static ImageView getSlideCover(){return slideCover;}
    public static ImageButton getChange(){return change;}
    public static String getJumpText(){return jumpTo.getText().toString();}
    public static ImageButton getAgain(){return again;}
    public static ImageButton getAnnihilate(){return annihilate;}
    public static ImageButton getRetreat(){return retreat;}
    public static ImageView getRollsCover(){return rollsCover;}
    public static TextView getDefenderTroops(){return defenderTroops;}
    public static TextView getAttackerTroops(){return attackerTroops;}
    public static TextView getaDie1(){return aDie1;}
    public static TextView getaDie2(){return aDie2;}
    public static TextView getaDie3(){return aDie3;}
    public static TextView getdDie1(){return dDie1;}
    public static TextView getdDie2(){return dDie2;}
    public static SeekBar getSlider(){return slider;}
    public static TextView getSlideTroops(){return slideTroops;}
    public static TextView getStatus(){return status;}
    public static void setJumpText(String set){jumpTo.setText(set);}
    public static void setWinFlag(Player winner){winnerFlag.setBackgroundResource(winner.getFlag());}
    public static void setPlayerInfo(String set){playerInfo.setText(set);}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        musicOpen = false;
        loadWindow();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(debugingOn) initialDebug();
                vars = context.getSharedPreferences("vars", 0);
                autosave();
                saveMaker();
                intitalAchives();
                drawer();
                makeAlerts();
                gameControls();
            }
        });
    }

    protected void initializeComponents(){
        super.initializeComponents();
        //navBar
        navBar = findViewById(R.id.navBar);
        saveMaker = findViewById(R.id.savePopup);
        saveInput = findViewById(R.id.saveInput);
        saveOK = findViewById(R.id.saveOK);
        saveCancel = findViewById(R.id.saveCancel);
        handle = findViewById(R.id.drawerHandle);
        saver = findViewById(R.id.saver);
        optioner = findViewById(R.id.optioner);
        stater = findViewById(R.id.stater);
        quitter = findViewById(R.id.quitter);
        //rolls
        attackerBackround = findViewById(R.id.attackerBackround);
        defenderBackround = findViewById(R.id.defenderBackround);
        attacker = findViewById(R.id.attacker);
        defender = findViewById(R.id.defender);
        defenderTroops = findViewById(R.id.defenderTroops);
        attackerTroops = findViewById(R.id.attackerTroops);
        defeated = findViewById(R.id.defeated);
        defeated2 = findViewById(R.id.defeated2);
        rollsCover = findViewById(R.id.rollsCover);
        aDie1 = findViewById(R.id.aDie1);
        aDie1.setTextColor(Color.BLACK);
        aDie2 = findViewById(R.id.aDie2);
        aDie2.setTextColor(Color.BLACK);
        aDie3 = findViewById(R.id.aDie3);
        aDie3.setTextColor(Color.BLACK);
        dDie1 = findViewById(R.id.dDie1);
        dDie1.setTextColor(Color.BLACK);
        dDie2 = findViewById(R.id.dDie2);
        dDie2.setTextColor(Color.BLACK);
        //slide
        sliderImage = findViewById(R.id.sliderImage);
        slideCover = findViewById(R.id.slideCover);
        slideTroops = findViewById(R.id.slideTroops);
        slider = findViewById(R.id.slide);
        //status
        status = findViewById(R.id.status);
        status.setTextSize(TypedValue.COMPLEX_UNIT_IN,.8f*BASE_TEXT_SCALE*inchWidth);
        statusCover = findViewById(R.id.statusCover);
        stageCover = findViewById(R.id.stageCover);
        //controllers
        annihilate = findViewById(R.id.annihilate);
        again = findViewById(R.id.again);
        retreat = findViewById(R.id.retreat);
        change = findViewById(R.id.change);
        turnBack = findViewById(R.id.turnbk);
        turnForward = findViewById(R.id.turnfd);
        //Achives
        achiveDrop = findViewById(R.id.achiveDropdown);
        achiveImage = findViewById(R.id.achiveImage);
        achiveTitle = findViewById(R.id.achiveText);
        toAchives = findViewById(R.id.toAchive);
        achiveTitle.setTextSize(TypedValue.COMPLEX_UNIT_IN,BASE_TEXT_SCALE*inchWidth);
        achiveTitle.setTextColor(Color.BLACK);

        jumpTo = findViewById(R.id.jumpTo);

        musicControls();
        makeDiplo();
        makeDipPopup();
        initAtWar();
    }

    private void loadWindow(){
        winLayout = findViewById(R.id.winLayout);
        winCover = findViewById(R.id.winCover);
        loadText = findViewById(R.id.loadText);
        loadText.setTextColor(Color.BLACK);
        loadText.setTextSize(TypedValue.COMPLEX_UNIT_IN,BASE_TEXT_SCALE*inchWidth);
        List<Integer> loadArray = Arrays.asList(LOAD_ROUNDS);
        int loadImg = loadArray.get((int)(Math.random()*LOAD_ROUNDS.length));
        winCover.setImageResource(loadImg);
        winCover.setScaleType(ImageView.ScaleType.CENTER_CROP);
        winLayout.setVisibility(View.VISIBLE);
        byte[] buffer = new byte[0];
        String notes = "";
        try {
            InputStream stream = getAssets().open("sacredTexts/loadScreens.txt");
            int size = stream.available();
            buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            notes = new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<Integer> breaks = new ArrayList(0);
        breaks.add(-1);
        for (int i = 0; i < notes.length(); i++)
            if (notes.charAt(i) == '\n' && notes.length() > i + 1) breaks.add(i);
        Log.i("BreaksSize", "" + breaks.size());
        int rand = (int)(Math.random()*breaks.size());
        Log.i("BreaksIndex", "" + breaks.get(rand));
        int lastEnter = notes.indexOf('\n', breaks.get(rand) + 1);
        if (lastEnter == -1) lastEnter = notes.length();
        notes = notes.substring(breaks.get(rand) + 1, lastEnter);
        loadText.setText(notes);
    }

    public static void changeStageIcon(int stage){
        if(stage == -1) stageCover.setBackgroundResource(R.drawable.stageset);
        if(stage == 0) stageCover.setBackgroundResource(R.drawable.stagerein);
        if(stage == 1) stageCover.setBackgroundResource(R.drawable.stageattack);
        if(stage == 2) stageCover.setBackgroundResource(R.drawable.stagetrans);
    }

    protected void touchProvince(Province touched) {
        super.touchProvince(touched);
        playerInfo.setText("Natives, Barbarians, and the like");
    }

    public static void achiveDrop(String tag){
        Log.i("drop", "achive");
        Object[] info = Achivements.infoFromTag(tag);
        //achiveImage.setBackgroundResource(from infoFromTag);
        achiveTitle.setText((String)info[0]);
        achiveImage.setBackgroundResource((int)info[2]);
        achiveDrop.animate().y(0).setDuration(2000);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                achiveDrop.animate().y(-1000).setDuration(2000);
            }

        }, 4000);
    }
    public void intitalAchives(){
        toAchives.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AchivementActivity.class);
                startActivity(intent);
            }
        });
    }

    private void drawer(){
        handle.setBackgroundResource(R.drawable.closenav);
        handle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNav = !openNav;
                if(openNav){
                    handle.setBackgroundResource(R.drawable.opennav);
                    handle.animate().x((float)(screenHeight*(.66))).setDuration(500).start();
                    navBar.animate().x((float)(screenHeight*(.71))).setDuration(500).start();
                    navBar.setVisibility(View.VISIBLE);
                }
                else{
                    handle.setBackgroundResource(R.drawable.closenav);
                    handle.animate().x((float)(screenHeight*(.93))).setDuration(500).start();
                    navBar.animate().x(screenHeight).setDuration(500).start();
                    navBar.setVisibility(View.VISIBLE);
                }
            }
        });
        saver.setBackgroundResource(R.drawable.navsave);
        saver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saver.setBackgroundResource(R.drawable.navsavedown);
                saver.postDelayed(new Runnable() {
                    @Override public void run() { saver.setBackgroundResource(R.drawable.navsave);}}, 500);
                Log.i("save", "open: " + openSave);
                if(!openSave)
                    saveMaker.animate().y(screenWidth/4).setDuration(500).start();
                openSave = true;
            }
        });
        quitter.setBackgroundResource(R.drawable.navquit);
        quitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        stater.setBackgroundResource(R.drawable.navstats);
        stater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, StatsActivity.class);
                //Bundle statsBundle = new Bundle();
                statsBundle = game.getAllStats();
                //intent.putExtra("statsBundle", statsBundle);
                handle.animate().x((float)(screenHeight*(.93))).setDuration(500).start();
                navBar.animate().x(screenHeight).setDuration(500).start();
                openNav = false;
                startActivity(intent);
            }
        });
        optioner.setBackgroundResource(R.drawable.navoptions);
        optioner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OptionsActivity.class);
                startActivity(intent);
            }
        });
    }

    protected void info() {
        super.info();
        if(!game.getImperium()){
            devasIcon.setVisibility(View.INVISIBLE);
            devlIcon.setVisibility(View.INVISIBLE);
            attriIcon.setVisibility(View.INVISIBLE);
        }
        else{
            devlIcon.setBackgroundResource(R.drawable.developicon);
            attriIcon.setBackgroundResource(R.drawable.attritionicon);
        }
        if(game.getImperium())
            ownerFlag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    info.animate().x(-2000).setDuration(500).start();
                    openInfo = false;
                    Log.i("info", "open: " + openInfo);
                    if(infoProv.getOwner() != null &&
                            infoProv.getOwner().getId() != game.getCurrentPlayer().getId() && game.isHistorical())
                        openDiplo(infoProv.getOwner().getTag());
                }
            });

        developer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //infoProv.manualDevelopment();
                developer.setBackgroundResource(R.drawable.developerdown);
                developer.postDelayed(new Runnable() {
                    @Override public void run() { developer.setBackgroundResource(R.drawable.developer);}}, 500);
                developer(infoProv);
            }
        });
        fortifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fortifier.setBackgroundResource(R.drawable.builderdown);
                fortifier.postDelayed(new Runnable() {
                    @Override public void run() { fortifier.setBackgroundResource(R.drawable.builder);}}, 500);
                fortifier(infoProv);
            }
        });
    }
    public void developer(Province prov){
        game.getCurrentPlayer().modMonetae(-10);
        prov.modDevelopment(Math.exp(-prov.modDevelopment(0)/15));
        updateInfo();
    }
    public void fortifier(Province prov){
        prov.fortify();
        updateInfo();
    }

    private void makeAlerts(){
        alertLayout = findViewById(R.id.alertHolder);
        alerts = new ArrayList<>(0);
    }
    public void alertVis(final boolean vis){
        runOnUiThread(new Runnable() {
            @Override public void run() {
                if(vis) alertLayout.setVisibility(View.VISIBLE);
                else alertLayout.setVisibility(View.INVISIBLE); }});
    }
    public void addAlert(final int id, final int type, final String group, final String from){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertBanner alert = new AlertBanner(context, id, type, group, from);
                alerts.add(alert);
                alertLayout.addView(alert.getAlertButton());
            }
        });
    }
    public void removeAlert(final int type, final String group, final String from){
        runOnUiThread(new Runnable() {
            @Override public void run() {
                Log.i("alert", ""+alerts.size());
                for(int i=0; i<alerts.size(); i++){
                    if(alerts.get(i).getType() == type && alerts.get(i).getFromTag().equals(from) && alerts.get(i).getGroup().equals(group)){
                        final int finalI = i;
                        alertLayout.removeView(alerts.get(finalI).getAlertButton());
                        alerts.remove(alerts.get(finalI));
                    }
                }
            }
        });
    }
    public void clearAlerts(){
        runOnUiThread(new Runnable() {
            @Override public void run() {
                alerts = new ArrayList<>(0);
                alertLayout.removeAllViews();
            }});
    }

    private void makeDiplo(){
        dipLogo = findViewById(R.id.dipLogo);
        dipType = findViewById(R.id.dipType);
        warDiplo = findViewById(R.id.warDiplo);
        allyDiplo = findViewById(R.id.allyDiplo);
        subDiplo = findViewById(R.id.subDiplo);
        textDiplo = findViewById(R.id.textDiplo);
        diploLayout = findViewById(R.id.diploLayout);
        closeDiplo = findViewById(R.id.closeDiplo);
        flagDiplo = findViewById(R.id.flagDiplo);
        backDiplo = findViewById(R.id.backDiplo);
        dipProg = findViewById(R.id.agreement);
        dipProgCover = findViewById(R.id.progCover);
        textDiplo.setText("Relation to us: ");
        closeDiplo.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { diploLayout.animate().x(-2000).setDuration(500);}});
    }
    public void openDiplo(final String tag){
        Player plater = game.getCurrentPlayer();
        diploTag = tag;

        Log.i("RelationText", (String) textDiplo.getText());
        textDiplo.setText("Relation to us: \nThey are neutral");
        if(plater.isTruce(tag)) textDiplo.setText("Relation to us: \nWe have a truce which expires in "
                +(-plater.getTruceEnd(tag)+(int)(game.getTurnNum()/(double)game.getPlayerList().length))+" turns");
        if(plater.isAllied(tag)) textDiplo.setText("Relation to us: \nWe are allies");
        if(plater.hasSubject(tag)) textDiplo.setText("Relation to us: \nThey are our subject");
        if(plater.isHostile(tag)) textDiplo.setText("Relation to us: \nWe are at war!");
        if(plater.hasOverlord()) if(getGame().playerFromTag(plater.getOverlord()).isAllied(tag)) textDiplo.setText("Relation to us: \nThey are a subject of our ally");

        Log.i("RelationText", (String) textDiplo.getText());

        diploLayout.setVisibility(View.VISIBLE);
        allyDiplo.setVisibility(View.VISIBLE);
        warDiplo.setVisibility(View.VISIBLE);
        subDiplo.setVisibility(View.VISIBLE);
        dipProg.setVisibility(View.INVISIBLE);
        boolean allyEnabled = true;
        boolean subEnabled = true;
        boolean warEnabled = true;
        dipProgCover.setVisibility(View.INVISIBLE);
        Bitmap prime = null;
        if(!game.getCurrentPlayer().isHostile(diploTag))prime = BitmapFactory.decodeResource(context.getResources(), R.drawable.declarewar);
        else prime = BitmapFactory.decodeResource(context.getResources(), R.drawable.peacedeal);warDiplo.setImageBitmap(prime);
        prime = BitmapFactory.decodeResource(context.getResources(), R.drawable.makeally); allyDiplo.setImageBitmap(prime);
        prime = BitmapFactory.decodeResource(context.getResources(), R.drawable.makesub); subDiplo.setImageBitmap(prime);
        warDiplo.setBackgroundColor(Color.TRANSPARENT);
        allyDiplo.setBackgroundColor(Color.TRANSPARENT);
        subDiplo.setBackgroundColor(Color.TRANSPARENT);


        if(game.getCurrentPlayer().hasOverlord() || game.playerFromTag(diploTag).hasOverlord()){
            warDiplo.setColorFilter(grayScale());
            allyDiplo.setColorFilter(grayScale());
            subDiplo.setColorFilter(grayScale());
            allyEnabled = false;
            subEnabled = false;
            warEnabled = false;
        }
        else if(!game.getCurrentPlayer().isAttackable(diploTag)){
            warDiplo.setColorFilter(grayScale());
            warEnabled = false;
        }
        /*else if(game.getCurrentPlayer().hasOverlord()){
            subDiplo.setColorFilter(grayScale());
            subEnabled = false;
        }*/
        else if(game.getCurrentPlayer().hasSubject(diploTag)){
            allyDiplo.setColorFilter(grayScale());
            allyEnabled = false;
        }
        else{
            warDiplo.clearColorFilter();
            allyDiplo.clearColorFilter();
            subDiplo.clearColorFilter();
            allyEnabled = true;
            subEnabled = true;
            warEnabled = true;
        }
        dipType.setBackgroundResource(R.drawable.diplomacy);
        dipLogo.setBackgroundResource(R.drawable.diplogo);
        flagDiplo.setBackgroundResource(game.playerFromTag(tag).getFlag());
        Log.i("Open Diplo", tag+game.playerFromTag(tag).getName());
        backDiplo.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { openDiplo(diploTag);}});
        final boolean finalAllyEnabled = allyEnabled;
        allyDiplo.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { if(finalAllyEnabled)allyScreen();}});
        final boolean finalSubEnabled = subEnabled;
        subDiplo.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { if(finalSubEnabled)subjectScreen();}});
        final boolean finalWarEnabled = warEnabled;
        warDiplo.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {
            if(game.getCurrentPlayer().isAttackable(diploTag) && finalWarEnabled){
                if(!game.getCurrentPlayer().isHostile(diploTag))
                    warScreen();
                else showDipPop(4, game.getCurrentPlayer().getWar(diploTag), "#nn");
            }
        }});
        diploLayout.animate().x(15).setDuration(500);
        warDiplo.setX(screenHeight*.04f); warDiplo.setY(screenWidth*.38f);
        allyDiplo.setX(screenHeight*.04f); allyDiplo.setY(screenWidth*.5f);
        subDiplo.setX(screenHeight*.04f); subDiplo.setY(screenWidth*.65f);
    }
    private void allyScreen(){
        dipType.setBackgroundResource(R.drawable.allytitle);
        dipLogo.setBackgroundResource(R.drawable.ally);
        subDiplo.setVisibility(View.INVISIBLE);
        warDiplo.setVisibility(View.INVISIBLE);
        allyDiplo.setVisibility(View.VISIBLE);
        dipProg.setVisibility(View.VISIBLE);
        dipProgCover.setVisibility(View.VISIBLE);
        allyDiplo.setY(screenWidth*.7f);
        Player target = game.getPlayerList()[game.playerIdFromTag(diploTag)];
        if(game.getCurrentPlayer().isAllied(diploTag)){
            textDiplo.setText("Breaking our alliance may help us forge our future, but will create a one turn truce");
            allyDiplo.setBackgroundResource(R.drawable.breakally);
            allyDiplo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    game.getCurrentPlayer().removeAlly(diploTag);
                    game.getPlayerList()[game.playerIdFromTag(diploTag)].removeAlly(game.getCurrentPlayer().getTag());
                    game.getCurrentPlayer().addTruce(diploTag, 1);
                    game.getPlayerList()[game.playerIdFromTag(diploTag)].addTruce(game.getCurrentPlayer().getTag(), 1);
                    backDiplo.performClick();
                }});
        }else {
            if (target.isHuman())
                textDiplo.setText("It is unknown if "+target.getName()+" will form an alliance");
            else{
                textDiplo.setText(target.getName()+" will most likely "+ ((!target.willAlly(game.getCurrentPlayer().getTag())) ? "not" : "")+
                        " become our ally");
                if(!target.willAlly(game.getCurrentPlayer().getTag()))
                    dipProgCover.setVisibility(View.INVISIBLE);
            }
            allyDiplo.setBackgroundResource(R.drawable.makeally);
            allyDiplo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //check if ai would ally
                    makeAlly(diploTag);
                    backDiplo.performClick();
                }
            });
        }
    }
    private void subjectScreen(){
        dipType.setBackgroundResource(R.drawable.subtitle);
        dipLogo.setBackgroundResource(R.drawable.sunject);
        subDiplo.setVisibility(View.VISIBLE);
        warDiplo.setVisibility(View.INVISIBLE);
        allyDiplo.setVisibility(View.INVISIBLE);
        dipProg.setVisibility(View.VISIBLE);
        dipProgCover.setVisibility(View.VISIBLE);
        subDiplo.setY(screenWidth*.7f);
        Player target = game.getPlayerList()[game.playerIdFromTag(diploTag)];
        if(game.getCurrentPlayer().hasSubject(diploTag)){
            textDiplo.setText("Releasing our subject, "+target.getName()+", may help us forge our future, but will create a one turn truce");
            subDiplo.setBackgroundResource(R.drawable.releasesub);
            subDiplo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    game.getCurrentPlayer().removeMinion(diploTag);
                    game.getPlayerList()[game.playerIdFromTag(diploTag)].removeOverlord(game.getCurrentPlayer().getTag());
                    game.getCurrentPlayer().addTruce(diploTag, 1);
                    game.getPlayerList()[game.playerIdFromTag(diploTag)].addTruce(game.getCurrentPlayer().getTag(), 1);
                    backDiplo.performClick();
                }});
        }else {
            if (target.isHuman())
                textDiplo.setText("It is unknown if "+target.getName()+" desires our protection");
            else{
                textDiplo.setText(target.getName()+" will most likely "+ ((!target.willSubmitTo(game.getCurrentPlayer().getTag())) ? "not" : "")+
                        " become our subject");
                if(!target.willSubmitTo(game.getCurrentPlayer().getTag()))
                    dipProgCover.setVisibility(View.INVISIBLE);
            }
            subDiplo.setBackgroundResource(R.drawable.makesub);
            subDiplo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    makeSubject(diploTag);
                    backDiplo.performClick();
                }
            });
        }
    }
    private void warScreen(){
        dipType.setBackgroundResource(R.drawable.wartitle);
        dipLogo.setBackgroundResource(R.drawable.war);
        subDiplo.setVisibility(View.INVISIBLE);
        warDiplo.setVisibility(View.VISIBLE);
        allyDiplo.setVisibility(View.INVISIBLE);
        dipProg.setVisibility(View.INVISIBLE);
        dipProgCover.setVisibility(View.INVISIBLE);
        warDiplo.setY(screenWidth*.7f);
        final Player target = game.getPlayerList()[game.playerIdFromTag(diploTag)];
        if(!game.getCurrentPlayer().isHostile(diploTag)) {
            textDiplo.setText("Declare war on " + target.getName() + " to expand our domain");
            warDiplo.setBackgroundResource(R.drawable.declarewar);
            warDiplo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    declareWar(diploTag);
                }
            });
        }
    }
    protected void makeAlly(String tag){
        game.getPlayerList()[game.playerIdFromTag(tag)].addRequestFrom(1, "000000", game.getCurrentPlayer().getTag());
    }
    protected void makeSubject(String tag){
        game.getPlayerList()[game.playerIdFromTag(tag)].addRequestFrom(2, "000000", game.getCurrentPlayer().getTag());
        ArrayList<String> strings = game.playerFromTag(tag).getDiplo()[1];
        for (int i = 0; i < strings.size(); i++) {
            String s = strings.get(i);
            game.playerFromTag(tag).removeAlly(s);
            i--;
        }
    }
    protected void declareWar(String tag){
        String currentTag = game.getCurrentPlayer().getTag();
        game.getCurrentPlayer().addToWar(currentTag+tag, currentTag);
        game.playerFromTag(tag).addToWar(currentTag+tag, tag);
        for(Province p : game.playerFromTag(tag).getAllOwned()) p.updateOwner();
        addWar(game.getCurrentPlayer().getTag(), tag);
    }
    private void makeDipPopup(){
        dipPopLayout = findViewById(R.id.diploPopup);
        dipPopConfirm = findViewById(R.id.dipPopConfirm);
        dipPopSurrender = findViewById(R.id.dipPopSurrender);
        dipPopConfirm.setBackgroundColor(Color.TRANSPARENT);
        dipPopSurrender.setBackgroundColor(Color.TRANSPARENT);
        dipPopCancel = findViewById(R.id.dipPopCancel);
        dipPopFlag = findViewById(R.id.dipPopFlag);
        dipPopTitle = findViewById(R.id.dipPopTitle);
        dipPopText = findViewById(R.id.dipPopText);
        popReasons = findViewById(R.id.popReasons);
        popProgress = findViewById(R.id.popAgreement);
        popProgCover = findViewById(R.id.popProgCover);
        dipPopCancel.setBackgroundResource(R.drawable.cancel);
    }
    public void requestChoice(final int type, final String group, final String from){
        Log.i("RequestChoice", ""+type+", "+ group+", "+from);
        String enemyTag = game.getCurrentPlayer().relationsFromWarTag(group)[1];
        String friendTag = game.getCurrentPlayer().relationsFromWarTag(group)[0];

        if(type == 10) {
            Log.i("Ading Ally", from+", currnet: "+game.getCurrentPlayer().getName());
            game.getCurrentPlayer().addAlly(from); game.playerFromTag(from).addAlly(game.getCurrentPlayer().getTag());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for(Province p : game.getMap().getList()) p.updateOwner();
                }
            });
        }
        if(type == 20) {game.getCurrentPlayer().addOverlord(from); game.playerFromTag(from).addMinion(game.getCurrentPlayer().getTag());}
        if(type == 30){
            game.getCurrentPlayer().addToWar(group, from);
            for(String s : game.playerFromTag(from).getDiplo()[3]){
                Log.i("CallToArjms", s.substring(0, 6)+group);
                        /*if(s.substring(0, 6).equals(group)) {
                            game.getCurrentPlayer().addHostile(s.substring(6), group.substring(0, 3), group.substring(3, 6));
                            game.playerFromTag(s.substring(6)).addHostile(game.getCurrentPlayer().getTag(), group.substring(0, 3), group.substring(3, 6));
                            game.getCurrentPlayer().addRecentWar(group);
                        }*/

                if(game.getCurrentPlayer().isAllied(s.substring(6))){
                    game.playerFromTag(s.substring(6)).removeAlly(game.getCurrentPlayer().getTag());
                    game.getCurrentPlayer().removeAlly(s.substring(6));
                }
            }
            addWar(game.getCurrentPlayer().getDiplo()[3].get(0).substring(0, 3), game.getCurrentPlayer().getDiplo()[3].get(0).substring(3, 6));
        }
        if((type == 40 || type == 41) && !from.equals("#nn")){
            game.playerFromTag(from).removeRequest(type, group, from);
            removeAlert(type, group, from);
            String winnerTag;
            if(type == 40) winnerTag = friendTag;
            else winnerTag = enemyTag;
            Player target = game.playerFromTag(enemyTag);
            String currentTag = game.getCurrentPlayer().getTag();
            game.getCurrentPlayer().removeFromWar(group, winnerTag);
            if(currentTag.equals(friendTag)) {
                for (String s : game.getCurrentPlayer().getDiplo()[1])
                    game.playerFromTag(s).removeFromWar(group, winnerTag);

                game.playerFromTag(enemyTag).removeFromWar(group, winnerTag);
                for (String s : target.getDiplo()[1])
                    game.playerFromTag(s).removeFromWar(group, winnerTag);

                removeWar(game.getCurrentPlayer().getTag(), friendTag);
            }

        }else if(/*type == 40 && */!game.getCurrentPlayer().getRecentWars().contains(group)){
            game.playerFromTag(enemyTag).addRequestFrom(type, group, game.getCurrentPlayer().getTag());
        }
        if(!from.equals("#nn")) {
            game.getCurrentPlayer().removeRequest(type, group, from);
            removeAlert(type, group, from);
        }
    }
    public void showDipPop(int type, final String group, final String from){
        if((""+type).length() == 1) type *= 10;
        String enemyTag = group.substring(0, 3);
        String friendTag = group.substring(3);
        if(game.getCurrentPlayer().isAllied(enemyTag)){
            enemyTag = group.substring(3);
            friendTag = group.substring(0, 3);
        }
        if(enemyTag.equals(game.getCurrentPlayer().getTag())) enemyTag = group.substring(3, 6);

        Bitmap prime;
        Bitmap surPrime;

        dipPopConfirm.setBackgroundColor(Color.TRANSPARENT);
        dipPopSurrender.setBackgroundColor(Color.TRANSPARENT);
        if(!from.equals("#nn")){
            dipPopFlag.setBackgroundResource(game.playerFromTag(from).getFlag());
            prime = BitmapFactory.decodeResource(context.getResources(), R.drawable.confirm);
            dipPopSurrender.setVisibility(View.INVISIBLE);
        }
        else{
            prime = BitmapFactory.decodeResource(context.getResources(), R.drawable.peacedeal);
            surPrime = BitmapFactory.decodeResource(context.getResources(), R.drawable.surrender);
            dipPopSurrender.setImageBitmap(surPrime);
            dipPopFlag.setBackgroundResource(game.playerFromTag(enemyTag).getFlag());
            dipPopSurrender.setVisibility(View.VISIBLE);
        }
        dipPopConfirm.setImageBitmap(prime);
        if(game.getCurrentPlayer().getRecentWars().contains(group)) {
            dipPopConfirm.setColorFilter(grayScale());
            dipPopSurrender.setColorFilter(grayScale());
        }
        else {
            dipPopConfirm.clearColorFilter();
            dipPopSurrender.clearColorFilter();
        }
        if(type == 10) {
            dipPopText.setText(game.playerFromTag(from).getName()+" has extended to us an offer for a new alliance");
            dipPopTitle.setBackgroundResource(R.drawable.allytitle);}
        if(type == 20){
            dipPopText.setText(game.playerFromTag(from).getName()+" would like to offer their protection as an overlord");
            dipPopTitle.setBackgroundResource(R.drawable.subtitle);
        }
        if(type == 30){
            dipPopText.setText(game.playerFromTag(from).getName()+" has send us a call to arms in their war against "+
                    game.playerFromTag(group.substring(3, 6)).getName()+" and their allies");
            dipPopTitle.setBackgroundResource(R.drawable.wartitle);
        }


        if(from.equals("#nn")){
            Log.i("DipType", ""+type+from);
            double peacePer = (int)(game.playerFromTag(enemyTag).reasonsToAcceptPeace(game.getCurrentPlayer().getTag())*100);
            popReasons.setText(game.playerFromTag(enemyTag).getName()+" is "+
                    peacePer+"% to accepting peace");
            peacePer /= 100.0;
            if(peacePer > 1) peacePer = 1;
            popProgress.animate().x((float) (-popProgress.getWidth()+peacePer*popProgress.getWidth())).setDuration(0);
            popProgress.setVisibility(View.VISIBLE);
            popProgCover.setVisibility(View.VISIBLE);
        }else{
            popProgress.setVisibility(View.INVISIBLE);
            popReasons.setText("");
            popProgCover.setVisibility(View.INVISIBLE);
        }
        if(type == 40 || type == 41){
            dipPopTitle.setBackgroundResource(R.drawable.peacetitle);
            if(from.equals("#nn")){
                dipPopText.setText("Propose to end the war between us and "+game.playerFromTag(enemyTag).getName()
                        +" in the "+game.playerFromTag(group.substring(0, 3)).getName()+"-"+game.playerFromTag(group.substring(3, 6)).getName()+" War of Aggression, if they accept defeat, they will release our land but we will keep theirs");
            }else if(type == 41){
                dipPopText.setText(game.playerFromTag(from).getName()+" would like to extend to us an offer of peace, " +
                        " in the "+game.playerFromTag(group.substring(0, 3)).getName()+"-"+game.playerFromTag(group.substring(3, 6)).getName()+" War of Aggression, if we accept, we will be defeated, forfeiting all of our gains in this war");
            }else{
                dipPopText.setText(game.playerFromTag(from).getName()+" would like to extend to us an offer of surrender, " +
                        " in the "+game.playerFromTag(group.substring(0, 3)).getName()+"-"+game.playerFromTag(group.substring(3, 6)).getName()+" War of Aggression, if we accept, they will be defeated, forfeiting all of their gains in this war");
            }
        }

        dipPopLayout.animate().y(50).setDuration(500);
        dipPopLayout.setVisibility(View.VISIBLE);
        final String finalEnemyTag = enemyTag;
        final String finalFriendTag = friendTag;
        final int finalType = type;
        dipPopConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!from.equals("#nn"))
                    requestChoice(finalType, group, from);
                else requestChoice(41, group, from); //they surrender
                dipPopLayout.animate().y(-1000).setDuration(500);
            }
        });
        dipPopSurrender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestChoice(40, group, from); //we surrender
                dipPopLayout.animate().y(-1000).setDuration(500);
            }
        });
        final int finalType1 = type;
        dipPopCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game.getCurrentPlayer().removeRequest(finalType1, group, from);
                removeAlert(finalType1, group, from);
                dipPopLayout.animate().y(-1000).setDuration(500);
            }
        });
    }
    public LinearLayout getPotalHolder(){return portalHolder;}
    public void clearWarPortals(){
        runOnUiThread(new Runnable() {
            @Override public void run() {
                warPortals = new ArrayList<>(0);
                portalHolder.removeAllViews();
            }});
    }
    private void initAtWar(){
        warPortals = new ArrayList<>(0);
        portalHolder = findViewById(R.id.warHolder);
        portalHolder.setVisibility(View.INVISIBLE);
        warVis(false);
    }
    public void addWar(final String atTag, final String defTag){
        runOnUiThread(new Runnable() {
            @Override public void run() {
                WarPortal war = new WarPortal(context, atTag + defTag);
                Log.i("warViews", "" + portalHolder.getChildCount());
                for (WarPortal wp : warPortals)
                    if (wp.getIdent().equals(war.getIdent()))
                        return;
                warPortals.add(war);
                war.addViews((warPortals.size() - 1) * (screenHeight * .15f));
                Log.i("warViews", "" + portalHolder.getChildCount());
                portalHolder.setVisibility(View.VISIBLE);

            }});
    }
    public void removeWar(final String atTag, final String defTag){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                WarPortal war = new WarPortal(context, atTag+defTag);
                Log.i("warViews", ""+portalHolder.getChildCount());
                warPortals.remove(war);
                war.removeViews();
                Log.i("warViews", ""+portalHolder.getChildCount());
                portalHolder.setVisibility(View.VISIBLE);
                portalHolder.removeAllViews();
                game.getCurrentPlayer().refreshWars();
            }
        });
    }
    public void warVis(final boolean vis){
        runOnUiThread(new Runnable() {
            @Override public void run() {
                if(vis) portalHolder.setVisibility(View.VISIBLE);
                else portalHolder.setVisibility(View.INVISIBLE); }});
    }
    public void turnMoveVis(final boolean vis){
        runOnUiThread(new Runnable() {
            @Override public void run() {
                if(vis){
                    turnBack.setVisibility(View.VISIBLE);
                    turnForward.setVisibility(View.VISIBLE);
                }else{
                    turnBack.setVisibility(View.INVISIBLE);
                    turnForward.setVisibility(View.INVISIBLE);
                } }});

    }

    private void musicControls(){
        final ConstraintLayout player = findViewById(R.id.musicPlayer);
        musicTitle = findViewById(R.id.musicTitle);
        ImageButton open = findViewById(R.id.openPlayer);
        ImageButton backward = findViewById(R.id.backward);
        ImageButton play = findViewById(R.id.play);
        ImageButton pause = findViewById(R.id.pause);
        ImageButton forward = findViewById(R.id.forward);
        player.animate().x(-screenHeight*.24f).setDuration(0);
        musicTitle.setTextSize(TypedValue.COMPLEX_UNIT_IN,BASE_TEXT_SCALE*inchWidth);
        musicTitle.setText(getSongTitle());
        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (musicOpen) player.animate().x(-screenHeight * .24f).setDuration(500);
                else player.animate().x(screenHeight * .01f).setDuration(500);
                musicOpen = !musicOpen;
            }
        });
        backward.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { backwardMusic(); musicTitle.setText(getSongTitle()); }});
        forward.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { forwardMusic(); musicTitle.setText(getSongTitle());}});
        play.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { playMusic(); }});
        pause.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { pauseMusic(); }});
    }
    public static void setMusicTitle(String set){musicTitle.setText(set);}
    protected void gameControls(){
        Log.i("Game controls", "activated");
        //again.setBackgroundColor(TRANSPARENT);
        again.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                game.again();
            }
        });
        if(game.isHistorical()) {
            change.setVisibility(View.INVISIBLE);
            turnBack.setVisibility(View.VISIBLE);
            turnForward.setVisibility(View.VISIBLE);
        }
        change.setBackgroundResource(R.drawable.endplacement);
        change.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(game.getCurrentPlayer().isHuman()){Log.i("human press", "press");game.changer();}
            }
        });
        //retreat.setBackgroundColor(TRANSPARENT);
        retreat.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                game.retreat();
            }
        });
        annihilate.setBackgroundResource(R.drawable.annihilate);
        annihilate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                game.annihilate();
                annihilate.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        annihilate.setBackgroundResource(R.drawable.annihilate);
                    }
                }, 500);
            }
        });
        turnForward.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { game.changer(); }});
        turnBack.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { game.changerRev(); }});
    }

    private void initialDebug(){
        Button removePlayer = findViewById(R.id.ownerNull);
        jumpTo.setVisibility(View.VISIBLE);
        removePlayer.setVisibility(View.VISIBLE);
        removePlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(infoProv != null){
                    infoProv.updatePress(-1);
                    infoProv.modTroops(-infoProv.getTroops());
                }
            }
        });
        playerInfo.setVisibility(View.VISIBLE);
        if(newDebugSave) {
            debugPlayers = new Player[debugNations.length];
            for (int i = 0; i < debugPlayers.length; i++)
                debugPlayers[i] = new Player(context, i, true, debugNations[i]);
        }

        infoMods();
    }

    private void infoMods() {
        Button[] minus = new Button[]{
                findViewById(R.id.troopsMinus), findViewById(R.id.devasMinus), findViewById(R.id.develoMinus), findViewById(R.id.attrnMinus), findViewById(R.id.fortMinus)};
        Button[] plus = new Button[]{
                findViewById(R.id.troopsPlus), findViewById(R.id.devasPlus), findViewById(R.id.develoPlus), findViewById(R.id.attrnPlus), findViewById(R.id.fortPlus)};
        for (Button b : minus){b.setVisibility(View.VISIBLE);}
        for (Button b : plus){b.setVisibility(View.VISIBLE);}

        minus[0].setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { infoProv.modTroops(-1); updateInfo();}});
        minus[1].setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { infoProv.modDevastation(-.1);updateInfo();}});
        minus[2].setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { infoProv.modDevelopment(-1);updateInfo();}});
        minus[3].setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { infoProv.setAttrition(infoProv.getAttrition()-.01);updateInfo();}});
        minus[4].setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { infoProv.setFortLevel(infoProv.getFortLevel()-1);updateInfo();}});

        plus[0].setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { infoProv.modTroops(1);updateInfo();}});
        plus[1].setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { infoProv.modDevastation(.1);updateInfo();}});
        plus[2].setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { infoProv.modDevelopment(1);updateInfo();}});
        plus[3].setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { infoProv.setAttrition(infoProv.getAttrition()+.01);updateInfo();}});
        plus[4].setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { infoProv.setFortLevel(infoProv.getFortLevel()+1);updateInfo();}});
    }

    public static void changeNationAt(){showNation.setBackgroundResource(game.getCurrentPlayer().getFlag());}

    private ColorMatrixColorFilter grayScale(){
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        return new ColorMatrixColorFilter(matrix);
    }

    private void saveMaker(){
        ImageView saveRound = findViewById(R.id.saveRound);
        saveRound.setScaleType(ImageView.ScaleType.FIT_XY);
        saveOK.setBackgroundResource(R.drawable.navsave);
        saveOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game.saveString();
                saveOK.setBackgroundResource(R.drawable.navsavedown);
                saveOK.postDelayed(new Runnable() {
                    @Override public void run() { saveOK.setBackgroundResource(R.drawable.navsave);}}, 500);
                if(saveInput.getText().toString().equals("") || saveInput.getText().toString().equals("autosave"))
                    Toast.makeText(context, "Type a new save name", Toast.LENGTH_SHORT).show();
                else {
                    saveGame(saveInput.getText().toString() + ".imprm");
                    saveInput.setText("autosave");
                    saveMaker.animate().y(1000).setDuration(500).start();
                    openSave = false;
                }
            }
        });
        saveCancel.setBackgroundResource(R.drawable.cancel);
        saveCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInput.setText("autosave");
                saveMaker.animate().y(1000).setDuration(500).start();
                openSave = false;
            }
        });
    }
    public void saveGame(final String saveId){
        FileOutputStream fos;
        File save = new File(SAVE_PATH, saveId);
        saveString = game.saveString();
        try {
            fos = new FileOutputStream(save);
            fos.write(saveString.getBytes());
            fos.close();
        } catch (Exception e) { e.printStackTrace(); }
        runOnUiThread(new Runnable() {
            @Override public void run() {
                try{ Toast.makeText(context, "Saved to " + SAVE_PATH + "/" + saveId, Toast.LENGTH_LONG).show();
                }catch (RuntimeException e){e.printStackTrace();}
            }});
    }

    private void autosave(){
        if(game != null) saveString = game.saveString();
        Thread lookingThread = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(120000);
                        if(game != null)
                            try{saveGame(AUTO_SAVE_ID);}catch (Exception e){e.printStackTrace();}

                    }
                } catch (InterruptedException e) { e.printStackTrace(); }
            }
        };lookingThread.start();
    }
}