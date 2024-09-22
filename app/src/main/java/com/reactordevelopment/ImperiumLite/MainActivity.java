package com.reactordevelopment.ImperiumLite;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

import static com.reactordevelopment.ImperiumLite.GameActivity.*;

//7/30/19 (started)
//2221 lines main code
//892 lines manifest code
//3083 lines total

//updated 3/29/20
//3307 lines manifest code
//10420 lines main code
//13727 lines total

public class MainActivity extends AppCompatActivity {
    public static final String AUTO_SAVE_ID = "autosave.imprm";
    private static final int ALPHA = 200;
    public static final int PLAYER_NONE = Color.argb(ALPHA, 188, 216, 157);
    public static final int BURN_COLOR = Color.argb(ALPHA, 93, 82, 60);
    public static final int GROW_COLOR = Color.argb(ALPHA, 0, 255, 68);
    public static final int FORT_COLOR = Color.argb(ALPHA, 0, 110, 255);
    public static final int DEV_COLOR = Color.argb(ALPHA, 255, 245, 0);
    public static final int DECAY_COLOR = Color.argb(ALPHA, 255, 0, 0);
    public static final int MIGHT_COLOR = Color.argb(ALPHA, 0, 255, 0);
    public static final int ALLY_COLOR = Color.argb(ALPHA, 0, 148, 7);
    public static final int TRUCE_COLOR = Color.argb(ALPHA, 247, 249, 215);
    public static final int SELF_COLOR = Color.argb(ALPHA, 86, 225, 91);
    public static final int SUBJECT_COLOR = Color.argb(ALPHA, 0, 245, 139);
    public static final int OVERLORD_COLOR = Color.argb(ALPHA, 92, 135, 255);
    public static final int MONETAE_TO_TROOPS = 5;
    public static final double SUBJECT_INCOME = .1;
    public static final int TRUCE_TIMER = 5;
    public static final int YEET = View.GONE;
    public static final int START_PAUSE = 500;
    public static final String SAVE_PATH = Environment.getExternalStorageDirectory().getPath()+"/Imperium/Saves";
    public static final String MUSIC_PATH = Environment.getExternalStorageDirectory().getPath()+"/Imperium/Music";
    public static final int[] SAVE_FORM = {3, 5, 4, 4, 4, 5, 4, 4, 3};
    public static final float BASE_TEXT_SCALE =  .05f;
    public static final int DEFAULT_YEAR_ALP = 17;
    public static final int DEFAULT_YEAR_ROM = 414;
    public static final int DEFAULT_YEAR_KAI = 1917;
    public static final int DEFAULT_YEAR_VIR = 2023;
    public static final boolean DEV_MODE = false;
    public static int devType = 0;
    private static boolean devScrollOpen = false;
    //turnNum, provTroops, playerTroops, Monetae, infamy, development, devastation, attrition, ownerId
    private int STORAGE_PERMISSION_CODE = 1;
    public static final double SAVE_VERSION = 1.31;
    //public static final boolean MAIN_APP_DED = true;

    private static String[] tracks;
    private static SharedPreferences vars;
    public static SharedPreferences activity;
    private static SharedPreferences musicOn;
    private static SharedPreferences firstLoad;
    public static SharedPreferences achives;
    public static String prevActivity;
    private static TextView version;
    private static Thread splashZoom = null;
    private static Context context;
    public static int screenWidth = -1;
    public static int screenHeight = -1;
    public static int inchWidth = -1;
    public static int inchHeight = -1;
    private static float fadeVol;
    private Activity mainActivity;
    private static Thread musicCycle;
    private static MediaPlayer media;
    private static int currentSongId = 0;

    public static boolean onMain = false;
    public static boolean onBuild = false;
    public static boolean onAbout = false;
    public static boolean onAchivement = false;
    public static boolean onGame = false;
    public static boolean onHelp = false;
    public static boolean onOpenSave = false;
    public static boolean onOptions = false;
    public static boolean onStats = false;
    public static boolean onTutorial = false;
    private static boolean musicPaused = false;
    private static boolean showNotesOnStart;
    public static int gameAt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        mainActivity = this;
        gameAt = -1;
        init();
        makeButtons();
        textMaker();
        music();
        startThread();
        setActivity("none");
        setMusicActivity("menu");
        onMain = true;
        Log.i("screen", activity.getString("activity", "none"));
    }
    @Override
    public void onDestroy() {
        Log.i("here", "gerf");
        //SharedPreferences.Editor editor = activity.edit();
        //editor.putString("activity", "none");
        //editor.commit();
        //endMusic();
        Log.i("screen", activity.getString("activity", "none"));
        super.onDestroy();
    }
    @Override
    public void onStart(){
        super.onStart();
        onMain = true;
    }
    @Override
    public void onStop(){
        setActivity("none");
        super.onStop();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.i("Permissions", "reuwest");
        if (requestCode == STORAGE_PERMISSION_CODE)  {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                Log.i("Perm", ""+(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED));
            } else
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }

    public static String formatInt(int num, int digits) {
        String numStr = ""+num;
        if(num == -1) numStr = "n";
        String formatted = "";
        for(int i=0; i<digits-numStr.length(); i++)
            formatted += "0";
        formatted += numStr;
        if(formatted.length() > digits) formatted = formatted.substring(0, digits);
        return formatted;
    }
    public static String formatDouble(double num, int digits){
        String formatted = "";
        if((""+num).length() >= digits) formatted = (""+num).substring(0, digits);
        else {
            for (int i = 0; i < digits - ("" + num).length(); i++)
                formatted += "0";
            formatted += num;
        }
        if(formatted.length() > digits) formatted = formatted.substring(0, digits);
        return formatted;
    }
    private void releaseNotes(){
        ImageButton updateBanner = findViewById(R.id.updateBanner);
        ImageButton superintelligent = findViewById(R.id.superintelligent);
        final ImageButton notesToggle = findViewById(R.id.notesToggle);
        final ImageButton notesCheck = findViewById(R.id.notesCheck);
        final ConstraintLayout releaseLayout = findViewById(R.id.releaseLayout);
        final TextView releaseNotes = findViewById(R.id.releaseText);
        ImageButton closeNotes = findViewById(R.id.closeNotes);
        releaseNotes.setMovementMethod(new ScrollingMovementMethod());
        releaseNotes.setTextSize(TypedValue.COMPLEX_UNIT_IN,.7f*BASE_TEXT_SCALE*inchWidth);
        byte[] buffer = new byte[0];
        String changeText = "";
        try {
            InputStream stream = getAssets().open("sacredTexts/about/changelog.txt");
            int size = stream.available();
            buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            changeText = new String(buffer);
            changeText = changeText.substring(0, changeText.indexOf("-------"));
            if(changeText.contains("Version-")) changeText = changeText.replace("Version-", "Version: "+BuildConfig.VERSION_NAME);
            if(changeText.contains("Save Encoding-")) changeText = changeText.replace("Save Encoding-", "Save Encoding: "+SAVE_VERSION);
        } catch (IOException e) { e.printStackTrace(); }
        final String finalChangeText = changeText;
        final View.OnClickListener defaultNotesCk = new View.OnClickListener() {
            @Override public void onClick(View v) {
                Log.i("NotesToggle", showNotesOnStart+", "+vars.getBoolean("notesOnStart", true));
                showNotesOnStart = !showNotesOnStart;
                SharedPreferences.Editor edit = vars.edit();
                if(showNotesOnStart) notesCheck.setVisibility(View.VISIBLE);
                else notesCheck.setVisibility(View.INVISIBLE);
                edit.putBoolean("notesOnStart", showNotesOnStart);
                edit.commit();
                Log.i("NotesToggle2", showNotesOnStart+", "+vars.getBoolean("notesOnStart", true));
            }};
        notesToggle.setOnClickListener(defaultNotesCk);
        superintelligent.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                notesCheck.setVisibility(View.INVISIBLE);
                notesToggle.setBackgroundResource(R.drawable.tostore);
                notesToggle.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("market://details?id=com.reactordevelopment.superintelligent"));
                        startActivity(intent);
                    }});
                releaseNotes.setText("Check out Superintelligent\nThe latest creation of Reactor Development" +
                        "\nIn Superintelligent, you are the world's first superintelligent artificial intelligence.  You have one simple goal: explore the universe, through any means necessary.\n" +
                        "\nGrow your knowledge through experience and upgrade your algorithms and technology through the tech tree.  Work with humanity to achieve utopia or vie for dominance as the world sole intelligence.");
                releaseLayout.animate().y(screenHeight*.4f).setDuration(500);
            }});

        updateBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(showNotesOnStart) notesCheck.setVisibility(View.VISIBLE);
                else notesCheck.setVisibility(View.INVISIBLE);
                releaseNotes.setText(finalChangeText);
                notesToggle.setOnClickListener(defaultNotesCk);
                notesToggle.setBackgroundResource(R.drawable.showonstart);
                releaseLayout.animate().y(screenHeight*.4f).setDuration(500);
            }});
        if(showNotesOnStart) notesCheck.setVisibility(View.VISIBLE);
        else notesCheck.setVisibility(View.INVISIBLE);
        Log.i("NotesToggle", showNotesOnStart+", "+vars.getBoolean("notesOnStart", true));
        closeNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { releaseLayout.animate().y(-1000).setDuration(500); }});
        if(showNotesOnStart) releaseLayout.animate().y(screenHeight*.4f).setDuration(500);
        else if(!firstLoad.getBoolean("firstLoad", true))
            releaseLayout.animate().y(-1000).setDuration(0);

        releaseNotes.setText(changeText);
    }
    public static void setActivity(String set){
        if(musicOn == null)
            musicOn = context.getSharedPreferences("music", 0);

        String musicAt = musicOn.getString("musicOn", "none");
        SharedPreferences.Editor editor = activity.edit();
        SharedPreferences.Editor editor2 = musicOn.edit();
        String activityAt = activity.getString("activity", "none");
        prevActivity = activityAt;
        editor.putString("activity", set);
        editor.commit();
        float vol = (float) (vars.getInt("music", 50) / 100.0);
        try {
            if (set.equals("none")) media.setVolume(0, 0);
            else if (fadeVol == 0) media.setVolume(vol, vol);
        }catch (Exception e){e.printStackTrace();}
        if(musicAt.equals("menu") && (set.equals("game") || set.equals("options") || set.equals("stats"))){
            editor2.putString("musicOn", "game");
            editor2.commit();
            endMusic();
        }
        if(musicAt.equals("game") && (set.equals("main") || set.equals("load") ||
                set.equals("about") || set.equals("help") || set.equals("build"))){
            editor2.putString("musicOn", "menu");
            editor2.commit();
            endMusic();
        }
        musicAt = musicOn.getString("musicOn", "none");

        Log.i("ActivityTrype", ""+set);
        Log.i("MusicType", ""+musicAt);
    }
    public static void setMusicActivity(String set){
        SharedPreferences.Editor editor = musicOn.edit();
        editor.putString("musicOn", set);
        editor.commit();
        Log.i("MusicType", ""+musicOn.getString("musicOn", "none"));
    }
    //private
    private void init(){
        SharedPreferences tutorialAt = context.getSharedPreferences("tutorial", 0);
        SharedPreferences.Editor tutEdit = tutorialAt.edit();
        tutEdit.putInt("at", 0);
        tutEdit.commit();
        achives = context.getSharedPreferences("achives", 0);
        Achivements.initAchives();
        String str = "";
        //Toast.makeText(this, ""+achives.getAll().containsKey("DEBUG"), Toast.LENGTH_SHORT).show();
        //Achivements.initAchives();
        vars = context.getSharedPreferences("vars", 0);
        showNotesOnStart = vars.getBoolean("notesOnStart", true);
        activity = context.getSharedPreferences("activity", 0);
        musicOn = context.getSharedPreferences("music", 0);
        firstLoad = context.getSharedPreferences("firstLoad", 0);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        if(screenWidth == -1) screenWidth = metrics.widthPixels;
        if(screenHeight == -1) screenHeight = metrics.heightPixels;
        inchHeight = (int) (screenHeight/metrics.xdpi);
        inchWidth = (int) (screenWidth/metrics.ydpi);
        Log.i("dimensions", "W: "+screenWidth+", H: "+screenHeight);
        Log.i("firstLoad", ""+firstLoad.getBoolean("firstLoad", true));
        releaseNotes();
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            requestStoragePermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                    "Permission needed to access storage");
            /*ActivityCompat.requestPermissions(mainActivity,
                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);*/
        else {
            Log.i("Permissions", "Granted");
            makeDirs();
            if(firstLoad.getBoolean("firstLoad", true)) firstLoad();
            listTracks();
        }
    }
    private void startThread(){
        new Thread(){
            @Override
            public void run(){
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(START_PAUSE);
                        //Log.i("startTread", ""+activity.getString("activity", "none"));
                        if(activity.getString("activity", "none").equals("none")) {
                            if(onMain) {
                                onMain = false;
                                setActivity("main");
                            }
                            if(onAbout) {
                                onAbout = false;
                                setActivity("about");
                            }
                            if(onBuild) {
                                onBuild = false;
                                setActivity("build");
                            }
                            if(onAchivement) {
                                onAchivement = false;
                                setActivity("achievement");
                            }
                            if(onGame) {
                                onGame = false;
                                setActivity("game");
                            }
                            if(onHelp) {
                                onHelp = false;
                                setActivity("help");
                            }
                            if(onOpenSave) {
                                onOpenSave = false;
                                setActivity("open");
                            }
                            if(onOptions) {
                                onOptions = false;
                                setActivity("options");
                            }
                            if(onStats) {
                                onStats = false;
                                setActivity("stats");
                            }
                            if(onTutorial) {
                                onTutorial = false;
                                setActivity("tutorial");
                            }
                        }
                    }
                } catch (InterruptedException e) { e.printStackTrace(); }
            }
        }.start();
    }
    private void firstLoad(){
        Log.i("First Loaded!", "");
        SharedPreferences.Editor edit = firstLoad.edit();
        edit.putBoolean("firstLoad", false);
        edit.commit();
        makeMusic();
    }

    private void makeMusic(){
        String path = Environment.getExternalStorageDirectory().getPath()+"/Imperium/Music/";
        try {
            String[] songs = getAssets().list("songs");
            Log.i("SongLen", ""+songs.length);
            for(String s : songs){
                Log.i("File", s);
                InputStream stream = getAssets().open("songs/"+s);
                int size = stream.available();
                byte[] buffer = new byte[size];
                stream.read(buffer);
                stream.close();
                File f = new File(path+s);
                FileOutputStream fos = new FileOutputStream(f);
                fos.write(buffer);
                fos.close();
            }
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void music(){
        Log.i("Music", "play");
        musicCycle();
        /*final Thread musicScan = new Thread(){
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(500);
                        String activityAt = activity.getString("activity", "none");
                        if(media != null) {
                            //Log.i("Activity", ""+activityAt);
                            float vol = (float) (vars.getInt("music", 50) / 100.0);
                            try {
                                if (activityAt.equals("none")) media.setVolume(0, 0);
                                else if (fadeVol == 0) media.setVolume(vol, vol);
                            }catch (IllegalStateException e){e.printStackTrace();}
                        }
                    }
                } catch (Exception e) { e.printStackTrace(); }
            }
        };musicScan.start();*/
    }
    private void musicCycle(){
        Log.i("MusicGame", "play");
        final float vol = (float) (vars.getInt("music", 50));
        media = new MediaPlayer();
        musicCycle = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        sleeper();
                        //Log.i("Test", "1");
                        if(media != null) {
                            //Log.i("Test", "2");
                            try {
                                if (!media.isPlaying() /*&& !musicPaused*/) {
                                    //Log.i("Test", "3");
                                    Log.i("MisicOn", musicOn.getString("musicOn", "none"));
                                    Thread.sleep(3000);
                                    media.stop();
                                    media.release();
                                    media = new MediaPlayer();
                                    if (musicOn.getString("musicOn", "none").equals("game")) {
                                        int rand;
                                        if(tracks.length == 0) listTracks();
                                        /*do {
                                            rand = (int) (Math.random() * tracks.length);
                                            Log.i("TrackId", "" + rand);
                                        } while (rand == currentSongId);
                                        currentSongId = rand;*/

                                        if(currentSongId < tracks.length-1) currentSongId++;
                                        else currentSongId = 0;
                                        media = MediaPlayer.create(context, Uri.fromFile(new File(MUSIC_PATH + "/" + tracks[currentSongId])));

                                        final String title = tracks[currentSongId];
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() { try{setMusicTitle(title.substring(0, title.indexOf(".")));}catch (NullPointerException e){}}});
                                        Log.i("duration", "" + media.getDuration());
                                        media.start();
                                    } else if (musicOn.getString("musicOn", "none").equals("menu")) {
                                        try { media = MediaPlayer.create(context, Uri.fromFile(new File(MUSIC_PATH + "/mainTheme.mp3"))); }
                                        catch (Exception e) { media = MediaPlayer.create(context, Uri.fromFile(new File(MUSIC_PATH + "/" + tracks[0]))); }
                                        media.start();
                                    }
                                } else Thread.sleep(3000);
                            } catch (Exception e) { e.printStackTrace(); media = new MediaPlayer();}
                        }
                        else{
                            Thread.sleep(1000);
                            media = new MediaPlayer();
                            Log.i("Media", "Nomedia");
                        }
                    }
                } catch (InterruptedException e) { e.printStackTrace(); }
            }
        };musicCycle.start();
    }
    public static void playMusic(){
        try{media.start(); setActivity("game"); musicPaused = false;}catch (Exception e){e.printStackTrace();}
    }
    public static void pauseMusic(){
        try{media.pause(); musicPaused = true;}catch (Exception e){e.printStackTrace();}
    }
    public static void forwardMusic(){
        try {
            media.stop();
            media.release();
            if (currentSongId < tracks.length - 1) currentSongId++;
            else currentSongId = 0;
            media = MediaPlayer.create(context, Uri.fromFile(new File(MUSIC_PATH + "/" + tracks[currentSongId])));
            Log.i("duration", "" + media.getDuration());
            media.start();
        }catch (Exception e){e.printStackTrace();}
    }
    public static void backwardMusic(){
        try {
            media.stop();
            media.release();
            if (currentSongId > 1) currentSongId--;
            else currentSongId = tracks.length - 1;
            media = MediaPlayer.create(context, Uri.fromFile(new File(MUSIC_PATH + "/" + tracks[currentSongId])));
            Log.i("duration", "" + media.getDuration());
            media.start();
        }catch (Exception e){e.printStackTrace();}
    }
    public static String getSongTitle(){
        try {
            String title = tracks[currentSongId];
            return title.substring(0, title.indexOf("."));
        }catch (NullPointerException | ArrayIndexOutOfBoundsException e){e.printStackTrace();}
        return "";
    }
    public static int getGameAt(){return gameAt;}
    public static void endMusic(){
        float vol = (float) (vars.getInt("music", 50)/100.0);
        Log.i("EndMusic", "in");
        if(media != null) {
            Log.i("EndMusic", "inIf");
            fadeVol = vol/2;
            final Thread fade = new Thread(){
                @Override
                public void run() {
                    try {
                        while (!isInterrupted()) {
                            if(fadeVol > .01) {
                                media.setVolume(fadeVol, fadeVol);
                                fadeVol /= 2;
                            }else if(fadeVol < .01 && fadeVol != 0){
                                media.stop();
                                media.release();
                                media = null;
                                fadeVol = 0;
                            }
                            Thread.sleep(1000);
                        }
                    } catch (Exception e) { e.printStackTrace(); }
                }
            };fade.start();
        }
    }
    public static void killMusic(){
        try {
            media.setVolume(0, 0);
            media.stop();
            media.release();
            media = null;
        }catch (Exception e){
            e.printStackTrace();
            Log.i("Music Err", "Could not stop");
        }
    }
    private void makeDirs(){
        String path = Environment.getExternalStorageDirectory().getPath()+"/Imperium";
        File dir = new File(path);
        Log.i("path", path + ", Exists? " + dir.exists());

        if(!dir.exists()) if(!dir.mkdir()) Log.i("No", "nomake");
        dir = new File(path+"/Saves");
        if(!dir.exists()) if(!dir.mkdir()) Log.i("No2", "nomake");
        dir = new File(path+"/Music");
        if(!dir.exists()) if(!dir.mkdir()) Log.i("No3", "nomake");
        dir = new File(path+"/Logs");
        if(!dir.exists()) if(!dir.mkdir()) Log.i("No4", "nomake");
        dir = new File(path+"/Game");
        if(!dir.exists()) if(!dir.mkdir()) Log.i("No5", "nomake");

        FileOutputStream fos;
        File save = new File(path+"/Music", "Readme.txt");
        String readme = "This folder stores Imperium's music. Paste any music files that you'd like in the game here!";
        try {
            fos = new FileOutputStream(save);
            fos.write(readme.getBytes());
            fos.close();
        } catch (Exception e) { e.printStackTrace(); }
        path = Environment.getExternalStorageDirectory().getPath()+"/Imperium/Game/";
        Log.i("path2", path);
        File state = new File(path, "State.txt");
        Log.i("StateFile", path+state.getName()+", "+new File(path+state.getName()).exists());
        if(!new File(path+state.getName()).exists()) {
            String statedesc = "Critical Game Files, Do Not Remove (Please)\n";
            for(int i=0; i<50; i++)
                statedesc += ""+(char)((int)(Math.random()*24)+65);
            statedesc +="[]";
            for(int i=0; i<50; i++)
                statedesc += ""+(char)((int)(Math.random()*24)+65);
            try {
                fos = new FileOutputStream(state);
                fos.write(statedesc.getBytes());
                fos.close();
            } catch (Exception e) { e.printStackTrace(); }
        }
    }
    private static void listTracks(){
        String path = Environment.getExternalStorageDirectory().getPath()+"/Imperium/Music";
        File dir = new File(path);
        String[] dirList = dir.list();
        ArrayList<String> dirArray = new ArrayList<>(0);
        try {
            for (String s : dirList) {
                String fileType = s.substring(s.lastIndexOf("."));
                if (fileType.equals(".mp3") || fileType.equals(".ogg")) {
                    dirArray.add(s);
                    Log.i("MusicFile", s);
                }
            }
            tracks = dirArray.toArray(new String[0]);
        }catch (NullPointerException e){
            e.printStackTrace();
            tracks = new String[0];
        }
        if(tracks.length <= 1)
            Toast.makeText(context, "The music directory (Imperium/music) is empty!", Toast.LENGTH_LONG).show();

    }
    private static void sleeper(){
        if(!((PowerManager) context.getSystemService(Context.POWER_SERVICE)).isScreenOn()){
            if(GameActivity.getGame() != null)
                //GameActivity.saveGame(AUTO_SAVE_ID);
            System.exit(0);
        }
    }
    private void requestStoragePermission(final String[] code, String message){
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, code[0])) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage(message)
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    code, STORAGE_PERMISSION_CODE);
                        }
                    }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) { dialog.dismiss(); }
                    }).create().show();

        }// else ActivityCompat.requestPermissions(this, code, STORAGE_PERMISSION_CODE);

    }
    private void textMaker() {
        version = findViewById(R.id.version);
        final TextView splash = findViewById(R.id.splash);
        final TextView backSplash = findViewById(R.id.backSplash);
        splash.setTextSize(TypedValue.COMPLEX_UNIT_IN, (float) (1.1 * BASE_TEXT_SCALE * inchWidth));
        backSplash.setTextSize(TypedValue.COMPLEX_UNIT_IN, (float) (1.1 * BASE_TEXT_SCALE * inchWidth));
        splash.setTypeface(splash.getTypeface(), Typeface.BOLD_ITALIC);
        backSplash.setTypeface(splash.getTypeface(), Typeface.BOLD_ITALIC);
        splash.setTextColor(Color.argb(255, 0, 219, 18));

        version.setTextSize(TypedValue.COMPLEX_UNIT_IN, (float) (BASE_TEXT_SCALE * inchWidth));
        version.setText("" + BuildConfig.VERSION_NAME);
        byte[] buffer = new byte[0];
        String notes = "";
        try {
            InputStream stream = getAssets().open("sacredTexts/about/splashes.txt");
            int size = stream.available();
            buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            notes = new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<Integer> breaks = new ArrayList(0);
        breaks.add(-1);
        for (int i = 0; i < notes.length(); i++)
            if (notes.charAt(i) == '\n' && notes.length() > i + 1) breaks.add(i);
        Log.i("Breaks", "" + breaks.size());
        Collections.shuffle(breaks);
        Log.i("Breaks", "" + breaks.get(0));
        int lastEnter = notes.indexOf('\n', breaks.get(0) + 1);
        if (lastEnter == -1) lastEnter = notes.length();
        notes = notes.substring(breaks.get(0) + 1, lastEnter);
        splash.setText(notes);
        backSplash.setText(notes);
        Log.i("Splashnull", "" + (splashZoom == null));
        final float[] sizeMod = {1};
        final float[] sizeModRate = {.005f};
        splashZoom = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(20);
                        //Log.i("Mods", ""+sizeMod[0]+", "+sizeModRate[0]);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //if(System.currentTimeMillis() % 20 == 1) Log.i("TextSplash", "ffoof");
                                splash.setTextSize(TypedValue.COMPLEX_UNIT_IN, sizeMod[0] * BASE_TEXT_SCALE * inchWidth);
                                backSplash.setTextSize(TypedValue.COMPLEX_UNIT_IN, sizeMod[0] * BASE_TEXT_SCALE * inchWidth);
                                backSplash.animate()
                                        .x(splash.getX() + 15f * sizeMod[0] * BASE_TEXT_SCALE * inchWidth)
                                        .y(splash.getY() + 15f * sizeMod[0] * BASE_TEXT_SCALE * inchWidth).setDuration(0);
                            }
                        });
                        sizeMod[0] += sizeModRate[0];
                        if (sizeMod[0] >= 1.1) sizeModRate[0] = -.005f;
                        if (sizeMod[0] <= .9) sizeModRate[0] = .005f;
                    }
                } catch (InterruptedException e) { e.printStackTrace(); }
            }
        };
        splashZoom.start();
    }
    private void makeButtons(){
        ImageButton tutorial = findViewById(R.id.tutorial);
        ImageButton newGame = findViewById(R.id.newGame);
        ImageButton makeMusic = findViewById(R.id.makeMusic);
        makeMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeMusic();
                Toast.makeText(context, "Default music inserted into music folder", Toast.LENGTH_LONG).show();
                listTracks();
            }
        });
        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BuildActivity.class);
                startActivity(intent);
            }
        });
        /*ImageButton multiplayer = findViewById(R.id.multiplayer);
        multiplayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MultiplayerActivity.class);
                startActivity(intent);
            }
        });*/
        ImageButton loadGame = findViewById(R.id.loadGame);
        loadGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(context, OpenSaveActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(context, "File permissions needed to load saves!", Toast.LENGTH_LONG).show();
                    requestStoragePermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                            "Storage permission needed to access save files");
                    /*ActivityCompat.requestPermissions(mainActivity,
                            new String[] {Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);*/
                }
            }
        });
        ImageButton quit = findViewById(R.id.quit);
        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
                System.exit(0);
            }
        });
        ImageButton about = findViewById(R.id.about);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AboutActivity.class);
                startActivity(intent);
            }
        });
        ImageButton achivements = findViewById(R.id.achivemnts);
        achivements.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AchivementActivity.class);
                startActivity(intent);
            }
        });

        tutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TutorialActivity.class);
                startActivity(intent);
            }
        });
        final Button devToggle = findViewById(R.id.devToggle);
        devToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(devType < 2) devType ++;
                else devType = 0;
                switch (devType) {
                    case 0:
                        devToggle.setText("No Dev");
                        debugingOn = false;
                        newDebugSave = false;
                        break;
                    case 1:
                        devToggle.setText("Editing");
                        debugingOn = true;
                        newDebugSave = false;
                        break;
                    case 2:
                        devToggle.setText("New Save");
                        debugingOn = true;
                        newDebugSave = true;
                        break;
                }
            }
        });
        final Button debugIdBtn = findViewById(R.id.debugId);
        if(!DEV_MODE){
            devToggle.setVisibility(View.INVISIBLE);
            debugIdBtn.setVisibility(View.INVISIBLE);
        }
        final ScrollView devIdScroll = findViewById(R.id.devIdScroll);
        final LinearLayout devIdLayout = findViewById(R.id.devIdLayout);
        devIdScroll.setVisibility(View.INVISIBLE);
        debugIdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(devScrollOpen){
                    devScrollOpen = false;
                    devIdScroll.setVisibility(View.INVISIBLE);
                }else{
                    devScrollOpen = true;
                    devIdScroll.setVisibility(View.VISIBLE);
                }
            }
        });
        byte[] buffer;
        ArrayList<String> ids = new ArrayList<>(0);
        String full = "";
        try {
            InputStream stream = getAssets().open("sacredTexts/timeLines/europeMap/dateTags.txt");
            int size = stream.available();
            buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            full = new String(buffer);
        } catch (IOException e) { e.printStackTrace(); }
        for(int i=0; i<full.length(); i++){
            Log.i("Add Ids", full.substring(i, full.indexOf("}", i)+1));
            ids.add(full.substring(i, full.indexOf("}", i)+1));
            final Button id = new Button(context);
            final String idAt = ids.get(ids.size()-1);
            id.setText(idAt.substring(0, idAt.indexOf("{")));
            id.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    overwriteDebugNations(idAt);
                    debugIdBtn.setText(id.getText().toString());
                    devScrollOpen = false;
                    devIdScroll.setVisibility(View.INVISIBLE);
                }
            });
            devIdLayout.addView(id);
            Log.i("Add Ids", ""+i);
            i = full.indexOf("}", i)+2;
            if(i == 1) break;
        }
        //initializes as alp17
        overwriteDebugNations(ids.get(0));

    }
    private void overwriteDebugNations(String idAt){
        ArrayList<String> temp = new ArrayList<>(0);
        int start = idAt.indexOf("{")+2;
        for(int i=start; i<idAt.length(); i+=7){
            Log.i("BuildId"+idAt.substring(0, start), idAt.substring(i, i+3));
            temp.add(idAt.substring(i, i+3));
        }
        debugNations = temp.toArray(new String[0]);
    }

    public static ColorMatrixColorFilter desaturate(boolean desaturate){
        ColorMatrix matrix = new ColorMatrix();
        if(desaturate) matrix.setSaturation(0);
        else matrix.setSaturation(1);
        return new ColorMatrixColorFilter(matrix);
    }

}