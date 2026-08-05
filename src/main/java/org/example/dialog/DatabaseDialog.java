package org.example.dialog;

import org.example.utile.AppLogger;
import org.example.utile.ExportData;
import org.example.utile.ImportData;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DatabaseDialog {
    private final ExportData exportData;
    private final ImportData importData;

    public DatabaseDialog(ExportData exportData, ImportData importData) {
        this.exportData = exportData;
        this.importData = importData;
    }

    public void export() {
        AppLogger.info("Export Attempt");
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Export");
        fileChooser.setFileFilter(new FileNameExtensionFilter("JSON (*.json)", "json"));
        fileChooser.setSelectedFile(new File("backup_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".json"));

        int result = fileChooser.showSaveDialog(null);
        if (result != JFileChooser.APPROVE_OPTION) {
            AppLogger.info("Export cancellation");
            return;
        }
        File file = fileChooser.getSelectedFile();
        if (file.exists()) {
            int confirm = JOptionPane.showConfirmDialog(null,
                    "The file already exists. Overwrite it?",
                    "Confirmation",
                    JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) {
                AppLogger.info("Export cancellation");
                return;
            }
        }
        exportData.exportToJSON(file);
        AppLogger.info("Export is successful");
    }

    public void imprt(){
        AppLogger.info("Import Attempt");
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Import");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("JSON (*.json)", "json"));

        int result = fileChooser.showOpenDialog(null);
        if (result != JFileChooser.APPROVE_OPTION) {
            AppLogger.info("Import cancellation");
            return;
        }

        File file = fileChooser.getSelectedFile();
        // 2. Подтверждение (импорт удалит текущие данные!)
        int confirm = JOptionPane.showConfirmDialog(null,
                "Attention! The import will delete all current data and replace it with data from the file.\n\nContinue?",
                "Import Confirmation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        if (confirm != JOptionPane.YES_OPTION) {
            AppLogger.info("Import cancellation");
            return;
        }
        importData.importToDb(file);
    }
}
