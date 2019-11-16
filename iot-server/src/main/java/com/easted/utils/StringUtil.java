package com.easted.utils;

import java.util.Arrays;

/**
 * 字符串处理工具
 */
public class StringUtil {

    /**
     * 16进制字符串转字节
     * @param hexString
     * @return
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.trim().replaceAll("\\s*", ""); // 去除字符串中的空格

        //String hexFormat = "0123456789ABCDEF";

        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            try {
                d[i] = (byte) (0xff & Integer.parseInt(hexString.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return d;
    }

    /**
     * 字节转16进制字符串
     * @param src
     * @return
     */
    public static String bytes2HexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString().toUpperCase();
    }

    public static String bytes2HexString(byte[] src, String delimiter) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        if (delimiter == null) {
            delimiter = " ";
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv).append(delimiter);
        }
        return stringBuilder.toString().toUpperCase();
    }

    public static void main(String args[]) {
        /*String str16 = bytes2HexString("0011".getBytes());
        System.out.println(str16);
        String str = new String(hexStringToBytes(str16), Charset.forName("GBK"));
        System.out.println(str);*/
        byte[] bytes = "~".getBytes();
        byte[] bytes1 = "~".getBytes();
        System.out.println(Arrays.equals(bytes, bytes1));
        int c = 0x8003;
        System.out.println(c);
    }
}
