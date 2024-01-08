package com.ddy.dyy.web.lang;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5Utils
 */
public class MD5Utils {
    /**
     * MD5加密操作
     *@param string
     */
    public static String md5(String string) {
        StringBuilder sb = new StringBuilder();
        try {
            //获取数据摘要器
            //参数：加密的方式
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            //摘要数据
            //参数：将要加密的明文转成byte数组
            byte[] digest = messageDigest.digest(string.getBytes());
            //加密操作
            for (int i = 0; i < digest.length; i++) {
                //MD5加密的核心原理：将一个byte数组通过与int类型进&运算得到一个int类型的正整数
                int result = digest[i] & 0xff;
                //可能得到的int数据会造成MD%加密的密文比较长，所以进行一个16进制的转换
                String hexString = Integer.toHexString(result);
                if (hexString.length() < 2) {
                    sb.append("0");
                }
                sb.append(hexString);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return string;
    }

    public static void test(String[] args) {
        System.out.println(md5("xxxx"));
        System.out.println(md5("xxxx"));
        System.out.println(md5("111"));
    }
}
