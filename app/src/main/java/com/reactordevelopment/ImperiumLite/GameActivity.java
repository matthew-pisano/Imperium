package com.reactordevelopment.ImperiumLite;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.reactordevelopment.ImperiumLite.MainActivity.*;

public class GameActivity extends AppCompatActivity implements PurchasesUpdatedListener {
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
    //info
    private static ImageButton closeInfo;
    private static ImageButton developer;
    private static ImageView fortifier;
    private static TextView infoText;
    private static ImageButton ownerFlag;
    private static ImageView coreFlag;
    private static ImageView devasIcon;
    private static ImageView devlIcon;
    private static ImageView attriIcon;
    private static TextView devlText;
    private static TextView davasText;
    private static TextView attriText;
    private static ConstraintLayout info;
    private static ImageView relation;
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
    //time view
    protected static boolean firstLoaded; //true if initial map has been loaded
    private SeekBar timeSlider;
    private ImageView timeCover;
    private TextView year;
    private TextView yearTitle;
    public static boolean historicalSave;
    private static EditText jumpTo;
    private static TextView playerInfo;
    private static ImageView nationFlag;
    private static ImageView showNation;
    private static ImageView showNationFrame;
    private LinearLayout flags;
    private LinearLayout selections;
    private ImageButton[] players;
    private ImageButton[] selects;
    private ImageButton toBuild;
    private String[] nations;
    private static Nation nationAt;
    private String timeLine = "";
    private String mapPath;
    //Achives
    private static ConstraintLayout achiveDrop;
    private static ImageView achiveImage;
    private static TextView achiveTitle;
    private ImageButton toAchives;
    //unlock
    private ConstraintLayout unlockPop;
    private ImageButton unlockClose;
    private ImageView locked;
    private static ArrayList<String> unlockedYears;
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
    //diploPopup
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
    //misc
    private static ImageView winnerFlag;
    private static ImageView winCover;
    private static TextView loadText;
    private static ConstraintLayout winLayout;
    private static ConstraintLayout masterLayout;
    private static RelativeLayout mapLayout;
    private static ImageView mapImage;
    private static Thread spinCycle = null;
    private float dXGear = 0;
    private float initX = 0;
    //vars
    public static BillingClient billingClient;
    public static ArrayList<String> skuDetails;
    //public static final String[] SKU_ARRAY = new String[]{"unlock_alp396", "unlock_alp477", "unlock_alp642", "unlock_alp802", "unlock_alp1066", "unlock_alp1248", "unlock_alp1445", "unlock_alp1532", "unlock_alp1618", "unlock_alp1756", "unlock_alp1811", "unlock_alp1823", "unlock_alp1914", "unlock_alp1931", "unlock_alp1939", "unlock_alp1966", "unlock_alp2020", "unlock_kai1917", "unlock_rom414", "unlock_rom631", "unlock_rom794"};
    public static final String[] SKU_ARRAY = new String[]{"unlock_modern", "unlock_althist"};
    private int yearAt = 0;
    private Integer[] years;
    private String[] titles;
    private int timeProgress = 0;
    private String yearInfo;
    protected static boolean timeView;
    protected static final int AI_STYLE = Ai.ROMAN;
    public static final float MIN_SCALE = .9f;
    public static final float MAX_SCALE = 10f;
    public static float scaling = 1/*(float)(screenWidth/480)*/;
    protected static Game game;
    public static Server server;
    public static String lastPingId;
    private static boolean switchServerPhase;
    private static boolean needConfirm;
    protected static boolean connectPhase;
    protected static boolean gameplayPlase;
    private static boolean breakServerThread;
    protected static ArrayList<String> lastUnconfirmed;
    private static ArrayList<Client> playerClients;
    private static int resendCycles;
    private static boolean forceClosed = false;
    protected static boolean provEnabled;
    private static boolean openNav;
    private static boolean openInfo;
    private static boolean openSave;
    private static boolean openMode;
    private static boolean[] openModes;
    private ScaleGestureDetector mScaleGestureDetector;
    public static ArrayList<ArrayList<ArrayList>> statsBundle;
    private static float dY;
    private static float dX;
    private static String saveString;
    protected static String loadString;
    protected static Context context;
    private static Province infoProv;
    private static Point down = new Point(0,0);
    private static long downtime = 0;
    protected static Map map;
    private static SharedPreferences vars;
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
    public static final Integer[] LOAD_ROUNDS = {R.drawable.loadhussars, R.drawable.loadnuke, R.drawable.loadplan, R.drawable.loaddisplay, R.drawable.loaddoge, R.drawable.loadnevsky, R.drawable.loadwillhelm, R.drawable.loadcurie, R.drawable.loadjoan};
    //public static final String DEBUG_TIMELINE = "kai";
    public static final String debugId = "kai1930";
    public static String[] debugNations;
    public static boolean debugingOn = false; //for just editiing a save
    public static boolean newDebugSave = false; //both true for new saves

    protected static Player[] debugPlayers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //null?
        setContentView(R.layout.activity_game);
        mScaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());
        context = this;
        openInfo = false;
        provEnabled = true;
        openNav = false;
        openSave = false;
        openMode = false;
        firstLoaded = false;
        musicOpen = false;
        switchServerPhase = false;
        breakServerThread = false;
        needConfirm = false;
        lastUnconfirmed = new ArrayList<>(0);
        lastUnconfirmed.add("nnn");
        lastPingId = "nnn";
        initBilling();
        loadWindow();
        new Thread(){
            @Override
            public void run() {
                try { Thread.sleep(500);} catch (InterruptedException e) { e.printStackTrace(); }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        vars = context.getSharedPreferences("vars", 0);
                        timeView = getIntent().getBooleanExtra("timeView", false);
                        ArrayList<Object> history = (ArrayList<Object>)getIntent().getSerializableExtra("historyFiles");
                        if(history != null){
                            timeLine = (String)history.get(0);
                            yearAt = (int)history.get(1);
                        }
                        initializeComponents();
                        Log.i("Create", "1");
                        if(debugingOn) initialDebug();
                        if(!createGame()) return;
                        Log.i("Create", "2");
                        tinter();
                        if(!timeView) {
                            autosave();
                            saveMaker();
                        }else timeView();
                        intitalAchives();
                        drawer();
                        info();
                        modeDropdown();
                        makeAlerts();
                        hiddenGears();
                        Log.i("Create", "made");
                        Log.i("game", "Players: " + getIntent().getIntExtra("players", 1) + " Map: " + getIntent().getIntExtra("mapId", 0));
                        Log.i("dimensions", "W: "+screenWidth+", H: "+screenHeight);
                        Log.i("mapdimensions", "W: "+mapLayout.getMeasuredWidth()+", H: "+mapLayout.getHeight()+", scl: "+ scaling);
                        winCover.setBackgroundResource(R.drawable.wincover);
                        loadText.setVisibility(View.INVISIBLE);
                        //new Event(context, "Test", "test", new String[]{"testbtn"}, R.drawable.dipround, "0");
                    }
                });
            }
        }.start();

    }
    @Override
    public void onDestroy() {
        Log.i("onDestron", "dedGameActivity");
        game.haltAis();
        killMusic();
        if(!forceClosed) {
            if(!timeView)saveGame(AUTO_SAVE_ID);
            breakServerThread = true;
            game = null;
            Intent mStartActivity = new Intent(context, MainActivity.class);
            startActivity(mStartActivity);
        }
        super.onDestroy();
    }
    @Override
    public void onStart(){
        Log.i("GameStart", "start");
        super.onStart();
        onGame = true;
    }
    @Override
    public void onStop(){
        setActivity("none");
        super.onStop();
    }
    @Override
    public void onBackPressed(){
        game.haltAis();
        Intent intent = new Intent(context, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        mScaleGestureDetector.onTouchEvent(motionEvent);
        return true;
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
    public static void changeNationAt(){showNation.setBackgroundResource(game.getCurrentPlayer().getFlag());}
    public static void changeStageIcon(int stage){
        if(stage == -1) stageCover.setBackgroundResource(R.drawable.stageset);
        if(stage == 0) stageCover.setBackgroundResource(R.drawable.stagerein);
        if(stage == 1) stageCover.setBackgroundResource(R.drawable.stageattack);
        if(stage == 2) stageCover.setBackgroundResource(R.drawable.stagetrans);
    }
    public static void resetHost(){
        switchServerPhase = true;
        if(server != null)
            server.reset();
        server = null;
    }
    public static void createHost(String serverIp, final boolean isHost){
        final ArrayList<String> hostValue = new ArrayList<>(0);
        if(isHost) hostValue.add("host");
        else hostValue.add("client");
        if(server == null) server = new Server(serverIp, isHost);
        switchServerPhase = false;
        connectPhase = true;
        gameplayPlase = false;
        resendCycles = 0;
        /*new Thread(){
            @Override
            public void run() {
                Looper.prepare();
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(200);
                        if(breakServerThread) break;
                        if(connectPhase) {
                            Log.i("HaltThread", "" + switchServerPhase);
                            if (switchServerPhase) {
                                //haltServerThread = false;
                                Log.i("HaltThread", "In:" + switchServerPhase);
                                connectPhase = false;
                            }
                            Log.i("Waiting", "Connecting...");
                            ArrayList<String> data = new ArrayList(0);
                            try {
                                data = (ArrayList) server.getData(true);
                            } catch (ClassCastException e) { e.printStackTrace(); }
                            if (data != null) {
                                Log.i("ServerData", hostValue.toString() + ", " + data.toString());
                                ArrayList<String> tmp = hostValue;
                                if(tmp.get(0).charAt(tmp.get(0).length()-1) != '1')
                                    tmp.set(0, tmp.get(0)+1);
                                server.sendData(tmp);
                                if (data.get(0).equals(hostValue.get(0) + 1)) {
                                    resetHost();
                                }
                                if (data.get(0).charAt(data.get(0).length() - 1) == '1') {
                                    serverPing();
                                }
                            } else
                                server.sendData(hostValue);
                        }else{
                            server.sendData((ArrayList)null);
                        }
                        if(gameplayPlase){
                            if(game != null)
                                if(game.outgoing != null && game.getCurrentPlayer() != null) {
                                    try {
                                        ArrayList<String> testIncoming = (ArrayList<String>) server.getData(false);
                                        if(testIncoming != null) {
                                            if (testIncoming.size() > 0) {
                                                Log.i("IncomtinTest", testIncoming.toString() + ", " + lastUnconfirmed.toString());
                                                game.incoming = testIncoming;
                                                lastPingId = testIncoming.get(0);
                                                game.parseIncoming();
                                            /&if(!testIncoming.get(testIncoming.size()-1).equals("c")){
                                                game.incoming = testIncoming;
                                                lastPingId = testIncoming.get(0);
                                                game.parseIncoming();
                                                testIncoming.add("c");
                                                Log.i("Data Confirmed", "Sending confirmation: "+testIncoming.toString());
                                                server.sendData(testIncoming);
                                                continue;
                                            }else if(testIncoming.get(0).equals(lastUnconfirmed.get(0)))
                                                needConfirm = false;&/
                                            }
                                        }

                                        Log.i("Outgoing", ""+game.outgoing.size()+", "+needConfirm+", "+game.outgoing.toString());
                                        if(resendCycles <= 5){
                                            resendCycles ++;
                                            Log.i("SendGameData", lastUnconfirmed.toString());
                                            server.sendData(lastUnconfirmed);
                                        }else {
                                            resendCycles = 0;
                                            lastUnconfirmed = game.outgoing;
                                            game.flushOutgoing();
                                        }
                                        /*if (lastUnconfirmed.size() > 1 && !needConfirm) {


                                            //needConfirm = true;
                                            //game.flushOutgoing();
                                        }*//*else if(needConfirm){
                                            Log.i("Data Unconfirmed", "resending: "+lastUnconfirmed.toString());
                                            server.sendData(lastUnconfirmed);
                                        }&/
                                    }catch (ClassCastException e){e.printStackTrace();}
                                }
                        }
                    }
                    Log.i("HaltThread", "In2:"+ switchServerPhase);
                }catch (InterruptedException e){e.printStackTrace();}
            }
        }.start();*/
    }
    public static void serverPing(){
        connectPhase = false;
        /*new Thread(){
            @Override
            public void run() {
                try{
                    while (!isInterrupted()){
                        Thread.sleep(500);
                        if(game != null)
                            if(game.outgoing != null) {
                                try {

                                    ArrayList<String> testIncoming = new ArrayList<>(0);
                                    try {
                                        testIncoming = (ArrayList<String>) server.getData();
                                    }catch (ClassCastException e){
                                        e.printStackTrace();
                                        game.outgoing = new ArrayList<>(0);
                                        game.outgoing.add("nnn"); game.outgoing.add("nnnnnnnnnnn");
                                        server.sendData(game.outgoing);
                                        game.flushOutgoing();
                                    }
                                    if(testIncoming.size() > 0) {
                                        Log.i("IncomtinTest", testIncoming.toString()+", "+lastUnconfirmed.toString());
                                        if(!testIncoming.get(testIncoming.size()-1).equals("c")){
                                            game.incoming = testIncoming;
                                            lastPingId = testIncoming.get(0);
                                            game.parseIncoming();
                                            testIncoming.add("c");
                                            Log.i("Data Confirmed", "Sending confirmation: "+testIncoming.toString());
                                            server.sendData(testIncoming);
                                            continue;
                                        }else if(testIncoming.get(0).equals(lastUnconfirmed.get(0)))
                                            needConfirm = false;
                                    }

                                    Log.i("Outgoing", ""+game.outgoing.size()+", "+needConfirm+", "+game.outgoing.toString());
                                    if (game.outgoing.size() > 1 && !needConfirm) {
                                        Log.i("SendGameData", game.outgoing.toString());
                                        server.sendData(game.outgoing);
                                        lastUnconfirmed = game.outgoing;
                                        needConfirm = true;
                                        game.flushOutgoing();
                                    }else if(needConfirm){
                                        Log.i("Data Unconfirmed", "resending: "+lastUnconfirmed.toString());
                                        server.sendData(lastUnconfirmed);
                                    }
                                }catch (ClassCastException e){e.printStackTrace();}
                            }
                    }
                }catch (InterruptedException e){e.printStackTrace();}
            }
        }.start();*/
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
    private void initializeComponents(){
        //background
        ImageView activityRound = findViewById(R.id.gameRound);
        activityRound.setScaleType(ImageView.ScaleType.FIT_XY);
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
        //info
        info = findViewById(R.id.info);
        developer = findViewById(R.id.developer);
        closeInfo = findViewById(R.id.closeInfo);
        infoText = findViewById(R.id.infoText);
        infoText.setTextSize(TypedValue.COMPLEX_UNIT_IN,BASE_TEXT_SCALE*inchWidth);
        fortifier = findViewById(R.id.fortifier);
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
        //time view
        timeSlider = findViewById(R.id.timeSlider);
        timeCover = findViewById(R.id.timeCover);
        year = findViewById(R.id.year);
        showNation = findViewById(R.id.showNation);
        showNationFrame = findViewById(R.id.showNationFrame);
        showNationFrame.setBackgroundResource(R.drawable.flagframe);
        yearTitle = findViewById(R.id.yearTitle);
        playerInfo = findViewById(R.id.playerInfo);
        jumpTo = findViewById(R.id.jumpTo);
        nationFlag = findViewById(R.id.nationFlag);
        nationFlag.setVisibility(View.INVISIBLE);
        flags = findViewById(R.id.flagLayout);
        selections = findViewById(R.id.selectLayout);
        toBuild = findViewById(R.id.toBuild);
        //Achives
        achiveDrop = findViewById(R.id.achiveDropdown);
        achiveImage = findViewById(R.id.achiveImage);
        achiveTitle = findViewById(R.id.achiveText);
        toAchives = findViewById(R.id.toAchive);
        achiveTitle.setTextSize(TypedValue.COMPLEX_UNIT_IN,BASE_TEXT_SCALE*inchWidth);
        achiveTitle.setTextColor(Color.BLACK);
        //misc
        //winLayout = findViewById(R.id.winLayout);
        winnerFlag = findViewById(R.id.winnerFlag);
        masterLayout = findViewById(R.id.gameLayout);
        mapLayout = findViewById(R.id.map);
        //mapLayout.animate().x(screenHeight/4).y(screenWidth/4).setDuration(2000);
        mapLayout.setScaleX(scaling);
        mapLayout.setScaleY(scaling);
        unlock();
        musicControls();
        makeDiplo();
        makeDipPopup();
        initAtWar();
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
                //}else unlockPopup();
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
        if(game.isHistorical() && !timeView) {
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

    public static Game getGame(){return game;}
    public static boolean getInfoOpen(){ return openInfo; }
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
    public static RelativeLayout getMapLayout(){return mapLayout;}
    public static ConstraintLayout getMasterLayout(){return masterLayout;}
    public static Province getInfoProv(){return infoProv;}
    public static ImageView getMapImage(){return mapImage;}
    //public static ConstraintLayout getAchiveDrop(){return achiveDrop;}

    public static void setInfoProv(Province prov){infoProv = prov;}
    public static void setPlayerInfo(String set){playerInfo.setText(set);}
    public static void setJumpText(String set){jumpTo.setText(set);}
    public static void setWinFlag(Player winner){winnerFlag.setBackgroundResource(winner.getFlag());}
    @SuppressLint("ClickableViewAccessibility")
    protected static void touched() {
        mapLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                //Log.i("Prov0", ""+game.getMap().getList()[0].getImage().getWidth()+", "+game.getMap().getList()[0].getImage().getMeasuredWidth());
                //Log.i("mspDims", "W:"+mapLayout.getWidth()+", H:"+mapLayout.getHeight());
                if (event.getPointerCount() == 1) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            dX = view.getX() - event.getRawX();
                            dY = view.getY() - event.getRawY();
                            down = new Point((int)event.getRawX(), (int)event.getRawY());
                            downtime = System.currentTimeMillis();
                            //Log.i("Touch Coords", "X: " + (event.getRawX() + dX) + ", Y: " + (event.getRawY() + dY));

                            //Log.i("Down Coords", "X: " + event.getX() + ", Y: " + event.getY() + ", downtime: " + downtime);
                            //Log.i("scaling", "Factor: "+scaling+", currentW: "+view.getWidth()+", Orgiina: "+610);
                            break;
                        case MotionEvent.ACTION_UP:
                            //Log.i("Up Coords", "X: " + event.getRawX() + ", Y: " + event.getRawY() + ", uptime: " + System.currentTimeMillis());
                            try {
                                if (Math.abs(down.x - event.getRawX()) < 30 && Math.abs(down.y - event.getRawY()) < 30) {
                                    ArrayList<Province> choices = new ArrayList<>(0);
                                    for (int i = 0; i < map.getList().length; i++) {
                                        Province at = map.getList()[i];
                                        Bitmap overlay = at.getOverlay();
                                        //Log.i("touchprov", ""+at.getX()+","+overlay.getWidth()+","+event.getX());
                                        if (event.getX() > at.getX() && event.getX() < at.getX() + overlay.getWidth() * map.getOverScale()
                                                && event.getY() > at.getY() && event.getY() < at.getY() + overlay.getHeight() * map.getOverScale()) {
                                            try {
                                                if (Color.alpha(overlay.getPixel((int) ((event.getX() - at.getX()) / map.getOverScale()), (int) ((event.getY() - at.getY()) / map.getOverScale()))) > 10)
                                                    choices.add(at);
                                            } catch (IllegalArgumentException e) {
                                                e.printStackTrace();
                                                Log.i("BitTouch",
                                                        "X: " + (event.getX() - at.getX()) + "Width:" + overlay.getWidth()
                                                                + "Y: " + (event.getY() - at.getY()) + "Height:" + overlay.getHeight());
                                            }
                                        }
                                    }
                                    int minDist = Integer.MAX_VALUE;
                                    Province touched = null;
                                    if (choices.size() == 1) touched = choices.get(0);
                                    else if (choices.size() > 1)
                                        for (int i = 0; i < choices.size(); i++)
                                            if (Math.abs(event.getX() - choices.get(i).getX()) < minDist) {
                                                minDist = (int) Math.abs(event.getX() - choices.get(i).getX());
                                                touched = choices.get(i);
                                            }
                                    Log.i("DiploToiuch", ""+game.getMapMode());
                                    if (System.currentTimeMillis() - downtime > 300 && touched != null)
                                        touched.doLongClick();
                                    else if (touched != null) {
                                        if(provEnabled) touched.doClick();
                                        if(touched.getOwnerId() != -1) {
                                            game.setFocusPlayer(touched.getOwner());
                                            Log.i("Focsd", "" + (game.getFocusPlayer() == null));
                                            if (game.getMapMode() == 8) game.updateAllOverlays();
                                            if (timeView) {
                                                Player owner = touched.getOwner();
                                                String playerText = owner.getName() + "\nLegions: " + (owner.getFreeTroops()) +
                                                        "\nDevelopment: " + owner.totalIncome()
                                                        + "\nOperations Efficiency: " + owner.getOpsEfficiency()
                                                        + "\nLegion Hardening: " + owner.getTroopHardening();
                                                nationFlag.setBackgroundResource(owner.getFlag());
                                                nationAt = owner.getNation();
                                                playerInfo.setText(playerText);
                                            } else {
                                                playerInfo.setText("Natives, Barbarians, and the like");
                                                nationFlag.setBackgroundResource(R.drawable.noflag);
                                                nationAt = null;
                                            }
                                        }
                                    }
                                }
                            }catch (Exception e){e.printStackTrace();}
                            break;
                        case MotionEvent.ACTION_MOVE:
                            view.animate().x(event.getRawX() + dX).y(event.getRawY() + dY).setDuration(0).start();
                            //Log.i("Touch Coords", "X: " + event.getRawX() + dX + ", Y: " + event.getRawY() + dY);
                            //Log.i("Map Coords", "X: " + mapLayout.getX() + ", Y: " + mapLayout.getY());
                            break;
                        default: return false;
                    }
                } else return false;
                return true;
            }
        });
    }
    @SuppressLint("ClickableViewAccessibility")
    private void hiddenGears(){
        ImageView gearCover = findViewById(R.id.gameGearCover);
        final ImageView gear1 = findViewById(R.id.gameGear1);
        final ImageView gear2 = findViewById(R.id.gameGear2);
        final ImageView gear3 = findViewById(R.id.gameGear3);
        final AnimationSet animSet1 = new AnimationSet(true);
        //animSet1.setInterpolator(new DecelerateInterpolator());
        animSet1.setFillAfter(true);animSet1.setFillEnabled(true);
        final AnimationSet animSet2 = new AnimationSet(true);
        //animSet2.setInterpolator(new DecelerateInterpolator());
        animSet2.setFillAfter(true);animSet2.setFillEnabled(true);
        final AnimationSet animSet3 = new AnimationSet(true);
        //animSet3.setInterpolator(new DecelerateInterpolator());
        animSet3.setFillAfter(true);animSet3.setFillEnabled(true);
        gear1.setVisibility(YEET);
        gear2.setVisibility(YEET);
        gear3.setVisibility(YEET);
        final float xDiff = screenWidth/4;
        gearCover.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getPointerCount() == 1) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            dXGear = view.getX() - event.getRawX();
                            initX = view.getX();
                            Log.i("InitX", ""+initX);
                            //down = new Point((int)event.getRawX(), (int)event.getRawY());
                            break;
                        case MotionEvent.ACTION_UP:
                            if(Math.pow(event.getRawX() + dXGear, .85) <= xDiff) view.animate().x(initX).setDuration(200);
                            break;
                        case MotionEvent.ACTION_MOVE:
                            Log.i("Movement", "raw: "+event.getRawX()+", changeX: "+dXGear+", powe: "+Math.pow(event.getRawX() + dXGear, .85)+", diff: "+xDiff);
                            view.animate().x((float) Math.pow(event.getRawX() + dXGear, .85)+initX).setDuration(0);
                            if(Math.pow(event.getRawX() + dXGear, .85) > xDiff){
                                view.animate().x(5000).setDuration(500);
                                gear1.setVisibility(View.VISIBLE);
                                gear2.setVisibility(View.VISIBLE);
                                gear3.setVisibility(View.VISIBLE);
                                if(spinCycle == null){
                                    Log.i("Spinsucle", "create");
                                    final float[] decreace = {2};
                                    spinCycle = new Thread() {
                                        @Override
                                        public void run() {
                                            try {
                                                while (!isInterrupted()) {
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            decreace[0] /= 2;
                                                            //maybe 3 of theese instead
                                                            final RotateAnimation animRotate1 = new RotateAnimation(0, 360*decreace[0], RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                                                                    RotateAnimation.RELATIVE_TO_SELF, 0.5f);
                                                            final RotateAnimation animRotate2 = new RotateAnimation(0, 360*decreace[0], RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                                                                    RotateAnimation.RELATIVE_TO_SELF, 0.5f);
                                                            final RotateAnimation animRotate3 = new RotateAnimation(0, 360*decreace[0], RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                                                                    RotateAnimation.RELATIVE_TO_SELF, 0.5f);
                                                            animRotate1.setDuration(5000);animRotate1.setFillAfter(true);
                                                            animRotate2.setDuration(5000);animRotate2.setFillAfter(true);
                                                            animRotate3.setDuration(5000);animRotate3.setFillAfter(true);
                                                            animSet1.addAnimation(animRotate1);
                                                            gear2.startAnimation(animSet1);
                                                            animSet2.addAnimation(animRotate2);
                                                            gear1.startAnimation(animSet2);
                                                            animSet3.addAnimation(animRotate3);
                                                            gear3.startAnimation(animSet3);
                                                        }
                                                    });
                                                    Thread.sleep(5000);
                                                }
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    };spinCycle.start();
                                }
                            }
                            break;
                        default: return false;
                    }
                } else return false;
                return true;
            }
        });
    }

    private void modeDropdown(){
        final ImageButton modeDropper = findViewById(R.id.modeDropper);
        final ImageView holderHider = findViewById(R.id.holderHider);
        modeDropper.setBackgroundResource(R.drawable.openmodes);
        final LinearLayout masterHolder = findViewById(R.id.masterMapHolder);
        final ImageButton[] modes;
        final int modesLength;
        final int totalModes = 9;
        modes = new ImageButton[totalModes];
        modes[0] = findViewById(R.id.geoMode);
        modes[1] = findViewById(R.id.polMode);
        modes[2] = findViewById(R.id.conMode);
        modes[3] = findViewById(R.id.devMode);
        modes[4] = findViewById(R.id.attMode);
        modes[5] = findViewById(R.id.brnMode);
        modes[6] = findViewById(R.id.frtMode);
        modes[7] = findViewById(R.id.mightMode);
        modes[8] = findViewById(R.id.diploMode);

        modes[0].setBackgroundResource(R.drawable.geomode);
        modes[1].setBackgroundResource(R.drawable.polmode);
        modes[7].setBackgroundResource(R.drawable.mitmode);
        if(game.getImperium()){
            modes[2].setVisibility(YEET);
            modes[3].setBackgroundResource(R.drawable.devmode);
            modes[4].setBackgroundResource(R.drawable.attmode);
            modes[5].setBackgroundResource(R.drawable.brnmode);
            modes[6].setBackgroundResource(R.drawable.frtmode);
            if(game.isHistorical()){
                modes[8].setBackgroundResource(R.drawable.dipmode);
                modesLength = 7;
            }
            else{
                modes[8].setVisibility(YEET);
                modesLength = 6;
            }

        }
        else{
            modesLength = 3;
            modes[3].setVisibility(YEET);
            modes[4].setVisibility(YEET);
            modes[5].setVisibility(YEET);
            modes[6].setVisibility(YEET);
            modes[8].setVisibility(YEET);
            modes[2].setBackgroundResource(R.drawable.conmode);
            Log.i("removal", "removed");
        }
        masterHolder.setLayoutParams(new ConstraintLayout.LayoutParams((int)(screenHeight*.45), (int)(screenWidth*.1*modesLength)));
        holderHider.setMaxWidth(modes[0].getWidth()/4);
        masterHolder.setY(-1000);
        modeDropper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(openMode){
                    modeDropper.setBackgroundResource(R.drawable.openmodes);
                    masterHolder.animate().y(-masterHolder.getHeight()).setDuration(500).start();
                }
                else{
                    masterHolder.setX((float)(screenHeight*.35));
                    masterHolder.animate().y(0).setDuration(500).start();
                    modeDropper.setBackgroundResource(R.drawable.closemodes);
                    for(int i=0; i<modes.length; i++){
                        if(game.getMapMode() != i) modes[i].animate().x(-3 * modes[game.getMapMode()].getWidth() / 4);
                    }
                }
                openMode = !openMode;
            }
        });
        for(int i=0; i<modes.length; i++){
            final int index = i;
            modes[i].setLayoutParams(new LinearLayout.LayoutParams((int)(screenHeight*.45*.75), ViewGroup.LayoutParams.MATCH_PARENT, (float)(1.0/modesLength)));

            modes[i].setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {
                if(game.getMapMode() != index) {
                    modes[index].animate().x(0).setDuration(200).start();
                    modes[game.getMapMode()].animate().x(-3 * modes[game.getMapMode()].getWidth() / 4).setDuration(200).start();
                    game.updateMapMode(index);
                }
                Log.i("mode", "mapMode: "+index);
            }});
        }

    }
    private void makeAlerts(){
        alertLayout = findViewById(R.id.alertHolder);
        alerts = new ArrayList<>(0);
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
    private ColorMatrixColorFilter grayScale(){
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        return new ColorMatrixColorFilter(matrix);
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
        Log.i("gflaf", tag+game.playerFromTag(tag).getName());
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
        if(plater.isTruce(tag)) textDiplo.setText("Relation to us: \nWe have a truce which expires in "
                +(-plater.getTruceEnd(tag)+(int)(game.getTurnNum()/(double)game.getPlayerList().length))+" turns");
        else if(plater.isAllied(tag)) textDiplo.setText("Relation to us: \nWe are allies");
        else if(plater.hasSubject(tag)) textDiplo.setText("Relation to us: \nThey are our subject");
        else if(plater.isHostile(tag)) textDiplo.setText("Relation to us: \nWe are at war!");
        else if(plater.hasOverlord()) if(getGame().playerFromTag(plater.getOverlord()).isAllied(tag)) textDiplo.setText("Relation to us: \nThey are a subject of our ally");
        else textDiplo.setText("Relation to us: \nThey are neutral");
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
        game.addOutgoing(game.getCurrentPlayer().getTag(), "aly", tag, "nnnn");
        game.getPlayerList()[game.playerIdFromTag(tag)].addRequestFrom(1, "000000", game.getCurrentPlayer().getTag());
    }
    protected void makeSubject(String tag){
        game.addOutgoing(game.getCurrentPlayer().getTag(), "sub", tag, "nnnn");
        game.getPlayerList()[game.playerIdFromTag(tag)].addRequestFrom(2, "000000", game.getCurrentPlayer().getTag());
        for(String s : game.playerFromTag(tag).getDiplo()[1]){
            game.playerFromTag(tag).removeAlly(s);
        }
    }
    protected void declareWar(String tag){
        game.addOutgoing(game.getCurrentPlayer().getTag(), "war", tag, "nnnn");
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

        }else if(type == 40 && !game.getCurrentPlayer().getRecentWars().contains(group)){
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
        if(timeView) portalHolder.setVisibility(View.INVISIBLE);
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
                if (!timeView) portalHolder.setVisibility(View.VISIBLE);

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
                if(!timeView) portalHolder.setVisibility(View.VISIBLE);
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
    public void showInfo(Province prov){
        Log.i("showInfo", "open: " + openInfo + "from " + prov.getName());
        infoProv = prov;
        if(prov.getOwner() != null) {
            int[] relations = game.getCurrentPlayer().getRelations(prov.getOwner().getTag());
            ownerFlag.setBackgroundResource(prov.getOwner().getFlag());
            if(!prov.getCore().equals(prov.getOwner().getTag()) && !prov.getCore().equals("#nn")) {
                coreFlag.setBackgroundResource(game.playerFromTag(prov.getCore()).getFlag());
                coreFlag.setVisibility(View.VISIBLE);
            }else
                coreFlag.setVisibility(View.INVISIBLE);
            if(relations[1] == 1) relation.setBackgroundResource(R.drawable.ally);
            else if(relations[2] == 1) relation.setBackgroundResource(R.drawable.sunject);
            else if(relations[3] == 1) relation.setBackgroundResource(R.drawable.war);
            else relation.setBackgroundResource(R.drawable.blank);
        }
        updateInfo();
        if(!openInfo){
            info.animate().x(0).setDuration(500).start();
            info.setVisibility(View.VISIBLE);
        }
        if(prov.getOwnerId() == game.getCurrentPlayer().getId() && !timeView)
            revealProvMods();
        else hideProvMods();
        openInfo = true;
    }
    public void showYearInfo(){
        infoText.setText(yearInfo);
        ownerFlag.setVisibility(View.INVISIBLE);
        devasIcon.setVisibility(View.INVISIBLE);
        attriIcon.setVisibility(View.INVISIBLE);
        devlIcon.setVisibility(View.INVISIBLE);
        if(!openInfo){
            info.setVisibility(View.VISIBLE);
            info.animate().x(0).setDuration(500).start();
            coreFlag.setVisibility(View.INVISIBLE);
        }
        hideProvMods();
        openInfo = true;
    }
    public void updateInfo(){
        String infoStr = "";
        ownerFlag.setVisibility(View.VISIBLE);
        devasIcon.setVisibility(View.VISIBLE);
        attriIcon.setVisibility(View.VISIBLE);
        devlIcon.setVisibility(View.VISIBLE);
        if(!game.getImperium()) {
            infoStr += "Province: " + infoProv.getName() + "\nTroops: " + (int) infoProv.getTroops();
            infoStr += "\n\nContinent: " + infoProv.getContinent().getName() + "\nBonus: " + infoProv.getContinent().getBonus();
            if(infoProv.getOwnerId() != -1) infoStr += "\nOwner: "+infoProv.getOwner().getName();
            else infoStr += "\nOwner: Unorganized Tribes";
        }else{
            //infoStr += "Id: "+infoProv.getId()+"\n";
            infoStr += "Province: " + infoProv.getName() + "\nTroops: " + (int)(infoProv.getTroops()*10)/10.0+"k";
            if(infoProv.getOwnerId() != -1) infoStr += "\nOwner: "+infoProv.getOwner().getName();
            else infoStr += "\nOwner: Unorganized Tribes";
            if (infoProv.modDevastation(0) < 0){
                davasText.setText(""+(-(int) (10 * infoProv.modDevastation(0)) / 10.0));
                devasIcon.setBackgroundResource(R.drawable.prospericon); //the image
            }
            else{
                davasText.setText(""+(int) (10 * infoProv.modDevastation(0)) / 10.0);
               devasIcon.setBackgroundResource(R.drawable.devastateicon); //the imcge
            }
            devlText.setText(""+(int) (10 * infoProv.modDevelopment(0)) / 10.0);
            attriText.setText(""+(int)(1-infoProv.getAttrition()*10)/10.0);
            infoStr += "\nFort Level: " + infoProv.getFortLevel();
        }
        infoStr += "\nThreatened By: " + Map.howSurrounded(infoProv) + " legion(s)";
        infoStr += "\nOccupants: "+  infoProv.stacksToString();
        infoText.setText(infoStr);
    }
    public void revealProvMods(){
        fortifier.setVisibility(View.VISIBLE);
        developer.setVisibility(View.VISIBLE);
    }
    public void hideProvMods(){
        fortifier.setVisibility(View.INVISIBLE);
        developer.setVisibility(View.INVISIBLE);
    }
    private void info() {
        closeInfo.setBackgroundResource(R.drawable.closenav);
        developer.setBackgroundResource(R.drawable.developer);
        fortifier.setBackgroundResource(R.drawable.builder);
        ownerFlag = findViewById(R.id.ownerFlag);
        coreFlag = findViewById(R.id.coreFlag);
        devasIcon = findViewById(R.id.dvaIcon);
        devlIcon = findViewById(R.id.dvpIcon);
        attriIcon = findViewById(R.id.attIcon);
        devlText = findViewById(R.id.dvpText);
        davasText = findViewById(R.id.dvaText);
        attriText = findViewById(R.id.attText);
        relation = findViewById(R.id.relation);
        devlText.setTextSize(TypedValue.COMPLEX_UNIT_IN, (float) (.7*BASE_TEXT_SCALE*inchWidth));
        davasText.setTextSize(TypedValue.COMPLEX_UNIT_IN,(float) (.7*BASE_TEXT_SCALE*inchWidth));
        attriText.setTextSize(TypedValue.COMPLEX_UNIT_IN,(float) (.7*BASE_TEXT_SCALE*inchWidth));
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
                            infoProv.getOwner().getId() != game.getCurrentPlayer().getId() && game.isHistorical() && !timeView)
                        openDiplo(infoProv.getOwner().getTag());
                }
            });
        closeInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                info.animate().x(-1000).setDuration(500).start();
                openInfo = false;
                Log.i("info", "open: " + openInfo);
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
        game.addOutgoing(game.getCurrentPlayer().getTag(), "dev", "#nn", ""+formatInt(prov.getId(), 4));
        game.getCurrentPlayer().modMonetae(-10);
        prov.modDevelopment(Math.exp(-prov.modDevelopment(0)/15));
        updateInfo();
    }
    public void fortifier(Province prov){
        game.addOutgoing(game.getCurrentPlayer().getTag(), "frt", "#nn", ""+formatInt(prov.getId(), 4));
        prov.fortify();
        updateInfo();
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

    private void initialDebug(){
        Button removePlayer = findViewById(R.id.ownerNull);
        if(!timeView) jumpTo.setVisibility(View.VISIBLE);
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
    private void initialTimeFiles(){
        byte[] lineFile = new byte[0];
        mapPath = game.getMap().getMapFilePath();
        timeLine = getIntent().getStringExtra("timeline");
        try {
            InputStream stream = getAssets().open("sacredTexts/timeLines/"+mapPath+timeLine+"titles.txt");
            int size = stream.available();
            lineFile = new byte[size];
            stream.read(lineFile);
            stream.close();
        } catch (IOException e) { e.printStackTrace(); }
        String yearString = new String(lineFile);
        ArrayList<Integer> yearList = new ArrayList<>(0);
        ArrayList<String> titleList = new ArrayList<>(0);
        for(int i=0; i<yearString.length(); i++) {
            if (yearString.charAt(i) == '#' && i<yearString.length()-1 && i<yearString.indexOf("\"")-1) {
                yearList.add(Integer.parseInt(yearString.substring(i+1, i + 6)));
                titleList.add(yearString.substring(i + 6, yearString.indexOf('#', i + 1)));
            }
        }
        years = yearList.toArray(new Integer[0]);
        for(int i : years) Log.i("yearPring", ""+i);
        titles = titleList.toArray(new String[0]);
    }
    private void timeFile(){
        byte[] mapFile = new byte[0];
        String mapPath = game.getMap().getMapFilePath();
        String timeline = getIntent().getStringExtra("timeline");
        try {
            InputStream stream = getAssets().open("sacredTexts/timeLines/"+mapPath+timeline+yearAt+".imprm");
            int size = stream.available();
            mapFile = new byte[size];
            stream.read(mapFile);
            stream.close();

        }catch (Exception e){e.printStackTrace();}
        loadString = new String(mapFile);
        yearInfo = loadString.substring(loadString.indexOf("\"")+1);
        int mapMode = game.getMapMode();
        game.inSetup = true;
        loadBuilder();
        game.loadOwnerFromTag();
        game.playerTitles();
        game.updateMapMode(mapMode);
        game.inSetup = false;
        Log.i("OutSetup", "out5");
        firstLoaded = true;
        Log.i("Time Files", "Done");
    }
    private void timeView(){
        historicalSave = true;
        initialTimeFiles();
        findViewById(R.id.timeProgress).setVisibility(View.VISIBLE);
        nationFlag.setVisibility(View.VISIBLE);
        showNation.setVisibility(View.INVISIBLE);
        showNationFrame.setVisibility(View.INVISIBLE);
        handle.setVisibility(View.INVISIBLE);
        change.setVisibility(View.INVISIBLE);
        statusCover.setVisibility(View.INVISIBLE);
        status.setVisibility(View.INVISIBLE);
        provEnabled = false;
        toBuild.setVisibility(View.VISIBLE);
        toBuild.setBackgroundResource(R.drawable.tocreate);
        timeSlider.setVisibility(View.VISIBLE);
        timeCover.setVisibility(View.VISIBLE);
        year.setVisibility(View.VISIBLE);
        yearTitle.setVisibility(View.VISIBLE);
        playerInfo.setVisibility(View.VISIBLE);
        ImageView jumpPast = findViewById(R.id.jumpPast);
        ImageView jumpFuture = findViewById(R.id.jumpFuture);
        final ImageView timeThumb = findViewById(R.id.timeThumb);
        final ImageView yearInfo = findViewById(R.id.yearInfo);
        yearInfo.setBackgroundResource(R.drawable.info);
        yearInfo.setVisibility(View.VISIBLE);
        jumpPast.setVisibility(View.VISIBLE);
        jumpFuture.setVisibility(View.VISIBLE);
        jumpPast.setBackgroundResource(R.drawable.past);
        jumpFuture.setBackgroundResource(R.drawable.future);
        jumpPast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpPast();
            }
        });
        jumpFuture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpFuture();
            }
        });
        yearInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showYearInfo();
            }
        });
        timeThumb.setVisibility(View.VISIBLE);
        timeThumb.animate().x(screenHeight/4);
        timeSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                timeProgress = progress;
                timeThumb.animate().x(timeSlider.getX()+progress/110f*timeSlider.getWidth()).setDuration(0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.i("Stop", "seekstop");
                int[] out = snapTo(timeProgress);
                //timeSlider.setProgress(out[0]);
                yearAt = out[1];
                if(!unlockedYears.contains("modern") && yearAt > 1800 && timeLine.equals("alp")) locked.setVisibility(View.VISIBLE);
                else if(!unlockedYears.contains("althist") && !timeLine.equals("alp")) locked.setVisibility(View.VISIBLE);
                else locked.setVisibility(View.INVISIBLE);
                year.setText("Year: "+yearAt);
                yearTitle.setText(titles[out[2]]);
                timeFile();
            }
        });
        int[] out = snapTo(timeProgress);
        timeSlider.setProgress(out[0]);
        yearAt = out[1];
        if(!unlockedYears.contains("modern") && yearAt > 1800 && timeLine.equals("alp")) locked.setVisibility(View.VISIBLE);
        else if(!unlockedYears.contains("althist") && !timeLine.equals("alp")) locked.setVisibility(View.VISIBLE);
        else locked.setVisibility(View.INVISIBLE);
        year.setText("Year: "+yearAt);
        yearTitle.setText(titles[out[2]]);
        timeFile();
        timeFile();

        toBuild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean send = false;
                for(String s : nations) if(s != null) if(s != "") send = true;

                Intent intent = new Intent(context, BuildActivity.class);
                ArrayList<Object> history = new ArrayList<Object>(0);
                history.add(timeLine);history.add(yearAt);history.add(mapPath);history.add(nations);
                if(send) intent.putExtra("historyFiles", history);
                forceClosed = true;
                startActivity(intent);
                finish();
            }
        });
        addSelections();
    }
    private void addSelections(){
        findViewById(R.id.timeCover).setBackgroundResource(R.drawable.timecover);
        findViewById(R.id.nationCover).setBackgroundResource(R.drawable.nationcover);
        nations = new String[4];
        for(String s : nations) s = "";
        players = new ImageButton[4];
        selects = new ImageButton[4];
        for(int i=0; i<selects.length; i++) {
            selects[i] = new ImageButton(context);
            selects[i].setBackgroundResource(R.drawable.remove);
            selects[i].setOnClickListener(unSelector(i));
            selections.addView(selects[i]);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            params.weight = .7f;
            selects[i].setLayoutParams(params);
        }
        for(int i=0; i<players.length; i++){
            players[i] = new ImageButton(context);
            players[i].setImageResource(R.drawable.noflag);
            players[i].setBackgroundResource(R.drawable.blank);
            players[i].setPadding(0, 0, 1, 0);
            players[i].setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            players[i].setOnClickListener(flager(i));
            flags.addView(players[i]);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            params.weight = .7f;
            players[i].setLayoutParams(params);
        }
    }
    private void resetNationSelect(){
        try {
            if(players != null)
                for (ImageButton b : players) b.setImageResource(R.drawable.noflag);
            if(nations != null)
                 for (int i=0; i<nations.length; i++) nations[i] = "";
        }catch(NullPointerException e){e.printStackTrace();}
    }
    private View.OnClickListener flager(final int id){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if(!LOCKED || yearAt == DEFAULT_YEAR_ALP) {
                    boolean already = false;
                    if (nationAt != null) {
                        for (String at : nations)
                            if (at != null)
                                if (at.equals(nationAt.getTag())) {
                                    already = true;
                                    Toast.makeText(context, "There is already a player with this nation, try again or unselect", Toast.LENGTH_SHORT).show();
                                }

                        if (!already) {
                            nations[id] = nationAt.getTag();
                            Log.i("Nation", "" + nations[id]);
                            //flagchange
                            players[id].setImageResource(nationAt.getFlag());
                        }
                    }
                //}
            }};
    }
    private View.OnClickListener unSelector(final int id){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nations[id] = null;
                players[id].setImageResource(R.drawable.noflag);
                /*if(id == 0) players[0].setBackgroundResource(R.drawable.blue);
                if(id == 1) players[1].setBackgroundResource(R.drawable.red);
                if(id == 2) players[2].setBackgroundResource(R.drawable.green);
                if(id == 3) players[3].setBackgroundResource(R.drawable.purple);*/
            }};
    }
    private void unlock(){
        unlockPop = findViewById(R.id.unlockPop);
        unlockClose = findViewById(R.id.unlockClose);
        locked = findViewById(R.id.locked);
        unlockedYears = new ArrayList<>(0);
        FileInputStream fis = null;
        final StringBuilder sb = new StringBuilder();
        try {
            fis = new FileInputStream(Environment.getExternalStorageDirectory().getPath()+"/Imperium/Game/State.txt");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String text;
            while ((text = br.readLine()) != null)
                sb.append(text).append("\n");

        } catch (Exception e) { e.printStackTrace(); }
        finally {
            if (fis != null)
                try { fis.close(); } catch (IOException e) { e.printStackTrace(); }
        }
        String years = "";
        try{years = sb.substring(sb.indexOf("[")+1, sb.indexOf("]"));}
        catch (StringIndexOutOfBoundsException e) {
            e.printStackTrace();
            years = "modern,althist,";
        }
        for(int i=0; i<years.length()-3; i++){
            unlockedYears.add(years.substring(i, years.indexOf(",", i)));
            i = years.indexOf(",", i);
        }
        Log.i("unlockedYears", unlockedYears.toString());
        ImageView playerLock = findViewById(R.id.playerLock);
        TextView unlockText = findViewById(R.id.unlockText);
        ImageButton toStore = findViewById(R.id.unlockButton);
        unlockText.setTextSize(TypedValue.COMPLEX_UNIT_IN,BASE_TEXT_SCALE*inchWidth);
        unlockText.setGravity(Gravity.CENTER);
        toStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0; i<SKU_ARRAY.length; i++) {
                    Log.i("CurrentTimeline", "unlock_" + timeLine + yearAt+", "+SKU_ARRAY[i]);
                    if (SKU_ARRAY[i].equals("unlock_modern") && timeLine.equals("alp")) launchBilling(i);
                    if (SKU_ARRAY[i].equals("unlock_althist") && !timeLine.equals("alp")) launchBilling(i);
                }
            }
        });
        locked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unlockPopup();
            }
        });
        unlockClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unlockPopdown();
            }
        });
    }
    private void unlockPopup(){
        unlockPop.setVisibility(View.VISIBLE);
        TextView unlockText = findViewById(R.id.unlockText);
        if(timeLine.equals("alp")) unlockText.setText("Unlock the entire modern timeline for .99 (USD)?\nThis will permanently unlock this timeline for your enjoyment!");
        else unlockText.setText("Unlock all alternate history timelines for .99 (USD)?\nThis will permanently unlock this timeline for your enjoyment!");
        unlockPop.animate().y(screenWidth*.3f).setDuration(500);
    }
    private void unlockPopdown(){
        ConstraintLayout unlockPop = findViewById(R.id.unlockPop);
        unlockPop.animate().y(1000).setDuration(500);
    }
    private int[] snapTo(int progress){
        resetNationSelect();
        int max = -10000;
        int min = 5000;
        for(int year : years){
            if(year > max) max = year;
            if(year < min) min = year;
        }
        if(years.length >= 3) {
            //int span = max-min;
            int span = years.length - 1;
            //int yearAt = (int) (progress/100f*span+min);
            int yearAt = (int) (progress / 100f * span);
            for (int i = 0; i < years.length - 1; i++)
                //if(yearAt >= years[i] && yearAt < years[i+1]){
                if (yearAt >= i && yearAt < i + 1) {
                    Log.i("YearAt:",""+years[i]);
                    if (i > 0 && i < years.length - 1)
                        return new int[]{(int) (/*(years[i]-min)*/i * 100f / span), years[i], i, years[i + 1], (int) (/*(years[i+1]-min)*/(i + 1) * 100f / span), years[i - 1], (int) (/*(years[i-1]-min)*/(i - 1) * 100f / span)};
                    else if (i == 0)
                        return new int[]{(int) (/*(years[i]-min)*/i * 100f / span), years[i], i, years[i + 1], (int) (/*(years[i+1]-min)*/(i + 1) * 100f / span), years[i], (int) (/*(years[i]-min)*/i * 100f / span)};
                    else if (i == years.length - 1)
                        return new int[]{(int) (/*(years[i]-min)*/i * 100f / span), years[i], i, years[i], (int) (/*(years[i]-min)*/i * 100f / span), years[i - 1], (int) (/*(years[i-1]-min)*/(i - 1) * 100f / span)};
                }

            return new int[]{(int) (/*(years[years.length-1]-min)*/(years.length - 1) * 100f / span), max, years.length - 1, max, 100, years[years.length - 2], (int) (/*(years[years.length-2]-min)*/(years.length - 2) * 100f / span)};
        }
        return new int[]{100, max, years.length - 1, max, 100, years[0], 0};
    }
    private void jumpPast(){
        int[] out = snapTo(timeProgress);
        timeSlider.setProgress(out[6]);
        yearAt = out[5];
        if(!unlockedYears.contains("modern") && yearAt > 1800 && timeLine.equals("alp")) locked.setVisibility(View.VISIBLE);
        else if(!unlockedYears.contains("althist") && !timeLine.equals("alp")) locked.setVisibility(View.VISIBLE);
        else locked.setVisibility(View.INVISIBLE);
        year.setText("Year: "+yearAt);
        if(out[2] > 0) yearTitle.setText(titles[out[2]-1]);
        timeFile();
    }
    private void jumpFuture(){
        int[] out = snapTo(timeProgress);
        timeSlider.setProgress(out[4]+1);
        yearAt = out[3];
        if(!unlockedYears.contains("modern") && yearAt > 1800 && timeLine.equals("alp")) locked.setVisibility(View.VISIBLE);
        else if(!unlockedYears.contains("althist") && !timeLine.equals("alp")) locked.setVisibility(View.VISIBLE);
        else locked.setVisibility(View.INVISIBLE);
        Log.i("out3", ""+out[3]);
        year.setText("Year: "+yearAt);
        if(out[2] < titles.length-1) yearTitle.setText(titles[out[2]+1]);
        timeFile();
    }
    protected int mapImperium(int mapId){
        Log.i("mapId: ", ""+mapId);
        ImageView mapImage = new ImageView(context);
        mapImage.setScaleType(ImageView.ScaleType.FIT_XY);
        RelativeLayout.LayoutParams mapParams = new RelativeLayout.LayoutParams(915, 470); //same as in layout
        if(mapId == 0) {
            mapImage.setBackgroundResource(Classic.MAP_DRAWABLE);
            mapLayout.addView(mapImage, mapParams);
            map = new Classic(context);
        }
        if(mapId == 1){
            mapImage.setBackgroundResource(Imperium.MAP_DRAWABLE);
            mapLayout.addView(mapImage, mapParams);
            map = new Imperium(context);
        }
        if(mapId == 2){
            mapImage.setBackgroundResource(Europe.MAP_DRAWABLE);
            mapLayout.addView(mapImage, mapParams);
            map = new Europe(context);
        }
        return map.isImperiumMap();
    }
    protected void setupMap(){
        if(game != null){
            game.setMap(map);
            game.getMap().place();
        }
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
    private boolean createGame(){ //false if fails
        Log.i("Greate", "InGame:"+getIntent().getStringExtra("tag"));

        if(getIntent().getStringExtra("tag").equals("loaded")) {
            loadString = getIntent().getStringExtra("loadedGame");
            Log.i("loadedString", "String: \"" + loadString+"\"");
            try{loadBuilder();}catch (Exception e){
                e.printStackTrace();
                Toast.makeText(context, "Save File Has Been Corrupted!", Toast.LENGTH_SHORT).show();
                forceClosed = true;
                finish();
                return false;
            }try{statsBuilder();}catch (Exception e){e.printStackTrace();}

            game.postLoad();
            Log.i("loadedGame", "");
            try {
                //Toast.makeText(context, "Loaded " + getFilesDir() + "/" + getIntent().getStringExtra("loadName"), Toast.LENGTH_SHORT).show();
            }catch (Exception e){e.printStackTrace();}
        }
        else if(getIntent().getStringExtra("tag").equals("new")){
            int mapId = getIntent().getIntExtra("mapId", 0);
            if(!debugingOn)game = new Game(this, getIntent().getIntArrayExtra("types"), mapImperium(mapId), new Object[]{"", ""}, true);
            else {
                mapImperium(DEBUG_MAP_ID);
                game = new Game(this, true, new Object[]{"", ""});
            }
            setupMap();
            game.postNew();
            gameControls();
            Log.i("newGame", "" + game);
            for(int i : getIntent().getIntArrayExtra("types"))
                Log.i("newGame","PLayer Types"+i);
        }
        return true;
    }
    private void loadBuilder() {
        new SaveBooter().buildVersion(Double.parseDouble(loadString.substring(0, 4)), timeLine, yearAt);
    }
    private void statsBuilder(){
        for(Player p : game.getPlayerList()) {
            int statStart = loadString.indexOf("{") + 1;
            Log.i("StatLen", "" + (loadString.indexOf("{", statStart) - statStart));
            int statLen = (loadString.indexOf("{", statStart) - statStart);
            int statAmt = get(loadString.indexOf("]") + 1, 1);
            ArrayList<ArrayList> stats = new ArrayList<>(0);
            Log.i("Stat Amt", "" + statAmt);
            Log.i("building", "playerTings");
            for (int i = 0; i < statAmt; i++) {
                ArrayList<Object> sub = new ArrayList<>(0);
                for (int j = 0; j < statLen / 5; j++) {
                    int startSlice = statStart + j * 5 + i * statLen;
                    Log.i("StatAt", "" + loadString.substring(startSlice, startSlice + 5) + ", " + (startSlice - statStart));
                    try{sub.add(getD(startSlice, 5));}catch (Exception e){e.printStackTrace();}
                }
                statStart++; //skips over each '{'
                stats.add(sub);
            }
            statStart = loadString.indexOf("{") + 1;
            //Log.i("Endindx", "" + (loadString.indexOf("|", statStart) + 1));
            //loadString.replaceFirst(loadString.substring(statStart, loadString.indexOf("|", statStart)+1), "");
            loadString = loadString.substring(0, statStart - 1) + loadString.substring(loadString.indexOf("|", statStart) + 1);
            Log.i("New Load", loadString);
            Log.i("StatLen", "" + statLen);
            //Log.i("Stat Len 2", "" + stats.size());
            p.parseStats(stats);
        }
    }
    protected int get(int start, int length){
        if(loadString.substring(start, start + length).contains("n")) return -1;
        return Integer.parseInt(loadString.substring(start, start + length));
    }
    protected double getD(int start, int length){
        if(loadString.substring(start, start + length).equals("n")) return -1;
        return Double.parseDouble(loadString.substring(start, start + length));
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
                            if(!timeView)
                                try{saveGame(AUTO_SAVE_ID);}catch (Exception e){e.printStackTrace();}

                    }
                } catch (InterruptedException e) { e.printStackTrace(); }
            }
        };lookingThread.start();
    }
    private void initBilling(){
        billingClient = BillingClient.newBuilder(context).enablePendingPurchases().setListener(this).build();
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if(billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK){

                }
            }

            @Override
            public void onBillingServiceDisconnected() {

            }
        });
    }
    private void launchBilling(final int skuId){
        Log.i("Ready", ""+billingClient.isReady());
        if(billingClient.isReady()){
            SkuDetailsParams params = SkuDetailsParams.newBuilder().setSkusList(Arrays.asList(SKU_ARRAY)).setType(BillingClient.SkuType.INAPP).build();
            billingClient.querySkuDetailsAsync(params, new SkuDetailsResponseListener() {
                @Override
                public void onSkuDetailsResponse(BillingResult billingResult, List<SkuDetails> list) {
                    Log.i("Billingresp", billingResult.getResponseCode()+", "+BillingClient.BillingResponseCode.OK);
                    if(billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK){
                        for(SkuDetails detail : list){
                            Log.i("Detail", detail.getSku()+", "+SKU_ARRAY[skuId]);
                            if(detail.getSku().equals(SKU_ARRAY[skuId])){
                                BillingFlowParams params = BillingFlowParams.newBuilder().setSkuDetails(detail).build();
                                billingClient.launchBillingFlow(GameActivity.this, params);
                            }
                        }
                    }
                }
            });
        }
    }
    @Override
    public void onPurchasesUpdated(BillingResult billingResult, List<Purchase> purchases) {
        int response = billingResult.getResponseCode();
        if(response == BillingClient.BillingResponseCode.OK && purchases != null){
            for(Purchase purchase : purchases)
                doPurchase(purchase);
        }if(response == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED){

        }
    }
    public void doPurchase(Purchase purchase) {
        String path = Environment.getExternalStorageDirectory().getPath() + "/Imperium/Game/";
        FileInputStream fis = null;
        FileOutputStream fos;
        StringBuilder sb = new StringBuilder();
        try {
            fis = new FileInputStream(path+"State.txt");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String text;
            while ((text = br.readLine()) != null)
                sb.append(text).append("\n");
        } catch (Exception e) { e.printStackTrace(); }
        finally { if (fis != null) try { fis.close(); } catch (IOException e) { e.printStackTrace(); } }
        String stateText = sb.toString();
        sb = new StringBuilder(stateText);
        File state = new File(path, "State.txt");
        if(timeLine.equals("alp")) sb.insert(sb.indexOf("]"), "modern,");
        else sb.insert(sb.indexOf("]"), "althist,");
        try {
            fos = new FileOutputStream(state);
            fos.write(sb.toString().getBytes());
            fos.close();
        } catch (Exception e) { e.printStackTrace(); }

    }
    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector){
            scaling *= scaleGestureDetector.getScaleFactor();
            scaling = Math.max(MIN_SCALE, Math.min(scaling, MAX_SCALE));
            Log.i("Scale", ""+ scaling);
            if(scaling > 2) {
                for (Province p : map.getList())
                    p.ownerVis(true);
                for (Player pl : game.getPlayerList())
                    pl.titleVis(false);
            }
            else {
                for (Province p : map.getList()) //could cause lag
                    p.ownerVis(false);
                for (Player pl : game.getPlayerList())
                    pl.titleVis(true);
            }
            mapLayout.setScaleX(scaling);
            mapLayout.setScaleY(scaling);
            return true;
        }
    }
    public void tinter(){
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            try {
                // to work on old SDKs
                int FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS = 0x80000000;
                window.addFlags(FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

                Class<?> cls = window.getClass();
                Method method = cls.getDeclaredMethod("setStatusBarColor", new Class<?>[] { Integer.TYPE });
                method.invoke(window, Color.parseColor("#854705"));
            } catch (Exception e) {e.printStackTrace();}
        }
    }

}