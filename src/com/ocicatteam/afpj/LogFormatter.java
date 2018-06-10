package com.ocicatteam.afpj;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * Created by SiriusWangs on 2017/4/22.
 */
public class LogFormatter extends Formatter {
    @Override
    public String format(LogRecord record) {
        SimpleDateFormat df = new SimpleDateFormat("===yyyy-MM-dd HH:mm:ss===");
        String out = df.format(new Date(record.getMillis()));
        out = out.replaceAll("\n", "\r\n");
        out += "\r\n" + record.getMessage() + "\r\n\r\n";
        return out;
    }
}
