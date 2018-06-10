package com.ocicatteam.afpj.view;

import com.ocicatteam.afpj.algo.Peak;
import com.ocicatteam.afpj.algo.PeakZone;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * Created by SiriusWangs on 2017/4/20.
 */

public class SpectrumView extends javax.swing.JFrame {

    float[][] results = null;
    PeakZone pz;


    /**
     * Creates a new instance of Java2DFrame
     */
    public SpectrumView(float[][] results, PeakZone pz) {
        this.results = results;
        this.pz = pz;
        this.setLayout(new FlowLayout());

    }

    /**
     * This is the method where the String is drawn.
     *
     * @param g
     *            The graphics object
     */
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        int blockSizeX = 2;
        int blockSizeY = 3;

        this.setSize(results.length * blockSizeX, results[0].length * blockSizeY);

        final SpectrumView that = this;

        int x, y, z, i, j;
        for (x = 0; x < results.length; x++) {
            for (y = 0; y < results[0].length; y++) {
                int magnitude = (int) (800 * Math.log10(results[x][y]));
                if (magnitude > 255) magnitude = 255;
                if (magnitude < 0) magnitude = 0;

                g2d.setColor(new Color(0, 0, magnitude));

                g2d.fillRect(x * blockSizeX , y * blockSizeY, blockSizeX, blockSizeY);
            }
        }

        float timeDiv = 0.032f;
        float freqDiv = 2 * 8000 / 512;

        Peak[] peaks;
        Peak a;

        for (i = 0; i < pz.width; i++) {
            for (j = 0; j < pz.height; j++) {
                peaks = pz.get(i, j);
                if (peaks == null) continue;
                for (z = 0; z < peaks.length; z++) {
                    a = peaks[z];
                    x = Math.round(a.T / timeDiv);
                    y = Math.round(a.F / freqDiv);
                    g2d.setColor(new Color(255, 0, 0));
                    g2d.fillRect(x * blockSizeX ,y * blockSizeY, blockSizeX, blockSizeY);
                }
            }
        }
    }

}