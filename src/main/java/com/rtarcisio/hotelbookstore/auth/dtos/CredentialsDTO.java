package com.rtarcisio.hotelbookstore.auth.dtos;


public record CredentialsDTO(String email, String password) {

    @Override
    public String toString() {
        return "CredentialsDTO{" +
                "email='" + email + '\'' +
                ", password='[PROTECTED]'" +  // Mascare a senha aqui
                '}';
    }
}
