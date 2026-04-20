package org.example.dto;

public record PasswordDto(
        int id,
        String encryptedPassword
) {
}
