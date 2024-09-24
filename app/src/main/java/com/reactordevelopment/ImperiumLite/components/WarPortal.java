package com.reactordevelopment.ImperiumLite.components;

import static com.reactordevelopment.ImperiumLite.activities.MainActivity.screenHeight;
import static com.reactordevelopment.ImperiumLite.activities.MainActivity.screenWidth;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.reactordevelopment.ImperiumLite.R;
import com.reactordevelopment.ImperiumLite.activities.MappedActivities.GameActivity;

public class WarPortal extends GameActivity {
    //private ImageButton flag;
    private ImageView frame;
    //private ImageView swords;
    private Context context;
    private String ident;
    private String enemyTag;

    public WarPortal(Context context, String tag){
        this.context = context;
        this.ident = tag;
        enemyTag = game.getCurrentPlayer().splitAttDef(ident)[1];
        Log.i("warportalCreatedwithtag", tag);
        init();
    }
    public String getIdent(){return ident;}
    private void init(){
        //flag = new ImageButton(context);
        frame = new ImageView(context);
        //swords = new ImageView(context);
        //flag.setLayoutParams(new LinearLayout.LayoutParams((int)(screenHeight*.11), (int)(screenWidth*.1)));
        frame.setLayoutParams(new LinearLayout.LayoutParams((int)(screenHeight*.12), (int)(screenWidth*.13)));
        //swords.setLayoutParams(new LinearLayout.LayoutParams((int)(screenHeight*.04), (int)(screenWidth*.05)));
        //frame.setBackgroundResource(R.drawable.warframe);
        frame.setImageBitmap(overlay());
        Log.i("enemyPortal", enemyTag);
        //flag.setBackgroundResource(game.playerFromTag(enemyTag).getFlag());
        //swords.setBackgroundResource(R.drawable.swordcross);
        frame.setOnClickListener(new View.OnClickListener() { //was flag
            @Override
            public void onClick(View v) {
                showDipPop(4, ident, "#nn");
            }
        });
    }
    public void addViews(float x){
        getPotalHolder().addView(frame);
        //getPotalHolder().addView(flag);
        //getPotalHolder().addView(swords);
        //swords.bringToFront();
        //frame.animate().x(x).setDuration(0);
        //flag.animate().x(x+screenWidth*.013f).y(x+screenHeight*.01f).setDuration(0);
        //swords.animate().x(x+screenWidth*.07f).y(x+screenHeight*.04f).setDuration(0);
    }
    public Bitmap overlay() {
        Bitmap frameBmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.warframe);
        Log.i("WarFrameImg", "w: "+frameBmp.getWidth()+", H: "+frameBmp.getHeight());
        Bitmap flagBmp;
        if(game.playerFromTag(enemyTag) != null)
            flagBmp = BitmapFactory.decodeResource(context.getResources(), game.playerFromTag(enemyTag).getFlag());
        else
            flagBmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.noflag);

        Log.i("WarFrameImg", "w: "+flagBmp.getWidth()+", H: "+flagBmp.getHeight());
        flagBmp = Bitmap.createScaledBitmap(flagBmp, (int) (frameBmp.getWidth()*.9), (int) (frameBmp.getHeight()*.8), false);
        Log.i("WarFrameImg", "w: "+flagBmp.getWidth()+", H: "+flagBmp.getHeight());
        Bitmap swordsBmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.swordcross);
        Log.i("WarFrameImg", "w: "+swordsBmp.getWidth()+", H: "+swordsBmp.getHeight());
        swordsBmp = Bitmap.createScaledBitmap(swordsBmp, (int) (frameBmp.getWidth()*.38), (int) (frameBmp.getHeight()*.5), false);
        Log.i("WarFrameImg", "w: "+swordsBmp.getWidth()+", H: "+swordsBmp.getHeight());
        Bitmap bmOverlay = Bitmap.createBitmap(frameBmp.getWidth(), frameBmp.getHeight(), frameBmp.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(frameBmp, new Matrix(), null);
        canvas.drawBitmap(flagBmp, (int) (frameBmp.getWidth()*.07), (int) (frameBmp.getHeight()*.12), null);
        canvas.drawBitmap(swordsBmp, (int) (frameBmp.getWidth()*.33), (int) (frameBmp.getHeight()*.4), null);
        return bmOverlay;
    }
    public void removeViews(){
        getPotalHolder().removeView(frame);
        //getPotalHolder().removeView(flag);
        //getPotalHolder().removeView(swords);
    }
}
