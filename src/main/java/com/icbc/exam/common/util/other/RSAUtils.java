package com.icbc.exam.common.util.other;

import com.icbc.exam.common.constant.DailyConstant;
import org.apache.commons.codec.binary.Base64;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lida
 * @title:
 * @projectName：plm_mgmt_npl
 * @description:
 * @date 2021/1/11
 */
public class RSAUtils {

    public RSAUtils() {
    }

    /**
     * 分段加密
     **/
    public static String encryptRSA(String jsonStr, String publicKeyStr) {
        if (jsonStr == null) {
            return null;
        }
        int length = jsonStr.length();
        int size = length / 100 + (length % 100 == 0 ? 0 : 1);
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < size; ++i) {
            int start = i * 100;
            int end = (i + 1) * 100;
            if (end > length) {
                end = length;
            }

            String tmp = jsonStr.substring(start, end);
            byte[] bytes = new byte[1024];

            try {
                bytes = (new BASE64Decoder()).decodeBuffer(publicKeyStr);
            } catch (IOException var14) {
                var14.printStackTrace();
            }

            PublicKey publicKey = restorePublicKey(bytes);
            byte[] encodedText = new byte[1024];

            try {
                //加密
                encodedText = RSAEncode(publicKey, tmp.getBytes("utf-8"));
            } catch (UnsupportedEncodingException var13) {
                var13.printStackTrace();
            }

            String tmpStr = Base64.encodeBase64String(encodedText);
            sb.append(tmpStr).append("#");
        }

        return sb.toString();
    }

    /**
     * 分段解密
     **/
    public static String decryptRSA(String jsonStr, String privateKeyStr) throws UnsupportedEncodingException {
        String[] base64Strs = jsonStr.toString().split("#");
        byte[] bytes = new byte[1024];

        try {
            bytes = (new BASE64Decoder()).decodeBuffer(privateKeyStr);
        } catch (IOException var7) {
            var7.printStackTrace();
        }

        PrivateKey privateKey = restorePrivateKey(bytes);
        StringBuilder sbuilder = new StringBuilder();

        for (int i = 0; i < base64Strs.length; ++i) {
            sbuilder.append(RSADecode(privateKey, Base64.decodeBase64(base64Strs[i])));
        }

        return sbuilder.toString();
    }

    /**
     * 加密
     **/
    public static String encrypt(String source, String publicKeyStr) throws Exception {
        byte[] bytes = new byte[1024];
        try {
            bytes = (new BASE64Decoder()).decodeBuffer(publicKeyStr);
        } catch (IOException var14) {
            var14.printStackTrace();
        }
        PublicKey publicKey = restorePublicKey(bytes);
        Cipher cipher = Cipher.getInstance(publicKey.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        cipher.update(source.getBytes("utf-8"));
        return Base64.encodeBase64String(cipher.doFinal());
    }

    /**
     * 分段加密
     **/
    public static String encrypt2(String source, String publicKeyStr) throws Exception {
        if (source == null) {
            return null;
        }
        String content = source;
        StringBuffer sb = new StringBuffer();
        int max = 60;
        int num = (content.length() / max) + 1;
        boolean flage = content.length() % max == 0;

        for (int i = 0; i < num; i++) {
            if (i == num - 1) {
                continue;
            }
            sb.append(encrypt(content.substring((i * max), (i + 1) * max), publicKeyStr));
            sb.append("#");
        }
        String substring = encrypt(content.substring(((num - 1) * max), content.length()), publicKeyStr);
        sb.append(substring);

        if (flage) {
            return sb.substring(0, sb.length() - 1);
        }
        return sb.toString();
    }


    /**
     * 解密
     **/
    public static String decrypt(String target, String privateKeyStr) throws Exception {
        byte[] bytes = new byte[1024];

        try {
            bytes = (new BASE64Decoder()).decodeBuffer(privateKeyStr);
        } catch (IOException var7) {
            var7.printStackTrace();
        }
        PrivateKey privateKey = restorePrivateKey(bytes);
        Cipher cipher = Cipher.getInstance(privateKey.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        cipher.update(Base64.decodeBase64(target.getBytes("utf-8")));

        return new String(cipher.doFinal(), "utf-8");
    }

    /**
     * 分段解密
     **/
    public static String decrypt2(String target, String privateKeySet) throws Exception {
        String[] targets = target.split("#");
        StringBuffer sb = new StringBuffer(1024);
        for (int i = 0; i < targets.length; i++) {
            String temp = targets[i];
            sb.append(decrypt(temp, privateKeySet));
        }
        return sb.toString();
    }


    public static Map generateKeyBytes() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
            Map keyMap = new HashMap(2);
            keyMap.put("publicKey", (new BASE64Encoder()).encode(publicKey.getEncoded()));
            keyMap.put("privateKey", (new BASE64Encoder()).encode(privateKey.getEncoded()));
            System.out.println("PUBLIC_KEY=" + (new BASE64Encoder()).encode(publicKey.getEncoded()));
            System.out.print("PRIVATE_KEY=" + (new BASE64Encoder()).encode(privateKey.getEncoded()));
            return keyMap;
        } catch (NoSuchAlgorithmException var5) {
            var5.printStackTrace();
            return null;
        }
    }

    public static PublicKey restorePublicKey(byte[] keyBytes) {
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);

        try {
            KeyFactory factory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = factory.generatePublic(x509EncodedKeySpec);
            return publicKey;
        } catch (Exception var4) {
            var4.printStackTrace();
            return null;
        }
    }

    public static PrivateKey restorePrivateKey(byte[] keyBytes) {
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);

        try {
            KeyFactory factory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = factory.generatePrivate(pkcs8EncodedKeySpec);
            return privateKey;
        } catch (Exception var4) {
            var4.printStackTrace();
            return null;
        }
    }

    public static byte[] RSAEncode(PublicKey key, byte[] plainText) {
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(1, key);
            byte[] bytes = cipher.doFinal(plainText);
            return bytes;
        } catch (Exception var3) {
            var3.printStackTrace();
            return null;
        }
    }

    public static String RSADecode(PrivateKey key, byte[] encodedText) {
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(2, key);
            return new String(cipher.doFinal(encodedText), "utf-8");
        } catch (Exception var3) {
            var3.printStackTrace();
            return null;
        }
    }


    public static void main(String[] args) {
        String name = "标准时间格式标准时间格式标准时间格" +
                "式标准时间格式标准时间格式标准时间格式标准时间" +
                "格式标准时间格式标准时间格式标准时间格式标准时间格" +
                "式标准时间格式标准时间格式标准时间格式标准时间格式标准时" +
                "间格式标准时间格式标准时间格式标准时间格式标准时间格式标准时间" +
                "格式标准时间格式标准时间格式标准时间格式标准时间格式标准时间格" +
                "式标准时间格式标准时间格式标准时间格式标准时间格式标准时间格式" +
                "标准时间格式标准时间格式标准时间格式";
        try {
            String s = RSAUtils.encrypt2(name, DailyConstant.publicKey);

            System.out.println(RSAUtils.decryptRSA(s, DailyConstant.privateKey));


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
