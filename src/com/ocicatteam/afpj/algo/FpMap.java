package com.ocicatteam.afpj.algo;

import java.util.HashMap;

/**
 * Created by SiriusWangs on 2017/4/19.
 */
public class FpMap {
    public HashMap<Integer, TimeIdStack> map;

    public FpMap() { map = new HashMap<>(); }

    public void put(int hash, int time, int id) {
        TimeIdStack s = map.computeIfAbsent(hash, ns -> new TimeIdStack());
        s.s.push(new TimeIdPair(time, id));
    }

    public TimeIdPair[] get(int hash) {
        TimeIdStack s = map.get(hash);
        if (s == null) {
            return null;
        }
        return s.s.toArray(new TimeIdPair[s.s.size()]);
    }
}



