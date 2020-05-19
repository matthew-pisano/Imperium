package com.reactordevelopment.ImperiumLite;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class AchiveTile extends AchivementActivity {
    private ImageButton cover;
    private ImageView icon;
    private TextView title;
    private String titleStr;
    private ImageView scroll;
    private String descStr;
    private AchiveTile self;
    private Object[] info;

    public AchiveTile(Context context, String tag, boolean got){
        info = Achivements.infoFromTag(tag);
        Bitmap prime;
        cover = new ImageButton(context);
        cover.setBackgroundResource(R.drawable.achivecover);
        icon = new ImageView(context);
        prime = BitmapFactory.decodeResource(context.getResources(), (int)info[2]);
        icon.setImageBitmap(prime);
        scroll = new ImageView(context);
        scroll.setBackgroundResource(R.drawable.scrollbodylong);
        titleStr = (String)info[0];
        descStr = (String)info[1];
        title = new TextView(context);
        title.setText(titleStr);
        self = this;
        Log.i(tag, "Got: "+got);
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        if(!got) {
            icon.setColorFilter(filter);
        }
        //clicks();
    }
    public String getTitleStr(){return titleStr;}
    public String getDescStr(){return descStr;}
    public ImageButton getCover(){return cover;}
    public ImageView getIcon(){return icon;}
    public ImageView getScroll(){return scroll;}
    public TextView getTitleView(){return title;}
    public int[] neededProvs(){return (int[])info[3];}

    public void animateTo(float x, float y, long duration){
        cover.animate().x(x).y(y).setDuration(duration);
        icon.animate().x(x).y(y+achiveHeight/8).setDuration(duration);
        title.animate().x(x+achiveWidth/2).y(y+achiveHeight/2).setDuration(duration);
    }
    public void animateBy(float dx, float dy, long duration){
        cover.animate().xBy(dx).yBy(dy).setDuration(duration);
        icon.animate().xBy(dx).yBy(dy).setDuration(duration);
        title.animate().xBy(dx).yBy(dy).setDuration(duration);
        scroll.animate().yBy(dy).setDuration(duration);
    }
    private void clicks(){
        cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("info", "sjowslh");
                showDesc(self);
            }
        });
    }
}
