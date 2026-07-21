package org.example.service;

import com.github.javakeyring.BackendNotSupportedException;
import com.github.javakeyring.PasswordAccessException;
import org.example.dialog.MasterKeyDialog;
import org.example.utile.KeyringMasterKeyUtil;

public class MasterKeyService {
    private final KeyringMasterKeyUtil keyUtil;
    private final MasterKeyDialog masterKeyDialog;

    public MasterKeyService(KeyringMasterKeyUtil keyUtil, MasterKeyDialog masterKeyDialog) {
        this.keyUtil = keyUtil;
        this.masterKeyDialog = masterKeyDialog;
    }

    public void masterKeyInit() throws InterruptedException {
        masterKeyDialog.setVisible(true);
        if (!masterKeyDialog.isSetupCompleted()){
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
        return masterKeyDialog.getMasterKey();
    }
}
