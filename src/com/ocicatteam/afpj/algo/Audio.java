package com.ocicatteam.afpj.algo;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.logging.Logger;

/**
 * Created by SiriusWangs on 2017/4/19.
 */
public class Audio {

    public final float[] data;
    public final float fs;
    public final int len;
    public final String format;

    private Audio(float[] data, float fs, String format) {
        super();
        this.data = data;
        this.fs = fs;
        this.format = format;
        len = data.length;

    }

    private static ByteBuffer genBuf(AudioInputStream stream, int bpf, int len) throws Exception
    {
        // TODO 待优化
        ByteBuffer buf = ByteBuffer.allocate(bpf * len);
        byte[] bytes = new byte[bpf * len];
        stream.read(bytes);
        buf.put(bytes);
        buf.rewind();
        return buf;
    }

    public static Audio read(File file) throws Exception
    {
        AudioInputStream stream = AudioSystem.getAudioInputStream(file);
        AudioFormat format = stream.getFormat();
        //fs很重要，样本和原声必须一致
        float fs = format.getSampleRate();
        int bpf = format.getFrameSize();
        int channels = format.getChannels();

        int len = (int) stream.getFrameLength();

        float[] data = new float[len];

        float tmp;

        ByteBuffer buf = genBuf(stream, bpf, len);

        if (format.isBigEndian()) {
            buf.order(ByteOrder.BIG_ENDIAN);
        } else {
            buf.order(ByteOrder.LITTLE_ENDIAN);
        }

        if (channels == 2) {
            for (int i = 0; i < len; i++) {
                tmp = buf.getShort() / 32768f;
                data[i] = (buf.getShort() / 32768f + tmp) / 2;
            }
        } else if (channels == 1) {
            for (int i = 0; i < len; i++) {
                data[i] = buf.getShort() / 32768f;
            }
        } else {
            throw new Exception("channels:" + String.valueOf(channels) + ", don't support");
        }

        return new Audio(data, fs, format.toString());
    }
}
