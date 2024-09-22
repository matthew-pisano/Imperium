package com.reactordevelopment.ImperiumLite;

import static com.reactordevelopment.ImperiumLite.MainActivity.SAVE_VERSION;
import static com.reactordevelopment.ImperiumLite.MainActivity.onAbout;
import static com.reactordevelopment.ImperiumLite.MainActivity.setActivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

public class AboutActivity extends AppCompatActivity {
    private TextView infoOut;
    private TextView infoType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ImageView aboutRound = findViewById(R.id.aboutRound);
        aboutRound.setScaleType(ImageView.ScaleType.FIT_XY);
        makeComps();
    }
    @Override
    public void onStart(){
        super.onStart();
        onAbout = true;
    }
    @Override
    public void onStop(){
        setActivity("none");
        super.onStop();
    }
    private void makeComps(){
        infoOut = findViewById(R.id.infoOut);
        infoType = findViewById(R.id.infoType);

        ImageButton aboutQuit = findViewById(R.id.aboutQuit);
        aboutQuit.setBackgroundResource(R.drawable.quit);
        aboutQuit.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                finish(); }});
        ImageButton aboutScroll = findViewById(R.id.aboutScroll);
        aboutScroll.setBackgroundResource(R.drawable.aboutscroll);
        aboutScroll.setOnClickListener(clicker("about"));

        ImageButton creditScroll = findViewById(R.id.creditScroll);
        creditScroll.setBackgroundResource(R.drawable.creditsscroll);
        creditScroll.setOnClickListener(clicker("credits"));

        ImageButton licensingScroll = findViewById(R.id.licensingScroll);
        licensingScroll.setBackgroundResource(R.drawable.licensingscroll);
        licensingScroll.setOnClickListener(clicker("licensing"));

        ImageButton changelogScroll = findViewById(R.id.changelogScroll);
        changelogScroll.setBackgroundResource(R.drawable.changescroll);
        changelogScroll.setOnClickListener(clicker("changelog"));
    }
    private View.OnClickListener clicker(final String type){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    InputStream stream = getAssets().open("sacredTexts/about/"+type+".txt");
                    int size = stream.available();
                    byte[] buffer = new byte[size];
                    stream.read(buffer);
                    stream.close();
                    String out = new String(buffer);
                    if(out.contains("Version-")) out = out.replace("Version-", "Version: "+BuildConfig.VERSION_NAME);
                    if(out.contains("Save Encoding-")) out = out.replace("Save Encoding-", "Save Encoding: "+SAVE_VERSION);
                    infoOut.setText(out);
                    infoType.setText((""+type.charAt(0)).toUpperCase()+type.substring(1));
                } catch (IOException e) { e.printStackTrace(); }
            }
        };
    }
}
