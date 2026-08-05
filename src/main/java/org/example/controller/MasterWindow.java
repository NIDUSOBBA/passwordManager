package org.example.controller;

import org.example.dialog.DatabaseDialog;
import org.example.panel.AccountPanel;
import org.example.panel.EmailPanel;
import org.example.panel.LogPanel;
import org.example.panel.PasswordPanel;
import org.example.utile.AppLogger;
import org.example.utile.ExportData;
import org.example.utile.ImportData;

import javax.swing.*;
import java.awt.*;

public class MasterWindow extends JFrame {
    private final ExportData exportData;
    private final ImportData importData;
    private final LogPanel logPanel;
    private final AccountPanel accountPanel;
    private final PasswordPanel passwordPanel;
    private final EmailPanel emailPanel;

    public MasterWindow(ExportData exportData, ImportData importData, LogPanel logPanel, AccountPanel accountPanel, PasswordPanel passwordPanel, EmailPanel emailPanel) throws HeadlessException {
        this.exportData = exportData;
        this.importData = importData;
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
        menuBarInit();
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

    public void menuBarInit(){
        JButton btnExport = new JButton("💾 Export");
        JButton btnImport = new JButton("💾 Import");

        DatabaseDialog dialog = new DatabaseDialog(exportData, importData);
        btnExport.addActionListener(e -> {
            dialog.export();
        });
        btnImport.addActionListener(e -> {
            dialog.imprt();
        });

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("⚙️");
        fileMenu.add(btnExport);
        fileMenu.add(btnImport);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
    }


}
