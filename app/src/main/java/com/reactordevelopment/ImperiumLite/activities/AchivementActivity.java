package com.reactordevelopment.ImperiumLite.activities;

import static com.reactordevelopment.ImperiumLite.activities.MainActivity.BASE_TEXT_SCALE;
import static com.reactordevelopment.ImperiumLite.activities.MainActivity.achives;
import static com.reactordevelopment.ImperiumLite.activities.MainActivity.inchWidth;
import static com.reactordevelopment.ImperiumLite.activities.MainActivity.onAchivement;
import static com.reactordevelopment.ImperiumLite.activities.MainActivity.screenHeight;
import static com.reactordevelopment.ImperiumLite.activities.MainActivity.screenWidth;
import static com.reactordevelopment.ImperiumLite.activities.MainActivity.setActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.reactordevelopment.ImperiumLite.components.AchiveTile;
import com.reactordevelopment.ImperiumLite.activities.MappedActivities.GameActivity;
import com.reactordevelopment.ImperiumLite.R;
import com.reactordevelopment.ImperiumLite.core.Achivements;

public class AchivementActivity extends AppCompatActivity {
    protected static LinearLayout achiveLister;
    protected static LinearLayout scrollList;
    private TextView achiveDesc;
    private ConstraintLayout infoLayout;
    private ImageButton close;
    private ImageButton highlight;
    private ImageView top;
    private ImageView bottom;
    private Context context;
    private float dY;
    private float lastY;
    private static AchiveTile[] achiveTileList;
    protected int achiveWidth = (int)(screenWidth*.64);
    protected int achiveHeight = (int)(screenHeight*.2);
    private int scrollPadding = (int)(screenWidth*.1);
    private AchiveTile achiveTile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achivement);
        context = this;
        init();
        listSaves();
        lister();
        quitter();
    }
    @Override
    public void onStart(){
        super.onStart();
        onAchivement = true;
    }
    public void onStop(){
        setActivity("none");
        super.onStop();
    }
    private void init(){
        achiveDesc = findViewById(R.id.achiveDesc);
        infoLayout = findViewById(R.id.infoLayout);
        close = findViewById(R.id.closeAchiveInfo);
        highlight = findViewById(R.id.highlight);
        achiveDesc.setTextSize(TypedValue.COMPLEX_UNIT_IN,BASE_TEXT_SCALE*inchWidth);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideDesc();
            }
        });
        highlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(GameActivity.getGame() != null) GameActivity.getGame().enablePulse(achiveTile.neededProvs());
                Log.i("achiveNull", ""+(GameActivity.getGame() == null));
                onBackPressed();
            }
        });
    }
    private void listSaves(){
        achiveLister = findViewById(R.id.savesList);
        scrollList = findViewById(R.id.scrollList);
        top = new ImageView(context);
        bottom = new ImageView(context);

        top.setBackgroundResource(R.drawable.scrolltop);
        top.setLayoutParams(new LinearLayout.LayoutParams((int)(.1 * achiveWidth), (int)(.3 * achiveHeight)));
        top.setX(scrollPadding);
        bottom.setBackgroundResource(R.drawable.scrollbottom);
        bottom.setLayoutParams(new LinearLayout.LayoutParams((int)(.1 * achiveWidth), (int)(.3 * achiveHeight)));
        bottom.setX(scrollPadding);
        scrollList.addView(top);
        achiveTileList = new AchiveTile[Achivements.ACHIVE_TAGS.length];
        for (int i = 0; i < achiveTileList.length; i++) {
            makeText(Achivements.ACHIVE_TAGS[i], i);
            Log.d("Achives", "AchiveName:" + Achivements.ACHIVE_TAGS[i]);
        }
        scrollList.addView(bottom);
    }
    @SuppressLint("ClickableViewAccessibility")
    private void lister(){
        scrollList.bringToFront();
        scrollList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getPointerCount() == 1) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            lastY = event.getRawY();
                            break;
                        case MotionEvent.ACTION_MOVE:
                            dY = event.getRawY() - lastY;
                            lastY = event.getRawY();
                            if(bottom.getY() > 50 && top.getY() < screenHeight*.35) animateAll(dY);
                            else if(bottom.getY() <= 50) animateAll(70, 500);
                            else if(top.getY() >= screenHeight*.35) animateAll(-70, 500);
                            Log.i("dY", ""+dY);
                            Log.i("bounds", "Top"+top.getY()+", Bottom"+bottom.getY());
                            break;
                        default:
                            return false;
                    }
                }
                else return false;

                return true;
            }
        });
    }
    public void showDesc(AchiveTile achiveTile){
        Log.i("achiveTile", "infoshown");
        this.achiveTile = achiveTile;
        achiveDesc.setText(achiveTile.getTitleStr()+": "+ achiveTile.getDescStr());
        infoLayout.animate().x(screenWidth*.3f).setDuration(1500);
        if(achiveTile.neededProvs().length == 0) highlight.setVisibility(View.INVISIBLE);
        else highlight.setVisibility(View.VISIBLE);
        infoLayout.setVisibility(View.VISIBLE);
    }
    public void hideDesc(){
        infoLayout.animate().x(1000).setDuration(1500);
    }
    private void animateAll(float dy){
        top.animate().yBy(dy).setDuration(0);
        bottom.animate().yBy(dy).setDuration(0);
        for(AchiveTile v : achiveTileList) v.animateBy(0, dy, 0);
    }
    private void animateAll(float dy, int duration){
        top.animate().yBy(dy).setDuration(duration);
        bottom.animate().yBy(dy).setDuration(duration);
        for(AchiveTile v : achiveTileList) v.animateBy(0, dy, duration);
    }
    private void makeText(final String tag, final int id){
        achiveTileList[id] = new AchiveTile(context, tag, achives.getBoolean(tag, false));
        achiveTileList[id].getCover().setLayoutParams(new LinearLayout.LayoutParams(achiveWidth, achiveHeight));
        achiveTileList[id].getIcon().setLayoutParams(new LinearLayout.LayoutParams(7*achiveWidth/16, 7*achiveHeight/8));
        achiveTileList[id].getTitleView().setLayoutParams(new LinearLayout.LayoutParams(7*achiveWidth/16, 3*achiveHeight/4));
        achiveTileList[id].animateTo(0, id * achiveHeight, 0);

        Log.i("textY", ""+ achiveTileList[id].getCover().getY());
        achiveTileList[id].getScroll().setLayoutParams(new LinearLayout.LayoutParams((int)(.1* achiveWidth), achiveHeight));
        achiveTileList[id].getScroll().setX(scrollPadding);
        achiveTileList[id].getTitleView().setTextColor(Color.BLACK);
        achiveTileList[id].getCover().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDesc(achiveTileList[id]);
            }
        });
        scrollList.addView(achiveTileList[id].getScroll());
        achiveLister.addView(achiveTileList[id].getCover());
        achiveLister.addView(achiveTileList[id].getIcon());
        achiveLister.addView(achiveTileList[id].getTitleView());
    }
    private void quitter(){
        ImageButton quitter = findViewById(R.id.quitter);
        quitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
