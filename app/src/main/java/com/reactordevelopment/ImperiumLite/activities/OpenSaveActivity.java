package com.reactordevelopment.ImperiumLite.activities;

import static com.reactordevelopment.ImperiumLite.activities.MainActivity.SAVE_PATH;
import static com.reactordevelopment.ImperiumLite.activities.MainActivity.onOpenSave;
import static com.reactordevelopment.ImperiumLite.activities.MainActivity.screenHeight;
import static com.reactordevelopment.ImperiumLite.activities.MainActivity.screenWidth;
import static com.reactordevelopment.ImperiumLite.activities.MainActivity.setActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.reactordevelopment.ImperiumLite.components.ListTextView;
import com.reactordevelopment.ImperiumLite.activities.MappedActivities.GameActivity;
import com.reactordevelopment.ImperiumLite.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class OpenSaveActivity extends AppCompatActivity {
    protected static LinearLayout saveLister;
    protected static LinearLayout scrollList;
    private static Context context;
    private static ListTextView[] savesList;
    private static EditText rename;
    private boolean fixClick = false;
    private ImageView top;
    private ImageView bottom;
    private float dY;
    private float lastY;
    private int filesLength = 0;
    private int fileWidth = (int)(screenWidth*.6);
    private int fileHeight = (int)(screenHeight*.05);
    private int scrollPadding = (int)(screenWidth*.1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_save);
        ImageView activityRound = findViewById(R.id.loadRound);
        activityRound.setScaleType(ImageView.ScaleType.FIT_XY);
        context = this;
        Log.i("width", ""+ fileWidth);
        filesLength = listSaves();
        lister();
        quitter();
        openSave();
        deleteSave();
        renameSave();
    }
    @Override
    public void onStart(){
        super.onStart();
        onOpenSave = true;
    }
    @Override
    public void onStop(){
        setActivity("none");
        super.onStop();
    }
    private void refreshList(){
        scrollList.removeView(top);
        scrollList.removeView(bottom);
        for(ListTextView file : savesList) file.deletusFrom();
        listSaves();
    }
    private int listSaves(){
        saveLister = findViewById(R.id.savesList);
        scrollList = findViewById(R.id.scrollList);
        top = new ImageView(context);
        bottom = new ImageView(context);

        top.setBackgroundResource(R.drawable.scrolltop);
        top.setLayoutParams(new LinearLayout.LayoutParams((int)(.1 * fileWidth), (int)(.3 * fileHeight)));
        top.setX(scrollPadding);
        bottom.setBackgroundResource(R.drawable.scrollbottom);
        bottom.setLayoutParams(new LinearLayout.LayoutParams((int)(.1 * fileWidth), (int)(.3 * fileHeight)));
        bottom.setX(scrollPadding);

        Log.d("Files", "Path: " + SAVE_PATH);
        File directory = new File(SAVE_PATH);
        File[] files = directory.listFiles();
        if(files == null) {
            String path = Environment.getExternalStorageDirectory().getPath()+"/Imperium";
            File dir = new File(path + "/Saves");
            if (!dir.exists()) if (!dir.mkdirs()) Log.i("NoSaves", "nomake");
            return 0;
        }

        boolean sorted = false;
        File temp;
        while(!sorted) {
            sorted = true;
            for (int i = 0; i < files.length - 1; i++) {
                if (files[i].lastModified() < files[i+1].lastModified()) {
                    temp = files[i+1];
                    files[i+1] = files[i];
                    files[i] = temp;
                    sorted = false;
                }
            }
        }
        Log.d("Files", "Size: "+ files.length);
        scrollList.addView(top);
        savesList = new ListTextView[files.length];
        for (int i = 0; i < files.length; i++) {
            makeText(files[i].getName(), i);
            Log.d("Files", "FileName:" + files[i].getName());
        }
        scrollList.addView(bottom);
        return files.length;
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
    private void animateAll(float dy){
        top.animate().yBy(dy).setDuration(0);
        bottom.animate().yBy(dy).setDuration(0);
        for(ListTextView v : savesList) v.animateBy(0, dy, 0);
    }
    private void animateAll(float dy, int duration){
        top.animate().yBy(dy).setDuration(duration);
        bottom.animate().yBy(dy).setDuration(duration);
        for(ListTextView v : savesList) v.animateBy(0, dy, duration);
    }
    private void makeText(final String NAME, final int id){
        savesList[id] = new ListTextView(context, id);
        savesList[id].getTextView().setLayoutParams(new LinearLayout.LayoutParams(fileWidth, fileHeight));
        savesList[id].getBackText().setLayoutParams(new LinearLayout.LayoutParams(fileWidth, fileHeight));
        savesList[id].animateTo(0, id * fileHeight, 0);

        Log.i("textY", ""+savesList[id].getTextView().getY());
        savesList[id].getImageView().setLayoutParams(new LinearLayout.LayoutParams((int)(.1*fileWidth), fileHeight));
        savesList[id].getImageView().setBackgroundResource(R.drawable.scrollbodylong);
        savesList[id].getImageView().setX(scrollPadding);
        savesList[id].getTextView().setTextColor(Color.parseColor("#c7ccd4"));
        savesList[id].getBackText().setTextColor(Color.parseColor("#000000"));
        savesList[id].setText(NAME);
        savesList[id].getTextView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deselectFiles(savesList[id].getId());
                if(!savesList[id].getSelected()) savesList[id].getTextView().setTextColor(Color.parseColor("#FFD700"));

                else savesList[id].getTextView().setTextColor(Color.parseColor("#c7ccd4"));
                Log.i("openSave", "clicked: "+NAME+" Selected: "+savesList[id].getSelected());
                savesList[id].setSelected(!savesList[id].getSelected());
            }
        });
        scrollList.addView(savesList[id].getImageView());
        saveLister.addView(savesList[id].getBackText());
        saveLister.addView(savesList[id].getTextView());
    }
    private void deselectFiles(int skipId) {
        for (int i = 0; i < savesList.length; i++)
            if (savesList[i].getId() != skipId){
                savesList[i].getTextView().setTextColor(Color.parseColor("#c7ccd4"));
                savesList[i].setSelected(false);
            }
    }
    private void quitter(){
        ImageButton quitter = findViewById(R.id.quitter);
        quitter.setBackgroundResource(R.drawable.navquit);
        quitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    private void renameSave(){
        rename = findViewById(R.id.rename);
        ImageButton renameSave = findViewById(R.id.renameSave);
        renameSave.setBackgroundResource(R.drawable.rename);
        renameSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < savesList.length; i++) {
                    if (savesList[i].getSelected()) {
                        if(rename.getText().toString().equals("") || rename.getText().toString().equals("Rename Saves"))
                            Toast.makeText(context, "Enter a new game name" , Toast.LENGTH_SHORT).show();
                        else {
                            renameGame(savesList[i].getTextView().getText().toString(), rename.getText().toString());
                            savesList[i].setText(rename.getText().toString()+".txt");
                        }
                        return;
                    }
                }
                Toast.makeText(context, "Select a file to rename" , Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void openSave(){
        ImageButton openSave = findViewById(R.id.openSave);
        openSave.setBackgroundResource(R.drawable.open);
        openSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < savesList.length; i++) {
                    if(savesList[i].getSelected()) {
                        Intent intent = new Intent(context, GameActivity.class);
                        intent.putExtra("loadedGame", loadGame(SAVE_PATH+"/"+savesList[i].getTextView().getText().toString()));
                        intent.putExtra("tag", "loaded");
                        intent.putExtra("loadName", ""+savesList[i].getTextView().getText().toString());
                        startActivity(intent);
                        return;
                    }
                }
                Toast.makeText(context, "Select a file to load" , Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void deleteSave(){
        ImageButton deleteSave = findViewById(R.id.deleteSave);
        deleteSave.setBackgroundResource(R.drawable.destroy);
        deleteSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < savesList.length; i++) {
                    if (savesList[i].getSelected()) {
                        //findViewById(R.id.deleteCheck).animate().y(screenHeight/5).setDuration(500);
                        Log.i("delete", "found"+", "+findViewById(R.id.deleteCheck).getY());
                        //if(!fixClick) ref
                        //fixClick = true;
                        findViewById(R.id.deleteCheck).setVisibility(View.VISIBLE);
                        return;
                    }
                }
                Toast.makeText(context, "Select a file to delete" , Toast.LENGTH_SHORT).show();
            }
        });
        ImageButton yes = findViewById(R.id.yes);
        yes.setBackgroundResource(R.drawable.savesdel);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < savesList.length; i++) {
                    if (savesList[i].getSelected()) {
                        deleteGame(savesList[i].getTextView().getText().toString());
                        refreshList();
                        //findViewById(R.id.deleteCheck).animate().y(2000).setDuration(500);
                        findViewById(R.id.deleteCheck).setVisibility(View.INVISIBLE);
                        return;
                    }
                }

            }
        });
        ImageButton no = findViewById(R.id.no);
        no.setBackgroundResource(R.drawable.cancel);
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //findViewById(R.id.deleteCheck).animate().y(2000).setDuration(500).start();
                findViewById(R.id.deleteCheck).setVisibility(View.INVISIBLE);
            }
        });
    }
    private String loadGame(String saveId){
        FileInputStream fis = null;
        StringBuilder sb = new StringBuilder();
        try {
            fis = new FileInputStream(saveId);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String text;
            while ((text = br.readLine()) != null)
                sb.append(text).append("\n");

        } catch (Exception e) { e.printStackTrace(); }
        finally {
            if (fis != null) {
                try { fis.close(); } catch (IOException e) { e.printStackTrace(); }
            }
        }
        return sb.toString();
    }
    private void deleteGame(String saveId){
        File victim = new File(SAVE_PATH + "/" + saveId);
        if(victim.exists())
            victim.delete();
        if(!victim.exists())
            Toast.makeText(this, "" + SAVE_PATH + "/" + saveId + " is gone, reduced to atoms", Toast.LENGTH_LONG).show();
    }
    private void renameGame(String saveId, String newName){
        File nohbdy = new File(SAVE_PATH + "/" + saveId);
        if(nohbdy.exists())
            nohbdy.renameTo(new File(SAVE_PATH + "/", newName+".txt"));
        if(!nohbdy.exists())
            Toast.makeText(this, ""+saveId+" renamed to "+newName+".txt", Toast.LENGTH_LONG).show();
    }
}
