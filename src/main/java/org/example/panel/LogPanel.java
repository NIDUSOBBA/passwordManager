package org.example.panel;

import org.example.utile.AppLogger;

import javax.swing.*;
import java.awt.*;

public class LogPanel extends BasePage {
    private JTextPane logPane;

    @Override
    public JPanel createPanel() {
        logPane = new JTextPane();
        logPane.setEditable(false);
        logPane.setFont(new Font("Consolas", Font.PLAIN, 12));
        logPane.setBackground(new Color(30, 30, 30)); // тёмный фон
        JScrollPane logScroll = new JScrollPane(logPane);
        logScroll.setPreferredSize(new Dimension(0, 180)); // высота панели логов


        JPanel logHeader = new JPanel(new BorderLayout());
        logHeader.add(new JLabel(" 📋 Консоль "), BorderLayout.WEST);
        buttonIn(logHeader);

        JPanel logPanel = new JPanel(new BorderLayout());
        logPanel.add(logHeader, BorderLayout.NORTH);
        logPanel.add(logScroll, BorderLayout.CENTER);

        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            AppLogger.error("Необработанное исключение в потоке " + t.getName(), e);
        });

        AppLogger.init(logPane, "app.log");
        return logPanel;
    }

    @Override
    public void buttonIn(JPanel jPanel) {
        JButton btnClearLogs = new JButton("Очистить логи");
        btnClearLogs.addActionListener(e -> {
            try {
                logPane.getStyledDocument().remove(0, logPane.getDocument().getLength());
                AppLogger.info("Логи очищены");
            } catch (Exception ex) {
                System.err.println("Create Logger button exception: " + ex.getMessage());
            }
        });
        jPanel.add(btnClearLogs, BorderLayout.EAST);
    }
}
