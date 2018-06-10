package com.ocicatteam.afpj;

import com.ocicatteam.afpj.algo.Arguments;

import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.*;

/**
 * Created by SiriusWangs on 2017/4/21.
 */
public class Main {

    private static String help = "-p xxx.properties i 使用配置文件进行插入操作\n" +
            "-p xxx.properties s 使用配置文件进行搜索操作\n" +
            "-p xxx.properties v xxx.wav 使用配置文件查看xxx.wav的峰值图\n" +
            "如果 -p xxx.properties 省略，直接 i s v，则默认读取 afpj.properties 作为配置文件\n" +
            "-p xxx.properties 只能放在最前面\n";

    public static void main(String[] args)
    {

        Logger log = Logger.getLogger("AFPJ.Log");
        log.setLevel(Level.ALL);

        try {
            run(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void run(String[] args) throws Exception {

        if (args.length == 0) {
            System.out.print(help);
            return;
        }

        Properties properties = new Properties();
        String pFile;
        String action;
        if (Objects.equals(args[0], "-p")) {
            pFile = args[1];
            action = args[2];
        } else {
            pFile = "./afpj.properties";
            action = args[0];
        }

        Logger log = Logger.getLogger("AFPJ.Log");



        properties.load(new FileInputStream(pFile));
        Arguments.load(properties);
        log.info("使用配置文件:" + pFile);

        if (Boolean.valueOf(properties.getProperty("writeLogToDir"))) {

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss");
            String logName = properties.getProperty("logDir") + df.format(new Date());
            if (Objects.equals(action, "i")) {
                logName += " 插入";
            } else if (Objects.equals(action, "s")) {
                logName += " 搜索";
            } else if (Objects.equals(action, "v")) {
                logName += " 查看";
            }

            logName += ".log";

            FileHandler fileHandler = new FileHandler(logName);
            fileHandler.setLevel(Level.ALL);
            fileHandler.setFormatter(new LogFormatter());
            log.addHandler(fileHandler);
        }

        String[] paths = new String[2];
        paths[0] = properties.getProperty("dbFile");
        switch (action) {
            case "i":
                log.info("执行插入");
                paths[1] = properties.getProperty("originSongsDir");
                Insert.main(paths);
                break;
            case "s":
                log.info("执行搜索");
                paths[1] = properties.getProperty("sampleSongsDir");
                Search.main(paths);
                break;
            case "v":
                log.info("执行查看");
                if (Objects.equals(args[0], "-p")) {
                    paths[0] = args[3];
                } else {
                    paths[0] = args[1];
                }
                View.main(paths);
                break;
            default:
                System.out.print(help);
        }

    }

}
