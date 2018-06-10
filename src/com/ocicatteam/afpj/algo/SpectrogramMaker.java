package com.ocicatteam.afpj.algo;

import org.jtransforms.fft.FloatFFT_1D;

/**
 * Created by SiriusWangs on 2017/4/18.
 */
public class SpectrogramMaker
{

    private final int size;
    private final float fs;
    private final float[] window;
    private final float minFreq;
    private final float maxFreq;
    private final FloatFFT_1D fftTransformer;
    private float[] fft;

    public SpectrogramMaker(int size, float fs, float minFreq, float maxFreq)
    {
        window = new float[size];
        fftTransformer = new FloatFFT_1D(size);
        fft = new float[size * 2];
        this.size = size;
        this.fs = fs;
        this.maxFreq = maxFreq;
        this.minFreq = minFreq;

        //hanning window
        for(int i = 0; i < size ; i ++){
            window[i] = (float) (0.5*(1-Math.cos(2 * Math.PI * (i + 1) /(size - 1))));
        }

    }

    public Spectrogram make(final float[] signal) throws Exception
    {

        int len = signal.length;
        int stepSize = size / 2;
        int count = len / stepSize - 1;
        int i, j, k;

        float[] T = new float[len / stepSize - 1];
        for(i = 0; i < count; i++) {
            T[i] = stepSize * i / fs;
        }

        int startFreqI = 0, endFreqI = stepSize - 1;
        float freqTmp;
        float[] Ftmp = new float[stepSize];

        for(i = 0; i < stepSize; i++) {
            freqTmp = fs * i / stepSize;
            if (freqTmp < minFreq) {
                startFreqI = i + 1;
                continue;
            }
            Ftmp[i] = freqTmp;

            if (freqTmp > maxFreq) {
                endFreqI = i - 1;
                break;
            }
        }

        int freqCount = endFreqI - startFreqI + 1;

        float[] F = new float[freqCount];

        System.arraycopy(Ftmp, startFreqI, F, 0, freqCount);

        float[][] P = new float[count][freqCount];


        for (i = 0; i < count; i++) {
            // TODO 考虑将两个for循环中的事情一次性都做了
            for (k = 0; k < size; k++) {
                fft[k * 2] = signal[i * stepSize + k] * window[k];
                fft[k * 2 + 1] = 0;
            }

            fftTransformer.complexForward(fft);

            //TODO 待优化
            for (k = 0; k < freqCount; k++) {
                P[i][k] = (float) Math.hypot(fft[(startFreqI + k) * 2], fft[(startFreqI + k) * 2 + 1]);
            }
        }

        return new Spectrogram(T, F, P, size, len, count, stepSize, fs);
    }
}
