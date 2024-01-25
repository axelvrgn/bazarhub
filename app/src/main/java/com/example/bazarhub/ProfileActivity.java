package com.example.bazarhub;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.bazarhub.Api.AuthApi;
import com.example.bazarhub.Api.RetrofitClient;
import com.example.bazarhub.Models.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private EditText editTextName, editTextEmail, editTextPassword, editTextAvatar;
    private ImageView imageViewAvatar;
    private Button buttonUpdateProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);
        ToolbarHelper.setupToolbar(this);

        // Initialize views
        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextAvatar = findViewById(R.id.editTextAvatar);
        imageViewAvatar = findViewById(R.id.imageViewAvatar);
        buttonUpdateProfile = findViewById(R.id.buttonUpdateProfile);

        // Load user role
        loadUserRole();

        // Set OnClickListener for the "Update Profile" button
        buttonUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Call method to update user profile
                updateUserProfile();
            }
        });
    }

    private void loadUserRole() {
        // Retrieve user details from SharedPreferences
        SharedPreferences preferences = getSharedPreferences("user_data", MODE_PRIVATE);
        String userName = preferences.getString("user_name", "");
        String userEmail = preferences.getString("user_email", "");
        String userPassword = preferences.getString("user_password", "");
        String userAvatar = preferences.getString("user_avatar", "");
        String userId = preferences.getString("user_id", "");


        // Log the user details
        Log.d("ProfileActivity", "User Name: " + userName);
        Log.d("ProfileActivity", "User Email: " + userEmail);
        Log.d("ProfileActivity", "User Password: " + userPassword);
        Log.d("ProfileActivity", "User Avatar: " + userAvatar);
        Log.d("ProfileActivity", "User Avatar: " + userId);

        // Populate the EditText and ImageView with user data
        editTextName.setText(userName);
        editTextEmail.setText(userEmail);
        editTextPassword.setText(userPassword);
        editTextAvatar.setText(userAvatar);

        Glide.with(this)
                .load(userAvatar)
                .centerCrop()
                .placeholder(R.drawable.ic_person)
                .into(imageViewAvatar);
    }

    private void updateUserProfile() {
        // Retrieve new values from EditText fields
        String newName = editTextName.getText().toString().trim();
        String newPassword = editTextPassword.getText().toString().trim();
        String newAvatar = editTextAvatar.getText().toString().trim();
        String newEmail = editTextEmail.getText().toString().trim();

        // Retrieve user details from SharedPreferences
        SharedPreferences preferences = getSharedPreferences("user_data", MODE_PRIVATE);
        int userIdInt = Integer.parseInt(preferences.getString("user_id", ""));

        // Create an instance of the User model
        User updatedUser = new User();

        // Conditionally set the fields that have changed
        if (!newName.isEmpty()) {
            updatedUser.setName(newName);
        }

        if (!newEmail.isEmpty()) {
            updatedUser.setEmail(newEmail);
        }

        if (!newPassword.isEmpty()) {
            updatedUser.setPassword(newPassword);
        }

        if (!newAvatar.isEmpty()) {
            updatedUser.setAvatar(newAvatar);
        }

        AuthApi authApi = RetrofitClient.getClient().create(AuthApi.class);
        Call<User> call = authApi.updateUserProfile(userIdInt, updatedUser);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    // Handle success
                    showUpdateSuccess();
                    // Refresh the activity
                    recreate();
                } else {
                    // Handle failure
                    showUpdateError("Failed to update user profile: " + response.message());
                    Log.e("ProfileActivity", "Failed to update user profile. Response code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                // Handle network error
                showUpdateError("Network error. Please check your internet connection.");
                Log.e("ProfileActivity", "Failed to update user profile. Error: " + t.getMessage(), t);
            }
        });
    }





    private void saveUserDetails(String email, String name, String password, String avatar) {
        // Save the user details in SharedPreferences
        SharedPreferences preferences = getSharedPreferences("user_data", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("user_email", email);
        editor.putString("user_name", name);
        editor.putString("user_password", password);
        editor.putString("user_avatar", avatar);
        editor.apply();
    }

    private void showUpdateSuccess() {
        Toast.makeText(ProfileActivity.this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
    }

    private void showUpdateError(String message) {
        Toast.makeText(ProfileActivity.this, "Update Error: " + message, Toast.LENGTH_SHORT).show();
    }
}
