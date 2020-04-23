package cn.segi.wanandroid.demo.base.network.preferences;

import android.text.TextUtils;

import java.io.UnsupportedEncodingException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import cn.segi.wanandroid.demo.application.FrameworkInitializer;
import cn.segi.wanandroid.demo.base.utils.EncodeUtils;

/**
 * description
 *
 * @author liangzx
 * @version 1.0
 * @time 2020-03-03 16:47
 **/
public class AESForSpUtils {
    /*   算法/模式/填充 */
    private static final String CIPHER_MODE = "AES/ECB/PKCS5Padding";
    /**
     * 密钥
     */
    private static SecretKeySpec mSecretKeySpec;

    /**
     * 创建密钥
     *
     * @return
     */
    private static SecretKeySpec createKey() {
        if (null != mSecretKeySpec) {
            return mSecretKeySpec;
        }
        byte[] data = null;
        String password = FrameworkInitializer.getContext().getPackageName().toLowerCase()
                + "aokfjhcxnwjehcxcjhqhfnskx";
        StringBuilder sb = new StringBuilder(32);
        sb.append(password);
        while (sb.length() < 32) {
            sb.append("0");
        }
        if (sb.length() > 32) {
            sb.setLength(32);
        }
        try {
            data = sb.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return mSecretKeySpec = new SecretKeySpec(data, "AES");
    }

    /**
     * 加密
     *
     * @param content
     * @return
     */
    public static String encrypt(String content) {
        if (TextUtils.isEmpty(content)) {
            return content;
        }
        try {
            byte[] data = content.getBytes("UTF-8");
            data = encrypt(data);
            return EncodeUtils.base64Encode2String(data);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return content;
    }

    /* 加密字节数据  */
    private static byte[] encrypt(byte[] content) {
        try {
            SecretKeySpec key = createKey();
            Cipher cipher = Cipher.getInstance(CIPHER_MODE);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] result = cipher.doFinal(content);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密
     *
     * @param content
     * @return
     */
    public static String decrypt(String content) {
        if (TextUtils.isEmpty(content)) {
            return content;
        }
        try {
            byte[] data = EncodeUtils.base64Decode(content.getBytes("UTF-8"));
            data = decrypt(data);
            return new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*解密字节数组*/
    private static byte[] decrypt(byte[] content) {
        try {
            SecretKeySpec key = createKey();
            Cipher cipher = Cipher.getInstance(CIPHER_MODE);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] result = cipher.doFinal(content);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}