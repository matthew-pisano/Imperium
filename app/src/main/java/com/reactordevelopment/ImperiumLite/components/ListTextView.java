package com.reactordevelopment.ImperiumLite.components;

import android.content.Context;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.TextView;

import com.reactordevelopment.ImperiumLite.activities.OpenSaveActivity;

public class ListTextView extends OpenSaveActivity {
    private int id;
    private Boolean selected;
    private TextView text;
    private TextView backText;
    private ImageView scroll;
    private int textSize;

    public ListTextView(Context context, int id){
        this.id = id;
        selected = false;
        text = new TextView(context);
        backText = new TextView(context);
        scroll = new ImageView(context);
    }
    public TextView getBackText(){return backText;}
    public TextView getTextView(){return text;}
    public ImageView getImageView(){return scroll;}
    public Boolean getSelected(){return selected;}
    public void setSelected(Boolean set){selected = set;}
    public int getId(){return id;}

    public void setText(String rename){
        setTextSize(35-rename.length()/2);
        text.setText(rename);
        backText.setText(rename);
    }
    public void setTextSize(int size){
        if(size > 20) size = 20;
        if(size < 10) size = 10;
        textSize = size;
        text.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize);
        backText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize);
        backText.animate().xBy((float)(.1*textSize)).setDuration(0);
    }
    public void deletusFrom(){
        saveLister.removeView(text);
        saveLister.removeView(backText);
        scrollList.removeView(scroll);
    }
    public void animateTo(float x, float y, long duration){
        text.animate().x(x).y(y).setDuration(duration);
        backText.animate().x((float)(x+.1*textSize)).y(y).setDuration(duration);
    }
    public void animateBy(float dx, float dy, long duration){
        text.animate().xBy(dx).yBy(dy).setDuration(duration);
        backText.animate().xBy(dx).yBy(dy).setDuration(duration);
        scroll.animate().yBy(dy).setDuration(duration);
    }
}
