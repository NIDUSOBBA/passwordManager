package org.example.dto;

import java.sql.Timestamp;

public record AccountResponseDto(
        int id,
        String serviceName,
        int email,
        String username,
        int encryptedPassword,
        Timestamp created,
        Timestamp updated
) {
    @Override
    public String toString() {
        return "id=" + id +
                ", serviceName=" + serviceName +
                ", email=" + email +
                ", username=" + username +
                ", encryptedPassword=" + encryptedPassword +
                ", created=" + created +
                ", updated=" + updated;
    }
}
