package com.reactordevelopment.ImperiumLite;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static com.reactordevelopment.ImperiumLite.GameActivity.*;
import static com.reactordevelopment.ImperiumLite.MainActivity.*;

public class BuildActivity extends AppCompatActivity {

    private ImageView player0;
    private ImageView player1;
    private ImageView player2;
    private ImageView player3;
    private ImageView ai0;
    private ImageView ai1;
    private ImageView ai2;
    private ImageView ai3;
    private ImageView mapSelected;
    private SeekBar playerSelect;
    private TextView playersTitle;
    private Button create;
    private ImageView timelineIcon;
    private ImageView mapCanvas;
    private ImageButton helper;
    private ImageButton chronometer;
    private ImageButton mapLeft;
    private ImageButton mapRight;
    private AbsoluteLayout absoluteLayout;
    private Context context;
    private String loadString;
    private ArrayList<Object> history;
    private static int mapAtId = 0;
    private static int players = 2;
    private int progressNum = 0;
    private boolean historical = false;
    private static int[] types;
    private int[] mapList;
    private String timeLine;
    private boolean shiftOpen;
    private String[] realityFiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_build);
        ImageView activityRound = findViewById(R.id.buildRound);
        activityRound.setScaleType(ImageView.ScaleType.FIT_XY);
        absoluteLayout = findViewById(R.id.flagCanvas);
        timelineIcon = findViewById(R.id.timelineIcon);
        Log.i("initial", "added " + players + " players");
        context = this;
        timeLine = "alp";
        shiftOpen = false;
        realityFiles = realityFiles(new Europe(context).getMapFilePath());
        timeShift();
        playerSelect();
        maps();
        if(getIntent().getSerializableExtra("historyFiles") != null)
            history = (ArrayList<Object>)getIntent().getSerializableExtra("historyFiles");

        if(history != null){
            hideSelect();
            if(mapAtId == 2) historical = true;
        }
        Log.i("Historical", ""+historical);
        Log.i("history", "null: "+(history == null));
        Log.i("MapId", ""+mapAtId);
        Log.i("initial", "has " + types[0] + " ais");
        createButtons();
    }
    @Override
    public void onStart(){
        Log.i("BuildStart", "start");
        super.onStart();
        onBuild = true;
    }
    @Override
    public void onStop(){
        setActivity("none");
        super.onStop();
    }
    @Override
    public void onDestroy(){
        players = 2;
        types = null;
        Log.i("Build destroy", "ddede");
        super.onDestroy();
    }
    private void maps(){
        mapList = new int[3];
        mapList[0] = Classic.MAP_DRAWABLE;
        mapList[1] = Imperium.MAP_DRAWABLE;
        mapList[2] = Europe.MAP_DRAWABLE;

        mapCanvas = findViewById(R.id.map);
        mapCanvas.setBackgroundResource(mapList[mapAtId]);
        mapLeft = findViewById(R.id.mapLeft);
        mapLeft.setBackgroundResource(R.drawable.closenav);
        mapRight = findViewById(R.id.mapRight);
        mapRight.setBackgroundResource(R.drawable.opennav);
        mapSelected = findViewById(R.id.mapSelected);
        final ImageView mapLock = findViewById(R.id.mapLock);

        final TextView mapVersion = findViewById(R.id.mapVersion);
        mapVersion.setTextSize(TypedValue.COMPLEX_UNIT_IN,BASE_TEXT_SCALE*inchWidth);

        mapRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Mapid", ""+mapAtId);
                if(mapAtId == 2) {
                    showSelect();
                    historical = false;
                }
                if(mapAtId <= mapList.length-2) mapAtId ++;
                mapSwitch(mapVersion);
                /*if(LOCKED){
                    if(mapAtId != 0) mapLock.setVisibility(View.VISIBLE);
                    else mapLock.setVisibility(View.INVISIBLE);
                }*/
            }});
        mapLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Mapid", ""+mapAtId);
                if(mapAtId == 2) {
                    showSelect();
                    historical = false;
                }
                if(mapAtId >= 1) mapAtId --;
                mapSwitch(mapVersion);
                /*if(LOCKED){
                    if(mapAtId != 0) mapLock.setVisibility(View.VISIBLE);
                    else mapLock.setVisibility(View.INVISIBLE);
                }*/
            }});
    }
    private void mapSwitch(TextView mapVersion){
        create.setVisibility(View.VISIBLE);
        if(mapAtId == 0) mapVersion.setText("Classic");
        if(mapAtId == 1) mapVersion.setText("Imperium");
        if(mapAtId == 2){
            mapVersion.setText("Europe");
            hideSelect();
            historical = (history != null);
        }
        mapCanvas.setBackgroundResource(mapList[mapAtId]);
    }
    private void aiToPlayer(String timeline, int year, String mapPath, String[] nations){
        byte[] buffer = new byte[0];
        try {
            InputStream stream = context.getAssets().open("sacredTexts/timeLines/"+mapPath+timeline+year+".imprm");
            int size = stream.available();
            buffer = new byte[size];
            stream.read(buffer);
            stream.close();
        } catch (IOException e) { e.printStackTrace(); }
        loadString = new String(buffer);
        for(String nat : nations) {
            if(nat != null && !nat.equals("")) {
                String tag = nat;
                Log.i("tag", nat);
                //loadString = loadString.re(tag + "1", tag + "0");
                replaceLast(tag + "1", tag + "0");
                Log.i("moded Load", "Str: {");
                for (int i = 0; i < loadString.length(); i += 4000)
                    if (i + 4000 < loadString.length())
                        Log.i("moded Load", loadString.substring(i, i + 4000));
                    else {
                        Log.i("moded Load", loadString.substring(i));
                        break;
                    }
                Log.i("moded load", "} String done");
            }
        }
    }
    public void replaceLast(String oldStr, String newStr){
        int lastPlace = loadString.lastIndexOf(oldStr);
        loadString = loadString.substring(0, lastPlace)+newStr+loadString.substring(lastPlace+oldStr.length());
    }
    private void hideSelect(){
        try {
            mapCanvas.setBackgroundResource(Europe.MAP_DRAWABLE);
        }catch (OutOfMemoryError e){
            mapCanvas.setBackgroundResource(R.drawable.blank);
        }
        mapAtId = 2;
        ((TextView)findViewById(R.id.mapVersion)).setText("Historical");
        playerSelect.setVisibility(View.INVISIBLE);
        findViewById(R.id.flagCanvas).setVisibility(View.INVISIBLE);
        ImageView hist1 = findViewById(R.id.hist1);
        ImageView hist2 = findViewById(R.id.hist2);
        ImageView hist3 = findViewById(R.id.hist3);
        ImageView hist4 = findViewById(R.id.hist4);
        hist1.setVisibility(View.VISIBLE);
        hist2.setVisibility(View.VISIBLE);
        hist3.setVisibility(View.VISIBLE);
        hist4.setVisibility(View.VISIBLE);
        View.OnClickListener click = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GameActivity.class);
                if(timeLine.equals("alp")) aiToPlayer(timeLine, DEFAULT_YEAR_ALP, new Europe().getMapFilePath(), new String[]{}); //always imperium map
                if(timeLine.equals("rom")) aiToPlayer(timeLine, DEFAULT_YEAR_ROM, new Europe().getMapFilePath(), new String[]{});
                if(timeLine.equals("kai")) aiToPlayer(timeLine, DEFAULT_YEAR_KAI, new Europe().getMapFilePath(), new String[]{});
                if(timeLine.equals("vir")) aiToPlayer(timeLine, DEFAULT_YEAR_VIR, new Europe().getMapFilePath(), new String[]{});
                intent.putExtra("timeView", true);
                intent.putExtra("timeline", timeLine);
                intent.putExtra("loadedGame", loadString);
                intent.putExtra("tag", "loaded");
                intent.putExtra("loadName", AUTO_SAVE_ID);
                startActivity(intent);
                finish();
            }
        };
        hist1.setOnClickListener(click);
        hist2.setOnClickListener(click);
        hist3.setOnClickListener(click);
        hist4.setOnClickListener(click);
        ImageView selectHider = findViewById(R.id.selectHider);
        selectHider.setVisibility(View.VISIBLE);
        String[] nations;
        if(history != null){
            nations = (String[])history.get(3);
            playersTitle.setText("<Players>");
        }
        else {
            nations = new String[]{null, null, null, null};
            playersTitle.setText("No nations selected, use stopwatch to select");
        }
        if(nations[0] != null) hist1.setBackgroundResource(new Nation(nations[0], "", 0).getFlag());
        if(nations[1] != null) hist2.setBackgroundResource(new Nation(nations[1], "", 0).getFlag());
        if(nations[2] != null) hist3.setBackgroundResource(new Nation(nations[2], "", 0).getFlag());
        if(nations[3] != null) hist4.setBackgroundResource(new Nation(nations[3], "", 0).getFlag());
    }
    private void showSelect(){
        mapCanvas.setBackgroundResource(Classic.MAP_DRAWABLE);
        mapLeft.setVisibility(View.VISIBLE);
        mapRight.setVisibility(View.VISIBLE);
        playerSelect.setVisibility(View.VISIBLE);

        findViewById(R.id.hist1).setVisibility(View.INVISIBLE);
        findViewById(R.id.hist2).setVisibility(View.INVISIBLE);
        findViewById(R.id.hist3).setVisibility(View.INVISIBLE);
        findViewById(R.id.hist4).setVisibility(View.INVISIBLE);

        findViewById(R.id.flagCanvas).setVisibility(View.VISIBLE);
        findViewById(R.id.selectHider).setVisibility(View.INVISIBLE);
        playersTitle.setText("Select Number Of Players");
    }
    private void timeShift(){
        final ConstraintLayout shiftLayout = findViewById(R.id.shiftLayout);
        ImageButton timeShift = findViewById(R.id.timeShift);
        ImageButton alp = findViewById(R.id.alp);
        ImageButton rom = findViewById(R.id.rom);
        ImageButton kai = findViewById(R.id.kai);
        ImageButton vir = findViewById(R.id.vir);

        timeShift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(shiftOpen)
                    shiftLayout.animate().x(screenWidth*.87f).setDuration(500);
                else
                    shiftLayout.animate().x(screenWidth*.3f).setDuration(500);
                shiftOpen = !shiftOpen;
            }
        });
        alp.setOnClickListener(shiftClick("alp"));
        rom.setOnClickListener(shiftClick("rom"));
        kai.setOnClickListener(shiftClick("kai"));
        vir.setOnClickListener(shiftClick("vir"));
        alp.performClick();
        shiftLayout.animate().x(screenWidth*.87f).setDuration(0);
        shiftLayout.setVisibility(View.VISIBLE);
    }


    private View.OnClickListener shiftClick(final String timeTag){
        final ImageButton alpCk = findViewById(R.id.alpCheck);
        final ImageButton romCk = findViewById(R.id.romCheck);
        final ImageButton kaiCk = findViewById(R.id.kaiCheck);
        final ImageView alpBubble = findViewById(R.id.alpBubble);
        final ImageView kaiBubble = findViewById(R.id.kaiBubble);
        final ImageView romBubble = findViewById(R.id.romBubble);

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeLine = timeTag;
                String desc = "";
                for(String s : realityFiles){
                    Log.i("realtiy string", s);
                    if(s.substring(0, 3).equals(timeTag)) {
                        desc = s.substring(3);
                        if (timeTag.equals("alp")) {
                            alpCk.setVisibility(View.VISIBLE);
                            romCk.setVisibility(View.INVISIBLE);
                            kaiCk.setVisibility(View.INVISIBLE);
                            alpBubble.setColorFilter(desaturate(false));
                            romBubble.setColorFilter(desaturate(true));
                            kaiBubble.setColorFilter(desaturate(true));
                            timelineIcon.setBackgroundResource(R.drawable.alplogo);
                        }
                        if (timeTag.equals("rom")) {
                            alpCk.setVisibility(View.INVISIBLE);
                            romCk.setVisibility(View.VISIBLE);
                            kaiCk.setVisibility(View.INVISIBLE);
                            alpBubble.setColorFilter(desaturate(true));
                            romBubble.setColorFilter(desaturate(false));
                            kaiBubble.setColorFilter(desaturate(true));
                            timelineIcon.setBackgroundResource(R.drawable.romlogo);
                        }
                        if (timeTag.equals("kai")) {
                            alpCk.setVisibility(View.INVISIBLE);
                            romCk.setVisibility(View.INVISIBLE);
                            kaiCk.setVisibility(View.VISIBLE);
                            alpBubble.setColorFilter(desaturate(true));
                            romBubble.setColorFilter(desaturate(true));
                            kaiBubble.setColorFilter(desaturate(false));
                            timelineIcon.setBackgroundResource(R.drawable.kailogo);
                        }
                    }
                }
                ((TextView)findViewById(R.id.timeInfo)).setText(desc);
            }
        };
    }
    private String[] realityFiles(String mapPath){
        byte[] buffer = new byte[0];
        String[] list = new String[0];
        ArrayList<String> timelineDescs = new ArrayList<>(0);
        try {
            list = context.getAssets().list("sacredTexts"+ File.separator+"timeLines"+File.separator+mapPath.substring(0, mapPath.length()-1));
            Log.i("listlen", ""+list.length+", "+mapPath.substring(0, mapPath.length()-1));
        } catch (IOException e) { e.printStackTrace(); }
        for (String s : list)
            if (s.contains("titles")) {
                try {
                    InputStream stream = getAssets().open("sacredTexts/timeLines/" + mapPath + s);
                    int size = stream.available();
                    buffer = new byte[size];
                    stream.read(buffer);
                    stream.close();
                } catch (IOException e) { e.printStackTrace(); }
                String loaded = new String(buffer);
                timelineDescs.add(s.substring(0, 3)+loaded.substring(loaded.indexOf("\"")+1));
                Log.i("title", s.substring(0, 3)+loaded.substring(loaded.indexOf("\"")+1));
            }
        return  timelineDescs.toArray(new String[0]);
    }
    private void createButtons(){
        ImageButton buildQuit = findViewById(R.id.buildQuit);
        buildQuit.setBackgroundResource(R.drawable.quit);
        buildQuit.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                finish(); }});

        create = findViewById(R.id.makeGame);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!historical && mapAtId == 2){
                    try{
                        Toast.makeText(context, "You must select a nation using the stopwatch first or select a different map", Toast.LENGTH_LONG).show();
                    }catch (Exception ignored){}
                    return;
                }

                Intent intent = new Intent(context, GameActivity.class);
                Log.i("HistoricalBuild", historical+"");
                if (!historical && loadString == null && !debugingOn) {
                    intent.putExtra("players", players);
                    Log.i("build", "added " + players + " players");
                    intent.putExtra("mapId", mapAtId);
                    //else intent.putExtra("mapId", 0);
                    Log.i("Typeslen", ""+types.length);
                    intent.putExtra("types", types);
                    intent.putExtra("tag", "new");
                }else if (historical && !debugingOn){
                    try {
                        Log.i("Historial", "load");
                        history = (ArrayList<Object>)getIntent().getSerializableExtra("historyFiles");
                        if(history == null) return;
                        String[] nations = (String[])history.get(3);

                        aiToPlayer((String)history.get(0), (Integer) history.get(1), (String)history.get(2), nations);
                        intent.putExtra("loadedGame", loadString);
                        intent.putExtra("tag", "loaded");
                        intent.putExtra("historyFiles", history);
                        intent.putExtra("loadName", AUTO_SAVE_ID);
                    }catch (Exception e){e.printStackTrace();}
                }
                else if(debugingOn){
                    try {
                        getAssets().open("sacredTexts/timeLines/"+DEBUG_MAP+debugId +".imprm");
                        aiToPlayer(debugId.substring(0, 3), Integer.parseInt(debugId.substring(3)), DEBUG_MAP, GameActivity.debugNations);
                        intent.putExtra("loadedGame", loadString);
                        intent.putExtra("tag", "loaded");
                        intent.putExtra("loadName", AUTO_SAVE_ID);
                    }catch (Exception e){
                        e.printStackTrace();
                        intent.putExtra("players", players);
                        Log.i("build", "added " + players + " players");
                        intent.putExtra("mapId", DEBUG_MAP_ID);
                        intent.putExtra("types", types);
                        intent.putExtra("tag", "new");
                    }
                }
                startActivity(intent);
                finish();
            }
        });
        helper = findViewById(R.id.helper);
        helper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, HelpActivity.class);
                startActivity(intent);
            }
        });
        chronometer = findViewById(R.id.chronometer);
        if(history != null)chronometer.setBackgroundResource(R.drawable.timeview);
        else chronometer.setBackgroundResource(R.drawable.timehide);
        chronometer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(history == null) {
                    Intent intent = new Intent(context, GameActivity.class);
                    if(timeLine.equals("alp")) aiToPlayer(timeLine, DEFAULT_YEAR_ALP, new Europe().getMapFilePath(), new String[]{}); //always imperium map
                    if(timeLine.equals("rom")) aiToPlayer(timeLine, DEFAULT_YEAR_ROM, new Europe().getMapFilePath(), new String[]{});
                    if(timeLine.equals("kai")) aiToPlayer(timeLine, DEFAULT_YEAR_KAI, new Europe().getMapFilePath(), new String[]{});
                    if(timeLine.equals("vir")) aiToPlayer(timeLine, DEFAULT_YEAR_VIR, new Europe().getMapFilePath(), new String[]{});
                    intent.putExtra("timeView", true);
                    intent.putExtra("timeline", timeLine);
                    intent.putExtra("loadedGame", loadString);
                    intent.putExtra("tag", "loaded");
                    intent.putExtra("loadName", AUTO_SAVE_ID);
                    startActivity(intent);
                    finish();
                }else {
                    history = null;
                    historical = false;
                    loadString = null;
                    chronometer.setBackgroundResource(R.drawable.timehide);
                    findViewById(R.id.hist1).setBackgroundResource(R.drawable.noflag);
                    findViewById(R.id.hist2).setBackgroundResource(R.drawable.noflag);
                    findViewById(R.id.hist3).setBackgroundResource(R.drawable.noflag);
                    findViewById(R.id.hist4).setBackgroundResource(R.drawable.noflag);
                }
            }
        });
    }
    private void createAiSelect(){
        ai0 = new ImageView(context);
        ai0.setBackgroundResource(R.drawable.helmet);
        ai1 = new ImageView(context);
        ai1.setBackgroundResource(R.drawable.helmet);
        ai2 = new ImageView(context);
        ai2.setBackgroundResource(R.drawable.helmet);
        ai3 = new ImageView(context);
        ai3.setBackgroundResource(R.drawable.helmet);
        ai0.setOnClickListener(clicker(0, ai0));
        ai1.setOnClickListener(clicker(1, ai1));
        ai2.setOnClickListener(clicker(2, ai2));
        ai3.setOnClickListener(clicker(3, ai3
        ));
    }
    private View.OnClickListener clicker(final int player, final ImageView aiAt){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(types.length >= player+1) {
                    if(types[player] == 0) types[player] = 1;
                    else types[player] = 0;
                    if (types[player] == 1) aiAt.setBackgroundResource(R.drawable.halus);
                    else aiAt.setBackgroundResource(R.drawable.helmet);
                    Log.i("types", "" + types[player]);
                }
            }
        };
    }
    private void playerSelect(){
        types = new int[players];
        playersTitle = findViewById(R.id.playersTitle);
        playersTitle.setTextSize(TypedValue.COMPLEX_UNIT_IN,.7f*BASE_TEXT_SCALE*inchWidth);
        playerSelect = findViewById(R.id.playerSelect);
        playerSelect.setProgress(0);
        player0 = new ImageView(context);
        player0.setBackgroundResource(R.drawable.blue);
        player1 = new ImageView(context);
        player1.setBackgroundResource(R.drawable.red);
        player2 = new ImageView(context);
        player2.setBackgroundResource(R.drawable.green);
        player3 = new ImageView(context);
        player3.setBackgroundResource(R.drawable.purple);
        createAiSelect();
        int masterWidth = (int)(screenWidth*.5*.12);
        int masterHeight = (int)(screenHeight*.07*.5);
        final int layoutW = (int)(screenWidth*.6);
        int layoutH = (int)(screenHeight*.07);
        absoluteLayout.addView(player0, new LinearLayout.LayoutParams(masterWidth, masterHeight));
        absoluteLayout.addView(player1, new LinearLayout.LayoutParams(masterWidth, masterHeight));
        absoluteLayout.addView(player2, new LinearLayout.LayoutParams(masterWidth, masterHeight));
        absoluteLayout.addView(player3, new LinearLayout.LayoutParams(masterWidth, masterHeight));
        absoluteLayout.addView(ai0, new LinearLayout.LayoutParams(masterWidth, masterHeight));
        absoluteLayout.addView(ai1, new LinearLayout.LayoutParams(masterWidth, masterHeight));
        absoluteLayout.addView(ai2, new LinearLayout.LayoutParams(masterWidth, masterHeight));
        absoluteLayout.addView(ai3, new LinearLayout.LayoutParams(masterWidth, masterHeight));
        player0.setX((float)(layoutW*.1));
        player1.setX((float)(layoutW*.7));
        player2.setX((float)(layoutW*1.3));
        player3.setX((float)(layoutW*1.3));

        ai0.setX((float)(layoutW*.1));
        ai1.setX((float)(layoutW*.7));
        ai2.setX((float)(layoutW*1.3));
        ai3.setX((float)(layoutW*1.3));
        ai0.setY((float)(layoutH*.5));
        ai1.setY((float)(layoutH*.5));
        ai2.setY((float)(layoutH*.5));
        ai3.setY((float)(layoutH*.5));
        playerSelect.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressNum = progress;
                players = (int) (progress/50.0) + 2;
                Log.i("build", "set " + players + " players");
                float one = (float)(.75*layoutW) - (float) (.22*layoutW*progress/50);
                float two = (float)(layoutW) - (float) (.25*layoutW*progress/50);
                float three = (float)(1.25*layoutW) - (float) (.25*layoutW*progress/50);
                player1.setX(one);
                player2.setX(two);
                player3.setX(three);
                ai1.setX(one);
                ai2.setX(two);
                ai3.setX(three);
                types = new int[players];
                ai0.setBackgroundResource(R.drawable.helmet);
                ai1.setBackgroundResource(R.drawable.helmet);
                ai2.setBackgroundResource(R.drawable.helmet);
                ai3.setBackgroundResource(R.drawable.helmet);
                playersTitle.setText(players + " players");
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                playerSelect.setProgress(50 * (int) (progressNum/40.0));
                playersTitle.setText("" + players + " players");
            }
        });
    }

}
