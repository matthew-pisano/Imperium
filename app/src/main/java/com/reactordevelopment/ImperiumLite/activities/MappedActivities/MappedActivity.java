package com.reactordevelopment.ImperiumLite.activities.MappedActivities;

import static com.reactordevelopment.ImperiumLite.activities.MainActivity.BASE_TEXT_SCALE;
import static com.reactordevelopment.ImperiumLite.activities.MainActivity.YEET;
import static com.reactordevelopment.ImperiumLite.activities.MainActivity.inchWidth;
import static com.reactordevelopment.ImperiumLite.activities.MainActivity.killMusic;
import static com.reactordevelopment.ImperiumLite.activities.MainActivity.onBuild;
import static com.reactordevelopment.ImperiumLite.activities.MainActivity.onGame;
import static com.reactordevelopment.ImperiumLite.activities.MainActivity.screenHeight;
import static com.reactordevelopment.ImperiumLite.activities.MainActivity.screenWidth;
import static com.reactordevelopment.ImperiumLite.activities.MainActivity.setActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.reactordevelopment.ImperiumLite.core.player.Ai;
import com.reactordevelopment.ImperiumLite.activities.BuildActivity;
import com.reactordevelopment.ImperiumLite.core.mapping.Classic;
import com.reactordevelopment.ImperiumLite.core.mapping.Europe;
import com.reactordevelopment.ImperiumLite.core.gameTypes.Game;
import com.reactordevelopment.ImperiumLite.core.mapping.Imperium;
import com.reactordevelopment.ImperiumLite.activities.MainActivity;
import com.reactordevelopment.ImperiumLite.core.mapping.Map;
import com.reactordevelopment.ImperiumLite.core.player.Player;
import com.reactordevelopment.ImperiumLite.core.mapping.Province;
import com.reactordevelopment.ImperiumLite.R;
import com.reactordevelopment.ImperiumLite.core.SaveBooter;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class MappedActivity extends AppCompatActivity {

    // province info
    protected static ImageButton closeInfo;
    protected static ImageButton developer;
    protected static ImageView fortifier;
    protected static TextView infoText;
    protected static ImageButton ownerFlag;
    protected static ImageView coreFlag;
    protected static ImageView devasIcon;
    protected static ImageView devlIcon;
    protected static ImageView attriIcon;
    protected static TextView devlText;
    protected static TextView davasText;
    protected static TextView attriText;
    protected static ConstraintLayout info;
    protected static ImageView relation;
    //map layout
    protected static ConstraintLayout masterLayout;
    protected static RelativeLayout mapLayout;
    protected static ImageView mapImage;
    //player info
    protected static TextView playerInfo;
    //hidden gears
    protected static Thread spinCycle = null;
    protected float dXGear = 0;
    protected float initX = 0;
    //game status
    protected static ImageView showNation;
    //vars
    //ui scaling
    public static final float MIN_SCALE = .9f;
    public static final float MAX_SCALE = 10f;
    public static float scaling = 1/*(float)(screenWidth/480)*/;
    //ui state
    protected static boolean openNav;
    protected static boolean openInfo;
    protected static boolean openSave;
    protected static boolean openMode;
    protected static boolean[] openModes;
    protected static boolean provEnabled = true;
    //map transformation
    protected ScaleGestureDetector mScaleGestureDetector;
    protected static float dY;
    protected static float dX;
    //timeline data
    protected String timeLine = "";
    protected int yearAt = 0;

    protected static String loadString;
    protected static Context context;  // TODO: replace with proper context retrieval

    protected static Game game;

    protected static Province infoProv;
    protected static Point down = new Point(0,0);
    protected static long downtime = 0;
    protected static Map map;

    public static boolean debugingOn = false; //for just editing a save
    public static boolean newDebugSave = false; //both true for new saves

    protected static final int AI_STYLE = Ai.ROMAN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //null?
        mScaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());
        context = this;
        openInfo = false;
        openNav = false;
        openSave = false;
        openMode = false;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ArrayList<Object> history = (ArrayList<Object>)getIntent().getSerializableExtra("historyFiles");
                timeLine = (String)history.get(0);
                yearAt = (int)history.get(1);
                initializeComponents();
                Log.i("Create", "1");
                if(!createGame()) return;
                Log.i("Create", "2");
                tinter();
                info();
                modeDropdown();
                Log.i("Create", "made");
                Log.i("game", "Players: " + getIntent().getIntExtra("players", 1) + " Map: " + getIntent().getIntExtra("mapId", 0));
                Log.i("dimensions", "W: "+screenWidth+", H: "+screenHeight);
                Log.i("mapdimensions", "W: "+mapLayout.getMeasuredWidth()+", H: "+mapLayout.getHeight()+", scl: "+ scaling);
            }
        });
    }
    @Override
    public void onDestroy() {
        Log.i("onDestron", "dedGameActivity");
        if(game != null) game.haltAis();
        Intent mStartActivity = new Intent(context, MainActivity.class);
        if(!onBuild) startActivity(mStartActivity);
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
        killMusic();
        Intent intent = new Intent(context, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        mScaleGestureDetector.onTouchEvent(motionEvent);
        return true;
    }

    protected void initializeComponents(){
        //background
        ImageView activityRound = findViewById(R.id.gameRound);
        activityRound.setScaleType(ImageView.ScaleType.FIT_XY);
        //info
        info = findViewById(R.id.info);
        developer = findViewById(R.id.developer);
        closeInfo = findViewById(R.id.closeInfo);
        infoText = findViewById(R.id.infoText);
        infoText.setMovementMethod(new ScrollingMovementMethod());
        infoText.setTextSize(TypedValue.COMPLEX_UNIT_IN,BASE_TEXT_SCALE*inchWidth);
        fortifier = findViewById(R.id.fortifier);

        showNation = findViewById(R.id.showNation);

        masterLayout = findViewById(R.id.gameLayout);
        mapLayout = findViewById(R.id.map);
        //mapLayout.animate().x(screenHeight/4).y(screenWidth/4).setDuration(2000);
        mapLayout.setScaleX(scaling);
        mapLayout.setScaleY(scaling);
    }

    public static Game getGame(){return game;}
    public static boolean getInfoOpen(){ return openInfo; }
    public static RelativeLayout getMapLayout(){return mapLayout;}
    public static ConstraintLayout getMasterLayout(){return masterLayout;}
    public static Province getInfoProv(){return infoProv;}
    public static ImageView getMapImage(){return mapImage;}

    public static void setInfoProv(Province prov){infoProv = prov;}

    @SuppressLint("ClickableViewAccessibility")
    protected void touched() {
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
                                    Log.i("DiploToiuch", ""+game.getCurrentMapMode());
                                    if (System.currentTimeMillis() - downtime > 300 && touched != null)
                                        touched.doLongClick();
                                    else if (touched != null) {
                                        if(provEnabled) touched.doClick();
                                        if(touched.getOwnerId() != -1)
                                            touchProvince(touched);
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

    protected void touchProvince(Province touched) {
        game.setFocusPlayer(touched.getOwner());
        Log.i("Focsd", "" + (game.getFocusPlayer() == null));
        if (game.getCurrentMapMode() == 8) game.updateAllOverlays();
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
                        if(game.getCurrentMapMode() != i) modes[i].animate().x(-3 * modes[game.getCurrentMapMode()].getWidth() / 4);
                    }
                }
                openMode = !openMode;
            }
        });
        for(int i=0; i<modes.length; i++){
            final int index = i;
            modes[i].setLayoutParams(new LinearLayout.LayoutParams((int)(screenHeight*.45*.75), ViewGroup.LayoutParams.MATCH_PARENT, (float)(1.0/modesLength)));

            modes[i].setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {
                if(game.getCurrentMapMode() != index) {
                    modes[index].animate().x(0).setDuration(200).start();
                    modes[game.getCurrentMapMode()].animate().x(-3 * modes[game.getCurrentMapMode()].getWidth() / 4).setDuration(200).start();
                    game.setMapMode(index);
                }
                Log.i("mode", "mapMode: "+index);
            }});
        }

    }

    public void showInfo(Province prov){
        Log.i("showInfo", "open: " + openInfo + "from " + prov.getName());
        infoProv = prov;
        if(prov.getOwner() != null) {
            int[] relations = game.getCurrPlayer().getRelations(prov.getOwner().getTag());
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
        if(prov.getOwnerId() == game.getCurrPlayer().getId())
            revealProvMods();
        else hideProvMods();
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
    protected void info() {
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

        closeInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                info.animate().x(-1000).setDuration(500).start();
                openInfo = false;
                Log.i("info", "open: " + openInfo);
            }
        });
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
    private boolean createGame(){ //false if fails
        Log.i("Greate", "InGame:"+getIntent().getStringExtra("tag"));
        // If intent is missing, go back to build
        if(getIntent() == null) {
            Toast.makeText(context, "Minor issue, try again", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, BuildActivity.class);
            startActivity(intent);
            return false;
        } else if(getIntent().getStringExtra("tag") == null){
            Toast.makeText(context, "Minor issue, try again", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, BuildActivity.class);
            startActivity(intent);
            return false;
        }

        if(getIntent().getStringExtra("tag").equals("loaded")) {
            loadString = getIntent().getStringExtra("loadedGame");
            Log.i("loadedString", "String: \"" + loadString+"\"");
            try{loadBuilder();}catch (Exception e){
                e.printStackTrace();
                Toast.makeText(context, "Save File Has Been Corrupted!", Toast.LENGTH_SHORT).show();
                finish();
                return false;
            }
            try{statsBuilder();}catch (Exception e){e.printStackTrace();}

            game.postLoad();
            Log.i("loadedGame", "");
            try {
                //Toast.makeText(context, "Loaded " + getFilesDir() + "/" + getIntent().getStringExtra("loadName"), Toast.LENGTH_SHORT).show();
            }catch (Exception e){e.printStackTrace();}
        }
        else if(getIntent().getStringExtra("tag").equals("new")){
            int mapId = getIntent().getIntExtra("mapId", 0);
            game = new Game(getIntent().getIntArrayExtra("types"), mapImperium(mapId), new Object[]{"", ""}, true);
            setupMap();
            game.postNew();
            Log.i("newGame", "" + game);
            for(int i : getIntent().getIntArrayExtra("types"))
                Log.i("newGame","PLayer Types"+i);
        }
        return true;
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

    protected void loadBuilder() {
        new SaveBooter().buildVersion(Double.parseDouble(loadString.substring(0, 4)), timeLine, yearAt);
    }

    protected class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
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