package org.example.utile;

import com.google.gson.Gson;

import java.io.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class ImportData {
    private final Connection connection;
    private final Gson gson;

    public ImportData(Connection connection) {
        this.connection = connection;
        this.gson = new Gson();
    }

    public void importToDb(File file){
        Map<String, Object> importData;
        try {
            try(Reader reader = new FileReader(file)) {
                importData = gson.fromJson(reader,Map.class);
            }
            Object application = importData.get("application");
            if (!application.toString().equals("Account Manager")){
                throw new RuntimeException(" The file type is not suitable");
            }
            clearTables();

            Map<String, List<Map<String, Object>>> tables =
                    (Map<String, List<Map<String, Object>>>) importData.get("tables");

            for (Map.Entry<String, List<Map<String, Object>>> entry : tables.entrySet()) {
                String tableName = entry.getKey();
                List<Map<String, Object>> rows = entry.getValue();
                importTable(tableName, rows);
            }
            NotifyDataChanged.syncAfterImport();
            AppLogger.info("The import is completed from the file: " + file.getAbsolutePath());

        }catch (IOException | SQLException e){
            AppLogger.error("Import exception: ", e);
        }

    }

    private void clearTables() throws SQLException {
        // Очищаем в обратном порядке
        String[] tables = {"account", "password", "email", "vault_metadata"};

        try (Statement stmt = connection.createStatement()) {
            for (String table : tables) {
                stmt.execute("DELETE FROM " + table);
                AppLogger.info("Table '" + table + "' cleared");
            }
        }
    }
    private void importTable(String tableName, List<Map<String, Object>> rows) throws SQLException {
        if (rows == null || rows.isEmpty()) {
            return;
        }

        // ✅ Получаем информацию о таблице через PRAGMA
        List<String> columns = new ArrayList<>();
        List<String> columnTypes = new ArrayList<>();

        String pragmaSql = "PRAGMA table_info(" + tableName + ")";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(pragmaSql)) {

            while (rs.next()) {
                String colName = rs.getString("name");
                String colType = rs.getString("type");
                columns.add(colName);
                columnTypes.add(colType);
            }
        }

        // Формируем INSERT (исключаем id если это AUTOINCREMENT)
        List<String> insertColumns = new ArrayList<>();
        List<String> placeholders = new ArrayList<>();
        List<Integer> columnIndices = new ArrayList<>();

        for (int i = 0; i < columns.size(); i++) {
            String col = columns.get(i);
            String type = columnTypes.get(i);

            // Пропускаем id если это INTEGER PRIMARY KEY (AUTOINCREMENT)
            if (col.equalsIgnoreCase("id") && type.toUpperCase().contains("INTEGER")) {
                continue;
            }

            insertColumns.add(col);
            placeholders.add("?");
            columnIndices.add(i);
        }

        String insertSql = "INSERT INTO " + tableName + " (" +
                String.join(",", insertColumns) + ") VALUES (" +
                String.join(",", placeholders) + ")";

        AppLogger.info("SQL: " + insertSql);

        try (PreparedStatement ps = connection.prepareStatement(insertSql)) {
            for (Map<String, Object> row : rows) {
                int paramIndex = 1;

                for (int colIdx : columnIndices) {
                    String colName = columns.get(colIdx);
                    String colType = columnTypes.get(colIdx);
                    Object value = row.get(colName);

                    // Конвертируем Base64 обратно в byte[]
                    if (isBinaryColumn(colName) && value instanceof String) {
                        value = Base64.getDecoder().decode((String) value);
                    }

                    // Конвертируем timestamp
                    if (colName.equals("created_at")) {
                        value = convertTimestamp(value, colType);
                    }

                    // ✅ Явно указываем тип при установке
                    setParameter(ps, paramIndex, value, colType);
                    paramIndex++;
                }

                ps.addBatch();
            }

            ps.executeBatch();
            AppLogger.info("Импортировано записей в таблицу '" + tableName + "': " + rows.size());
        }
    }

    private void setParameter(PreparedStatement ps, int index, Object value, String sqlType) throws SQLException {
        if (value == null) {
            ps.setNull(index, Types.VARCHAR);
            return;
        }

        String type = sqlType.toUpperCase();

        if (type.contains("INT")) {
            ps.setInt(index, ((Number) value).intValue());
        } else if (type.contains("REAL") || type.contains("FLOAT") || type.contains("DOUBLE")) {
            ps.setDouble(index, ((Number) value).doubleValue());
        } else if (type.contains("BLOB") || type.contains("BINARY")) {
            ps.setBytes(index, (byte[]) value);
        } else {
            ps.setString(index, value.toString());
        }
    }

    private Object convertTimestamp(Object value, String colType) {
        if (value instanceof Number) {
            // Unix timestamp в миллисекундах
            long timestamp = ((Number) value).longValue();
            return new Timestamp(timestamp);
        } else if (value instanceof String) {
            // Строка формата "2026-08-05T21:43:01.961"
            try {
                LocalDateTime ldt = LocalDateTime.parse((String) value);
                return Timestamp.valueOf(ldt);
            } catch (Exception e) {
                return value;
            }
        }
        return value;
    }

    private boolean isBinaryColumn(String columnName) {
        return columnName.equals("encrypted_password") ||
                columnName.equals("salt") ||
                columnName.equals("password_fingerprint");
    }
}
