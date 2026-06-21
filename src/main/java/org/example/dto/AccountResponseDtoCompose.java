package org.example.dto;

public record AccountResponseDtoCompose(
        int id,
        String serviceName,
        String email,
        String username,
        String encryptedPassword
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
