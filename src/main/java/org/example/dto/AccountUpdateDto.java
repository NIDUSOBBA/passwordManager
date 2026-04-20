package org.example.dto;

public record AccountUpdateDto(
        int id,
        String serviceName,
        String username,
        int passwordId
) {
}
