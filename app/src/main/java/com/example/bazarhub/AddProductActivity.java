package com.example.bazarhub;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bazarhub.Api.ApiService;
import com.example.bazarhub.Api.RetrofitClient;
import com.example.bazarhub.Models.Category;
import com.example.bazarhub.Models.Product;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

public class AddProductActivity extends AppCompatActivity {

    private EditText etName, etImageURL, etPrice, etDescription;
    private Spinner spinnerCategory;
    private List<Category> categories;
    private ArrayAdapter<Category> categoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        etName = findViewById(R.id.etName1);
        etImageURL = findViewById(R.id.etImageURL1);

        etPrice = findViewById(R.id.etPrice1);
        etDescription = findViewById(R.id.etDescription1);
        spinnerCategory = findViewById(R.id.spinnerCategory);

        categories = new ArrayList<>();
        categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(categoryAdapter);

        // Load categories
        loadCategories();

        findViewById(R.id.btnAddProduct).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Retrieve the selected category
                Category selectedCategory = (Category) spinnerCategory.getSelectedItem();

                // Ensure a category is selected
                if (selectedCategory != null) {
                    int categoryId = selectedCategory.getId();
                    String title = etName.getText().toString().trim();
                    double price = Double.parseDouble(etPrice.getText().toString().trim());
                    String description = etDescription.getText().toString().trim();
                    String imageURL = etImageURL.getText().toString().trim();

                    // Create a Product object with the categoryId
                    ArrayList<String> imageUrls = new ArrayList<>();
                    imageUrls.add(imageURL);
                    Product newProduct = new Product(title, price, description, categoryId, imageUrls);

                    // Make the API call with the newProduct object
                    addProduct(newProduct);
                } else {
                    Toast.makeText(AddProductActivity.this, "No category selected", Toast.LENGTH_SHORT).show();
                }
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

                    // Call populateSpinner here
                    populateSpinner(categories);
                } else {
                    Toast.makeText(AddProductActivity.this, "Failed to fetch categories", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Toast.makeText(AddProductActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateSpinner(List<Category> categories) {
        List<String> categoryNames = new ArrayList<>();
        for (Category category : categories) {
            categoryNames.add(category.getName());
        }

        ArrayAdapter<Category> spinnerAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                categories
        );

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(spinnerAdapter);
    }

    private void addProduct(Product newProduct) {
        ApiService api = RetrofitClient.getClient().create(ApiService.class);
        Call<ResponseBody> call = api.createProduct(newProduct);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AddProductActivity.this, "Successfully Added!", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        Toast.makeText(AddProductActivity.this, "Failed to add product. Error body: " + errorBody, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(AddProductActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(AddProductActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        etName.getText().clear();
        etImageURL.getText().clear();

        etPrice.getText().clear();
        etDescription.getText().clear();
    }
}
