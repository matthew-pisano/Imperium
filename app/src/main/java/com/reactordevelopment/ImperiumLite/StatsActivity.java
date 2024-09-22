package com.reactordevelopment.ImperiumLite;

import static com.reactordevelopment.ImperiumLite.MainActivity.onStats;
import static com.reactordevelopment.ImperiumLite.MainActivity.setActivity;
import static com.reactordevelopment.ImperiumLite.MappedActivities.GameActivity.statsBundle;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.reactordevelopment.ImperiumLite.MappedActivities.GameActivity;

import java.util.ArrayList;

public class StatsActivity extends AppCompatActivity {
    private static GraphView statGraph;
    private static ImageButton provinceStat;
    private static ImageButton totalTroopStat;
    private static ImageButton reinforceStat;
    private static ImageButton infamyStat;
    private static ImageButton conquersStat;
    private static ImageButton conMonStat;
    private static ImageButton quitter;
    private Context context;
    private static boolean imperium;
    private static ArrayList<ArrayList<ArrayList>> stats;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        statGraph = findViewById(R.id.statGraph);
        statGraph.getViewport().setMaxX(GameActivity.getGame().getTurnNum()/GameActivity.getGame().getPlayerList().length);
        statGraph.getViewport().setXAxisBoundsManual(true);
        context = this;
        imperium = GameActivity.getGame().getImperium();
        ImageView activityRound = findViewById(R.id.statRound);
        activityRound.setScaleType(ImageView.ScaleType.FIT_XY);
        //Bundle statsBundle = getIntent().getBundleExtra("statsBundle");
        //stats = (ArrayList<ArrayList<ArrayList>>)statsBundle.getSerializable("stats");
        choices();
    }
    @Override
    public void onStart(){
        super.onStart();
        onStats = true;
    }
    @Override
    public void onStop(){
        setActivity("none");
        super.onStop();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    @Override
    public void onBackPressed(){finish();}
    private void extractStats(int type){
        statGraph.removeAllSeries();
        for(int i=0; i<statsBundle.size(); i++){
            double[] doubles = new double[statsBundle.get(i).get(type).size()];
            for(int j=0; j<statsBundle.get(i).get(type).size(); j++){
                doubles[j] = Double.parseDouble("" + statsBundle.get(i).get(type).get(j));
                //Log.i("extraction", ""+stats.get(i).get(type).get(j)+" is at item "+j);
            }
            makeGraph(doubles, i);
        }
    }
    private void makeGraph(double[] stats, int playerId){
        DataPoint[] data = new DataPoint[stats.length];
        for(int i=0; i<stats.length; i++) {
            data[i] = new DataPoint(i, stats[i]);
        }
        statGraph.getViewport().setScalable(true);
        statGraph.getViewport().setScalableY(true);
        LineGraphSeries line = new LineGraphSeries<>(data);
        Player[] players = GameActivity.getGame().getPlayerList();
        line.setColor(players[playerId].getColor());
        /*if(playerId == 0) line.setColor(PLAYER_BLUE);
        if(playerId == 1) line.setColor(PLAYER_RED);
        if(playerId == 2) line.setColor(PLAYER_GREEN);
        if(playerId == 3) line.setColor(PLAYER_PURPLE);*/
        statGraph.addSeries(line);
    }
    private void choices(){
        provinceStat = findViewById(R.id.provinceStat);
        provinceStat.setBackgroundResource(R.drawable.provinces);
        totalTroopStat = findViewById(R.id.totalTroopsStat);
        totalTroopStat.setBackgroundResource(R.drawable.legions);
        reinforceStat = findViewById(R.id.reinforceStat);
        reinforceStat.setBackgroundResource(R.drawable.reinforce);
        infamyStat = findViewById(R.id.infamyStat);
        infamyStat.setBackgroundResource(R.drawable.infamy);
        conquersStat = findViewById(R.id.conquersStat);
        conquersStat.setBackgroundResource(R.drawable.conquests);
        conMonStat = findViewById(R.id.continentStat);
        conMonStat.setBackgroundResource(R.drawable.continents);
        if(imperium)
            conMonStat.setBackgroundResource(R.drawable.monetaedown);
        quitter = findViewById(R.id.quitter);
        quitter.setBackgroundResource(R.drawable.navquit);
        provinceStat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                extractStats(0);
                provinceStat.setBackgroundResource(R.drawable.provincesdown);
                unselect(0);
                statGraph.setTitle("Owned Provinces");
            }
        });
        totalTroopStat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                extractStats(1);
                totalTroopStat.setBackgroundResource(R.drawable.legionsdown);
                unselect(1);
                statGraph.setTitle("Total Legions");
            }
        });
        reinforceStat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                extractStats(2);
                reinforceStat.setBackgroundResource(R.drawable.reinforcedown);
                unselect(2);
                statGraph.setTitle("Reinforcements");
            }
        });
        infamyStat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                extractStats(3);
                infamyStat.setBackgroundResource(R.drawable.infamydown);
                unselect(3);
                statGraph.setTitle("Infamy");
            }
        });
        conquersStat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                extractStats(4);
                conquersStat.setBackgroundResource(R.drawable.conquestsdown);
                unselect(4);
                statGraph.setTitle("Conquered Provinces");
            }
        });
        conMonStat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                extractStats(5);
                conMonStat.setBackgroundResource(R.drawable.monetaedown);
                statGraph.setTitle("Treasury");
                if(!imperium){
                    statGraph.setTitle("Owned continents");
                    conMonStat.setBackgroundResource(R.drawable.continentsdown);}
                unselect(5);

            }
        });
        quitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    private void unselect(int except){
        if(except != 0) provinceStat.setBackgroundResource(R.drawable.provinces);
        if(except != 1) totalTroopStat.setBackgroundResource(R.drawable.legions);
        if(except != 2) reinforceStat.setBackgroundResource(R.drawable.reinforce);
        if(except != 3) infamyStat.setBackgroundResource(R.drawable.infamy);
        if(except != 4) conquersStat.setBackgroundResource(R.drawable.conquests);
        if(except != 5){
            conMonStat.setBackgroundResource(R.drawable.monetae);
            if(!imperium) conMonStat.setBackgroundResource(R.drawable.continents);
        }
    }
}
