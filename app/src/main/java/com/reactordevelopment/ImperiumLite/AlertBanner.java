package com.reactordevelopment.ImperiumLite;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import static com.reactordevelopment.ImperiumLite.MainActivity.*;

public class AlertBanner extends GameActivity {
    private ImageButton alertButton;
    private int type;
    private String group;
    private String fromTag;
    private int id;

    public AlertBanner(Context context, int id, int type, String group, String from){
        alertButton = new ImageButton(context);
        this.id = id;
        this.type = type;
        fromTag = from;
        this.group = group;
        inital();
        Log.i("Alert", ""+type+from);
    }
    private void inital(){
        alertButton.setLayoutParams(new LinearLayout.LayoutParams((int)(screenHeight*.05), (int)(screenWidth*.1)));
        alertButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { showDipPop(type, group, fromTag); }});
        if(type == 10) alertButton.setBackgroundResource(R.drawable.ally);
        if(type == 20) alertButton.setBackgroundResource(R.drawable.sunject);
        if(type == 30) alertButton.setBackgroundResource(R.drawable.swordcross);
        if(type == 41) alertButton.setBackgroundResource(R.drawable.peace);
        if(type == 40) alertButton.setBackgroundResource(R.drawable.surrender);
    }

    public ImageButton getAlertButton(){return alertButton;}
    public int getType(){return type;}
    public String getFromTag(){return fromTag;}
    public String getGroup(){return group;}
}
