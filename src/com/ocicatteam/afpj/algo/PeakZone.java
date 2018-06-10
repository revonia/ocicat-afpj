package com.ocicatteam.afpj.algo;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Created by SiriusWangs on 2017/4/19.
 */
public class PeakZone {

    public final int width;
    public final int height;
    public final float[] band;
    public MaxNPeak[][] list;
    public final int C;
    public final int limit;

    public PeakZone(int count,int limit, int C, float[] band) {
        width = count / C + 1;
        height = band.length - 1;
        list = new MaxNPeak[width][height];

        this.band = band;
        this.C = C;
        this.limit = limit;
    }

    public Peak[] get(int x, int y) {
        return list[x][y] == null ? null : list[x][y].get();
    }

    public void put(Peak p) throws Exception{
        int x = p.I / C;
        int y = judge(p.F);
        if (list[x][y] == null) list[x][y] = new MaxNPeak(limit);
        list[x][y].put(p);
    }

//    public void trim(int limit) {
//        if (limit <= 0) return;
//        int size;
//        for (int x = 0; x < width; x++) {
//            for (int y = 0; y < height; y++) {
//                if (list[x][y] == null) continue;
//                list[x][y].l.sort((a, b) -> -Float.compare(a.P, b.P));
//                size = list[x][y].l.size();
//                if (size > limit) {
//                    size = limit;
//                }
//                list[x][y].l = new ArrayList<>(list[x][y].l.subList(0, size));
//            }
//        }
//    }

    private int judge(float F) {
        int i;
        for (i = 1; i < band.length; i++) {
            if (F > band[i - 1] && F < band[i]) {
                return i - 1;
            }
        }
        return i - 1;
    }
}

class MaxNPeak {
    PriorityQueue<Peak> pq;
    public final int limit;
    private boolean ready = false;
    private Peak[] peaks;

    // TODO 最大堆的问题
    public MaxNPeak(int limit) {
        this.limit = limit;
        Comparator<Peak> comparator = (p1, p2) -> {
            if (p2.P > p1.P) {
                return -1;
            } else if(p2.P == p1.P) {
                return 0;
            } else {
                return 1;
            }
        };

        this.pq = new PriorityQueue<>(limit + 1, comparator);
    }

    public void put(Peak peak) throws Exception {
        if (this.ready) {
            throw new Exception("Peak is ready for this zone");
        }
        pq.add(peak);
        if (limit > 0 && pq.size() > limit) {
            pq.poll();
        }
    }

    public Peak[] get() {
        if (!ready) {
            peaks = pq.toArray(new Peak[pq.size()]);
            this.pq = null;
            //Arrays.sort(peaks);
            ready = true;
        }
        //peaks.clone();
        //Peak[] peaks = pq.toArray(new Peak[pq.size()]);
        return peaks;
    }
}