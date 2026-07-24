package org.example.controller;

import org.example.dialog.CreateMasterKeyDialog;
import org.example.dialog.MasterKeyPromptDialog;
import org.example.utile.KeyringMasterKeyUtil;

public class MasterKeyDialogManager {

    private final CreateMasterKeyDialog createMasterKeyDialog;
    private final MasterKeyPromptDialog masterKeyPromptDialog;

    public MasterKeyDialogManager(KeyringMasterKeyUtil keyringMasterKeyUtil) {
        createMasterKeyDialog = new CreateMasterKeyDialog(null);
        masterKeyPromptDialog = new MasterKeyPromptDialog(null,keyringMasterKeyUtil);
    }

    public void masterKeyInit() throws InterruptedException {
        createMasterKeyDialog.setVisible(true);
        if (!createMasterKeyDialog.isSetupCompleted()){
            System.exit(0);
        }
    }

    public void masterKeyRemember(){
        masterKeyPromptDialog.setVisible(true);
        if (!masterKeyPromptDialog.isValid()){
            System.exit(0);
        }
    }

    public String getCrDiKey(){
        return createMasterKeyDialog.getMasterKey();
    }
}
