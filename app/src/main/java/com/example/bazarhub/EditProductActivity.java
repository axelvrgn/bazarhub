package com.example.bazarhub;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bazarhub.Api.ApiService;
import com.example.bazarhub.Api.RetrofitClient;
import com.example.bazarhub.Models.Category;
import com.example.bazarhub.Models.Product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProductActivity extends AppCompatActivity {

    private Product product;
    private EditText etName, etImageURL1, etPrice, etDescription;
    private Spinner spinnerCategory;
    private Button btnUpdateProduct;

    private ApiService apiService;
    private List<Category> categories;
    private ArrayAdapter<Category> categoryAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        apiService = RetrofitClient.getClient().create(ApiService.class);

        etName = findViewById(R.id.etName1);
        etImageURL1 = findViewById(R.id.etImageURL1);
        etPrice = findViewById(R.id.etPrice1);
        etDescription = findViewById(R.id.etDescription1);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        btnUpdateProduct = findViewById(R.id.btnUpdateProduct);

        Intent intent = getIntent();

        if (intent.hasExtra("product")) {
            product = intent.getParcelableExtra("product");
            if (product != null) {
                // Populate the UI with the current product data
                populateUI(product);

                // Set up the update button click listener
                btnUpdateProduct.setOnClickListener(v -> onUpdateClick());
            } else {
                Log.e("EditProductActivity", "Received null product from intent");
            }
        } else {
            Log.e("EditProductActivity", "Intent does not contain 'product' extra");
        }

        categories = new ArrayList<>();
        categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(categoryAdapter);

        // Load categories
        loadCategories();
    }

    private void populateUI(Product product) {
        etName.setText(product.getTitle());
        List<String> images = product.getImages();
        if (images != null && images.size() >= 1) {
            etImageURL1.setText(images.get(0));
            // You can add similar lines for etImageURL2, etImageURL3, and etImageURL4 if needed
        }
        etPrice.setText(String.valueOf(product.getPrice()));
        etDescription.setText(product.getDescription());
    }

    private void onUpdateClick() {
        String updatedName = etName.getText().toString();
        double updatedPrice = Double.parseDouble(etPrice.getText().toString());
        String updatedDescription = etDescription.getText().toString();
        String updatedImageURL1 = etImageURL1.getText().toString();
        // You can add similar lines for etImageURL2, etImageURL3, and etImageURL4 if needed

        // Get the selected category ID from the spinner
        Category selectedCategory = (Category) spinnerCategory.getSelectedItem();
        int updatedCategoryId = selectedCategory != null ? selectedCategory.getId() : 0;

        // Update the existing product object with the new data
        product.setTitle(updatedName);
        product.setPrice(updatedPrice);
        product.setDescription(updatedDescription);
        product.setImages(Collections.singletonList(updatedImageURL1));
        // You can add similar lines for updating images if you have more image URLs
        product.setCategoryId(updatedCategoryId);

        // Make an API call to update the product on the server
        apiService.updateProduct(product.getId(), product).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Handle the successful update
                    Product updatedProduct = response.body();
                    // Update the UI or take any necessary actions

                    Toast.makeText(EditProductActivity.this, "Product updated successfully", Toast.LENGTH_SHORT).show();
                } else {
                    // Handle the case where the update failed
                    Log.e("EditProductActivity", "Failed to update product. Response code: " + response.code());
                    Toast.makeText(EditProductActivity.this, "Failed to update product", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                // Handle network errors or other failures
                Log.e("EditProductActivity", "Error: " + t.getMessage());
                Toast.makeText(EditProductActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadCategories() {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

        Call<List<Category>> call = apiService.getCategories();
        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Category> fetchedCategories = response.body();

                    // Clear existing categories and add the fetched ones
                    categories.clear();
                    categories.addAll(fetchedCategories);

                    categoryAdapter.notifyDataSetChanged();

                    // Set the selected category in the spinner
                    setSpinnerSelection(product.getCategoryId());

                } else {
                    Toast.makeText(EditProductActivity.this, "Failed to fetch categories", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Toast.makeText(EditProductActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private int getProductCategoryIndex(int categoryId) {
        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).getId() == categoryId) {
                return i;
            }
        }
        return 0; // Default to the first item if not found
    }



    private void setSpinnerSelection(int categoryId) {
        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).getId() == categoryId) {
                spinnerCategory.setSelection(i);
                break;
            }
        }
    }


}
