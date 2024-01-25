package com.example.bazarhub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.bazarhub.Adapters.CategoriesAdapter;
import com.example.bazarhub.Adapters.ProductAdapter;
import com.example.bazarhub.Api.ApiService;
import com.example.bazarhub.Api.RetrofitClient;
import com.example.bazarhub.Models.Category;
import com.example.bazarhub.Models.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements ProductAdapter.OnProductClickListener, CategoriesAdapter.OnCategoryClickListener {

    private RecyclerView revProducts;

    private RecyclerView revCategories;

    private CategoriesAdapter categoriesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ToolbarHelper.setupToolbar(this);

        RecyclerView recyclerViewProducts = findViewById(R.id.recyclerViewProducts);
        recyclerViewProducts.setLayoutManager(new GridLayoutManager(this, 2));

        RecyclerView recyclerViewCategories = findViewById(R.id.recyclerViewCategories);
        recyclerViewCategories.setLayoutManager(new GridLayoutManager(this, 3));
        categoriesAdapter = new CategoriesAdapter();
        categoriesAdapter.setOnCategoryClickListener(this);
        recyclerViewCategories.setAdapter(categoriesAdapter);

        loadAllProducts();

        loadCategories();



        Button btnProducts = findViewById(R.id.btnProducts);

        Button btnCategories = findViewById(R.id.btnCategories);


        btnProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ListProductsActivity.class));
            }
        });

        btnCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CategoriesActivity.class));
            }
        });


    }

    private void loadAllProducts() {
        ApiService api = RetrofitClient.getClient().create(ApiService.class);
        Call<List<Product>> call = api.getProductsPaginated(0, 10);

        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    List<Product> products = response.body();
                    showProducts(products);
                } else {
                    Toast.makeText(MainActivity.this, "Failed to fetch products", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showProducts(List<Product> products) {
        int spanCount = 2;
        revProducts = findViewById(R.id.recyclerViewProducts);
        revProducts.setLayoutManager(new GridLayoutManager(MainActivity.this, spanCount));

        ProductAdapter productAdapter = new ProductAdapter(MainActivity.this, products);
        productAdapter.setOnProductClickListener(this);

        revProducts.setAdapter(productAdapter);
    }

    @Override
    public void onProductClick(Product product) {
        // Ouvrir la page de détails avec le produit sélectionné
        Intent intent = new Intent(this, ProductDetailActivity.class);
        intent.putExtra("product", product);
        startActivity(intent);
    }



    private void loadCategories() {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

        Call<List<Category>> call = apiService.getCategoriesPaginated(0, 3);
        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Category> categories = response.body();
                    categoriesAdapter.setCategories(categories);
                } else {
                    Toast.makeText(MainActivity.this, "Failed to fetch categories", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(MainActivity.this, "Category deleted successfully", Toast.LENGTH_SHORT).show();

                    loadCategories();
                } else {
                    Toast.makeText(MainActivity.this, "Failed to delete category", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                // Handle network errors or other failures
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void onEditClick(Category category) {
        Intent intent = new Intent(this, EditCategoryActivity.class);

        intent.putExtra("category", category);

        startActivity(intent);
    }

}

