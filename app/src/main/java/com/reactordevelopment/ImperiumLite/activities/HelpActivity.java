package com.reactordevelopment.ImperiumLite.activities;

import static com.reactordevelopment.ImperiumLite.activities.MainActivity.onHelp;
import static com.reactordevelopment.ImperiumLite.activities.MainActivity.screenHeight;
import static com.reactordevelopment.ImperiumLite.activities.MainActivity.setActivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.reactordevelopment.ImperiumLite.R;

import java.io.IOException;
import java.io.InputStream;

public class HelpActivity extends AppCompatActivity {
    private TextView helpOut;
    private TextView helpType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        makeComps();
    }
    @Override
    public void onStart(){
        super.onStart();
        onHelp = true;
    }
    @Override
    public void onStop(){
        setActivity("none");
        super.onStop();
    }
    private void makeComps(){
        helpOut = findViewById(R.id.helpOut);
        helpType = findViewById(R.id.helpType);
        ImageButton helpQuit = findViewById(R.id.helpQuit);
        ImageButton gameplay = findViewById(R.id.gameplay); gameplay.setBackgroundResource(R.drawable.gamehelp);
        ImageButton history = findViewById(R.id.histHelp); history.setBackgroundResource(R.drawable.historicalhelp);
        ImageButton turns = findViewById(R.id.turns); turns.setBackgroundResource(R.drawable.turnshelp);
        ImageButton build = findViewById(R.id.buildHelp); build.setBackgroundResource(R.drawable.buildhelp);
        ImageButton impValue = findViewById(R.id.impValue); impValue.setBackgroundResource(R.drawable.valuehelp);

        ImageButton clasInfo = findViewById(R.id.clasInfo); clasInfo.setBackgroundResource(R.drawable.classichelp); clasInfo.setPadding(0, (int) (screenHeight/100.0), 0, 0);
        ImageButton clasContinents = findViewById(R.id.clasContinents); clasContinents.setBackgroundResource(R.drawable.continentshelp);

        ImageButton impInfo = findViewById(R.id.impInfo); impInfo.setBackgroundResource(R.drawable.imperiumhelp);
        ImageButton impAttrition = findViewById(R.id.impAttrition); impAttrition.setBackgroundResource(R.drawable.attritionhelp);
        ImageButton impDevelopment = findViewById(R.id.impDevelopment); impDevelopment.setBackgroundResource(R.drawable.develophelp);
        ImageButton impForts = findViewById(R.id.impForts); impForts.setBackgroundResource(R.drawable.forthelp);
        ImageButton impDevastation = findViewById(R.id.impDevastation); impDevastation.setBackgroundResource(R.drawable.devastathelp);
        ImageButton impMonetae = findViewById(R.id.impMonetae); impMonetae.setBackgroundResource(R.drawable.monetaehelp);

        helpQuit.setBackgroundResource(R.drawable.quit);
        helpQuit.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                finish(); }});
        gameplay.setOnClickListener(clicker("gameplay"));
        history.setOnClickListener(clicker("historical"));
        turns.setOnClickListener(clicker("turns"));
        build.setOnClickListener(clicker("build"));
        impValue.setOnClickListener(clicker("impValue"));

        clasInfo.setOnClickListener(clicker("clasInfo"));
        clasContinents.setOnClickListener(clicker("clasContinents"));

        impInfo.setOnClickListener(clicker("impInfo"));
        impAttrition.setOnClickListener(clicker("impAttrition"));
        impDevelopment.setOnClickListener(clicker("impDevelopment"));
        impForts.setOnClickListener(clicker("impForts"));
        impDevastation.setOnClickListener(clicker("impDevastation"));
        impMonetae.setOnClickListener(clicker("impMonetae"));

        build.performClick(); //default info
    }
    private View.OnClickListener clicker(final String type){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    InputStream stream = getAssets().open("sacredTexts/help/"+type+".txt");
                    int size = stream.available();
                    byte[] buffer = new byte[size];
                    stream.read(buffer);
                    stream.close();
                    String out = new String(buffer);
                    helpType.setText(out.substring(0, out.indexOf('|')));
                    helpOut.setText(out.substring(out.indexOf('|')+1));
                } catch (IOException e) { e.printStackTrace(); }
            }
        };
    }
}
