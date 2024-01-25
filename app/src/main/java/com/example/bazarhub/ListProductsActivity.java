package com.example.bazarhub;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bazarhub.Adapters.ProductAdapter;
import com.example.bazarhub.Api.ApiService;
import com.example.bazarhub.Api.RetrofitClient;
import com.example.bazarhub.Models.Category;
import com.example.bazarhub.Models.Product;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListProductsActivity extends AppCompatActivity implements ProductAdapter.OnProductClickListener {

    private RecyclerView revProducts;
    private SearchView searchView;
    private EditText etMinPrice, etMaxPrice;
    private FavoritesDataSource favoritesDataSource;

    private List<Product> originalProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        ToolbarHelper.setupToolbar(this);
        // Initialize views
        searchView = findViewById(R.id.searchView);
        etMinPrice = findViewById(R.id.etMinPrice);
        etMaxPrice = findViewById(R.id.etMaxPrice);


        Category selectedCategory = (Category) getIntent().getSerializableExtra("selectedCategory");

        if (selectedCategory != null) {

            int categoryId = selectedCategory.getId();
            loadProductsByCategory(categoryId);
        } else {

            loadAllProducts();
        }


        setUpFilterListeners();
    }

    private void setUpFilterListeners() {
        // Set up search filter
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterProducts(query, etMinPrice.getText().toString(), etMaxPrice.getText().toString());
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterProducts(newText, etMinPrice.getText().toString(), etMaxPrice.getText().toString());
                return true;
            }
        });

        // Set up price range filter
        etMinPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                filterProducts(searchView.getQuery().toString(), editable.toString(), etMaxPrice.getText().toString());
            }
        });

        etMaxPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                filterProducts(searchView.getQuery().toString(), etMinPrice.getText().toString(), editable.toString());
            }
        });
    }

    private void filterProducts(String title, String minPrice, String maxPrice) {
        List<Product> filteredProducts = new ArrayList<>(originalProducts);

        filteredProducts = filterByTitle(filteredProducts, title);

        filteredProducts = filterByPriceRange(filteredProducts, minPrice, maxPrice);

        showProducts(filteredProducts);
    }

    private List<Product> filterByTitle(List<Product> products, String title) {
        List<Product> filteredList = new ArrayList<>();
        if (title.isEmpty()) {
            filteredList.addAll(products);
        } else {
            for (Product product : products) {
                if (product.getTitle().toLowerCase().contains(title.toLowerCase())) {
                    filteredList.add(product);
                }
            }
        }
        return filteredList;
    }

    private List<Product> filterByPriceRange(List<Product> products, String minPrice, String maxPrice) {
        List<Product> filteredList = new ArrayList<>();
        double min = minPrice.isEmpty() ? Double.MIN_VALUE : Double.parseDouble(minPrice);
        double max = maxPrice.isEmpty() ? Double.MAX_VALUE : Double.parseDouble(maxPrice);

        for (Product product : products) {
            double productPrice = product.getPrice();
            if (productPrice >= min && productPrice <= max) {
                filteredList.add(product);
            }
        }

        return filteredList;
    }

    private void loadProductsByCategory(int categoryId) {
        ApiService api = RetrofitClient.getClient().create(ApiService.class);
        Call<List<Product>> call = api.getProductsByCategory(categoryId);

        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    List<Product> products = response.body();
                    originalProducts = products; // Store the original list for filtering
                    showProducts(products);
                } else {
                    Toast.makeText(ListProductsActivity.this, "Failed to fetch products", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(ListProductsActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadAllProducts() {
        ApiService api = RetrofitClient.getClient().create(ApiService.class);
        Call<List<Product>> call = api.getProducts();

        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    List<Product> products = response.body();
                    originalProducts = products; // Store the original list for filtering
                    showProducts(products);
                } else {
                    Toast.makeText(ListProductsActivity.this, "Failed to fetch products", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(ListProductsActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showProducts(List<Product> products) {
        int spanCount = 2;
        revProducts = findViewById(R.id.revProducts);
        revProducts.setLayoutManager(new GridLayoutManager(ListProductsActivity.this, spanCount));

        ProductAdapter productAdapter = new ProductAdapter(ListProductsActivity.this, products);
        productAdapter.setOnProductClickListener(this);

        revProducts.setAdapter(productAdapter);
    }

    @Override
    public void onProductClick(Product product) {
        Intent intent = new Intent(this, ProductDetailActivity.class);
        intent.putExtra("product", product);
        startActivity(intent);
    }


}
