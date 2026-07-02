package org.example.controller;

import org.example.panel.AccountPanel;
import org.example.panel.EmailPanel;
import org.example.panel.LogPanel;
import org.example.panel.PasswordPanel;
import org.example.utile.AppLogger;

import javax.swing.*;
import java.awt.*;

public class WindowManager extends JFrame {

    private LogPanel logPanel;
    private AccountPanel accountPanel;
    private PasswordPanel passwordPanel;
    private EmailPanel emailPanel;

    public WindowManager(LogPanel logPanel, AccountPanel accountPanel, PasswordPanel passwordPanel, EmailPanel emailPanel) throws HeadlessException {
        this.logPanel = logPanel;
        this.accountPanel = accountPanel;
        this.passwordPanel = passwordPanel;
        this.emailPanel = emailPanel;
    }

    public void start(){
        windowIn();
    }

    public void windowIn() {
        setTitle("Менеджер Аккаунтов");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        assemblingWindow();
        AppLogger.info("Приложение запущено успешно");
    }

    public void assemblingWindow(){
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tabsInit(), logPanel.createPanel());
        splitPane.setResizeWeight(0.8); // 80% вкладкам, 20% логам
        splitPane.setDividerLocation(500);
        splitPane.setOneTouchExpandable(true); // кнопка "свернуть/развернуть"

        setLayout(new BorderLayout());
        add(splitPane, BorderLayout.CENTER);
    }

    public JTabbedPane tabsInit(){
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("🔑 Пароли", passwordPanel.createPanel());
        tabs.addTab("🔐 Аккаунты", accountPanel.createPanel());
        tabs.addTab("📧 Почта",emailPanel.createPanel());
        return tabs;
    }


}
