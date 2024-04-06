package com.example.keychat;

import android.security.keystore.KeyGenParameterSpec;
import android.util.Base64;
import android.util.Log;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Encryptor {
    private static SecretKey key;
    private static String tgt;
    private static String session_key;

    public static String getTgt() {
        return tgt;
    }

    public static void setTgt(String tgt) {
        Encryptor.tgt = tgt;
    }

    public static String getSession_key() {
        return session_key;
    }

    public static void setSession_key(String session_key) {
        Encryptor.session_key = session_key;
    }

    public static SecretKey getKey() {
        return key;
    }

    public static String encrypt(String input) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] cipherText = cipher.doFinal(input.getBytes(StandardCharsets.UTF_8));
        return new String(cipherText, StandardCharsets.UTF_8);
    }
    public static String decrypt(String input) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        byte[] cipherbytes = Base64.decode(input, Base64.DEFAULT);
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] plainText = cipher.doFinal(cipherbytes);
        return new String(plainText, StandardCharsets.UTF_8);
    }

    public static void setKey(String newKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        key = new SecretKeySpec(newKey.getBytes(StandardCharsets.UTF_8), "AES");
    }
}