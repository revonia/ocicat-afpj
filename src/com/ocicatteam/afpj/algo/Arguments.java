package com.ocicatteam.afpj.algo;

import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by SiriusWangs on 2017/4/21.
 */
public class Arguments {
//    public static int fSize = 512;
//    public static float minPower = 0.005f;
//    public static float[] band = {0f, 317f, 592f, 941f, 1385f, 1950f};
//    public static int C = 16;
//    public static int insertPeakLimit = 3;
//    public static int searchPeakLimit = 10; //搜索的时候，peak多一点
//    public static int hitLimit = 200;
//    public static int hitMin = 100;  //至少匹配次数

    public static int fSize;
    public static float minPower;
    public static float[] band;
    public static int C;
    public static int insertPeakLimit;
    public static int searchPeakLimit;
    public static int hitLimit;
    public static int hitMin;
    private static boolean loaded = false;

    public static void load(Properties p) {
        fSize = Integer.valueOf(p.getProperty("fSize"));
        minPower = Float.valueOf(p.getProperty("minPower"));
        String[] bandStr = p.getProperty("band").split(",");
        band = new float[bandStr.length];
        for (int i = 0; i < bandStr.length; i++) {
            band[i] = Float.valueOf(bandStr[i]);
        }
        C = Integer.valueOf(p.getProperty("C"));
        insertPeakLimit = Integer.valueOf(p.getProperty("insertPeakLimit"));
        searchPeakLimit = Integer.valueOf(p.getProperty("searchPeakLimit"));
        hitLimit = Integer.valueOf(p.getProperty("hitLimit"));
        hitMin = Integer.valueOf(p.getProperty("hitMin"));
        loaded = true;
    }

    public static void loadDefault() {
        if (loaded) return;
        fSize = 512;
        minPower = 0.005f;
        band = new float[]{0f, 317f, 592f, 941f, 1385f, 1950f};
        C = 16;
        insertPeakLimit = 3;
        searchPeakLimit = 10;
        hitLimit = 200;
        hitMin = 100;
    }

    public static void print() {
        Logger log = Logger.getLogger("AFPJ.Log");
        String tpl = "参数：\n\tfSize: %d\n\tminPower: %f\n\tC: %d\n\tinsertPeakLimit: %d\n\tsearchPeakLimit: %d\n\thitLimit: %d \n\thitMin: %d\n\tband: %s";
        String bandStr = Arrays.toString(band);
        log.info(String.format(tpl, fSize, minPower, C, insertPeakLimit, searchPeakLimit, hitLimit, hitMin, bandStr));
    }
}
