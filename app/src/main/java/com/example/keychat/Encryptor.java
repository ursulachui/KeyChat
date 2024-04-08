package com.example.keychat;

import android.security.keystore.KeyGenParameterSpec;
import android.util.Base64;
import android.util.Log;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Encryptor {
    private static String tgt;
    private static SecretKey session_key;

    public static String getTgt() {
        return tgt;
    }

    public static void setTgt(String tgt) {
        Encryptor.tgt = tgt;
    }

    public static SecretKey getSession_key() {
        return session_key;
    }

    public static void setSession_key(SecretKey session_key) {
        Encryptor.session_key = session_key;
    }


    public static String encrypt(String input, SecretKey key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] cipherText = cipher.doFinal(input.getBytes(StandardCharsets.UTF_8));
        return new String(cipherText, StandardCharsets.UTF_8);
    }

    public static String decrypt(String input, SecretKey key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        byte[] cipherbytes = Base64.decode(input, Base64.DEFAULT);
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] plainText = cipher.doFinal(cipherbytes);
        return new String(plainText, StandardCharsets.UTF_8);
    }
    public static byte[] decrypt(byte[] cipherbytes, SecretKey key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] plainText = cipher.doFinal(cipherbytes);
        return plainText;
    }

    public static SecretKey getKeyFromString(String newKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return new SecretKeySpec(newKey.getBytes(StandardCharsets.UTF_8), "AES");
    }

    public static SecretKey getKeyFromPassword(String password) throws NoSuchAlgorithmException {
        byte[] inputBytes = password.getBytes(StandardCharsets.UTF_8);
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] newKey = md.digest(inputBytes);
        return new SecretKeySpec(newKey,"AES");
    }
}
