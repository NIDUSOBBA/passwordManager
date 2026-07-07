package org.example.dto;

public record PasswordDto(
        int id,
        String encryptedPassword
) {
    @Override
    public String toString() {
        return encryptedPassword;
    }
}
