package com.reactordevelopment.ImperiumLite.core.mapping;

import android.util.Log;

import com.reactordevelopment.ImperiumLite.core.gameTypes.Game;
import com.reactordevelopment.ImperiumLite.core.player.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Map extends Game implements Serializable {
    protected static Province[] provinceList;
    protected static Continent[] continents = new Continent[0];
    protected ArrayList<Integer> borders;
    protected int imperiumMap;
    protected static String mapFilePath;
    protected int id;
    protected double statusScale;
    protected double overScale;

    public Map(){}
    public int getId(){return id;}
    public double getOverScale(){return overScale;}
    public static String getMapFilePath(){return mapFilePath;}
    public Province[] getList(){ return provinceList; }
    public Continent[] getContinents(){
        return continents;
    }
    public double getStatusScale() {return statusScale;}
    public int isImperiumMap(){return imperiumMap;}

    public void place(){
        for(Province prov : provinceList)
            prov.place();
        for(Province prov : provinceList)
            prov.addCapComps();

        if(imperiumMap == 0)
            for(int i=0; i<continents.length; i++)
                continents[i].makeList();
    }
    public int getTotalDev(){
        double dev = 0;
        for(Province p : provinceList)
            dev += p.calcOutput();
        return (int) dev;
    }

    public int getMaxDev(){
        double maxDev = 0;
        for(Province p : provinceList) {
            double provDev = p.calcOutput();
            if (provDev > maxDev) maxDev = provDev;
        }
        return (int) maxDev;
    }
    public void updateDevastation(){
        for(Province p : map.getList()) p.modDevastation(-.003);
    }

    public void resetAll(){for(Province p :provinceList) p.resetValues();}
    public boolean allTaken(){ //checks if all a maps provinces is owned by a player
        for(int i=0; i<provinceList.length; i++)
            if(provinceList[i].getOwnerId() == -1)
                return false;
        return true;
    }
    public boolean allOwned(Player player){ //chacks if a given player owns the whole map
        for(int i=0; i<provinceList.length; i++)
            if(provinceList[i].getOwnerId() != player.getId())
                return false;

        return true;
    }
    public int bonuses(Player player){
        Province[] provinces = player.getAllOwned();
        int totalBonuses = 0;
        for(int i=0; i<continents.length; i++){
            if(continents[i].hasComplete(provinces))
                totalBonuses += continents[i].getBonus();
        }
        return totalBonuses;
    }
    public int ownedContinents(Player player){
        Province[] provinces = player.getAllOwned();
        int total = 0;
        for(int i=0; i<continents.length; i++){
            if(continents[i].hasComplete(provinces))
                total++;
        }
        return total;
    }

    public void decayAll(){
        for(int i=0; i<provinceList.length; i++)
            provinceList[i].decay();
    }
    protected int[] bordering(){
        int[] out = new int[borders.size()];
        for(int i=0; i<out.length; i++)
            out[i] = borders.get(i);
        return out;
    }
    public int hegemonyBonus(Province[] owned){
        double per = provinceList.length/(double)owned.length;
        if(per > .35) return 1;
        if(per > .4) return 2;
        if(per > .5) return 3;
        if(per > .75) return 4;
        return  0;
    }
    public void resetSelections(){
        for(Province p : provinceList) if(p.isSelected()) p.setSelected(false);
    }
    public double[] vector(Province a, Province b){ //{magnitude, angle}
        double[] vector = new double[2];
        double diffX = a.getCenter().x - b.getCenter().x; double diffY = a.getCenter().y - b.getCenter().y;
        vector[0] = Math.sqrt(diffX*diffX + diffY*diffY);
        if(diffX < 0)vector[1] =  Math.toDegrees(Math.atan(diffY/diffX)) + 180;
        else vector[1] =  Math.toDegrees(Math.atan(diffY/diffX));
        return vector;
    }
    public static int howSurrounded(Province province){ //returns number of enemy provinces that border an owned province
        int count = 0;
        for(Province test : province.getBordering())
            if(test.getOwnerId() != province.getOwnerId())
                count += test.getTroops();
        return  count;
    }
    public double maxTroops(){
        double max = 0;
        for(Province p : provinceList)
            if(p.getTroops() > max) max = p.getTroops();
        return max;
    }
    protected double vary(){return Math.random()*.2-.1;}

    public Province provFromId(int id){
        for(Province p : getList())
            if(p.getId() == id)
                return p;
        return null;
    }
    public Province provFromId(String id){
        return provFromId(Integer.parseInt(id));
    }
    public void logContinents(){
        for(Continent cont : getContinents()) {
            Log.i("ContCheck", "Continent:"+cont.getName());
            for (Province prov : cont.getList())Log.i("ContCheck", "     Province:"+prov.getName());
        }
    }
    public double mostInterest(){
        double max = 0;
        for(Province p : getList())
            if(p.getInterest() > max)
                max = p.getInterest();
        return max;
    }
}
