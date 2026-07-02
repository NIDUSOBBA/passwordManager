package org.example.dto;

import java.sql.Timestamp;

public record AccountResponseDtoCompose(
        int id,
        String serviceName,
        String email,
        String username,
        String encryptedPassword,
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
