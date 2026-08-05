package org.example.utile;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ExportData {
    private final Connection connection;
    private final Gson gson;
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public ExportData(Connection connection) {
        this.connection = connection;
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create();
    }

    public void exportToJSON(File file) {
        Map<String, Object> exportData = new LinkedHashMap<>();
        exportData.put("version","1.0");
        exportData.put("exportDate", LocalDateTime.now().format(DATE_FORMAT));
        exportData.put("application","Account Manager");

        Map<String, List<Map<String, Object>>> tables = new LinkedHashMap<>();

        try {
            tables.put("vault_metadata", exportTable("vault_metadata"));
            tables.put("password", exportTable("password"));
            tables.put("email", exportTable("email"));
            tables.put("account", exportTable("account"));

            exportData.put("tables", tables);

            try(Writer writer = new FileWriter(file)) {
                gson.toJson(exportData,writer);
            }
        } catch (SQLException | IOException e) {
            AppLogger.error("Export exception", e);
        }
    }

    private List<Map<String, Object>> exportTable(String tableName) throws SQLException {
        List<Map<String, Object>> rows = new ArrayList<>();

        String sql = "SELECT * FROM " + tableName;

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (rs.next()) {
                Map<String, Object> row = new LinkedHashMap<>();

                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    int columnType = metaData.getColumnType(i);

                    // ✨ КРИТИЧНО: Проверяем тип колонки
                    if (columnType == Types.VARBINARY ||
                            columnType == Types.BINARY ||
                            columnType == Types.BLOB ||
                            columnType == Types.LONGVARBINARY) {

                        // Это бинарное поле — конвертируем в Base64 строку!
                        byte[] bytes = rs.getBytes(i);
                        if (bytes != null && !rs.wasNull()) {
                            row.put(columnName, Base64.getEncoder().encodeToString(bytes));
                        } else {
                            row.put(columnName, null);
                        }
                    } else if (columnType == Types.TIMESTAMP || columnType == Types.TIMESTAMP_WITH_TIMEZONE) {
                        // Timestamp конвертируем в читаемую строку
                        Timestamp ts = rs.getTimestamp(i);
                        if (ts != null && !rs.wasNull()) {
                            row.put(columnName, ts.toLocalDateTime().toString());
                        } else {
                            row.put(columnName, null);
                        }
                    } else {
                        // Обычные поля (String, Integer и т.д.)
                        Object value = rs.getObject(i);
                        if (rs.wasNull()) {
                            value = null;
                        }
                        row.put(columnName, value);
                    }
                }

                rows.add(row);
            }
        }

        return rows;
    }
}
