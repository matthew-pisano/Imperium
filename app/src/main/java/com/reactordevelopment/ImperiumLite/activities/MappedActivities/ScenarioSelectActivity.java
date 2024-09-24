package com.reactordevelopment.ImperiumLite.activities.MappedActivities;

import static com.reactordevelopment.ImperiumLite.activities.MainActivity.BASE_TEXT_SCALE;
import static com.reactordevelopment.ImperiumLite.activities.MainActivity.inchWidth;
import static com.reactordevelopment.ImperiumLite.activities.MainActivity.screenHeight;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.reactordevelopment.ImperiumLite.activities.BuildActivity;
import com.reactordevelopment.ImperiumLite.core.Game;
import com.reactordevelopment.ImperiumLite.core.player.Nation;
import com.reactordevelopment.ImperiumLite.core.player.Player;
import com.reactordevelopment.ImperiumLite.core.mapping.Province;
import com.reactordevelopment.ImperiumLite.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ScenarioSelectActivity extends MappedActivity {

    //time view
    private SeekBar timeSlider;
    private ImageView timeCover;
    private TextView year;
    private TextView yearTitle;
    public static boolean historicalSave;
    private static ImageView nationFlag;
    private static ImageView showNationFrame;
    private LinearLayout flags;
    private LinearLayout selections;
    private ImageButton[] players;
    private ImageButton[] selects;
    private ImageButton toBuild;
    private String[] nations;
    private static Nation nationAt;
    private String mapPath;

    private Integer[] years;
    private String[] titles;
    private int timeProgress = 0;
    private String yearInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                timeView();
            }
        });
    }

    protected void touchProvince(Province touched) {
        super.touchProvince(touched);
        Player owner = touched.getOwner();
        String playerText = owner.getName() + "\nLegions: " + (owner.getFreeTroops()) +
                "\nDevelopment: " + owner.totalIncome()
                + "\nOperations Efficiency: " + owner.getOpsEfficiency()
                + "\nLegion Hardening: " + owner.getTroopHardening();
        nationFlag.setBackgroundResource(owner.getFlag());
        nationAt = owner.getNation();
        playerInfo.setText(playerText);
    }

    protected void initializeComponents(){
        super.initializeComponents();
        //time view
        timeSlider = findViewById(R.id.timeSlider);
        timeCover = findViewById(R.id.timeCover);
        year = findViewById(R.id.year);
        showNationFrame = findViewById(R.id.showNationFrame);
        showNationFrame.setBackgroundResource(R.drawable.flagframe);
        yearTitle = findViewById(R.id.yearTitle);
        playerInfo = findViewById(R.id.playerInfo);
        playerInfo.setMovementMethod(new ScrollingMovementMethod());
        playerInfo.setTextSize(TypedValue.COMPLEX_UNIT_IN,BASE_TEXT_SCALE*inchWidth);
        nationFlag = findViewById(R.id.nationFlag);
        nationFlag.setVisibility(View.INVISIBLE);
        flags = findViewById(R.id.flagLayout);
        selections = findViewById(R.id.selectLayout);
        toBuild = findViewById(R.id.toBuild);
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
        Game.inSetup = true;
        loadBuilder();
        game.loadOwnerFromTag();
        game.playerTitles();
        game.updateMapMode(mapMode);
        Game.inSetup = false;
        Log.i("OutSetup", "out5");
        Log.i("Time Files", "Done");
    }
    private void timeView(){
        historicalSave = true;
        initialTimeFiles();
        findViewById(R.id.timeProgress).setVisibility(View.VISIBLE);
        nationFlag.setVisibility(View.VISIBLE);
        showNation.setVisibility(View.INVISIBLE);
        showNationFrame.setVisibility(View.INVISIBLE);
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
                year.setText("Year: "+yearAt);
                yearTitle.setText(titles[out[2]]);
                timeFile();
            }
        });
        int[] out = snapTo(timeProgress);
        timeSlider.setProgress(out[0]);
        yearAt = out[1];
        year.setText("Year: "+yearAt);
        yearTitle.setText(titles[out[2]]);
        timeFile();
        timeFile();

        toBuild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean send = false;
                for (String s : nations) if (s != null) if (!s.equals("")) send = true;

                Intent intent = new Intent(context, BuildActivity.class);
                ArrayList<Object> history = new ArrayList<>(0);
                history.add(timeLine);
                history.add(yearAt);
                history.add(mapPath);
                history.add(nations);
                if (send) intent.putExtra("historyFiles", history);
                Log.i("tobuild", "send: "+send);
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
        year.setText("Year: "+yearAt);
        if(out[2] > 0) yearTitle.setText(titles[out[2]-1]);
        timeFile();
    }
    private void jumpFuture(){
        int[] out = snapTo(timeProgress);
        timeSlider.setProgress(out[4]+1);
        yearAt = out[3];
        Log.i("out3", ""+out[3]);
        year.setText("Year: "+yearAt);
        if(out[2] < titles.length-1) yearTitle.setText(titles[out[2]+1]);
        timeFile();
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
}
