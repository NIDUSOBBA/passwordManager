package org.example.controller;

import org.example.panel.AccountPanel;
import org.example.panel.EmailPanel;
import org.example.panel.LogPanel;
import org.example.panel.PasswordPanel;
import org.example.utile.AppLogger;

import javax.swing.*;
import java.awt.*;

public class MasterWindow extends JFrame {

    private final LogPanel logPanel;
    private final AccountPanel accountPanel;
    private final PasswordPanel passwordPanel;
    private final EmailPanel emailPanel;

    public MasterWindow(LogPanel logPanel, AccountPanel accountPanel, PasswordPanel passwordPanel, EmailPanel emailPanel) throws HeadlessException {
        this.logPanel = logPanel;
        this.accountPanel = accountPanel;
        this.passwordPanel = passwordPanel;
        this.emailPanel = emailPanel;
    }

    public void start() {
        windowIn();
    }

    public void windowIn() {
        setTitle("Account Manager");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        assemblingWindow();
        AppLogger.info("Application running");
    }

    public void assemblingWindow() {
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tabsInit(), logPanel.createPanel());
        splitPane.setResizeWeight(0.8); // 80% вкладкам, 20% логам
        splitPane.setDividerLocation(500);
        splitPane.setOneTouchExpandable(true); // кнопка "свернуть/развернуть"

        setLayout(new BorderLayout());
        add(splitPane, BorderLayout.CENTER);
    }

    public JTabbedPane tabsInit() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("🔑 Password", passwordPanel.createPanel());
        tabs.addTab("🔐 Account", accountPanel.createPanel());
        tabs.addTab("📧 Mail", emailPanel.createPanel());
        return tabs;
    }


}
