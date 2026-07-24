package org.example.service;

import com.github.javakeyring.BackendNotSupportedException;
import com.github.javakeyring.PasswordAccessException;
import org.example.utile.KeyringMasterKeyUtil;

public class MasterKeyService {
    private final KeyringMasterKeyUtil keyUtil;
    public MasterKeyService(KeyringMasterKeyUtil keyUtil) {
        this.keyUtil = keyUtil;
    }

    public void masterKeyCreate(String key) {
        try {
            keyUtil.create(key);
        } catch (PasswordAccessException | BackendNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public String get() {
        try {
            return keyUtil.get();
        } catch (PasswordAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
