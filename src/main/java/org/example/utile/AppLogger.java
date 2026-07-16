package org.example.utile;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AppLogger {
    private static AppLogger instance;

    private static JTextPane logPane;
    private static StyledDocument doc;
    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm:ss");

    private static Style infoStyle;
    private static Style warnStyle;
    private static Style errorStyle;
    private static boolean initialized = false;

    private AppLogger(JTextPane logPane) {
        AppLogger.logPane = logPane;
        doc = logPane.getStyledDocument();

        // Настраиваем стили
        infoStyle = doc.addStyle("INFO", null);
        StyleConstants.setForeground(infoStyle, new Color(50, 200, 50));

        warnStyle = doc.addStyle("WARN", null);
        StyleConstants.setForeground(warnStyle, new Color(200, 150, 0));

        errorStyle = doc.addStyle("ERROR", null);
        StyleConstants.setForeground(errorStyle, new Color(200, 0, 0));
        StyleConstants.setBold(errorStyle, true);
    }

    public static synchronized void init(JTextPane logPane) {
        if (instance == null) {
            instance = new AppLogger(logPane);
        }
        initialized = true;
    }

    public static void info(String msg) {
        log("INFO", msg);
    }

    public static void warn(String msg) {
        log("WARN", msg);
    }

    public static void error(String msg, Throwable e) {
        String errorMsg = msg + e.getMessage();
        log("ERROR", errorMsg);
    }

    private static void log(String level, String msg) {
        if (instance == null) return;
        String timestamp = LocalDateTime.now().format(instance.fmt);
        String line = String.format("[%s] [%s] %s%n", timestamp, level, msg);

        if ("ERROR".equals(level)) {
            System.err.print(line);
        } else {
            System.out.print(line);
        }
        if (initialized) {
            Style actualStyle = switch (level) {
                case "WARN" -> warnStyle;
                case "ERROR" -> errorStyle;
                default -> infoStyle;
            };

            SwingUtilities.invokeLater(() -> {
                try {
                    doc.insertString(doc.getLength(), line, actualStyle);
                    logPane.setCaretPosition(doc.getLength());
                    if (doc.getLength() > 500_000) {
                        doc.remove(0, 100_000);
                    }
                } catch (BadLocationException e) {
                    System.err.println("Exception log: " + e.getMessage());
                }
            });
        }
    }
}
