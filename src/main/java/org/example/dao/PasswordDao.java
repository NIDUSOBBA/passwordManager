package org.example.dao;

import org.example.dto.PasswordDto;
import org.example.service.VaultEncryptionService;

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
                INSERT INTO password (encrypted_password, password_fingerprint)
                VALUES (?,?);
                """;
        try (PreparedStatement statement = connection.prepareStatement(insertQuery)) {
            statement.setBytes(1, vaultEncryptionService.encryptForStorage(password));
            statement.setString(2, vaultEncryptionService.generateFingerprint(password));
            statement.executeUpdate();
        } catch (Exception e) {
            System.out.println("Password create exception: " + e.getMessage());
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
            System.out.println("Password getById exception: " + e.getMessage());
        }
        return null;
    }

    public List<PasswordDto> getAll() {
        String selectQuery = """
                SELECT id, encrypted_password
                FROM password;
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
            System.out.println("Password getAll exception: " + e.getMessage());
        }
        return resultList;
    }

    public void updateById(PasswordDto passwordDto) {
        String updateQuery = """
                UPDATE password
                SET encrypted_password = ?, password_fingerprint = ?, created_at = CURRENT_TIMESTAMP
                WHERE id = ?;
                """;
        try (PreparedStatement statement = connection.prepareStatement(updateQuery)) {
            statement.setBytes(1, vaultEncryptionService.encryptForStorage(passwordDto.encryptedPassword()));
            statement.setString(2, vaultEncryptionService.generateFingerprint(passwordDto.encryptedPassword()));
            statement.setInt(3, passwordDto.id());
            statement.executeUpdate();
        } catch (Exception e) {
            System.out.println("Password updateById exception: " + e.getMessage());
        }
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
            System.out.println("Password deleteById exception: " + e.getMessage());
        }
    }
}
