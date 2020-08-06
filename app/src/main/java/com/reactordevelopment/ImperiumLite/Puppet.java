package com.reactordevelopment.ImperiumLite;

import android.content.Context;
import android.util.Log;

public class Puppet extends Player {
    public Puppet(Context cont, int ident, boolean imperium, String tag) {
        super(cont, ident, imperium, tag);
        human = true;
        puppet = true;
    }

    private void testPress(final Province province, final String tag) {
        try { province.doClick(); } catch (NullPointerException e) {
            Log.i("AiProvince", tag);
            e.printStackTrace();
        }
    }

    public void pressChange() { changer(); }
    public void pressRevChange() { changerRev(); }
    public void pressAgain() { again(); }
    public void pressRetreat() { retreat(); }
    public void pressAnnihilate() { annihilate(); }
    public void pressDeveloper(Province prov) { developer(prov); }
    public void pressFortifier(Province prov) { fortifier(prov); }
    public void pressWar(String tag){ declareWar(tag); }
    public void pressAlly(String tag){ makeAlly(tag); }
    public void pressSubject(String tag){ makeSubject(tag); }
    public void pressProvince(String id){ getGame().getMap().provFromId(id).doClick(); }
}
