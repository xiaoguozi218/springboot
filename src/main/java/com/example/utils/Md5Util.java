package com.example.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by peichunting on 18/1/29.
 */
public class Md5Util
{
    /**
     * 对文件全文生成MD5摘要
     *
     * @param file
     *            要加密的文件
     * @return MD5摘要码
     */
    static char hexdigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8',

            '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    /**
     * @Author shGuo
     * @Date 2016年8月18日下午8:57:41
     * @param inputStream
     * @return
     * @return String
     * @Desc   对文件进行MD5加密
     */
    public static String getMD5(InputStream inputStream) {
        try {
            StringBuffer sb = new StringBuffer();
            MessageDigest digest = MessageDigest.getInstance("md5");
//	            FileInputStream fin = new FileInputStream(file);
            int len = -1;
            byte[] buffer = new byte[1024];//设置输入流的缓存大小 字节
            //将整个文件全部读入到加密器中
            while ((len = inputStream.read(buffer)) != -1) {
                digest.update(buffer, 0, len);
            }
            //对读入的数据进行加密
            byte[] bytes = digest.digest();
            for (byte b : bytes) {
                // 数byte 类型转换为无符号的整数
                int n = b & 0XFF;
                // 将整数转换为16进制
                String s = Integer.toHexString(n);
                // 如果16进制字符串是一位，那么前面补0
                if (s.length() == 1) {
                    sb.append("0" + s);
                } else {
                    sb.append(s);
                }
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 把byte[]数组转换成十六进制字符串表示形式
     * @param tmp    要转换的byte[]
     * @return 十六进制字符串表示形式
     */
    private static String byteToHexString(byte[] tmp) {
        String s;
        // 用字节表示就是 16 个字节
        char str[] = new char[16 * 2]; // 每个字节用 16 进制表示的话，使用两个字符，
        // 所以表示成 16 进制需要 32 个字符

        int k = 0; // 表示转换结果中对应的字符位置

        for (int i = 0; i < 16; i++) { // 从第一个字节开始，对 MD5 的每一个字节

            // 转换成 16 进制字符的转换

            byte byte0 = tmp[i]; // 取第 i 个字节

            str[k++] = hexdigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换,

            // >>> 为逻辑右移，将符号位一起右移

            str[k++] = hexdigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换

        }

        s = new String(str); // 换后的结果转换为字符串

        return s;

    }


    /**
     *
     * @param plain
     *            明文
     * @return 32位小写密文
     */
    public static String encryption(String plain) {
        if (StringUtils.isBlank(plain)) {
            return null;
        }
        String re_md5 = new String();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plain.getBytes());
            byte b[] = md.digest();

            int i;

            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i));
            }

            re_md5 = buf.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return re_md5;
    }
}
