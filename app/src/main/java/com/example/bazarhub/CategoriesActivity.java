package com.example.bazarhub;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.bazarhub.Adapters.CategoriesAdapter;
import com.example.bazarhub.Api.ApiService;
import com.example.bazarhub.Api.RetrofitClient;
import com.example.bazarhub.Models.Category;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.content.SharedPreferences;

public class CategoriesActivity extends AppCompatActivity implements CategoriesAdapter.OnCategoryClickListener {

    private RecyclerView recyclerView;
    private CategoriesAdapter categoriesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {





        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        ToolbarHelper.setupToolbar(this);
        recyclerView = findViewById(R.id.recyclerViewCategories);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        categoriesAdapter = new CategoriesAdapter();
        categoriesAdapter.setOnCategoryClickListener(this);
        categoriesAdapter.setUserRole(getUserRole()); // Set the user role
        recyclerView.setAdapter(categoriesAdapter);

        loadCategories();
    }

    private void loadCategories() {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

        Call<List<Category>> call = apiService.getCategories();
        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Category> categories = response.body();
                    categoriesAdapter.setCategories(categories);
                } else {
                    Toast.makeText(CategoriesActivity.this, "Failed to fetch categories", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Toast.makeText(CategoriesActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onCategoryClick(Category category) {
        // Pass the selected category to the ProductsActivity
        Intent intent = new Intent(this, ListProductsActivity.class);
        intent.putExtra("selectedCategory", category);
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(Category category) {
        // Call the API to delete the category
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<Boolean> call = apiService.deleteCategory(category.getId());

        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful() && response.body() != null && response.body()) {
                    // Show a message on successful deletion
                    Toast.makeText(CategoriesActivity.this, "Category deleted successfully", Toast.LENGTH_SHORT).show();

                    loadCategories();
                } else {
                    Toast.makeText(CategoriesActivity.this, "Failed to delete category", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(CategoriesActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onEditClick(Category category) {
        Intent intent = new Intent(this, EditCategoryActivity.class);

        intent.putExtra("category", category);

        startActivity(intent);
    }

    private String getUserRole() {
        // Retrieve user role from SharedPreferences
        SharedPreferences preferences = getSharedPreferences("user_data", MODE_PRIVATE);
        return preferences.getString("user_role", "");
    }
}
