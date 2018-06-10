package com.ocicatteam.afpj;

import com.ocicatteam.afpj.algo.*;
import com.ocicatteam.afpj.view.SpectrumView;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.logging.Logger;

public class View {

    public static void main(String[] args)
    {
        File file = new File(args[0]);
        Arguments.loadDefault();
        try {
            run(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void run(File audioFile) throws Exception
    {
        Audio a = Audio.read(audioFile);
        Logger log = Logger.getLogger("AFPJ.Log");
        log.info("音频格式：" + a.format);
        Arguments.print();

        SpectrogramMaker sm = new SpectrogramMaker(Arguments.fSize,
                a.fs,
                Arguments.band[0],
                Arguments.band[Arguments.band.length - 1]
        );

        PeakFinder pf = new PeakFinder(
                Arguments.C,
                Arguments.searchPeakLimit,
                Arguments.minPower,
                Arguments.band
        );

        Spectrogram s = sm.make(a.data);
        PeakZone pz = pf.find(s);
        float[][] p = s.P;
        SpectrumView audioWindow = new SpectrumView(p, pz);
        audioWindow.setVisible(true);

        audioWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
