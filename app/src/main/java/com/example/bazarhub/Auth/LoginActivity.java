package com.example.bazarhub.Auth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bazarhub.AdminPanelActivity;
import com.example.bazarhub.Api.AuthApi;
import com.example.bazarhub.Api.RetrofitClient;
import com.example.bazarhub.AuthResponse;
import com.example.bazarhub.MainActivity;
import com.example.bazarhub.Models.UserResponse;
import com.example.bazarhub.ProfileActivity;
import com.example.bazarhub.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private Button loginButton;
    private EditText emailInput, passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        loginButton = findViewById(R.id.login_btn);
        emailInput = findViewById(R.id.login_email_input);
        passwordInput = findViewById(R.id.login_password_input);

        TextView registerNowText = findViewById(R.id.register_now_text);
        registerNowText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToRegisterActivity();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
    }

    private void navigateToRegisterActivity() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }


    private void navigateToProfileActivity(UserResponse userResponse) {
        Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);


        intent.putExtra("userProfile", (CharSequence) userResponse);


        startActivity(intent);
        finish();
    }

    private void navigateBasedOnRole(String role) {
        if (role != null && role.equalsIgnoreCase("admin")) {
            saveUserRole("admin");
            Intent intent = new Intent(LoginActivity.this, AdminPanelActivity.class);
            startActivity(intent);
            finish();
        } else {
            saveUserRole("user");
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void saveUserRole(String role) {
        // Save the user's role in SharedPreferences
        SharedPreferences preferences = getSharedPreferences("user_data", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("user_role", role);
        editor.apply();
    }

    private void saveUserDetails(String email, String name, String role, String avatar, int idUser) {
        // Save user details in SharedPreferences
        SharedPreferences preferences = getSharedPreferences("user_data", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("user_email", email);
        editor.putString("user_name", name);
        editor.putString("user_role", role);
        editor.putString("user_avatar", avatar);
        editor.putString("user_id", String.valueOf(idUser));
        editor.apply();
    }

    private void fetchUserProfile(String accessToken) {
        AuthApi authApi = RetrofitClient.getClient().create(AuthApi.class);
        Call<UserResponse> call = authApi.getUserProfile("Bearer " + accessToken);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    UserResponse userResponse = response.body();
                    if (userResponse != null) {
                        // Print user email and name to log
                        Log.d("LoginActivity", "User Email: " + userResponse.getEmail());
                        Log.d("LoginActivity", "User Name: " + userResponse.getName());

                        saveUserDetails(userResponse.getEmail(), userResponse.getName(), userResponse.getRole(),userResponse.getAvatar(),userResponse.getId());

                        String role = userResponse.getRole();
                        navigateBasedOnRole(role);
                    } else {
                        showLoginError("User profile body is null");
                    }
                } else {
                    showLoginError("Failed to fetch user profile: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                // Handle network error
                showNetworkError();
            }
        });
    }

    private void loginUser() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        Log.d("LoginActivity", "Email: " + email);
        Log.d("LoginActivity", "Password: " + password);

        AuthApi authApi = RetrofitClient.getClient().create(AuthApi.class);
        Call<AuthResponse> call = authApi.loginUser(new LoginRequest(email, password));
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful()) {
                    AuthResponse authResponse = response.body();
                    if (authResponse != null) {
                        // Print access token to log
                        Log.d("LoginActivity", "Access Token: " + authResponse.getAccessToken());

                        // Fetch user profile to get the role and save user details
                        fetchUserProfile(authResponse.getAccessToken());
                    } else {
                        showLoginError("Response body is null");
                    }
                } else {
                    int statusCode = response.code();
                    if (statusCode == 401) {
                        showLoginError("Invalid credentials. Please check your email and password.");
                    } else if (statusCode == 500) {
                        showLoginError("Server error. Please try again later.");
                    } else {
                        showLoginError("Login failed. Please try again.");
                    }
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                showNetworkError();
            }
        });
    }

    private void showLoginError(String message) {
        Toast.makeText(LoginActivity.this, "Login Error: " + message, Toast.LENGTH_SHORT).show();
    }

    private void showNetworkError() {
        Toast.makeText(LoginActivity.this, "Network error. Please check your internet connection.", Toast.LENGTH_SHORT).show();
    }
}
