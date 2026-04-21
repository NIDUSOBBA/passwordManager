package org.example.service;

import org.bouncycastle.util.encoders.Hex;
import org.example.dao.MetadataDao;
import org.example.utile.PasswordEncryptor;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class VaultEncryptionService {
    private final SecretKey masterKey;
    private final byte[] hmacSecretKey;
    private final byte[] salt;

    public VaultEncryptionService(String masterPassword, MetadataDao metadataDao)
            throws Exception {
        byte[] salt = metadataDao.get();
        this.hmacSecretKey = masterPassword.getBytes(StandardCharsets.UTF_8);
        if (salt == null) {
            // Первый запуск - генерируем новую соль
            metadataDao.create(PasswordEncryptor.generateSalt());
            this.salt = metadataDao.get();
            this.masterKey = PasswordEncryptor.deriveKeyFromPassword(masterPassword.toCharArray(), this.salt);
        } else {
            // Загрузка существующей соли
            this.salt = salt;
            this.masterKey = PasswordEncryptor.deriveKeyFromPassword(masterPassword.toCharArray(), this.salt);
        }
    }

    public String generateFingerprint(String password) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec keySpec = new SecretKeySpec(hmacSecretKey, "HmacSHA256");
            mac.init(keySpec);

            byte[] hashBytes = mac.doFinal(password.getBytes(StandardCharsets.UTF_8));
            return Hex.toHexString(hashBytes);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Generate fingerprint exception", e);
        }
    }

    // Шифрование для хранения в БД
    public byte[] encryptForStorage(String plainPassword) throws Exception {
        byte[] passwordBytes = plainPassword.getBytes(StandardCharsets.UTF_8);
        PasswordEncryptor.EncryptedData encrypted =
                PasswordEncryptor.encryptPassword(passwordBytes, masterKey);

        // Объединяем IV и зашифрованные данные в один массив
        byte[] result = new byte[encrypted.iv().length + encrypted.encryptedBytes().length];
        System.arraycopy(encrypted.iv(), 0, result, 0, encrypted.iv().length);
        System.arraycopy(encrypted.encryptedBytes(), 0, result, encrypted.iv().length,
                encrypted.encryptedBytes().length);
        return result;
    }

    // Дешифрование из БД
    public String decryptFromStorage(byte[] storedData) throws Exception {
        // Извлекаем IV (первые 12 байт) и зашифрованные данные
        byte[] iv = new byte[12];
        byte[] encrypted = new byte[storedData.length - 12];
        System.arraycopy(storedData, 0, iv, 0, 12);
        System.arraycopy(storedData, 12, encrypted, 0, encrypted.length);

        PasswordEncryptor.EncryptedData encryptedData =
                new PasswordEncryptor.EncryptedData(encrypted, iv);

        byte[] decrypted = PasswordEncryptor.decryptPassword(encryptedData, masterKey);
        return new String(decrypted, StandardCharsets.UTF_8);
    }

}
