package com.reactordevelopment.ImperiumLite;

import android.util.Log;

import java.io.InputStream;

import static com.reactordevelopment.ImperiumLite.MainActivity.SAVE_FORM;

import com.reactordevelopment.ImperiumLite.MappedActivities.GameActivity;

public class SaveBooter extends GameActivity {

    public SaveBooter(/*double ver, String timeline, int year*/){
        //buildVersion(ver, timeline, year);
    }
    public void buildVersion(double ver, String timeline, int year){
        if(ver == 1.1){
            logLoad();
            int plStrLen = 3 + 1 + 1 + SAVE_FORM[2] + SAVE_FORM[3] + SAVE_FORM[4]; //length of player information
            int pStrLen = SAVE_FORM[8] + SAVE_FORM[1] + SAVE_FORM[5] + SAVE_FORM[6] + SAVE_FORM[7] + 1; //length of province information
            int init = 8; //encodin, mapId, turnNum
            int plLen = 3;
            mapImperium(get(4, 1));
            Log.i("plterLen", loadString.substring(init + pStrLen * map.getList().length, init + pStrLen * map.getList().length+pStrLen)+", "+map.getList().length+", "+(init + pStrLen * map.getList().length));
            game = new Game(context, get(init + pStrLen * map.getList().length, 3), get(loadString.indexOf("|") - 1, 1), new Object[]{"", ""});
            setupMap();
            game.setPlayerLength(get(init + pStrLen * map.getList().length, 3));
            game.getMap().resetAll();

            game.setTurnNum(get(5, SAVE_FORM[0]));
            if(debugingOn) game.setTurnNum(0);
            for(int i=0; i<game.getMap().getList().length; i++){
                Province pAt = game.getMap().getList()[i];
                Log.i("inProv", "{"+loadString.substring(init+pStrLen*i, init+6+pStrLen*i)+"}");
                //pAt.updatePress(get(init+pStrLen*i, SAVE_FORM[8]));
                pAt.setLoadTag(loadString.substring(init+pStrLen*i, init+pStrLen*i+SAVE_FORM[8]));
                pAt.modTroops(getD(init+SAVE_FORM[8]+pStrLen*i, SAVE_FORM[1]));
                pAt.setDevelopment(getD(init+SAVE_FORM[8]+SAVE_FORM[1]+pStrLen*i, SAVE_FORM[5]));
                pAt.modDevastation(getD(init+SAVE_FORM[8]+SAVE_FORM[1]+SAVE_FORM[5]+pStrLen*i, SAVE_FORM[6]));
                pAt.setAttrition(getD(init+SAVE_FORM[8]+SAVE_FORM[1]+SAVE_FORM[5]+SAVE_FORM[6]+pStrLen*i, SAVE_FORM[7]));
                pAt.setFortLevel(get(init+SAVE_FORM[8]+SAVE_FORM[1]+SAVE_FORM[5]+SAVE_FORM[6]+SAVE_FORM[7]+pStrLen*i, 1));
            }
            Log.i("building", "player strt");
            boolean[] ais = new boolean[game.getPlayerList().length];
        /*for(int i=0; i<get(init+pStrLen*map.getList().length, 1); i++){
            Log.i("buiding", "{"+loadString.substring(i+(init+plLen+1+pStrLen*map.getList().length+plStrLen*game.getPlayerList().length), 1+i+(init+plLen+1+pStrLen*map.getList().length+plStrLen*game.getPlayerList().length))+"}");
            if(get(i+(init+plLen+1+pStrLen*map.getList().length+plStrLen*game.getPlayerList().length), 1) == 0) {
                ais[i] = false;//game.getPlayerList()[i] = new Player(context, i, game.getImperium());
            }
            else ais[i] = true;//game.getPlayerList()[i] = new Ai(context, i, AI_STYLE, game.getImperium());
        }*/
            for(int i=0; i<game.getPlayerList().length; i++){
                int index = init+plLen+pStrLen*map.getList().length+plStrLen*i;
                ais[i] = loadString.charAt(index+3) == '1';
            }
            for(int player=0; player<game.getPlayerList().length; player++){
                int index = init+plLen+pStrLen*map.getList().length+plStrLen*player;
                Log.i("PlayerSave", "Text: "+loadString.substring(index, 17+index)+", "+ais[player]);
                if(!ais[player] || debugingOn) game.getPlayerList()[player] = new Player(context, player, game.getImperium(), loadString.substring(index, index+3));
                else game.getPlayerList()[player] = new Ai(context, player, AI_STYLE, game.getImperium(), loadString.substring(index, index+3));
                index += 3+1;
                game.getPlayerList()[player].setStage(get(index, 1));
                index += 1;
                Log.i("TroopsSet", ""+loadString.substring(index, index+SAVE_FORM[2]));
                game.getPlayerList()[player].setTroops(getD(index, SAVE_FORM[2]));
                index += SAVE_FORM[2];
                Log.i("monrtes", ""+loadString.substring(index, index+SAVE_FORM[3]));
                game.getPlayerList()[player].setMonetae(get(index, SAVE_FORM[3]));
                index += SAVE_FORM[3];
                Log.i("conquests", loadString.substring(index, index+SAVE_FORM[4]));
                game.getPlayerList()[player].setConquers(get(index, SAVE_FORM[4]));
            }
            Log.i("currentPlayer", loadString.substring(init+plLen+pStrLen*map.getList().length+plStrLen*game.getPlayerList().length, init+plLen+pStrLen*map.getList().length+plStrLen*game.getPlayerList().length+1));
            game.setCurrentPlayer(get(init+plLen+pStrLen*map.getList().length+plStrLen*game.getPlayerList().length, 1));
            gameControls();
        }
        if(ver == 1.2) {
            logLoad();
            int plStrLen = 3 + 1 + 1 + SAVE_FORM[2] + SAVE_FORM[3] + SAVE_FORM[4]; //length of player information
            int pStrLen = SAVE_FORM[8] + SAVE_FORM[1] + SAVE_FORM[5] + SAVE_FORM[6] + SAVE_FORM[7] + 1; //length of province information
            int init = 8; //encoding, mapId, turnNum
            int plLen = 3;
            mapImperium(get(4, 1));
            Log.i("Testyear", loadString.substring(loadString.indexOf('[')+1, loadString.indexOf(',', loadString.indexOf('[')))
                    +", "+loadString.substring(loadString.indexOf(',', loadString.indexOf('['))+1, loadString.indexOf(']')));
            Log.i("plterLen", loadString.substring(init + pStrLen * map.getList().length, init + pStrLen * map.getList().length + pStrLen) + ", " + map.getList().length + ", " + (init + pStrLen * map.getList().length));
            game = new Game(context, get(init + pStrLen * map.getList().length, 3), get(loadString.indexOf("|") - 1, 1),
                    new Object[]{loadString.substring(loadString.indexOf('[')+1, loadString.indexOf(',', loadString.indexOf('['))), loadString.substring(loadString.indexOf(',', loadString.indexOf('['))+1, loadString.indexOf(']'))});

            setupMap();
            game.setPlayerLength(get(init + pStrLen * map.getList().length, 3));
            game.setTimeline(loadString.substring(loadString.indexOf('[')+1, loadString.indexOf(',', loadString.indexOf('['))));
            game.setYear(Integer.parseInt(loadString.substring(loadString.indexOf(',', loadString.indexOf('['))+1, loadString.indexOf(']'))));
            game.getMap().resetAll();

            game.setTurnNum(get(5, SAVE_FORM[0]));
            if (debugingOn) game.setTurnNum(0);
            for (int i = 0; i < game.getMap().getList().length; i++) {
                Province pAt = game.getMap().getList()[i];
                Log.i("inProv", "{" + loadString.substring(init + pStrLen * i, init + 6 + pStrLen * i) + "}");
                pAt.setLoadTag(loadString.substring(init + pStrLen * i, init + pStrLen * i + SAVE_FORM[8]));
                pAt.modTroops(getD(init + SAVE_FORM[8] + pStrLen * i, SAVE_FORM[1]));
                pAt.setDevelopment(getD(init + SAVE_FORM[8] + SAVE_FORM[1] + pStrLen * i, SAVE_FORM[5]));
                pAt.modDevastation(getD(init + SAVE_FORM[8] + SAVE_FORM[1] + SAVE_FORM[5] + pStrLen * i, SAVE_FORM[6]));
                pAt.setAttrition(getD(init + SAVE_FORM[8] + SAVE_FORM[1] + SAVE_FORM[5] + SAVE_FORM[6] + pStrLen * i, SAVE_FORM[7]));
                pAt.setFortLevel(get(init + SAVE_FORM[8] + SAVE_FORM[1] + SAVE_FORM[5] + SAVE_FORM[6] + SAVE_FORM[7] + pStrLen * i, 1));
            }
            Log.i("building", "player strt");
            boolean[] ais = new boolean[game.getPlayerList().length];

            for (int i = 0; i < game.getPlayerList().length; i++) {
                int index = init + plLen + pStrLen * map.getList().length + plStrLen * i;
                ais[i] = loadString.charAt(index + 3) == '1';
            }
            for (int player = 0; player < game.getPlayerList().length; player++) {
                int index = init + plLen + pStrLen * map.getList().length + plStrLen * player;
                Log.i("PlayerSave", "Text: " + loadString.substring(index, 17 + index));
                if (!ais[player] || debugingOn)
                    game.getPlayerList()[player] = new Player(context, player, game.getImperium(), loadString.substring(index, index + 3));
                else
                    game.getPlayerList()[player] = new Ai(context, player, AI_STYLE, game.getImperium(), loadString.substring(index, index + 3));
                index += 3 + 1;
                game.getPlayerList()[player].setStage(get(index, 1));
                index += 1;
                Log.i("TroopsSet", "" + loadString.substring(index, index + SAVE_FORM[2]));
                game.getPlayerList()[player].setTroops(getD(index, SAVE_FORM[2]));
                index += SAVE_FORM[2];
                Log.i("monrtes", "" + loadString.substring(index, index + SAVE_FORM[3]));
                game.getPlayerList()[player].setMonetae(get(index, SAVE_FORM[3]));
                index += SAVE_FORM[3];
                Log.i("conquests", loadString.substring(index, index + SAVE_FORM[4]));
                game.getPlayerList()[player].setConquers(get(index, SAVE_FORM[4]));
            }
            Log.i("currentPlayer", loadString.substring(init + plLen + pStrLen * map.getList().length + plStrLen * game.getPlayerList().length, init + plLen + pStrLen * map.getList().length + plStrLen * game.getPlayerList().length + 1));
            game.setCurrentPlayer(get(init + plLen + pStrLen * map.getList().length + plStrLen * game.getPlayerList().length, 1));
            gameControls();
        }
        if(ver == 1.3) {
            logLoad();
            int plStrLen = 3 + 1 + 1 + SAVE_FORM[2] + SAVE_FORM[3] + SAVE_FORM[4]; //length of player information
            int pStrLen = SAVE_FORM[8] + SAVE_FORM[1] + SAVE_FORM[5] + SAVE_FORM[6] + SAVE_FORM[7] + 1; //length of province information
            int init = 8; //encoding, mapId, turnNum
            int plLen = 3;
            mapImperium(get(4, 1));
            Log.i("Testyear", loadString.substring(loadString.indexOf('[')+1, loadString.indexOf(',', loadString.indexOf('[')))
                    +", "+loadString.substring(loadString.indexOf(',', loadString.indexOf('['))+1, loadString.indexOf(']')));
            Log.i("plterLen", loadString.substring(loadString.indexOf("!"), loadString.indexOf("!") + plLen) + ", " + map.getList().length + ", " + (init + pStrLen * map.getList().length));
            game = new Game(context, get(loadString.indexOf("!")+1, plLen), get(loadString.indexOf("|") - 1, 1),
                    new Object[]{loadString.substring(loadString.indexOf('[')+1, loadString.indexOf(',', loadString.indexOf('['))), loadString.substring(loadString.indexOf(',', loadString.indexOf('['))+1, loadString.indexOf(']'))});

            setupMap();
            game.setPlayerLength(get(loadString.indexOf("!")+1, plLen));
            game.setTimeline(loadString.substring(loadString.indexOf('[')+1, loadString.indexOf(',', loadString.indexOf('['))));
            game.setYear(Integer.parseInt(loadString.substring(loadString.indexOf(',', loadString.indexOf('['))+1, loadString.indexOf(']'))));
            game.getMap().resetAll();

            game.setTurnNum(get(5, SAVE_FORM[0]));
            if (debugingOn) game.setTurnNum(0);
            int lastStart = 0;
            lastStart = loadString.indexOf("<", lastStart)+1;
            for (int i = 0; i < game.getMap().getList().length; i++) {
                Province pAt = game.getMap().getList()[i];
                Log.i("inProv", "{" + loadString.substring(lastStart, lastStart+pStrLen) + "}");
                pAt.setLoadTag(loadString.substring(lastStart, lastStart + SAVE_FORM[8]));
                pAt.modTroops(getD(lastStart + SAVE_FORM[8], SAVE_FORM[1]), loadString.substring(lastStart, lastStart + SAVE_FORM[8]));
                pAt.setDevelopment(getD(lastStart + SAVE_FORM[8] + SAVE_FORM[1], SAVE_FORM[5]));
                pAt.modDevastation(getD(lastStart + SAVE_FORM[8] + SAVE_FORM[1] + SAVE_FORM[5], SAVE_FORM[6]));
                pAt.setAttrition(getD(lastStart + SAVE_FORM[8] + SAVE_FORM[1] + SAVE_FORM[5] + SAVE_FORM[6], SAVE_FORM[7]));
                pAt.setFortLevel(get(lastStart + SAVE_FORM[8] + SAVE_FORM[1] + SAVE_FORM[5] + SAVE_FORM[6] + SAVE_FORM[7], 1));
                lastStart = loadString.indexOf("<", lastStart)+1;
            }
            Log.i("building", "player strt");
            int saveStart = lastStart;
            boolean[] ais = new boolean[game.getPlayerList().length];

            for (int i = 0; i < game.getPlayerList().length; i++) {
                //int index = init + plLen + pStrLen * map.getList().length + plStrLen * i;
                ais[i] = loadString.charAt(lastStart + 3) == '1';
                lastStart = loadString.indexOf("<", lastStart)+1;
            }
            lastStart = saveStart;
            for (int player = 0; player < game.getPlayerList().length; player++) {
                //int index = init + plLen + pStrLen * map.getList().length + plStrLen * player;
                int index = lastStart;
                Log.i("PlayerSave", "Text: " + loadString.substring(index, 17 + index));
                if (!ais[player] || debugingOn)
                    game.getPlayerList()[player] = new Player(context, player, game.getImperium(), loadString.substring(index, index + 3));
                else
                    game.getPlayerList()[player] = new Ai(context, player, AI_STYLE, game.getImperium(), loadString.substring(index, index + 3));
                index += 3 + 1;
                game.getPlayerList()[player].setStage(get(index, 1));
                index += 1;
                Log.i("TroopsSet", "" + loadString.substring(index, index + SAVE_FORM[2]));
                game.getPlayerList()[player].setTroops(getD(index, SAVE_FORM[2]));
                index += SAVE_FORM[2];
                Log.i("monrtes", "" + loadString.substring(index, index + SAVE_FORM[3]));
                game.getPlayerList()[player].setMonetae(get(index, SAVE_FORM[3]));
                index += SAVE_FORM[3];
                Log.i("conquests", loadString.substring(index, index + SAVE_FORM[4]));
                game.getPlayerList()[player].setConquers(get(index, SAVE_FORM[4]));

                index = loadString.indexOf("(", index)+1;
                int endDip = loadString.indexOf(")", index);
                int dipAt = 0;
                while (index < endDip){
                    Log.i("BuoldDip", loadString.substring(lastStart, lastStart+3)+"| "+loadString.substring(index, index+1)+"| "+loadString.substring(index, endDip));
                    if(loadString.charAt(index) == ','){
                        index++; dipAt++;
                        continue;
                    }
                    if(dipAt == 0){
                        //Log.i("LoadRequest", "to "+game.getPlayerList()[player].getTag()+" from "+loadString.substring(index+8, index+11));
                        game.getPlayerList()[player].addRequestFrom(Integer.parseInt(loadString.substring(index, index+1)), loadString.substring(index+1, index+1+6), loadString.substring(index+7, index+10));
                    }
                    if(dipAt == 1) game.getPlayerList()[player].addAlly(loadString.substring(index, index+3));
                    if(dipAt == 2) game.getPlayerList()[player].addMinion(loadString.substring(index, index+3));
                    if(dipAt == 3) game.getPlayerList()[player].getDiplo()[3].add(loadString.substring(index, index+3)+loadString.substring(index+3, index+6)+loadString.substring(index+6, index+9));
                    if(dipAt == 4) game.getPlayerList()[player].addTruce(loadString.substring(index, index+7));
                    if(dipAt == 5) game.getPlayerList()[player].addOverlord(loadString.substring(index, index+3));
                    index += 3;
                    if(dipAt == 4) index += 4;
                    if(dipAt == 3) index += 6;
                    if(dipAt == 0) index += 7;
                }
                lastStart = loadString.indexOf("<", lastStart)+1;
            }
            Log.i("currentPlayer", loadString.substring(loadString.indexOf("!", loadString.indexOf("!")), loadString.indexOf("!", loadString.indexOf("!"))+3));
            game.setCurrentPlayer(get(loadString.indexOf("!", loadString.indexOf("!")+1)+1, 3));
            Log.i("currentplayer", ""+loadString.substring(loadString.indexOf("!", loadString.indexOf("!")+1)+1));
            for(Player p : game.getPlayerList()) p.printDiplo();
            gameControls();
            game.inSetup = false;
            Log.i("OutSetup", "out3");
        }
        if(ver == 1.31) {
            logLoad();
            int plStrLen = 3 + 1 + 1 + SAVE_FORM[2] + SAVE_FORM[3] + SAVE_FORM[4]; //length of player information
            int pStrLen = SAVE_FORM[8] + SAVE_FORM[1] + SAVE_FORM[5] + SAVE_FORM[6] + SAVE_FORM[7] + 1; //length of province information
            int init = 8; //encoding, mapId, turnNum
            int plLen = 3;

            mapImperium(get(4, 1));
            Log.i("Testyear", loadString.substring(loadString.indexOf('[')+1, loadString.indexOf(',', loadString.indexOf('[')))
                    +", "+loadString.substring(loadString.indexOf(',', loadString.indexOf('['))+1, loadString.indexOf(']')));
            Log.i("plterLen", loadString.substring(loadString.indexOf("!"), loadString.indexOf("!") + plLen) + ", " + map.getList().length + ", " + (init + pStrLen * map.getList().length));
            game = new Game(context, get(loadString.indexOf("!")+1, plLen), get(loadString.indexOf("|") - 1, 1),
                    new Object[]{loadString.substring(loadString.indexOf('[')+1, loadString.indexOf(',', loadString.indexOf('['))), loadString.substring(loadString.indexOf(',', loadString.indexOf('['))+1, loadString.indexOf(']'))});

            setupMap();

            game.setPlayerLength(get(loadString.indexOf("!")+1, plLen));
            game.setTimeline(loadString.substring(loadString.indexOf('[')+1, loadString.indexOf(',', loadString.indexOf('['))));
            game.setYear(Integer.parseInt(loadString.substring(loadString.indexOf(',', loadString.indexOf('['))+1, loadString.indexOf(']'))));
            game.getMap().resetAll();

            game.setTurnNum(get(5, SAVE_FORM[0]));
            if (debugingOn) game.setTurnNum(0);
            int lastStart = 0;
            lastStart = loadString.indexOf("<", lastStart)+1;
            boolean ranOut = false;
            for (int i = 0; i < game.getMap().getList().length; i++) {
                Province pAt = game.getMap().getList()[i];
                Log.i("inProv", pAt.getName()+": {" + loadString.substring(lastStart, loadString.indexOf("<", lastStart)) + "}");
                pAt.setLoadTag(loadString.substring(lastStart, lastStart + SAVE_FORM[8]));
                //pAt.modTroops(getD(lastStart + SAVE_FORM[8], SAVE_FORM[1]), loadString.substring(lastStart, lastStart + SAVE_FORM[8]));
                pAt.setCoreOwner(loadString.substring(lastStart + SAVE_FORM[8], lastStart + SAVE_FORM[8] + 3));
                pAt.setDevelopment(getD(lastStart + SAVE_FORM[8] + 3, SAVE_FORM[5]));
                pAt.modDevastation(getD(lastStart + SAVE_FORM[8] + 3 + SAVE_FORM[5], SAVE_FORM[6]));
                pAt.setAttrition(getD(lastStart + SAVE_FORM[8] + 3 + SAVE_FORM[5] + SAVE_FORM[6], SAVE_FORM[7]));
                pAt.setFortLevel(get(lastStart + SAVE_FORM[8] + 3 + SAVE_FORM[5] + SAVE_FORM[6] + SAVE_FORM[7], 1));
                int stackAt = loadString.indexOf("(", lastStart)+1;
                while(stackAt < loadString.indexOf(")", lastStart)-1){
                    String stackString = loadString.substring(stackAt, loadString.indexOf(",", stackAt));
                    Log.i("MakeStack", stackString+", "+stackString.length()+", "
                            +stackString.substring(0, 3));
                    TroopStack ts = new TroopStack(stackString.substring(0, 3),
                            Double.parseDouble(stackString.substring(3, 8))
                            , Integer.parseInt(stackString.substring(8, 9)));
                    pAt.getTroopStacks().add(ts);
                    stackAt += stackString.length()+1;
                }
                if(loadString.indexOf("!", lastStart) < loadString.indexOf("<", lastStart)) {
                    ranOut = true;
                    break; //break after ran out of provinces to load, while creating new provinces not in save
                }
                lastStart = loadString.indexOf("<", lastStart)+1;
            }
            if(ranOut) lastStart = loadString.indexOf("<", lastStart)+1;
            Log.i("building", "player strt");
            int saveStart = lastStart;
            boolean[] ais = new boolean[game.getPlayerList().length];

            for (int i = 0; i < game.getPlayerList().length; i++) {
                //int index = init + plLen + pStrLen * map.getList().length + plStrLen * i;
                ais[i] = loadString.charAt(lastStart + 3) == '1';
                lastStart = loadString.indexOf("<", lastStart)+1;
            }
            lastStart = saveStart;
            for (int player = 0; player < game.getPlayerList().length; player++) {
                //int index = init + plLen + pStrLen * map.getList().length + plStrLen * player;
                int index = lastStart;
                if (!ais[player] || debugingOn)
                    game.getPlayerList()[player] = new Player(context, player, game.getImperium(), loadString.substring(index, index + 3));
                else
                    game.getPlayerList()[player] = new Ai(context, player, AI_STYLE, game.getImperium(), loadString.substring(index, index + 3));
                Log.i("PlayerSave", game.getPlayerList()[player].getTag()+"Text: " + loadString.substring(index, 17 + index));
                index += 3 + 1;
                Log.i("LoadStage", "Stage: "+loadString.substring(index-1, index+1));
                game.getPlayerList()[player].setStage(get(index, 1));
                index += 1;
                Log.i("TroopsSet", "" + loadString.substring(index, index + SAVE_FORM[2]));
                game.getPlayerList()[player].setTroops(getD(index, SAVE_FORM[2]));
                index += SAVE_FORM[2];
                Log.i("monrtes", "" + loadString.substring(index, index + SAVE_FORM[3]));
                game.getPlayerList()[player].setMonetae(get(index, SAVE_FORM[3]));
                index += SAVE_FORM[3];
                Log.i("conquests", loadString.substring(index, index + SAVE_FORM[4]));
                game.getPlayerList()[player].setConquers(get(index, SAVE_FORM[4]));

                index = loadString.indexOf("(", index)+1;
                int endDip = loadString.indexOf(")", index);
                int dipAt = 0;
                while (index < endDip){
                    Log.i("BuoldDip", loadString.substring(lastStart, lastStart+3)+"| "+loadString.substring(index, index+1)+"| "+loadString.substring(index, endDip));
                    if(loadString.charAt(index) == ','){
                        index++; dipAt++;
                        continue;
                    }
                    if(dipAt == 0) {
                        Log.i("LoadRequest", "to "+game.getPlayerList()[player].getTag()+" from "+loadString.substring(index+8, index+11));
                        game.getPlayerList()[player].addRequestFrom(Integer.parseInt(loadString.substring(index, index+2)), loadString.substring(index+2, index+2+6), loadString.substring(index+8, index+11));
                    }
                    Log.i("DiploBuild", ""+game.inSetup);
                    if(dipAt == 1) game.getPlayerList()[player].addAlly(loadString.substring(index, index+3));
                    Log.i("DiploBuild2", ""+game.inSetup);
                    if(dipAt == 2) game.getPlayerList()[player].addMinion(loadString.substring(index, index+3));
                    if(dipAt == 3) game.getPlayerList()[player].getDiplo()[3].add(loadString.substring(index, index+3)+loadString.substring(index+3, index+6)+loadString.substring(index+6, index+9));
                    if(dipAt == 4) game.getPlayerList()[player].addTruce(loadString.substring(index, index+7));
                    if(dipAt == 5) game.getPlayerList()[player].addOverlord(loadString.substring(index, index+3));
                    index += 3;
                    if(dipAt == 4) index += 4;
                    if(dipAt == 3) index += 6;
                    if(dipAt == 0) index += 8;
                }
                lastStart = loadString.indexOf("<", lastStart)+1;
            }
            Log.i("currentPlayer", loadString.substring(loadString.indexOf("!", loadString.indexOf("!")), loadString.indexOf("!", loadString.indexOf("!"))+3));
            game.setCurrentPlayer(get(loadString.indexOf("!", loadString.indexOf("!")+1)+1, 3));
            Log.i("currentplayer", ""+loadString.substring(loadString.indexOf("!", loadString.indexOf("!")+1)+1));
            for(Player p : game.getPlayerList()) p.printDiplo();
            gameControls();
            game.inSetup = false;
            Log.i("OutSetup", "out4");
        }
    }
    private void logLoad(){
        Log.i("loadedString", "String: {");
        for (int i = 0; i < loadString.length(); i += 4000)
            if (i + 4000 < loadString.length())
                Log.i("loadedString", loadString.substring(i, i + 4000));
            else {
                Log.i("loadedString", loadString.substring(i));
                break;
            }
        Log.i("loadedString", "} String done");
    }
    public String loadFileString(String mapPath, String timeline, int year){
        byte[] buffer = new byte[0];
        try {
            Log.i("Assset", mapPath+timeline+year+", "+(context == null));
            InputStream stream = context.getAssets().open("sacredTexts/timeLines/"+mapPath+timeline+year+".imprm");
            int size = stream.available();
            buffer = new byte[size];
            stream.read(buffer);
            stream.close();
        } catch (Exception e) { e.printStackTrace(); }
        return new String(buffer);
    }
}
