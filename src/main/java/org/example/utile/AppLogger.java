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

    private final JTextPane logPane;
    private final StyledDocument doc;
    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm:ss");

    private final Style infoStyle, warnStyle, errorStyle;

    private AppLogger(JTextPane logPane) {
        this.logPane = logPane;
        this.doc = logPane.getStyledDocument();

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
    }

    public static AppLogger getInstance() {
        if (instance == null) throw new IllegalStateException("Logger не инициализирован!");
        return instance;
    }

    public static void info(String msg) {
        log("INFO", msg, instance.infoStyle);
    }

    public static void warn(String msg) {
        log("WARN", msg, instance.warnStyle);
    }

    public static void error(String msg, Throwable e) {
        String errorMsg = msg + e.getMessage();
        log("ERROR", errorMsg, instance.errorStyle);
    }

    private static void log(String level, String msg, Style style) {
        if (instance == null) return;
        String timestamp = LocalDateTime.now().format(instance.fmt);
        String line = String.format("[%s] [%s] %s%n", timestamp, level, msg);

        if ("ERROR".equals(level)){
            System.err.print(line);
        } else{
            System.out.print(line);
        }
        SwingUtilities.invokeLater(() -> {
            try {
                instance.doc.insertString(instance.doc.getLength(), line, style);
                instance.logPane.setCaretPosition(instance.doc.getLength());
                if (instance.doc.getLength() > 500_000) {
                    instance.doc.remove(0, 100_000);
                }
            } catch (BadLocationException e) {
                System.err.println("Exception log: " + e.getMessage());
            }
        });
    }
}
