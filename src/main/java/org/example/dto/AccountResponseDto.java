package org.example.dto;

public record AccountResponseDto(
        int id,
        String serviceName,
        int email,
        String username,
        int encryptedPassword
) {
}
