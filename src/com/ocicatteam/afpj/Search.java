package com.ocicatteam.afpj;

import com.ocicatteam.afpj.algo.*;
import com.ocicatteam.afpj.db.Db;

import java.io.File;
import java.util.logging.Logger;

public class Search {

    public static void main(String[] args)
    {
        String dbPath = args[0];
        File file = new File(args[1]);
        Logger log = Logger.getLogger("AFPJ.Log");
        Arguments.loadDefault();
        Arguments.print();
        try {
            final Db db = new Db(dbPath);
            log.info("使用数据库: " + dbPath);
            int fileCount;
            Counter c = new Counter();
            if (file.isDirectory()) {
                log.info("使用样本文件夹: " + args[1]);
                String[] list = file.list();
                fileCount = list.length;
                if (list == null) return;
                log.info("找到音频个数: " + fileCount);
                if (list == null) return;
                for (String f : list) {
                    File songFile = new File(args[1] + f);
                    log.info("------------开始匹配音频: \"" + f + "\"");
                    if (run(db, songFile)) {
                        c.add();
                    }
                }
            } else {
                fileCount = 1;
                log.info("找到音频个数: 1");
                log.info("开始匹配音频: \"" + file.getName() + "\"");
                if (run(db, file)) {
                    c.add();
                }
            }

            log.info("匹配结果：" + c.get() + "/" + fileCount);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static boolean run(Db db, File audioFile) throws Exception
    {
        Logger log = Logger.getLogger("AFPJ.Log");
        Audio a = Audio.read(audioFile);
        log.info("音频格式：" + a.format);
        FingerprintMaker fm = new FingerprintMaker(
                Arguments.fSize,
                Arguments.C,
                Arguments.searchPeakLimit,
                Arguments.minPower,
                Arguments.band
        );

        Matcher m = new Matcher(Arguments.hitLimit);
        FpMap testFp = new FpMap();

        db.prepareFind();

        fm.make(a, (int hash, int time) -> {
            testFp.put(hash, time, -1);  //-1代表匹配样本
            db.pushFind(hash);
        });

        FpMap foundFp = db.find();

        int[] result = m.match(foundFp, testFp);

        if (result[0] != -1) {
            if (result[2] > Arguments.hitMin) {
                String name = db.findSongNameById(result[0]);
                if (name != null) {
                    log.info("结果：\n\t音频匹配id: " + result[0] + "\n\t匹配文件: " + name + "\n\t时间差dTime: " + result[1] + "\n\t命中次数hit: " + result[2] );
                    return true;
                }
            }
        }

        log.info("音频匹配失败");
        return false;
    }
}
