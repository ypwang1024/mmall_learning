package com.mmall.util;

import org.springframework.util.StringUtils;

import java.security.MessageDigest;

/**
 * @program: mmall
 * @description: MD5加密
 * @author: ypwang
 * @create: 2018-04-13 22:14
 **/
public class MD5Util {
    private static String byteArrayToHexString(byte b[]) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i]));
        }

        return resultSb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n += 256;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    /**
     * 返回大写MD5
     *
     * @param origin      要加密的字符串
     * @param charsetname 采用的字符集，为空时采用
     * @return
     */
    private static String MD5Encode(String origin, String charsetname) {
        String resultString = null;
        try {
            resultString = new String(origin);
            MessageDigest md = MessageDigest.getInstance("MD5");
            if (charsetname == null || "".equals(charsetname))
                resultString = byteArrayToHexString(md.digest(resultString.getBytes()));

            else
                resultString = byteArrayToHexString(md.digest(resultString.getBytes(charsetname)));

        } catch (Exception exception) {
        }
        return resultString != null ? resultString.toUpperCase() : null;
    }

    /**
     * 直接返回UTF8形式的加密串
     *
     * @param origin
     * @return
     */
    public static String MD5EncodeUtf8(String origin) {
        // 增加盐值，提高密码复杂度。（也就是加一段随机字符串）
        // origin = origin + PropertiesUtil.getProperty("password.salt", "");
        return MD5Encode(origin, "utf-8");
    }

    private static final String hexDigits[] = {"0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

}
