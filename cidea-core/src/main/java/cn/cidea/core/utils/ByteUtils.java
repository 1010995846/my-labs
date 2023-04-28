package cn.cidea.core.utils;

import java.nio.charset.StandardCharsets;

/**
 * @author Charlotte
 */
public class ByteUtils {

    public static byte[] toBytes(long str) {
        byte[] byteNum = new byte[8];
        for (int ix = 0; ix < 8; ++ix) {
            int offset = 64 - (ix + 1) * 8;
            byteNum[ix] = (byte) ((str >> offset) & 0xff);
        }
        return byteNum;
    }

    public static byte[] toBytes(String str) {
        return str.getBytes(StandardCharsets.UTF_8);
    }

    public static long toLong(byte[] bytes) {
        long num = 0;
        for (int ix = 0; ix < 8; ++ix) {
            num <<= 8;
            num |= (bytes[ix] & 0xff);
        }
        return num;
    }

    public static String toString(byte[] bytes) {
        return new String(bytes);
    }

}
