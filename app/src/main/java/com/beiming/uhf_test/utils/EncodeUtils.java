//package com.sgcc.ma.mpp.network.utils;
//
//
//import android.util.Log;
//
//import org.bouncycastle.util.encoders.Base64;
//
//import java.io.UnsupportedEncodingException;
//import java.security.InvalidAlgorithmParameterException;
//import java.security.InvalidKeyException;
//import java.security.Key;
//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
//import java.security.spec.InvalidKeySpecException;
//
//import javax.crypto.BadPaddingException;
//import javax.crypto.Cipher;
//import javax.crypto.IllegalBlockSizeException;
//import javax.crypto.NoSuchPaddingException;
//import javax.crypto.SecretKeyFactory;
//import javax.crypto.spec.DESedeKeySpec;
//import javax.crypto.spec.IvParameterSpec;
//
///**
// * Created by dell on 2016/10/9.
// */
//public class EncodeUtils {
//    //    // 定义加密算法，DESede即3DES
////    private static final String Algorithm = "DESede";
////    // 加密密钥
////    private static final String PCRYPTKEY = "cattle_2016";
//    private static final int vk = 24;
////
////    /**
////     * 解密函数
////     *
////     * @param src 密文的字节数组
////     * @return
////     */
////    public static String decryptMode(String src) {
////        try {
////            byte[] bytes = decode(src);
////            SecretKey deskey = new SecretKeySpec(ts(PCRYPTKEY), Algorithm);
////            Cipher c1 = Cipher.getInstance(Algorithm);
////            c1.init(Cipher.DECRYPT_MODE, deskey);
////            return new String(c1.doFinal(bytes));
////        } catch (NoSuchAlgorithmException e1) {
////            e1.printStackTrace();
////        } catch (NoSuchPaddingException e2) {
////            e2.printStackTrace();
////        } catch (Exception e3) {
////            e3.printStackTrace();
////        }
////        return null;
////    }
//
//    /**
//     * 根据字符串生成密钥24位的字节数组
//     *
//     * @param keyStr
//     * @return
//     * @throws UnsupportedEncodingException
//     */
//    public static byte[] ts(String keyStr) throws UnsupportedEncodingException {
//        byte[] key = new byte[vk];
//        byte[] temp = keyStr.getBytes("UTF-8");
//        if (key.length > temp.length) {
//            System.arraycopy(temp, 0, key, 0, temp.length);
//        } else {
//            System.arraycopy(temp, 0, key, 0, key.length);
//        }
//        return key;
//    }
//
//    /**
//     * Converts the specified Hex-encoded String into a raw byte array.  This is a
//     * convenience method that merely delegates to {@link #decode(char[])} using the
//     * argument's hex.toCharArray() value.
//     *
//     * @param hex a Hex-encoded String.
//     * @return A byte array containing binary data decoded from the supplied String's char array.
//     */
//    public static byte[] decode(String hex) {
//        return decode(hex.toCharArray());
//    }
//
//    /**
//     * Converts an array of characters representing hexidecimal values into an
//     * array of bytes of those same values. The returned array will be half the
//     * length of the passed array, as it takes two characters to represent any
//     * given byte. An exception is thrown if the passed char array has an odd
//     * number of elements.
//     *
//     * @param data An array of characters containing hexidecimal digits
//     * @return A byte array containing binary data decoded from
//     * the supplied char array.
//     * @throws IllegalArgumentException if an odd number or illegal of characters
//     *                                  is supplied
//     */
//    public static byte[] decode(char[] data) throws IllegalArgumentException {
//        int len = data.length;
//        if ((len & 0x01) != 0) {
//            throw new IllegalArgumentException("Odd number of characters.");
//        }
//        byte[] out = new byte[len >> 1];
//        // two characters form the hex value.
//        for (int i = 0, j = 0; j < len; i++) {
//            int f = toDigit(data[j], j) << 4;
//            j++;
//            f = f | toDigit(data[j], j);
//            j++;
//            out[i] = (byte) (f & 0xFF);
//        }
//        return out;
//    }
//
//    protected static int toDigit(char ch, int index) throws IllegalArgumentException {
//        int digit = Character.digit(ch, 16);
//        if (digit == -1) {
//            throw new IllegalArgumentException("Illegal hexadecimal charcter " + ch + " at index " + index);
//        }
//        return digit;
//    }
//
//
//    // 密钥 ?度不得?于24
//    private final static String secretKey = "cattle123456789012345678";
//    // 向量 可有可? 终端后台也要约定
//    private final static String iv = "cattle11";
//    // 加解密统?使?的编码?式
//    private final static String encoding = "utf-8";
//
//    /**
//     * 3DES加密
//     *
//     * @param plainText 普通?本
//     * @return
//     * @throws Exception
//     */
//    public static String encryptMode(String plainText) {
//
//        try {
//            Key deskey = null;
//            DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes());
//            SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
//            deskey = keyfactory.generateSecret(spec);
//
//            Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
//            IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
//            cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
//            byte[] encryptData = cipher.doFinal(plainText.getBytes(encoding));
//            return new String(Base64.encode(encryptData));
//        } catch (Exception e) {
//            Log.e("des3Encode", "加密失败");
//        }
//        return "";
//    }
//
//    /**
//     * 3DES解密
//     *
//     * @param encryptText 加密
//     * @return
//     * @throws Exception
//     */
//    public static String decryptMode(String encryptText) {
//        try {
//            DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes());
//            SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
//            Key deskey = keyfactory.generateSecret(spec);
//            Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
//            IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
//            cipher.init(Cipher.DECRYPT_MODE, deskey, ips);
//            byte[] decryptData = cipher.doFinal(Base64.decode(encryptText));
//            return new String(decryptData, encoding);
//        } catch (InvalidKeyException e) {
//            e.printStackTrace();
//        } catch (NoSuchPaddingException e) {
//            e.printStackTrace();
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (IllegalBlockSizeException e) {
//            e.printStackTrace();
//        } catch (BadPaddingException e) {
//            e.printStackTrace();
//        } catch (InvalidAlgorithmParameterException e) {
//            e.printStackTrace();
//        } catch (InvalidKeySpecException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public static String getMD5String(String str) {
//        try {
//            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
//            byte[] encodeDisgest = mdTemp.digest(str.getBytes("UTF-8"));
//            return hexString(encodeDisgest);
//        } catch (Exception var9) {
//            return null;
//        }
//    }
//
//    private static String hexString(byte[] bytes) {
//        String hexValue = "";
//        for (int i = 0; i < bytes.length; i++) {
//            int val = bytes[i] & 0xff;
//            String temStr = Integer.toHexString(val);
//            if (temStr.length() < 2) hexValue += "0" + temStr;
//            else hexValue += temStr;
//        }
//        return hexValue;
//    }
//
//
//}


package com.beiming.uhf_test.utils;

import java.io.UnsupportedEncodingException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by mikebian on 16/9/17.
 */


public class EncodeUtils {

    // 定义加密算法，DESede即3DES
    private static final String Algorithm = "DESede";
    // 加密密钥
    private static final String P_CRYPT_KEY = "hrcw520";

    /**
     * 加密方法
     *
     * @param string 源数据的字节数组
     * @return
     */
    public static String encryptMode(String string) {
        try {
            byte[] src = string.getBytes();
            // 生成密钥
            SecretKey deskey = new SecretKeySpec(
                    build3DesKey(P_CRYPT_KEY), Algorithm);
            // 实例化Cipher
            Cipher cipher = Cipher.getInstance(Algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, deskey);
            return encodeToString(cipher.doFinal(src));
        } catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }

    /**
     * 解密函数
     *
     * @param src 密文的字节数组
     * @return
     */
    public static String decryptMode(String src) {
        try {
            byte[] bytes = decode(src);
            SecretKey deskey = new SecretKeySpec(
                    build3DesKey(P_CRYPT_KEY), Algorithm);
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.DECRYPT_MODE, deskey);
            return new String(c1.doFinal(bytes));
        } catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }

    /**
     * 根据字符串生成密钥24位的字节数组
     *
     * @param keyStr
     * @return
     * @throws UnsupportedEncodingException
     */
    public static byte[] build3DesKey(String keyStr)
            throws UnsupportedEncodingException {
        byte[] key = new byte[24];
        byte[] temp = keyStr.getBytes("UTF-8");

        if (key.length > temp.length) {
            System.arraycopy(temp, 0, key, 0, temp.length);
        } else {
            System.arraycopy(temp, 0, key, 0, key.length);
        }
        return key;
    }

    private static final char[] DIGITS = {
            '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
    };

    public static String encodeToString(byte[] bytes) {
        char[] encodedChars = encode(bytes);
        return new String(encodedChars).toUpperCase();
    }

    public static char[] encode(byte[] data) {
        int l = data.length;
        char[] out = new char[l << 1];
        // two characters form the hex value.
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = DIGITS[(0xF0 & data[i]) >>> 4];
            out[j++] = DIGITS[0x0F & data[i]];
        }
        return out;
    }


    /**
     * Converts the specified Hex-encoded String into a raw byte array.  This is a
     * convenience method that merely delegates to {@link #decode(char[])} using the
     * argument's hex.toCharArray() value.
     *
     * @param hex a Hex-encoded String.
     * @return A byte array containing binary data decoded from the supplied String's char array.
     */
    public static byte[] decode(String hex) {
        return decode(hex.toCharArray());
    }

    /**
     * Converts an array of characters representing hexidecimal values into an
     * array of bytes of those same values. The returned array will be half the
     * length of the passed array, as it takes two characters to represent any
     * given byte. An exception is thrown if the passed char array has an odd
     * number of elements.
     *
     * @param data An array of characters containing hexidecimal digits
     * @return A byte array containing binary data decoded from
     * the supplied char array.
     * @throws IllegalArgumentException if an odd number or illegal of characters
     *                                  is supplied
     */
    public static byte[] decode(char[] data) throws IllegalArgumentException {
        int len = data.length;
        if ((len & 0x01) != 0) {
            throw new IllegalArgumentException("Odd number of characters.");
        }
        byte[] out = new byte[len >> 1];
        // two characters form the hex value.
        for (int i = 0, j = 0; j < len; i++) {
            int f = toDigit(data[j], j) << 4;
            j++;
            f = f | toDigit(data[j], j);
            j++;
            out[i] = (byte) (f & 0xFF);
        }
        return out;
    }

    protected static int toDigit(char ch, int index) throws IllegalArgumentException {
        int digit = Character.digit(ch, 16);
        if (digit == -1) {
            throw new IllegalArgumentException("Illegal hexadecimal charcter " + ch + " at index " + index);
        }
        return digit;
    }
}
