package com.ocicatteam.afpj.algo;

import java.util.ArrayList;

/**
 * Created by SiriusWangs on 2017/4/19.
 */
public class Peak implements Comparable<Peak> {
    public final float F;
    public final float T;
    public final float P;
    public final int I; //帧数，第几帧

    Peak(float T, float F, float P, int I) {
        this.T = T;
        this.F = F;
        this.P = P;
        this.I = I;
    }

    ArrayList list = new ArrayList();
    public int compareTo(Peak other) {
        int[] s = new int[4];
        if (other.P > P) {
            return 1;
        } else if(other.P == P) {
            return 0;
        } else {
            return -1;
        }
    }

}
