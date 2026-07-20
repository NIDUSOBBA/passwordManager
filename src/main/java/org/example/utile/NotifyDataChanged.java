package org.example.utile;

import org.example.panel.AccountPanel;

public class NotifyDataChanged {
    private static AccountPanel accountPanel;

    public static void setAccountPanel(AccountPanel accountP) {
        accountPanel = accountP;
    }

    public static void syncAfterRemoval(){
        accountPanel.deleteAllRow();
        accountPanel.loadTable();
    }
}
