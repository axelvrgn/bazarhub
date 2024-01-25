package com.example.bazarhub.Auth;

public class EmailAvailabilityRequest {

    private String email;

    public EmailAvailabilityRequest(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}