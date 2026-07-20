package org.example.service;

import com.github.javakeyring.BackendNotSupportedException;
import com.github.javakeyring.PasswordAccessException;
import org.example.controller.MasterKeyWindow;
import org.example.utile.KeyringMasterKeyUtil;

import javax.swing.*;

public class MasterKeyService {
    private final KeyringMasterKeyUtil keyUtil;
    private final MasterKeyWindow masterKeyWindow;

    public MasterKeyService(KeyringMasterKeyUtil keyUtil, MasterKeyWindow masterKeyWindow) {
        this.keyUtil = keyUtil;
        this.masterKeyWindow = masterKeyWindow;
    }

    public void masterKeyInit() throws InterruptedException {
        masterKeyWindow.setVisible(true);
        if (!masterKeyWindow.isSetupCompleted()){
            System.exit(0);
            return;
        }
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

    public String getGenKey(){
        return masterKeyWindow.getMasterKey();
    }
}
