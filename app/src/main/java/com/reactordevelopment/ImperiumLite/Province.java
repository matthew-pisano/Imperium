package com.reactordevelopment.ImperiumLite;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.TRANSPARENT;
import static com.reactordevelopment.ImperiumLite.MainActivity.BURN_COLOR;
import static com.reactordevelopment.ImperiumLite.MainActivity.DECAY_COLOR;
import static com.reactordevelopment.ImperiumLite.MainActivity.DEV_COLOR;
import static com.reactordevelopment.ImperiumLite.MainActivity.FORT_COLOR;
import static com.reactordevelopment.ImperiumLite.MainActivity.GROW_COLOR;
import static com.reactordevelopment.ImperiumLite.MainActivity.MIGHT_COLOR;
import static com.reactordevelopment.ImperiumLite.MainActivity.MONETAE_TO_TROOPS;
import static com.reactordevelopment.ImperiumLite.MainActivity.PLAYER_NONE;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class Province extends Continent implements Serializable {
    private int id;
    private int ownerId;
    private String loadTag;
    private int continentId;
    private int x;
    private int y;
    private int fortLevel;
    private int lastDevelops;
    private int[] borders;
    private double interest;
    private double devastation;
    private double troops;
    private double savedTroops;
    private double savedDevelopment;
    private double development;
    private double attrition; //troops multiplied by each turn
    private boolean selected;
    private boolean imperium;
    private String name;
    private String coreOwner;
    private String lastUndoable;
    private Province self;
    private Province[] bordering;
    private RelativeLayout mapLayout;
    private TextView status;
    private TextView guestStatus;
    private ImageView overlay;
    private ImageView owner;
    private ImageView guestTroops;
    private ImageView showSelected;
    private ImageView aimAttack;
    private Context context;
    private Bitmap overBit;
    private Point center;
    private ArrayList<TroopStack> troopStacks;
    private int filterAt;

    public Province(Context context, int id, int continent, int[] borders, int x, int y, String name, double interest) { //classic Mode
        imperium = false;
        this.name = name;
        this.x = x;
        this.y = y;
        this.borders = borders;
        this.id = id;
        this.interest = interest;
        this.context = context;
        continentId = continent;
        coreOwner = "#nn";
        filterAt = Color.argb(0, 255, 255, 255);
        /*ownerId = -1;
        devastation = 0;
        fortLevel = 1;
        attrition = 1;*/
        resetValues();
        troopStacks = new ArrayList<>(0);
        status = new TextView(context);
        status.setText("");
        guestStatus = new TextView(context);
        guestStatus.setText("");
        overlay = new ImageView(context);
        aimAttack = new ImageView(context);
        owner = new ImageView(context);
        guestTroops = new ImageView(context);
        showSelected = new ImageView(context);
        selected = false;
        mapLayout = getMapLayout();
        self = this;
        loadTag = "#nn";
    }

    public Province(Context context, int id, int[] borders, int x, int y, String name, double interest, double dev, double attrition) { //inperium Mode
        this(context, id, -1, borders, x, y, name, interest);
        development = dev;
        lastDevelops = 0;
        this.attrition = attrition;
        imperium = true;
    }

    public void resetValues() {
        Log.i("ResetValuse", "");
        ownerId = -1;
        devastation = 0;
        fortLevel = 1;
        attrition = 1;
        troops = 0;
        troopStacks = new ArrayList<>(0);

    }

    public int getId() {
        return id;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getName() {
        return name;
    }

    public String getCore() { return coreOwner; }

    public int getOwnerId() {
        return ownerId;
    }

    public Point getCenter() {
        return center;
    }

    public int getContinentId() {
        return continentId;
    }

    public Continent getContinent() {
        return getMap().getContinents()[continentId];
    }

    public Province[] getBordering() {
        return bordering;
    }

    public int getLastDevelops() {
        return lastDevelops;
    }

    public ArrayList<TroopStack> getTroopStacks(){return troopStacks;}

    public TroopStack getStackFrom(String tag){
        for(int i=0; i<troopStacks.size(); i++)
            if(troopStacks.get(i).getOwnerTag().equals(tag))
                return troopStacks.get(i);
        return null;
    }

    public double getAttrition() {
        return attrition;
    }

    public double calcOutput() {
        return development * (1 - devastation);
    }

    public Player getOwner() {
        if (getGame() != null && ownerId >= 0) return getGame().getPlayerList()[ownerId];
        return null;
    }

    public boolean isSelected() {
        return selected;
    }

    public int getFortLevel() {
        return fortLevel;
    }

    public double getSavedTroops() {
        return savedTroops;
    }

    public double getSavedDevelopment() {
        return savedDevelopment;
    }

    public String getLastUndoable() {
        return lastUndoable;
    }

    public Bitmap getOverlay() {
        return overBit;
    }

    public ImageView getImage() {
        return overlay;
    }

    public double getInterest() {
        if (imperium) return interest + (development / (maxDev + .01) - 1 + attrition);
        return interest + getContinent().getInterest();
    }

    public void setColor(int color) {
        overlay.setColorFilter(color);
    }

    public void setText(String text) {
        status.setText(text);
    }

    public void setGuestText(String text) {
        guestStatus.setText(text);
    }

    public void setSelected(boolean set) {
        if(set && getCurrentPlayer().select(0) > 0) {
            selected = true;
            getCurrentPlayer().select(-1);
        }
        else if(!set) {
            getCurrentPlayer().select(1);
            selected = false;
        }
        //Log.i("provinceSelect", name+", isSelected: "+selected+", Selections left: "+getCurrentPlayer().select(0));
    }

    public void showSelection(boolean set) {
        if (set) showSelected.setVisibility(View.VISIBLE);
        else showSelected.setVisibility(View.INVISIBLE);

    }

    public void setAndShowSelection(boolean set) {
        setSelected(set);
        showSelection(set);
    }

    public void setDevelopment(double set) {
        if (set > 1)
            development = set;
        else
            development = 1;
    }
    public void setCoreOwner(String core){coreOwner = core;}
    public void setAttrition(double set) {
        attrition = set;
    }

    public void setFortLevel(int set) {
        if (set >= 1) fortLevel = set;
        else fortLevel = 1;
    }

    public double modDevastation(double mod) {
        if (devastation + mod < 1 && devastation + mod > -.08) devastation += mod;
        if (mod != 0 && mapMode == 5)
            overlay.setColorFilter(fade(BURN_COLOR, GROW_COLOR, devastation, 1, -.08));
        return devastation;
    }

    public double modDevelopment(double mod) {
        if (getCurrentPlayer().canSpend()) {
            development += mod;
            if (attrition < 1.02) attrition += mod * .02;
            if (mod != 0) {
                updateMaxDev();
                if (mapMode == 3) overlay.setColorFilter(fade(DEV_COLOR, development, maxDev));
                lastUndoable = "d";
            }
        }
        return development;
    }

    public boolean transportTo(double troops, Province to){
        if(getTroops()-troops <= 0) return false;
        modTroops(-troops, getCurrentPlayer().getTag());
        //to.getTroopStacks().add(new TroopStack(getCurrentPlayer().getTag(), 0, 0));
        to.modTroops(troops, getCurrentPlayer().getTag());
        if(getCurrentPlayer().getStage() == 2)
            if(to.getStackFrom(getCurrentPlayer().getTag()) != null)
                to.getStackFrom(getCurrentPlayer().getTag()).move();
        return true;
    }
    public void place() {
        bordering = new Province[borders.length];
        for (int i = 0; i < bordering.length; i++)
            bordering[i] = provinceList[borders[i] - 1];
        buildComps();
    }

    public void manualDevelopment() {
        lastDevelops++;
    }

    public void decay() {
        if (getCurrentPlayer().getStage() != -1 && troops > 1)
            if ((troops * (attrition + (1 - attrition) / getPlayerList().length) <= troops + 0.5 / getPlayerList().length))
                troops *= (attrition + (1 - attrition) / getPlayerList().length);
            else troops += 0.5 / getPlayerList().length;
    }

    public void setLoadTag(String tag) { loadTag = tag; }

    public String getLoadTag() { return loadTag; }

    public void updatePress(final int ownerId) {
        int prevId = this.ownerId;
        this.ownerId = ownerId;
        if(ownerId == -1) Log.i("Negg", "");
        maxTroops = maxTroops();
        if (ownerId != prevId)
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mapMode == 1 && ownerId != -1 && getOwner() != null)
                        overlay.setColorFilter(getOwner().getColor());
                    else if (mapMode == 1) overlay.setColorFilter(PLAYER_NONE);
                    if (mapMode == 7) overlay.setColorFilter(fade(MIGHT_COLOR, troops, maxTroops));
                    updateOwner();
                }
            });
    }

    /*public void updateStatus() {
        double guestTroops = 0;
        if (ownerId != getCurrentPlayer().getId() && hasGuestStackFrom(getCurrentPlayer().getTag())) {
            guestTroops = getTroopsFrom(getCurrentPlayer().getTag());
        }
        if (imperium && getTroops() > 0 && getTroops() % 1 != 0 && getTroops() < 100) {
            setText("" + (int) (getTroops() * 10) / 10.0 + "k");
            setGuestText("" + (int) (guestTroops * 10) / 10.0 + "k");
        } else if (imperium && getTroops() >= 100 || getTroops() % 1 == 0) {
            setText("" + (int) (getTroops()) + "k");
            setGuestText("" + (int) (guestTroops) + "k");
        }
        if (!imperium && getTroops() > 0)
            setText("" + (int) getTroops());
        if (getTroops() == 0) {
            setText("");
            setGuestText("");
        }
    }*/
    public void updateOwner(){ updateOwner(getCurrentPlayer()); }
    public void updateOwner(Player focus) {
        //Log.i("ownerid", ""+ownerId);
        //updateStatus();
        if (getOwner() != null) {
            //Log.i("update", "" + ownerId + ", " + focus.getId());
            double secondTroops = 0;
            boolean friendlyBool = focus.isFriendly(getOwner().getTag());
            if(getOwner().hasOverlord())
                friendlyBool = friendlyBool || focus.isFriendly(getOwner().getOverlord());
            if(focus.hasOverlord())
                    friendlyBool = friendlyBool || playerFromTag(focus.getOverlord()).isFriendly(getOwner().getTag());
            if (ownerId == focus.getId()) {
                owner.setBackgroundResource(R.drawable.friendtroops);
                if (hasGuestStack()) {
                    boolean hasFriend = false;
                    for(TroopStack ts : troopStacks)
                        if(focus.isFriendly(ts.getOwnerTag())){
                            hasFriend = true;
                            secondTroops = ts.getTroops();
                            break;
                        }
                    guestTroops.setVisibility(View.VISIBLE);
                    guestStatus.setTextSize(TypedValue.COMPLEX_UNIT_DIP, (float) (2 * getMap().getStatusScale()));
                    if(!hasFriend)
                        guestTroops.setBackgroundResource(R.drawable.neutraltroops);
                    else
                        guestTroops.setBackgroundResource(R.drawable.allytroops);
                } else {
                    guestTroops.setVisibility(View.INVISIBLE);
                    guestStatus.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 0);
                }
            } else if (focus.isHostile(getOwner().getTag())) {
                owner.setBackgroundResource(R.drawable.foetroops);
                guestTroops.setVisibility(View.INVISIBLE);
                guestStatus.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 0);
            }
            else if (friendlyBool) {
                owner.setBackgroundResource(R.drawable.allytroops);
                if (hasGuestStack()) {
                    boolean hasMine = false;
                    for(TroopStack ts : troopStacks)
                        if(ts.getOwnerTag().equals(focus.getTag())){
                            hasMine = true;
                            secondTroops = getTroopsFrom(focus.getTag());
                            break;
                        }
                    guestTroops.setVisibility(View.VISIBLE);
                    guestStatus.setTextSize(TypedValue.COMPLEX_UNIT_DIP, (float) (2 * getMap().getStatusScale()));
                    if(!hasMine)
                        guestTroops.setBackgroundResource(R.drawable.neutraltroops);
                    else
                        guestTroops.setBackgroundResource(R.drawable.friendtroops);
                } else {
                    guestTroops.setVisibility(View.INVISIBLE);
                    guestStatus.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 0);
                }
            } else {
                owner.setBackgroundResource(R.drawable.neutraltroops);
                guestTroops.setVisibility(View.INVISIBLE);
                guestStatus.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 0);
            }


            if (imperium && getTroops() > 0 && getTroops() % 1 != 0 && getTroops() < 100) {
                setText("" + (int) (getTroops() * 10) / 10.0 + "k");
                setGuestText("" + (int) (secondTroops * 10) / 10.0 + "k");
            }
            else if (imperium && getTroops() >= 100 || getTroops() % 1 == 0) {
                setText("" + (int) (getTroops()) + "k");
                setGuestText("" + (int) (secondTroops) + "k");
            }
            else if (!imperium && getTroops() > 0)
                setText("" + (int) getTroops());
            else if (getTroops() == 0) {
                setText("");
                setGuestText("");
            }
        }else if (ownerId == -1) {
            Log.i("onverNull", "");
            owner.setBackgroundResource(R.drawable.blank);
        }
    }

    public boolean bordering(Province province) {
        if(province == null) return false;
        for (Province border : bordering) {
            if (border.getId() == province.getId())
                return true;
        }
        return false;
    }
    public void updateOverlays() {
                overlay.setVisibility(View.VISIBLE);
                if (mapMode == 0) {
                    overlay.setColorFilter(TRANSPARENT);
                    overlay.setVisibility(View.INVISIBLE);
                }
                if(mapMode == 1 && ownerId != -1 && ownerId < getPlayerList().length && getOwner() != null) overlay.setColorFilter(getOwner().getColor());
                else if(mapMode == 1) overlay.setColorFilter(PLAYER_NONE);
                if (mapMode == 2) overlay.setColorFilter(getContinent().getColor());
                if (mapMode == 3) overlay.setColorFilter(fade(DEV_COLOR, development, maxDev));
                //Log.i("dev", "" + maxDev);
                if (mapMode == 4) overlay.setColorFilter(fade(DECAY_COLOR, 1 - attrition, .2));
                if (mapMode == 5) overlay.setColorFilter(fade(BURN_COLOR, GROW_COLOR, devastation, 1, -.08));
                if (mapMode == 6) overlay.setColorFilter(fade(FORT_COLOR, fortLevel, 5));
                if (mapMode == 7) overlay.setColorFilter(fade(MIGHT_COLOR, troops, maxTroops));
                if(mapMode == 8) overlay.setColorFilter(PLAYER_NONE);
    }

    public double modTroops(double mod) {
        if(troopStacks.size() < 1  && mod > 0 && getOwner() != null) troopStacks.add(new TroopStack(getOwner().getTag(), 0));
        else if(troopStacks.size() < 1 && mod > 0) troopStacks.add(new TroopStack(getCurrentPlayer().getTag(), 0));
        //troopStacks.get(0).modTroops(mod);
        TroopStack saveStack = null;
        for(TroopStack stack : troopStacks)
            if(stack.getOwnerTag().equals(getCurrentPlayer().getTag())) {
                stack.modTroops(mod);
                saveStack = stack;
            }
        troops = 0;
        for(TroopStack stack : troopStacks)
            troops += stack.getTroops();

        if(mod != 0){
            Log.i("ProvinceModded", ""+name+": "+ (troops - mod) + " modded by " + mod + ", troops: " + troops + ", blocked? false");
            Log.i("Stackisze", ""+troopStacks.size()+", troops: "+troopStacks.get(0).getTroops());
        }
        /*if (() >= 0) {
            troops += mod;
            if (mod != 0)

        } else {
            //Log.i("ProvinceModded", "" + (troops - num) + " modded by " + num + ", troops: " + troops + ", blocked? true");
            if (mod != 0)
                Log.i("ProvinceModded", "" +name+": "+ (troops - mod) + " modded by " + mod + ", troops: " + 0 + ", blocked? true");
            troops = 0;
        }*/
        if(saveStack != null) return saveStack.getTroops();
        //updateStatus();
        return 0;
    }
    public double modTroops(double mod, String player){
        boolean already = false;
        double stackTroops = 0;
        for (int i = 0; i < troopStacks.size(); i++) {
            TroopStack stack = troopStacks.get(i);
            Log.i("StackTrans", stack.getOwnerTag());
            if (stack.getOwnerTag().equals(player)) {
                already = true;
                troopStacks.get(i).modTroops(mod);
                stackTroops = stack.getTroops();
                if (stackTroops <= 0.1){
                    troopStacks.remove(stack);
                    if(i > 0) i--;
                }
            }
        }
        if(!already && mod > 0){
            troopStacks.add(new TroopStack(player, mod));
            Log.i("StackTrans", "added: "+troopStacks.get(troopStacks.size()-1).getOwnerTag());
        }
        troops = 0;
        for(TroopStack stack : troopStacks)
            troops += stack.getTroops();
        Log.i("ProvinceModdedTag: "+player, ""+name+": "+ (troops - mod) + " modded by " + mod + ", troops: " + troops + ", blocked? false");
        //if(getCurrentPlayer() != null) updateStatus();
        return stackTroops;
    }
    public String stacksToString(){
        String info = "";
        for(TroopStack ts : troopStacks){
            info += (int)(ts.getTroops()*10)/10.0 +"k solders from "+playerFromTag(ts.getOwnerTag()).getName()+"\n";
        }
        return info;
    }
    public double getTroops(){
        troops = 0;
        for(TroopStack ts : troopStacks)
            troops += ts.getTroops();
        return troops;
    }

    public double getTroopsFrom(String player){
        double troops = 0;
        for(TroopStack stack : troopStacks)
            if(stack.getOwnerTag().equals(player))
                troops += stack.getTroops();
        return troops;
    }

    public double attackMod(double mod){
        int stackAt = troopStacks.size()-1;
        while(stackAt >= 0 && mod < 0){
            double stackTroops = troopStacks.get(stackAt).getTroops();
            modTroops(mod, troopStacks.get(stackAt).getOwnerTag());
            //troopStacks.get(stackAt).modTroops(mod);
            //if(troopStacks.get(stackAt).getTroops() == 0) troopStacks.remove(stackAt);
            mod -= stackTroops;
            stackAt--;
        }
        return troops;
    }
    public boolean hasStackFrom(String player){
        for(TroopStack stack : troopStacks)
            if(stack.getOwnerTag().equals(player) && ownerId != -1) return true;
        return false;
    }
    public boolean hasGuestStackFrom(String player){
        for(TroopStack stack : troopStacks)
            if(stack.getOwnerTag().equals(player) && ownerId != -1) return !stack.getOwnerTag().equals(getOwner().getTag());
        return false;
    }
    public boolean hasGuestStack(){
        for(TroopStack stack : troopStacks)
            if(!stack.getOwnerTag().equals(getOwner().getTag())) return true;
        return false;
    }
    public boolean borders(String player){
        for(Province p : bordering)
            if(p.getOwner() != null)
                if(p.getOwner().getTag().equals(player))
                    return true;
        return false;
    }
    public String fortify() {
        if (getCurrentPlayer().modMonetae(0) > 20 * (fortLevel + 1)) {
            if (fortLevel < 5) {
                fortLevel++;
                getCurrentPlayer().modMonetae(-20 * (fortLevel + 1));
                if(mapMode == 6) overlay.setColorFilter(fade(FORT_COLOR, fortLevel, 5));
                return "succeed";
            } else return "levelFail";
        } else return "monetaeFail";
    }



    public void saveTroops() { savedTroops = troops; }
    public void saveDevelopment() {
        savedDevelopment = development;
        lastDevelops = 0;
    }


    public void ownerVis(boolean vis) {
        if (vis) {
            owner.setVisibility(View.VISIBLE);
            if(ownerId != getCurrentPlayer().getId() && hasGuestStackFrom(getCurrentPlayer().getTag())) {
                guestTroops.setVisibility(View.VISIBLE);
                guestStatus.setTextSize(TypedValue.COMPLEX_UNIT_DIP, (float) (2*getMap().getStatusScale()));
            }
            status.setTextSize(TypedValue.COMPLEX_UNIT_DIP, (float) (2*getMap().getStatusScale()));

        } else {
            owner.setVisibility(View.INVISIBLE);
            guestTroops.setVisibility(View.INVISIBLE);
            status.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 0);
            guestStatus.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 0);
        }
    }
    public void showAim(Province b){
        final double[] vector = new double[2];
        Bitmap prime = BitmapFactory.decodeResource(context.getResources(), R.drawable.attackarrow);
        double diffX = center.x - b.getCenter().x; double diffY = center.y - b.getCenter().y;
        vector[0] = Math.sqrt(diffX*diffX + diffY*diffY);
        if(diffX > 0){ //for negative rot
            aimAttack.setX((float) (center.x-8-aimAttack.getWidth()*Math.cos(vector[1])));
            vector[1] =  Math.toDegrees(Math.atan(diffY/diffX)) + 180;
            Matrix matrix = new Matrix();
            matrix.preScale(1.0f, -1.0f);
            prime = Bitmap.createBitmap(prime, 0, 0, prime.getWidth(), prime.getHeight(), matrix, true);
        }
        else{
            vector[1] =  Math.toDegrees(Math.atan(diffY/diffX));
            aimAttack.setX((float) (center.x-8));
        }
        Log.i("vector", ""+vector[1]);
        if(diffY > 0) aimAttack.setY((float) (center.y-8-Math.abs(aimAttack.getHeight()*Math.sin(vector[1]))));
        else aimAttack.setY((float) (center.y-8));

        final Bitmap second = prime;
        runOnUiThread(new Runnable() {
            @Override public void run() {
                aimAttack.setImageDrawable(new BitmapDrawable(context.getResources(), second) {
                    @Override
                    public void draw(final Canvas canvas) {
                        canvas.save();
                        canvas.rotate((float)(vector[1]), second.getWidth() / 2, second.getHeight() / 2);
                        super.draw(canvas);
                        canvas.restore();
                    }
                });
                aimAttack.setVisibility(View.VISIBLE); }});
        //roatae and stretch aimattack
    }
    public void hideAim(){ runOnUiThread(new Runnable() {
        @Override public void run() {
            aimAttack.setVisibility(View.INVISIBLE); }}); }
    public void pulse(int newColor, double percent){
        int oldColor = filterAt;
        int red = (int) ((Color.red(newColor)*percent+ Color.red(oldColor)*(1-percent)));
        int blue = (int) ((Color.blue(newColor)*percent+ Color.blue(oldColor)*(1-percent)));
        int green = (int) ((Color.green(newColor)*percent+ Color.green(oldColor)*(1-percent)));
        int avg = Color.argb(255, red, green, blue);
        overlay.setColorFilter(avg);
    }
    private int fade(int color, double value, double max){
        int average = (Color.red(color)+Color.green(color)+Color.blue(color))/3+50;
        int red = (int)((1-value/max)*average+(value/max)*Color.red(color));
        int green = (int)((1-value/max)*average+(value/max)*Color.green(color));
        int blue = (int)((1-value/max)*average+(value/max)*Color.blue(color));
        filterAt = Color.argb(255, red, green, blue);
        return Color.argb(255, red, green, blue);
    }
    private int fade(int color, int negColor, double value, double max, double min){
        if(value < 0)return fade(negColor, -value, min);
        else return fade(color, value, max);
    }
    public void addCapComps(){
        mapLayout.addView(showSelected);
        mapLayout.addView(guestTroops);
        mapLayout.addView(owner);
        mapLayout.addView(status);
        mapLayout.addView(guestStatus);
        mapLayout.addView(aimAttack);
    }
    private void refreshComps(){
        runOnUiThread(new Runnable() {
            @Override public void run() {
                updateOverlays();
                updateOwner(); }});
    }
    public void doClick() {
        long startClick = System.currentTimeMillis();
        if(focusPlayer != null) Log.i("Focus", "Mode: "+mapMode+", flus: "+focusPlayer.getId());
        stopPulse();
        Log.i("ClickDelay5", ""+(System.currentTimeMillis()-startClick));
        if(debug){
            if(getInfoOpen()) {
                updatePress(-1);
                modTroops(-getTroops());
                //attrition-=.01;
                showInfo(self);
                return;
            }
            if(ownerId != getCurrentPlayer().getId())
                troopStacks = new ArrayList<>(0);
            if(ownerId == getCurrentPlayer().getId() || ownerId == -1) modTroops(1);
            updatePress(getCurrentPlayer().getId());
            coreOwner = getCurrentPlayer().getTag();
            return;
        }
        Log.i("ClickDelay4", ""+(System.currentTimeMillis()-startClick));
        if(mapMode != 8 || !getCurrentPlayer().isHuman()) {
            Log.i("doClick", "did, Stage:" + getCurrentPlayer().getStage() + ", PLayerId: "+getCurrentPlayer().getId() + ", OwnerId: "+ownerId);
            if (getCurrentPlayer().getStage() == -1 && (ownerId == -1 || ownerId == getCurrentPlayer().getId())) {
                boolean did = false;
                if (!imperium && getCurrentPlayer().getTroops() > 0) {
                    if (getMap().allTaken()) {
                        modTroops(1);
                        refreshComps();
                        getCurrentPlayer().modTroops(-1);
                        did = true;
                    }
                    if (ownerId == -1) {
                        ownerId = getCurrentPlayer().getId();
                        if(ownerId == -1) Log.i("Neggt", "");
                        modTroops(1);
                        refreshComps();
                        getCurrentPlayer().modTroops(-1);
                        did = true;
                    }
                } else if (imperium && getCurrentPlayer().modMonetae(0) >= MONETAE_TO_TROOPS) {
                    if (getMap().allTaken()) {
                        modTroops(1);
                        refreshComps();
                        getCurrentPlayer().modMonetae(-1 * MONETAE_TO_TROOPS);
                        did = true;
                    }
                    if (ownerId == -1) {
                        ownerId = getCurrentPlayer().getId();
                        if(ownerId == -1) Log.i("Neggp", "");
                        modTroops(1);
                        refreshComps();
                        getCurrentPlayer().modMonetae(-1 * MONETAE_TO_TROOPS);
                        did = true;
                    }
                }
                if ((imperium && getCurrentPlayer().modMonetae(0) < MONETAE_TO_TROOPS)
                        || (!imperium && getCurrentPlayer().getTroops() == 0)) {
                    Log.i("Staging", "setupDone");
                    getCurrentPlayer().setStage(1);
                    Log.i("swtupStage", "place0");
                    if(!isHistorical())
                        runOnUiThread(new Runnable() {
                        @Override public void run() {
                                getChange().setVisibility(View.VISIBLE);
                                getChange().setBackgroundResource(R.drawable.endattack); }});
                    if (getMap().isImperiumMap() == 1)
                        runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            revealProvMods(); }});
                } else if (did) {
                    Log.i("Skip", "did");
                    getCurrentPlayer().calcAllOwned(true);
                    changePlayer(false);
                }
            } else if (getCurrentPlayer().getStage() == 0 && ownerId == getCurrentPlayer().getId()) {
                Log.i("ClickDelay1", ""+(System.currentTimeMillis()-startClick));
                if (!imperium && getCurrentPlayer().getTroops() > 0) {
                    modTroops(1);
                    getCurrentPlayer().modTroops(-1);
                    getCurrentPlayer().setTempProvince(getMap().getList()[id - 1]);
                }
                if (imperium && getCurrentPlayer().modMonetae(0) >= MONETAE_TO_TROOPS) {
                    Log.i("TroopPLace", "Placed troop at " + name);
                    modTroops(1);
                    Log.i("ClickDelay2", ""+(System.currentTimeMillis()-startClick));
                    getCurrentPlayer().modMonetae(-1 * MONETAE_TO_TROOPS);
                    getCurrentPlayer().setTempProvince(getMap().getList()[id - 1]);
                    Log.i("ClickDelay3", ""+(System.currentTimeMillis()-startClick));
                }
            } else if (getCurrentPlayer().getStage() == 1 || getCurrentPlayer().getStage() == 2) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (selected) {
                            setAndShowSelection(false);
                            //getCurrentPlayer().select(1);
                        } else if (!selected && getCurrentPlayer().select(0) > 0) {
                            setAndShowSelection(true);
                            //getCurrentPlayer().select(-1);
                        }
                    }
                });
            }
            /*if (getOwner() != null){
                updatePress(ownerId);
            }*/
        }
        Log.i("ClickDelayFinasl", ""+(System.currentTimeMillis()-startClick));
    }
    public void doLongClick(){showInfo(self);}

    private void buildComps(){
        try{
            InputStream in = context.getAssets().open("overlays/"+getMap().getMapFilePath()+id+".png");
            byte[] inBytes = new byte[in.available()];
            in.read(inBytes);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inMutable = true;
            try {
                overBit = BitmapFactory.decodeByteArray(inBytes, 0, inBytes.length, options);
            }catch(OutOfMemoryError e){e.printStackTrace();}
            overlay.setImageBitmap(overBit);
            //Log.i("provId", ""+id+", Bitwidth:"+overBit.getWidth()+", Bitheight"+overBit.getHeight());
        }catch(Exception e){ e.printStackTrace();}
        x += 3; y += 1;
        owner.setLayoutParams(new RelativeLayout.LayoutParams((int)(10*getMap().getStatusScale()), (int)(5*getMap().getStatusScale())));
        guestTroops.setLayoutParams(new RelativeLayout.LayoutParams((int)(8*getMap().getStatusScale()), (int)(4*getMap().getStatusScale())));
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int)(overBit.getWidth()*getMap().overScale), (int)(overBit.getHeight()*getMap().overScale));
        params.leftMargin = x; params.topMargin = y;
        overlay.setLayoutParams(params);
        showSelected.setLayoutParams(new RelativeLayout.LayoutParams(20, 20));
        status.setLayoutParams(new RelativeLayout.LayoutParams(20, 20));
        guestStatus.setLayoutParams(new RelativeLayout.LayoutParams(20, 20));
        aimAttack.setLayoutParams(new RelativeLayout.LayoutParams(60, 60));
        mapLayout.addView(overlay);

        status.bringToFront();
        center = new Point(x+(int)(overBit.getWidth()/2*getMap().overScale), y+(int)(overBit.getHeight()/2*getMap().overScale));
        aimAttack.setX(center.x-8);
        aimAttack.setY(center.y-8);
        owner.setX(center.x-5);
        owner.setY(center.y-3);
        guestTroops.setX(center.x-4);
        guestTroops.setY(center.y+2);
        guestStatus.setX(center.x-3);
        guestStatus.setY(center.y+1);
        showSelected.setX(center.x-10);
        showSelected.setY(center.y-10);
        status.setX(center.x-3);
        status.setY(center.y-3);
        //overlay.setVisibility(View.INVISIBLE);
        overlay.setColorFilter(PLAYER_NONE);
        showSelected.setVisibility(View.INVISIBLE);
        owner.setVisibility(View.INVISIBLE);
        guestTroops.setVisibility(View.INVISIBLE);
        showSelected.setBackgroundResource(R.drawable.selected);
        status.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 0);
        guestStatus.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 0);
        status.setTextColor(BLACK);
        guestStatus.setTextColor(BLACK);
    }
}
