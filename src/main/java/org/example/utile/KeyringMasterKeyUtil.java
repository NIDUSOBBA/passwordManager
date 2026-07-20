package org.example.utile;

import com.github.javakeyring.BackendNotSupportedException;
import com.github.javakeyring.Keyring;
import com.github.javakeyring.PasswordAccessException;

//Класс предназначен для хранения и получения мастер ключа при помощи ОС

public class KeyringMasterKeyUtil {
    private static final String KEYRING_SERVICE_NAME = "PasswordManagerMasterKey";
    private static final String KEYRING_ACCOUNT_NAME = "master_key";
    private final Keyring keyring;

    public KeyringMasterKeyUtil(Keyring keyring) {
        this.keyring = keyring;
    }

    public void create(String masterKey) throws PasswordAccessException, BackendNotSupportedException {
        keyring.setPassword(KEYRING_SERVICE_NAME, KEYRING_ACCOUNT_NAME, masterKey);
    }

    public String get() throws PasswordAccessException {
        return keyring.getPassword(KEYRING_SERVICE_NAME, KEYRING_ACCOUNT_NAME);
    }

}