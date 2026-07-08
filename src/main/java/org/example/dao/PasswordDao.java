package org.example.dao;

import org.example.dto.PasswordDto;
import org.example.service.VaultEncryptionService;
import org.example.utile.AppLogger;
import org.example.utile.TimestampUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PasswordDao {
    private final Connection connection;
    private final VaultEncryptionService vaultEncryptionService;

    public PasswordDao(Connection connection, VaultEncryptionService vaultEncryptionService) {
        this.connection = connection;
        this.vaultEncryptionService = vaultEncryptionService;
    }

    public void crete(String password) {
        String insertQuery = """
                INSERT INTO password (encrypted_password, password_fingerprint, created_at)
                VALUES (?,?,?);
                """;
        try (PreparedStatement statement = connection.prepareStatement(insertQuery)) {
            statement.setBytes(1, vaultEncryptionService.encryptForStorage(password));
            statement.setString(2, vaultEncryptionService.generateFingerprint(password));
            statement.setTimestamp(3, TimestampUtil.getCurrentTimestamp());
            statement.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Dublicat password");
        }
    }

    public PasswordDto getById(int id) {
        String selectQuery = """
                SELECT id, encrypted_password
                FROM password
                WHERE id = ?
                """;
        try (PreparedStatement statement = connection.prepareStatement(selectQuery)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            return new PasswordDto(
                    resultSet.getInt("id"),
                    vaultEncryptionService.decryptFromStorage(resultSet.getBytes("encrypted_password")
                    ));

        } catch (Exception e) {
            AppLogger.error("Password getById exception: ", e);
        }
        return null;
    }

    public List<PasswordDto> getAll() {
        String selectQuery = """
                SELECT id, encrypted_password
                FROM password
                GROUP BY id;
                """;
        List<PasswordDto> resultList = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(selectQuery);
            while (resultSet.next()) {
                resultList.add(new PasswordDto(
                        resultSet.getInt("id"),
                        vaultEncryptionService.decryptFromStorage(resultSet.getBytes("encrypted_password")
                        )));
            }
        } catch (Exception e) {
            AppLogger.error("Password getAll exception: ", e);
        }
        return resultList;
    }

    public void deleteById(int id) {
        String deleteQuery = """
                DELETE FROM password
                WHERE id = ?;
                """;
        try (PreparedStatement statement = connection.prepareStatement(deleteQuery)) {
            statement.setInt(1, id);
            statement.execute();
        } catch (SQLException e) {
            AppLogger.error("Password deleteById exception: ", e);
        }
    }

    public PasswordDto getLast(){
        String getLast = """
                SELECT *
                FROM password
                WHERE id = (
                 SELECT MAX(id)
                 FROM password
                 );
                """;
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(getLast);
            return new PasswordDto(
                    resultSet.getInt("id"),
                    vaultEncryptionService.decryptFromStorage(resultSet.getBytes("encrypted_password")
                    ));
        } catch (Exception e) {
            AppLogger.error("Password getLast exception: ", e);
        }
        return null;
    }
}
