package org.example.dto;

public record AccountCreateDto(
        String serviceName,
        int email,
        String username,
        int encryptedPassword
) {
}
