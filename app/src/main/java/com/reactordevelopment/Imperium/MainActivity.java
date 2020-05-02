package com.reactordevelopment.Imperium;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import static com.reactordevelopment.Imperium.GameActivity.*;

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
    public static final int OVERLORD_COLOR = Color.argb(ALPHA, 0, 90, 245);
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
    //turnNum, provTroops, playerTroops, Monetae, infamy, development, devastation, attrition, ownerId
    private int STORAGE_PERMISSION_CODE = 1;
    public static final double SAVE_VERSION = 1.31;
    public static final boolean LOCKED = false;

    private static String[] tracks;
    private static SharedPreferences vars;
    public static SharedPreferences activity;
    private static SharedPreferences musicOn;
    private static SharedPreferences firstLoad;
    public static SharedPreferences achives;
    public static String prevActivity;
    private static TextView version;
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
        versionMaker();
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
        final ConstraintLayout releaseLayout = findViewById(R.id.releaseLayout);
        final TextView releaseNotes = findViewById(R.id.releaseText);
        releaseNotes.setMovementMethod(new ScrollingMovementMethod());
        ImageButton closeNotes = findViewById(R.id.closeNotes);
        releaseLayout.animate().y(-1000).setDuration(0);

        byte[] buffer = new byte[0];
        String notes = "";
        try {
            InputStream stream = getAssets().open("sacredTexts/about/changelog.txt");
            int size = stream.available();
            buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            notes = new String(buffer);
            if(notes.contains("Version-")) notes = notes.replace("Version-", "Version: "+BuildConfig.VERSION_NAME);
            if(notes.contains("Save Encoding-")) notes = notes.replace("Save Encoding-", "Save Encoding: "+SAVE_VERSION);
        } catch (IOException e) { e.printStackTrace(); }
        releaseNotes.setTextSize(TypedValue.COMPLEX_UNIT_IN,BASE_TEXT_SCALE*inchWidth);
        releaseNotes.setText(notes);

        updateBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { releaseLayout.animate().y(screenHeight*.4f).setDuration(500); }});
        closeNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { releaseLayout.animate().y(-1000).setDuration(500); }});
    }
    public static void setActivity(String set){
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
        activity = context.getSharedPreferences("activity", 0);
        musicOn = context.getSharedPreferences("music", 0);
        firstLoad = context.getSharedPreferences("firstLoad", 0);
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(mainActivity,
                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        else {
            makeDirs();
            if(firstLoad.getBoolean("firstLoad", true)) firstLoad();
            listTracks();
        }
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        if(screenWidth == -1) screenWidth = metrics.widthPixels;
        if(screenHeight == -1) screenHeight = metrics.heightPixels;
        inchHeight = (int) (screenHeight/metrics.xdpi);
        inchWidth = (int) (screenWidth/metrics.ydpi);
        Log.i("dimensions", "W: "+screenWidth+", H: "+screenHeight);
        releaseNotes();
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
        String path = Environment.getExternalStorageDirectory().getPath()+"/Imperium/Music/";
        SharedPreferences.Editor edit = firstLoad.edit();
        edit.putBoolean("firstload", false);
        edit.commit();
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
                                if (!media.isPlaying() && !musicPaused) {
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
                                            public void run() { setMusicTitle(title.substring(0, title.indexOf("."))); }});
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
        }catch (NullPointerException e){e.printStackTrace();}
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
            media.stop();
            media.release();
            media = null;
        }catch (Exception e){e.printStackTrace();}
    }
    private void makeDirs(){
        String path = Environment.getExternalStorageDirectory().getPath()+"/Imperium";
        Log.i("path", path);
        File dir = new File(path);
        if(!dir.exists()) if(!dir.mkdir()) Log.i("No", "nomake");;
        dir = new File(path+"/Saves");
        if(!dir.exists()) if(!dir.mkdir()) Log.i("No2", "nomake");
        dir = new File(path+"/Music");
        if(!dir.exists()) if(!dir.mkdir()) Log.i("No3", "nomake");
        dir = new File(path+"/Logs");
        if(!dir.exists()) if(!dir.mkdir()) Log.i("No4", "nomake");
        FileOutputStream fos;
        File save = new File(path+"/Music", "Readme.txt");
        String readme = "This folder stores Imperium's music. Paste any music files that you'd like in the game here!";
        try {
            fos = new FileOutputStream(save);
            fos.write(readme.getBytes());
            fos.close();
        } catch (Exception e) { e.printStackTrace(); }

    }
    private static void listTracks(){
        String path = Environment.getExternalStorageDirectory().getPath()+"/Imperium/Music";
        File dir = new File(path);
        String[] dirList = dir.list();
        ArrayList<String> dirArray = new ArrayList<>(0);
        for(String s : dirList) {
            String fileType = s.substring(s.lastIndexOf("."));
            if (fileType.equals(".mp3") || fileType.equals(".ogg")) {
                dirArray.add(s);
                Log.i("MusicFile", s);
            }
        }
        tracks = dirArray.toArray(new String[0]);
    }
    private static void sleeper(){
        if(!((PowerManager) context.getSystemService(Context.POWER_SERVICE)).isScreenOn()){
            if(GameActivity.getGame() != null)
                GameActivity.saveGame(AUTO_SAVE_ID);
            System.exit(0);
        }
    }
    /*private void requestStoragePermission(final String code){
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, code)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of this and that")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[] {code}, STORAGE_PERMISSION_CODE);
                        }
                    }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) { dialog.dismiss(); }
                    }).create().show();

        } else ActivityCompat.requestPermissions(this, new String[] {code}, STORAGE_PERMISSION_CODE);

    }*/
    private void versionMaker(){
        version = findViewById(R.id.version);
        version.setText(""+BuildConfig.VERSION_NAME);
        if(LOCKED) version.setText("Imperium: Lite");
    }
    private void makeButtons(){
        ImageButton tutorial = findViewById(R.id.tutorial);
        ImageButton newGame = findViewById(R.id.newGame);
        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BuildActivity.class);
                startActivity(intent);
            }
        });
        ImageButton loadGame = findViewById(R.id.loadGame);
        loadGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(context, OpenSaveActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(context, "File permissions needed to load saves!", Toast.LENGTH_LONG).show();
                    ActivityCompat.requestPermissions(mainActivity,
                            new String[] {Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
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
    }

}