package com.reactordevelopment.Imperium;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import static com.reactordevelopment.Imperium.MainActivity.*;

public class TutorialActivity extends AppCompatActivity {
    private int tutorialAt;
    private SharedPreferences tutScreen;
    private SharedPreferences.Editor edit;
    private Context context;
    private static final int[] IMAGES = new int[]{R.drawable.tutmain, R.drawable.tutbuild, R.drawable.tuthistbuild,
            R.drawable.tuthist, R.drawable.tuthist2, R.drawable.tuthistbuild2, R.drawable.tutplace, R.drawable.tutattc,
            R.drawable.tutendattc, R.drawable.tuttrans};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        context = this;
        tutScreen = this.getSharedPreferences("tutorial", 0);
        edit = tutScreen.edit();
        tutorialAt = tutScreen.getInt("at", 0);
        buttons();
    }
    @Override
    public void onStart(){
        super.onStart();
        onTutorial = true;
    }
    @Override
    public void onStop(){
        setActivity("none");
        super.onStop();
    }
    private void buttons(){
        ImageButton back = findViewById(R.id.back);
        ImageButton forth = findViewById(R.id.forth);
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams((int)(screenWidth*.2), (int) (screenHeight*.1));
        //params.leftMargin = (int) (screenWidth*.07);
        //params.topMargin = (int) (screenWidth*.96);
        back.setLayoutParams(params);
        //params.leftMargin = (int) (screenWidth*.93);
        forth.setLayoutParams(params);
        if(tutorialAt < 3 || tutorialAt == 5) {
            back.animate().x(screenWidth * .06f).y(screenHeight * .86f).setDuration(0);
            forth.animate().x(screenWidth * .77f).y(screenHeight * .86f).setDuration(0);
        }else{
            back.animate().x(screenHeight * .06f).y(screenWidth * .74f).setDuration(0);
            forth.animate().x(screenHeight * .86f).y(screenWidth * .74f).setDuration(0);
        }
        final ImageView tutorialImage = findViewById(R.id.tutorialRound);
        tutorialImage.setBackgroundResource(IMAGES[tutorialAt]);

        back.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if(tutorialAt > 0) tutorialAt--;
                edit.putInt("at", tutorialAt);
                edit.commit();
                if(tutorialAt == 3) setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                if(tutorialAt == 2) setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
                if(tutorialAt == 6) setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                if(tutorialAt == 5) setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);}});
                tutorialImage.setBackgroundResource(IMAGES[tutorialAt]);
        forth.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if(tutorialAt < IMAGES.length-1) tutorialAt++;
                else tutorialAt = 0;
                edit.putInt("at", tutorialAt);
                edit.commit();
                if(tutorialAt == 3) setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                if(tutorialAt == 2) setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
                if(tutorialAt == 6) setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                if(tutorialAt == 5) setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
                if(tutorialAt == 0) setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
                tutorialImage.setBackgroundResource(IMAGES[tutorialAt]); }});
    }
}
