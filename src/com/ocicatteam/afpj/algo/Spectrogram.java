package com.ocicatteam.afpj.algo;

/**
 * Created by SiriusWangs on 2017/4/19.
 */
public class Spectrogram {
    public final float fs;
    public final int size;
    public final int stepSize;
    public final float[] F;
    public final float[] T;
    public final int len;
    public final int count;
    public final float[][] P;

    Spectrogram(float[] T, float[] F, float[][] P, int size, int len, int count, int stepSize, float fs) {
        super();
        this.T = T;
        this.F = F;
        this.P = P;
        this.size = size;
        this.stepSize = stepSize;
        this.len = len;
        this.count = count;
        this.fs = fs;
    }
}
