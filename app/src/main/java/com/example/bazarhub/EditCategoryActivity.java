package com.example.bazarhub;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bazarhub.Api.ApiService;
import com.example.bazarhub.Api.RetrofitClient;
import com.example.bazarhub.Models.Category;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EditCategoryActivity extends AppCompatActivity {

    private Category category;
    private EditText editTextCategoryName;
    private EditText editTextCategoryImage;
    private Button updateButton;

    private ApiService apiService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category);
        ToolbarHelper.setupToolbar(this);
        apiService = RetrofitClient.getClient().create(ApiService.class);

        editTextCategoryName = findViewById(R.id.editTextCategoryName);
        editTextCategoryImage = findViewById(R.id.editTextCategoryImage);
        updateButton = findViewById(R.id.updateButton);

        Intent intent = getIntent();

        if (intent.hasExtra("category")) {
            category = (Category) intent.getSerializableExtra("category");
            if (category != null) {
                editTextCategoryName.setText(category.getName());
                editTextCategoryImage.setText(category.getImage());

                updateButton.setOnClickListener(v -> onUpdateClick());
            } else {
                Log.e("EditCategoryActivity", "Received null category from intent");
            }
        } else {
            Log.e("EditCategoryActivity", "Intent does not contain 'category' extra");
        }
    }


    private void onUpdateClick() {
        String updatedName = editTextCategoryName.getText().toString();
        String updatedImage = editTextCategoryImage.getText().toString();
        category.setName(updatedName);
        category.setImage(updatedImage);

        UpdateCategoryRequest updateRequest = new UpdateCategoryRequest(updatedName, updatedImage);

        apiService.updateCategory(category.getId(), updateRequest).enqueue(new Callback<Category>() {
            @Override
            public void onResponse(Call<Category> call, Response<Category> response) {
                if (response.isSuccessful() && response.body() != null) {
                    category = response.body();

                    editTextCategoryName.setText(category.getName());
                    editTextCategoryImage.setText(category.getImage());

                    Toast.makeText(EditCategoryActivity.this, "Category updated successfully", Toast.LENGTH_SHORT).show();

                    finish();
                    startActivity(getIntent());

                    refreshCategoriesActivity();
                } else {
                    // Handle update failure, show an error message or log the error
                    Toast.makeText(EditCategoryActivity.this, "Failed to update category", Toast.LENGTH_SHORT).show();
                }
            }

            private void refreshCategoriesActivity() {
                Intent intent = new Intent(EditCategoryActivity.this, CategoriesActivity.class);
                startActivity(intent);
                finish();
            }


            @Override
            public void onFailure(Call<Category> call, Throwable t) {
                Toast.makeText(EditCategoryActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}