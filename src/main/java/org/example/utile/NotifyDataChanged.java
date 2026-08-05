package org.example.utile;

import org.example.panel.AccountPanel;
import org.example.panel.EmailPanel;
import org.example.panel.PasswordPanel;

//Клас нужный для синхронизации удаления паролей и почты для аккаунтов
public class NotifyDataChanged {
    private static AccountPanel accountPanel;
    private static PasswordPanel passwordPanel;
    private static EmailPanel emailPanel;

    public static void setAccountPanel(AccountPanel accountP) {
        accountPanel = accountP;
    }

    public static void setPasswordPanel(PasswordPanel passwordPanel) {
        NotifyDataChanged.passwordPanel = passwordPanel;
    }

    public static void setEmailPanel(EmailPanel emailPanel) {
        NotifyDataChanged.emailPanel = emailPanel;
    }

    public static void syncAfterRemoval(){
        accountPanel.deleteAllRow();
        accountPanel.loadTable();
    }

    public static void syncAfterImport(){
        passwordPanel.deleteAllRow();
        passwordPanel.loadTable();
        emailPanel.deleteAllRow();
        emailPanel.loadTable();
        accountPanel.deleteAllRow();
        accountPanel.loadTable();
    }
}
