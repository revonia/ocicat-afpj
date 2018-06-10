package com.ocicatteam.afpj;

import com.ocicatteam.afpj.algo.Arguments;
import com.ocicatteam.afpj.algo.Audio;
import com.ocicatteam.afpj.algo.FingerprintMaker;
import com.ocicatteam.afpj.db.Db;

import java.io.File;
import java.util.logging.Logger;

/**
 * Created by SiriusWangs on 2017/4/20.
 */
public class Insert {

    public static void main(String[] args)
    {
        String dbPath = args[0];
        File file = new File(args[1]);
        Logger log = Logger.getLogger("AFPJ.Log");
        Arguments.loadDefault();
        Arguments.print();
        String[] list;
        try {
            final Db db = new Db(dbPath);
            log.info("使用数据库: " + dbPath);
            if (file.isDirectory()) {
                log.info("使用原声文件夹: " + args[1]);
                list = file.list();
                if (list == null) return;
                for (int i = 0; i < list.length; i++) {
                    list[i] = args[1] + list[i];
                }
            } else {
                list = new String[1];
                list[0] = args[1];
            }

            for (String f : list) {
                File songFile = new File(f);
                log.info("------------开始处理音频:\"" + f + "\"");
                if (db.findIdBySongName(songFile.getName()) != -1) {
                    log.info("跳过音频:\"" + f + "\" 已经存在于数据库中");
                } else {
                    run(db, songFile);
                    log.info("音频:\"" + f + "\" 指纹写入数据库成功");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void run(Db db, File audioFile) throws Exception
    {
        Audio a = Audio.read(audioFile);
        Logger log = Logger.getLogger("AFPJ.Log");
        log.info("音频格式：" + a.format);
        String name = audioFile.getName();
        FingerprintMaker fm = new FingerprintMaker(
                Arguments.fSize,
                Arguments.C,
                Arguments.insertPeakLimit,
                Arguments.minPower,
                Arguments.band
        );
        int id = db.insertSong(name);

        db.prepareInsertFp(id);

        fm.make(a, (int hash, int time) -> {
            try {
                db.insertFp(hash, time);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        db.saveFp();
    }
}
