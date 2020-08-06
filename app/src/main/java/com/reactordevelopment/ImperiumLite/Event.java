package com.reactordevelopment.ImperiumLite;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.support.constraint.ConstraintLayout;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.io.PipedOutputStream;
import java.util.ArrayList;

import static com.reactordevelopment.ImperiumLite.MainActivity.inchHeight;
import static com.reactordevelopment.ImperiumLite.MainActivity.screenHeight;
import static com.reactordevelopment.ImperiumLite.MainActivity.screenWidth;

public class Event extends Game{
    private Context context;
    private float dX;
    private float dY;

    public Event(Context context, String title, String desc, String[] choices, int background, String type){
        this.context = context;
        Log.i("CreateEvent", title+", "+desc);
        mkEvent(title, desc, choices, background, type);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void mkEvent(String title, String desc, String[] choices, int background, String type){
        final RelativeLayout eventLayout = new RelativeLayout(context);
        ImageView eventRound = new ImageView(context);
        eventRound.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        eventRound.setBackgroundResource(background);
        eventLayout.addView(eventRound);
        TextView eventText = new TextView(context);
        TextView titleText = new TextView(context);
        titleText.setLayoutParams(new RelativeLayout.LayoutParams((int)(screenHeight*.6), (int)(screenWidth*.1)));
        eventText.setMovementMethod(new ScrollingMovementMethod());
        eventText.setVerticalScrollBarEnabled(true);
        eventText.setVerticalFadingEdgeEnabled(true);
        eventText.setTextSize(TypedValue.COMPLEX_UNIT_IN, inchHeight*.027f);
        eventText.setText(desc);
        titleText.setText(title);
        titleText.setTextColor(Color.BLACK);
        eventText.setTextColor(Color.BLACK);
        eventLayout.setLayoutParams(new ConstraintLayout.LayoutParams((int)(screenHeight*.4), (int)(screenWidth*.45)));
        titleText.setTextSize(TypedValue.COMPLEX_UNIT_IN, inchHeight*.035f);
        titleText.setGravity(Gravity.CENTER);
        eventText.setLayoutParams(new RelativeLayout.LayoutParams((int)(screenHeight*.35), (int)(screenWidth*.25)));
        ImageButton choice = new ImageButton(context);
        choice.setLayoutParams(new RelativeLayout.LayoutParams((int)(screenHeight*.35), (int)(screenWidth*.1)));
        choice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("RemoveEvent", "rem");
                getMasterLayout().removeView(eventLayout);
            }
        });
        eventLayout.addView(choice);
        TextView choiceText = new TextView(context);
        choiceText.setLayoutParams(new RelativeLayout.LayoutParams((int)(screenHeight*.35), (int)(screenWidth*.1)));
        choiceText.setText(choices[0]);
        choiceText.setTextSize(TypedValue.COMPLEX_UNIT_IN, inchHeight*.027f);
        choiceText.setGravity(Gravity.CENTER);
        eventLayout.addView(choiceText);
        choice.animate().yBy(screenWidth * .32f).setDuration(0);
        choice.animate().xBy(screenWidth*.03f).setDuration(0);
        choiceText.animate().yBy(screenWidth * .32f).setDuration(0);

        //eventText.animate().yBy(-screenWidth*.05f).setDuration(0);
        eventLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getPointerCount() == 1) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            dX = view.getX() - event.getRawX();
                            dY = view.getY() - event.getRawY();
                        case MotionEvent.ACTION_MOVE:
                            if(event.getRawX() + dX > 0
                                    && event.getRawY() + dY > 0
                                    && event.getRawX() + dX < screenHeight*.7
                                    && event.getRawY() + dY  < screenWidth*.5)
                                view.animate().x(event.getRawX() + dX).y(event.getRawY() + dY).setDuration(0).start();
                            else view.animate().x(screenHeight*.4f).y(screenWidth*.3f).setDuration(1000).start();
                            break;
                        default: return false;
                    }
                } else return false;
                return true;
            }
        });
        eventLayout.addView(eventText);
        eventLayout.addView(titleText);
        getMasterLayout().addView(eventLayout);
        eventText.animate().xBy((int)(screenHeight*.05)).setDuration(0);
        eventLayout.animate().x(screenHeight*.15f).y(screenWidth*.1f).setDuration(0);
        eventText.animate().y((int)(screenWidth*.1)).setDuration(0);
    }



}
