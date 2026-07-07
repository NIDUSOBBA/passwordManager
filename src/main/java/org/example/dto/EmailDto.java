package org.example.dto;

public record EmailDto(
        int id,
        String email
) {
    @Override
    public String toString() {
        return email;
    }
}
