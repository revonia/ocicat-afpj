package com.ocicatteam.afpj.algo;

/**
 * Created by SiriusWangs on 2017/4/19.
 */
public class FingerprintMaker {

    private int fSize;
    private float timeDiv;
    private float freqDiv;
    private float minPower;
    private float[] band;
    private int C;
    private int peakLimit;

    public FingerprintMaker(int fSize, int C, int peakLimit, float minPower, float[] band) {
        this.fSize = fSize;
        this.C = C;
        this.peakLimit = peakLimit;
        this.minPower = minPower;
        this.band = band;
    }

    public void make(Audio a, DoWithFp dwf) throws Exception {
        timeDiv = fSize / 2 / a.fs;
        freqDiv = 2 * a.fs / fSize;

        SpectrogramMaker sm = new SpectrogramMaker(fSize, a.fs, band[0], band[band.length - 1]);
        PeakFinder pf = new PeakFinder(C, peakLimit, minPower, band);

        Spectrogram s = sm.make(a.data);
        PeakZone pz = pf.find(s);

        link(pz, dwf);
    }

    private void link(PeakZone pz, DoWithFp dwf) {
        int x, y, z, k;
        Peak a, t;
        Peak[] pre, cur;
        //TODO 待优化
        //同频带的前后两个区域进行连线
        for (x = 1; x < pz.width; x++) {
            for (y = 0; y < pz.height; y++) {
                pre = pz.get(x - 1, y); if (pre == null) continue;
                cur = pz.get(x, y); if (cur == null) continue;
                //点两两相连
                for (z = 0; z < pre.length; z++) {
                    for (k = 0; k < cur.length; k++) {
                        a = pre[z];
                        t = cur[k];
                        dwf.doWithHash(hash(a.F, t.F, a.T, t.T, freqDiv, timeDiv), timeConv(a.T, timeDiv));
                    }
                }
            }
        }
    }

    //TODO: 寻找更可靠的算法
    //t2一定要比t1大
    public static int hash(float f1, float f2, float t1, float t2, float freqDiv, float timeDiv) {
        //神秘的hash
        return (Math.round(f1 / freqDiv) * 200 + Math.round(f2 / freqDiv)) * 200
                + Math.round(t2 / timeDiv - t1 / timeDiv);
    }

    private static int timeConv(float time, float timeDiv) {
        return Math.round(time / timeDiv);
    }

}



