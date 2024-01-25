package com.example.bazarhub;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.bazarhub.Api.ApiService;
import com.example.bazarhub.Api.RetrofitClient;
import com.example.bazarhub.Auth.LoginActivity;
import com.example.bazarhub.Models.Category;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCategoryActivity extends AppCompatActivity {

    private EditText categoryNameEditText, categoryImageEditText;
    private Button addCategoryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);
        ToolbarHelper.setupToolbar(this);
        categoryNameEditText = findViewById(R.id.editTextCategoryName);
        categoryImageEditText = findViewById(R.id.editTextCategoryImage);
        addCategoryButton = findViewById(R.id.buttonAddCategory);

        addCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addCategory(
                        categoryNameEditText.getText().toString().trim(),
                        categoryImageEditText.getText().toString().trim()
                );
            }
        });
    }

    private void addCategory(String categoryName, String categoryImage) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

        AddCategoryRequest addCategoryRequest = new AddCategoryRequest(categoryName, categoryImage);

        if (categoryImage == null || categoryImage.trim().isEmpty()) {
            addCategoryRequest.setImage(null);
        }

        Call<Category> call = apiService.addCategory(addCategoryRequest);
        call.enqueue(new Callback<Category>() {
            @Override
            public void onResponse(Call<Category> call, Response<Category> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Category addedCategory = response.body();
                    Toast.makeText(AddCategoryActivity.this, "Category added: " + addedCategory.getName(), Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(AddCategoryActivity.this, CategoriesActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    if (response.code() == 400) {
                        showAddCategoryError();
                    } else if (response.code() == 401) {
                        showUnauthorizedError();
                    } else {
                        showAddCategoryError();

                        // Print response code and body for debugging
                        Log.e("API_RESPONSE", "Code: " + response.code() + ", Body: " + response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<Category> call, Throwable t) {
                showNetworkError();
            }
        });
    }

    private void showAddCategoryError() {
        // Display a message to the user indicating category addition failure
        Toast.makeText(this, "Failed to add category. Please try again.", Toast.LENGTH_SHORT).show();
    }
    private void showUnauthorizedError() {

        Toast.makeText(this, "Unauthorized access. Please login again.", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void showNetworkError() {

        Toast.makeText(this, "Network error. Please check your internet connection and try again.", Toast.LENGTH_SHORT).show();
    }
}

