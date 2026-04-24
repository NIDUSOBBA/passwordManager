package org.example.dto;

public record AccountResponseDto(
        int id,
        String serviceName,
        int email,
        String username,
        int encryptedPassword
) {
    @Override
    public String toString() {
        return "id=" + id +
                ", serviceName=" + serviceName +
                ", email=" + email +
                ", username=" + username +
                ", encryptedPassword=" + encryptedPassword;
    }
}
