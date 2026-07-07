package org.example.dto;

public record AccountUpdateDto(
        int id,
        String serviceName,
        int emailId,
        String username,
        int passwordId
) {
}
