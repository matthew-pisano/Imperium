package com.reactordevelopment.Imperium;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import static com.reactordevelopment.Imperium.MainActivity.*;

public class OptionsActivity extends AppCompatActivity {
    private static final double BAR_WIDTH = screenWidth*.5;
    private static final double BIG_WIDTH = screenWidth*.35;
    private static final double SMOL_WIDTH = screenWidth*.2;
    private static final int ATT_MIN = 200;
    private static final int ATT_MAX= 1000-ATT_MIN;
    private static final int SPE_MIN = 200;
    private static final int SPE_MAX= 5000-SPE_MIN;
    private SeekBar volScroll;
    private ImageView volBar;
    private TextView volAt;
    private ImageButton volCheck;

    private SeekBar attScroll;
    private ImageView attBar;
    private TextView attAt;
    private ImageButton attCheck;

    private SeekBar spedScroll;
    private ImageView spedBar;
    private TextView spedAt;
    private ImageButton spedCheck;

    private ImageView big;
    private ImageView smol;
    private static Context context;
    private long t0 = 0;
    private Point s0 = new Point();
    private Point sc = new Point();
    private double rotation = 0;
    private double omega;
    private SharedPreferences vars;
    private SharedPreferences.Editor edit;
    private static int checked;
    private int music;
    private int attackSpeed;
    private int turnSpeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oprtions);
        checked = -1;
        context = this;
        vars = context.getSharedPreferences("vars", 0);
        music = vars.getInt("music", 100);
        attackSpeed = vars.getInt("attackSpeed", ATT_MAX+ATT_MIN);
        turnSpeed = vars.getInt("turnSpeed", SPE_MAX+SPE_MIN);
        makeComps();
        seekSet();
        makeChecks();
        spinnyBoi();
        spinner();
    }
    @Override
    public void onStart(){
        super.onStart();
        onOptions = true;
    }
    @Override
    public void onStop(){
        setActivity("none");
        super.onStop();
    }
    @Override
    public void onDestroy(){
        savePrefs();
        super.onDestroy();
    }
    @Override
    public void onBackPressed(){
        finish();
    }
    private void makeChecks(){
        volCheck.setBackgroundResource(R.drawable.unselect);
        volCheck.setOnClickListener(onCheck(0));
        attCheck.setBackgroundResource(R.drawable.unselect);
        attCheck.setOnClickListener(onCheck(1));
        spedCheck.setBackgroundResource(R.drawable.unselect);
        spedCheck.setOnClickListener(onCheck(2));
    }
    private void makeComps(){
        big = findViewById(R.id.big);
        smol = findViewById(R.id.smol);
        big.setPivotX((float) (BIG_WIDTH/2));
        big.setPivotY((float) (BIG_WIDTH/2));
        smol.setPivotX((float) (SMOL_WIDTH/2));
        smol.setPivotY((float) (SMOL_WIDTH/2));
        volScroll = findViewById(R.id.volScroll);
        volBar = findViewById(R.id.volBar);
        volBar.setBackgroundResource(R.drawable.progressbar);
        volAt = findViewById(R.id.volAt);
        volAt.setTextSize(TypedValue.COMPLEX_UNIT_IN, (float) (BASE_TEXT_SCALE*inchWidth));
        volCheck = findViewById(R.id.volCheck);
        spedScroll = findViewById(R.id.spedScroll);
        spedBar = findViewById(R.id.spedBar);
        spedBar.setBackgroundResource(R.drawable.progressbar);
        spedAt = findViewById(R.id.spedAt);
        spedAt.setTextSize(TypedValue.COMPLEX_UNIT_IN, (float) (BASE_TEXT_SCALE*inchWidth));
        spedCheck = findViewById(R.id.spedCheck);
        attScroll = findViewById(R.id.attScroll);
        attBar = findViewById(R.id.attBar);
        attBar.setBackgroundResource(R.drawable.progressbar);
        attAt = findViewById(R.id.attAt);
        attAt.setTextSize(TypedValue.COMPLEX_UNIT_IN, (float) (BASE_TEXT_SCALE*inchWidth));
        attCheck = findViewById(R.id.attCheck);
        volAt.setText(music+"%");
        attAt.setText(attackSpeed+"ms");
        spedAt.setText(turnSpeed+"ms");

        ImageButton optionsQuit = findViewById(R.id.optionsQuit);
        optionsQuit.setBackgroundResource(R.drawable.quit);
        optionsQuit.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                finish(); }});

    }
    private View.OnClickListener onCheck(final int type){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checked == type) checked = -1;
                else checked = type;
                volCheck.setBackgroundResource(R.drawable.unselect);
                attCheck.setBackgroundResource(R.drawable.unselect);
                spedCheck.setBackgroundResource(R.drawable.unselect);
                if(checked == 0) volCheck.setBackgroundResource(R.drawable.select);
                if(checked == 1) attCheck.setBackgroundResource(R.drawable.select);
                if(checked == 2) spedCheck.setBackgroundResource(R.drawable.select);
            }
        };
    }
    private void spinnyBoi(){
        big.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.i("Touch", "gearDown");
                        t0 = System.currentTimeMillis();
                        s0 = new Point((int)event.getX(), (int)event.getY());
                        sc = new Point((int)(s0.x-BIG_WIDTH/2), (int)(s0.y-BIG_WIDTH/2));
                        break;
                    case MotionEvent.ACTION_MOVE:
                        //Log.i("Touch", "gearMove");
                        long dt = System.currentTimeMillis()-t0;
                        Point ds = new Point((int)event.getX()-s0.x, (int)event.getY()-s0.y);
                        double[] v = {(double)ds.x/(int)dt, (double)ds.y/(int)dt};
                        //Log.i("Velocity", "Vx: "+v[0]+"Vy: "+v[1]);
                        //Log.i("CenterDist", "Sx: "+sc.x+"Sy: "+sc.y);
                        omega = (v[0]*(sc.y)-v[1]*(sc.x))/Math.sqrt(sc.x*sc.x+sc.y*sc.y);
                        t0 = System.currentTimeMillis();
                        s0 = new Point((int)event.getX(), (int)event.getY());
                        sc = new Point((int)(s0.x-BIG_WIDTH/2), (int)(s0.y-BIG_WIDTH/2));
                        break;
                    default: return false;
                }
                return true;
            }
        });
    }
    private void spinner(){
        Thread spinning = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(50);
                        //if(omega != 0)Log.i("Omgeg", ""+omega);
                        if(omega > 0.1) omega-=.1;
                        else if(omega < -.1) omega+=.1;
                        else omega = 0;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                double from = rotation;
                                rotation -= omega*.5;
                                double to = rotation;
                                rotate(to, from);
                                if(checked == 0) volScroll.setProgress((int) (volScroll.getProgress()+(to-from)*10));
                                if(checked == 1) attScroll.setProgress((int) (attScroll.getProgress()+(to-from)*10));
                                if(checked == 2) spedScroll.setProgress((int) (spedScroll.getProgress()+(to-from)*10));
                            }
                        });
                    }
                } catch (InterruptedException e) { e.printStackTrace(); }
            }
        };spinning.start();
    }
    private void seekSet(){
        int progress = vars.getInt("music", 100);
        volScroll.setProgress(progress);
        animateX((int) (BAR_WIDTH*(-1+progress/100.0)), volBar, volAt);
        volScroll.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                animateX((int) (BAR_WIDTH*(-1+progress/100.0)), volBar, volAt);
                music = progress;
                edit = vars.edit();
                edit.putInt("music", music);
                edit.commit();
                volAt.setText(""+music+"%");
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override public void onStopTrackingTouch(SeekBar seekBar) { }
        });
        progress = (int)(vars.getInt("attackSpeed", ATT_MAX+ATT_MIN)/(double)(ATT_MAX+ATT_MIN)*100);
        attScroll.setProgress(progress);
        animateX((int) (BAR_WIDTH*(-1+progress/100.0)), attBar, attAt);
        attScroll.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                animateX((int) (BAR_WIDTH*(-1+progress/100.0)), attBar, attAt);
                attackSpeed = (int)(progress/100.0*ATT_MAX+ATT_MIN);
                attAt.setText(""+attackSpeed+"ms");
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override public void onStopTrackingTouch(SeekBar seekBar) { }
        });
        progress = (int)(vars.getInt("turnSpeed", SPE_MAX+SPE_MIN)/(double)(SPE_MAX+SPE_MIN)*100);
        spedScroll.setProgress(progress);
        animateX((int) (BAR_WIDTH*(-1+progress/100.0)), spedBar, spedAt);
        spedScroll.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                animateX((int) (BAR_WIDTH*(-1+progress/100.0)), spedBar, spedAt);
                turnSpeed = (int)(progress/100.0*SPE_MAX+SPE_MIN);
                spedAt.setText(""+turnSpeed+"ms");
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override public void onStopTrackingTouch(SeekBar seekBar) { }
        });
    }
    private void animateX(int to, ImageView bar, TextView at){
        bar.animate().x(to).setDuration(0);
        if(to > -BAR_WIDTH+at.getWidth() && to < at.getWidth())at.animate().x((float) (to+BAR_WIDTH-at.getWidth())).setDuration(0);
    }
    private void rotate(double to, double from){
        AnimationSet animSet = new AnimationSet(true);
        AnimationSet smolSet = new AnimationSet(true);
        animSet.setInterpolator(new DecelerateInterpolator());
        smolSet.setInterpolator(new DecelerateInterpolator());
        animSet.setFillAfter(true);
        animSet.setFillEnabled(true);
        smolSet.setFillAfter(true);
        smolSet.setFillEnabled(true);
        final RotateAnimation animRotate = new RotateAnimation((float)Math.toDegrees(from), (float) Math.toDegrees(to),
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        final RotateAnimation smolRotate = new RotateAnimation((float)Math.toDegrees(to), (float) Math.toDegrees(from),
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        animRotate.setDuration(50);
        smolRotate.setDuration(50);
        animRotate.setFillAfter(true);
        smolSet.setFillAfter(true);
        animSet.addAnimation(animRotate);
        smolSet.addAnimation(smolRotate);
        big.startAnimation(animSet);
        smol.startAnimation(smolSet);
    }
    private void savePrefs(){
        Log.i("prefs", "sved");
        edit = vars.edit();
        edit.putInt("music", music);
        edit.putInt("attackSpeed", attackSpeed);
        edit.putInt("turnSpeed", turnSpeed);
        edit.commit();
        Log.i("prefs", ""+vars.getInt("music", 50));
    }
}
