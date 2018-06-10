package com.ocicatteam.afpj.algo;

/**
 * Created by SiriusWangs on 2017/4/19.
 */

public class PeakFinder {

    private float minPower;// = 0.005f;
    private float[] band;// = {0f, 317f, 592f, 941f, 1385f, 1950f};
    private int C;// = 16;
    private int limit;

    public PeakFinder(int C, int peakLimit, float minPower, float[] band) {
        this.minPower = minPower;
        this.C = C;
        this.limit = peakLimit;
        this.band = band;
    }

    public void findPeaks(int I, float T, float[] F, float[] P, PeakZone pz) throws Exception
    {
        // TODO 和link一起做了
        for (int i = 1; i < F.length - 1; i++) {
            if (P[i] > minPower && P[i] > P[i - 1] && P[i] > P[i + 1]) {
                pz.put(new Peak(T, F[i], P[i], I));
            }
        }
    }

    public PeakZone find(Spectrogram s) throws Exception
    {
        PeakZone pz = new PeakZone(s.count, limit, C, band);

        for (int i = 0; i < s.count; i++) {
            findPeaks(i, s.T[i], s.F, s.P[i], pz);
        }

        return pz;
    }
}
