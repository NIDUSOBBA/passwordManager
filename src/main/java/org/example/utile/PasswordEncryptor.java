package org.example.utile;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.security.spec.KeySpec;

public class PasswordEncryptor {
    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final int GCM_TAG_LENGTH = 128; // бит
    private static final int GCM_IV_LENGTH = 12;   // байт, рекомендуется для GCM
    private static final int AES_KEY_SIZE = 256;

    // Генерация ключа из мастер-пароля
    public static SecretKey deriveKeyFromPassword(char[] password, byte[] salt)
            throws Exception {

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password, salt, 310000, AES_KEY_SIZE);
        SecretKey tmp = factory.generateSecret(spec);
        return new SecretKeySpec(tmp.getEncoded(), "AES");
    }

    // Генерация случайной соли
    public static byte[] generateSalt() {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        return salt;
    }

    // Генерация случайного IV
    public static byte[] generateIV() {
        byte[] iv = new byte[GCM_IV_LENGTH];
        new SecureRandom().nextBytes(iv);
        return iv;
    }

    // Шифрование пароля
    public static EncryptedData encryptPassword(byte[] password, SecretKey key)
            throws Exception {

        byte[] iv = generateIV();
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, spec);

        byte[] encrypted = cipher.doFinal(password);

        return new EncryptedData(encrypted, iv);
    }

    // Дешифрование пароля
    public static byte[] decryptPassword(EncryptedData encryptedData, SecretKey key)
            throws Exception {

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, encryptedData.iv);
        cipher.init(Cipher.DECRYPT_MODE, key, spec);

        return cipher.doFinal(encryptedData.encryptedBytes);
    }

    public record EncryptedData(byte[] encryptedBytes, byte[] iv) {
    }

}
