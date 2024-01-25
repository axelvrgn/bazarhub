package com.example.bazarhub.Auth;

public class EmailAvailabilityResponse {

    private boolean isAvailable;

    public boolean isAvailable() {
        return isAvailable;
    }

    @Override
    public String toString() {
        return "EmailAvailabilityResponse{" +
                "isAvailable=" + isAvailable +
                '}';
    }
}