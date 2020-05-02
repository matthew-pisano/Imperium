package com.reactordevelopment.Imperium;

import android.util.Log;

public class TroopStack {
    private String ownerTag;
    private double troops = 0;
    private int movesLeft;

    public TroopStack(String tag, double initTroops){
        ownerTag = tag;
        troops = initTroops;
        movesLeft = 1;
    }
    public TroopStack(String tag, double initTroops, int moves){
        ownerTag = tag;
        troops = initTroops;
        movesLeft = moves;
    }

    public void setTroops(double set){troops = set;}
    public void modTroops(double mod){
        Log.i("Stackmod of "+ownerTag, "mod: "+mod+", tropsbfore: "+troops);
        if(troops + mod > 0) troops += mod;
        else troops = 0;
    }
    public double getTroops(){return troops;}
    public String getOwnerTag(){return ownerTag;}
    public void move(){if(movesLeft >= 1) movesLeft--;}
    public void resetMoves(){movesLeft = 1;}
    public int getMovesLeft(){return movesLeft;}
}
