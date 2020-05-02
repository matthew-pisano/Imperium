package com.reactordevelopment.Imperium;

import android.util.Log;

import java.io.Serializable;

public class Continent extends Map implements Serializable {
    private String name;
    private int id;
    private int bonus;
    private int baseBonus;
    private double interest;
    private Province[] provinces;
    private int[] provinceIds;
    private int color;
    public Continent(){}
    public Continent(int id, int color, String name, int bonus, double interest){
        this.color = color;
        this.name = name;
        this.id = id;
        baseBonus = bonus;
        this.bonus = bonus;
        this.interest = interest;
    }
    public int getColor(){return color;}
    public double getInterest(){return interest;}
    public int getBonus(){return bonus;}
    public int getId(){return id;}
    public String getName(){return name;}
    public Province[] getList(){return provinces;}

    public void fill(int from, int to){
        provinceIds = new int[to-from+1];
        for(int i=from; i<to+1; i++)
            provinceIds[i-from] = i+1;

    }
    public void makeList(){
        provinces = new Province[provinceIds.length];
        for(int i=provinceIds[0]-1; i<provinceIds[provinceIds.length-1]; i++)
            provinces[i-(provinceIds[0]-1)] = provinceList[i];

    }
    public int hasIn(Province[] list){
        int count = 0;
        for(Province owned : list) {
            if(owned.getContinentId() == id) count++;
            /*for (Province in : provinces) {
                if (owned.getId() == in.getId()) {
                    count++;
                    //Log.i("continent", "contains " + provinces[j].getName() + " in continent " + name);
                }
            }*/
        }
        //Log.i("continent", "" + count + " provinces out of " + provinces.length + " in " + name);
        return count;
    }
    public boolean hasComplete(Province[] list){ return hasIn(list) == provinces.length; }
    public Player completedBy(){
        Player hedgemon = null;
        for(Player p : getPlayerList())
            if(hasComplete(p.getAllOwned())) hedgemon = p;
        return hedgemon;
    }
    public boolean isFull(){
        for(int i=0; i<provinces.length; i++){
                if(provinces[i].getOwnerId() == -1)
                    return false;
            }
        return true;
    }
    public double totalDevastation(){
        double total = 0;
        for (int i=0; i<provinces.length; i++){
            total += provinces[i].modDevastation(0);
        }
        bonus = baseBonus - (int) total;
        Log.i("totalDevatation", "new bonus: " + bonus);
        return total;
    }
}
