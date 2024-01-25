package com.example.bazarhub.Auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bazarhub.Api.AuthApi;
import com.example.bazarhub.Api.RetrofitClient;
import com.example.bazarhub.CreateUserRequest;
import com.example.bazarhub.Models.UserResponse;
import com.example.bazarhub.R;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RegisterActivity extends AppCompatActivity {

    private EditText usernameInput, emailInput, passwordInput;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameInput = findViewById(R.id.register_username_input);
        emailInput = findViewById(R.id.register_email_input);
        passwordInput = findViewById(R.id.register_password_input);
        registerButton = findViewById(R.id.register_btn);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        String username = usernameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check email availability before attempting to create a new user
        // checkEmailAvailability(email, new EmailAvailabilityCallback() {
        //     @Override
        //     public void onAvailable() {
        //         // Email is available, proceed with user creation
        createUser(username, email, password);
        //     }

        //     @Override
        //     public void onUnavailable() {
        //         // Email is already in use
        //         Toast.makeText(RegisterActivity.this, "Email is already in use", Toast.LENGTH_SHORT).show();
        //     }

        //     @Override
        //     public void onError() {
        //         // Handle error in checking email availability
        //         Toast.makeText(RegisterActivity.this, "Failed to check email availability", Toast.LENGTH_SHORT).show();
        //     }
        // });
    }

    private void createUser(String name, String email, String password) {
        AuthApi authApi = RetrofitClient.getClient().create(AuthApi.class);
        Call<UserResponse> call = authApi.createUser(new CreateUserRequest(name, email, password, "https://example.com/avatar.jpg"));
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    UserResponse userResponse = response.body();
                    Toast.makeText(RegisterActivity.this, "User created successfully", Toast.LENGTH_SHORT).show();

                    navigateToLoginActivity();
                } else {
                    handleRegisterError(response);
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateToLoginActivity() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void handleRegisterError(Response<UserResponse> response) {
        try {
            String errorBody = response.errorBody().string();
            Log.e("UserCreationError", "Error body: " + errorBody);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Toast.makeText(RegisterActivity.this, "Failed to create user", Toast.LENGTH_SHORT).show();
    }

    // ... (Existing methods for handling errors and navigation)

    // Email availability check method (commented)
    // private interface EmailAvailabilityCallback {
    //     void onAvailable();
    //     void onUnavailable();
    //     void onError();
    // }

    // private void checkEmailAvailability(String email, EmailAvailabilityCallback callback) {
    //     AuthApi authApi = RetrofitClient.getClient().create(AuthApi.class);
    //     Call<EmailAvailabilityResponse> call = authApi.checkEmailAvailability(new EmailAvailabilityRequest(email));
    //     call.enqueue(new Callback<EmailAvailabilityResponse>() {
    //         @Override
    //         public void onResponse(Call<EmailAvailabilityResponse> call, Response<EmailAvailabilityResponse> response) {
    //             if (response.isSuccessful()) {
    //                 EmailAvailabilityResponse availabilityResponse = response.body();

    //                 // Log email and response for debugging
    //                 Log.d("EmailAvailability", "Email: " + email + ", Response: " + availabilityResponse);

    //                 if (availabilityResponse != null && availabilityResponse.isAvailable()) {
    //                     // Email is available
    //                     callback.onAvailable();
    //                 } else {
    //                     // Email is already in use
    //                     callback.onUnavailable();
    //                 }
    //             } else {
    //                 // Log the error response for debugging
    //                 try {
    //                     String errorBody = response.errorBody().string();
    //                     Log.e("EmailAvailability", "Error response: " + errorBody);
    //                 } catch (IOException e) {
    //                     e.printStackTrace();
    //                 }

    //                 // Handle error in email availability check
    //                 callback.onError();
    //             }
    //         }

    //         @Override
    //         public void onFailure(Call<EmailAvailabilityResponse> call, Throwable t) {
    //             // Log the network error for debugging
    //             Log.e("EmailAvailability", "Network error: " + t.getMessage());

    //             // Handle network error
    //             callback.onError();
    //         }
    //     });
    // }
}
