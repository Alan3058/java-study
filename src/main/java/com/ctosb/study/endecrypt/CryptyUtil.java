package com.ctosb.study.endecrypt;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CryptyUtil {

    public static String UTF8 = "UTF-8";
    public static String MD5 = "MD5";
    public static String SHA = "SHA";

    /**
     * BASE64解密
     *
     * @param source
     * @return
     * @throws IOException
     * @author Alan
     * @createTime 2015-11-28 上午10:19:53
     */
    public static String decryptBASE64(String source) throws IOException {
        return new String(new BASE64Decoder().decodeBuffer(source), UTF8);
    }

    /**
     * BASE64加密
     *
     * @param source
     * @return
     * @throws UnsupportedEncodingException
     * @author Alan
     * @createTime 2015-11-28 上午10:21:34
     */
    public static String encryptBASE64(String source) throws UnsupportedEncodingException {
        return new BASE64Encoder().encodeBuffer(source.getBytes(UTF8));
    }

    /**
     * MD5加密
     *
     * @param source
     * @return
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     * @author Alan
     * @createTime 2015-11-28 上午10:24:35
     */
    public static String encryptMD5(String source) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md5 = MessageDigest.getInstance(MD5);
        byte[] byteArray = md5.digest(source.getBytes(UTF8));

        StringBuffer stringBuffer = new StringBuffer();
        // 将加密后的byte数组转换为十六进制的字符串,否则的话生成的字符串会乱码
        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
                stringBuffer.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
            } else {
                stringBuffer.append(Integer.toHexString(0xFF & byteArray[i]));
            }
        }
        return stringBuffer.toString();
    }

    /**
     * SHA加密
     *
     * @param source
     * @return
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     * @author Alan
     * @createTime 2015-11-28 上午10:24:35
     */
    public static String encryptSHA(String source) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md5 = MessageDigest.getInstance(SHA);
        byte[] byteArray = md5.digest(source.getBytes(UTF8));

        StringBuffer stringBuffer = new StringBuffer();
        // 将加密后的byte数组转换为十六进制的字符串,否则的话生成的字符串会乱码
        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
                stringBuffer.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
            } else {
                stringBuffer.append(Integer.toHexString(0xFF & byteArray[i]));
            }
        }
        return stringBuffer.toString();
    }


    public static void main(String[] args) throws Exception {
        System.out.println(CryptyUtil.encryptBASE64("dddd"));
        System.out.println(CryptyUtil.decryptBASE64("ZGRkZA=="));
        System.out.println(CryptyUtil.encryptMD5("我是李连钢"));
        System.out.println(CryptyUtil.encryptSHA("我是李连钢122~！@@&……#*#（（#（#（"));
    }
}
