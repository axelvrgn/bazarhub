package com.example.bazarhub;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.bazarhub.Models.Product;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageView ivProduct, imageEdit, imageDelete, imageFavoris;
    private TextView tvName, tvDescription, tvPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        ToolbarHelper.setupToolbar(this);
        ivProduct = findViewById(R.id.ivProductDetail);
        tvName = findViewById(R.id.tvNameDetail);
        tvDescription = findViewById(R.id.tvDescriptionDetail);
        tvPrice = findViewById(R.id.tvPriceDetail);
        imageEdit = findViewById(R.id.imageEdit);
        imageDelete = findViewById(R.id.imageDelete);
        imageFavoris = findViewById(R.id.imageFavoris);

        if (getIntent().hasExtra("product")) {
            Product product = getIntent().getParcelableExtra("product");
            if (product != null) {
                Glide.with(this)
                        .load(product.getImages().get(0))
                        .centerCrop()
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .into(ivProduct);

                tvName.setText(product.getTitle());
                tvDescription.setText(product.getDescription());
                tvPrice.setText("Price: " + product.getPrice());

                imageEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        navigateToEditProduct(product);
                    }
                });

                imageDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onDeleteProduct(product.getId());
                    }
                });

                imageFavoris.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onAddProductToFavoris(product.getId());
                    }
                });

                String userRole = getUserRole();
                if ("admin".equalsIgnoreCase(userRole)) {
                    imageEdit.setVisibility(View.VISIBLE);
                    imageDelete.setVisibility(View.VISIBLE);
                } else {
                    imageEdit.setVisibility(View.GONE);
                    imageDelete.setVisibility(View.GONE);
                }
            }
        }
    }

    private void navigateToEditProduct(Product product) {
        Intent intent = new Intent(ProductDetailActivity.this, EditProductActivity.class);
        intent.putExtra("product", product);
        startActivity(intent);
    }

    private void onAddProductToFavoris(int productId) {
        Toast.makeText(ProductDetailActivity.this, "Product added successfully to fav", Toast.LENGTH_SHORT).show();
    }

    private void onDeleteProduct(int productId) {

        Toast.makeText(ProductDetailActivity.this, "Product deleted successfully", Toast.LENGTH_SHORT).show();
    }

    private String getUserRole() {
        SharedPreferences preferences = getSharedPreferences("user_data", MODE_PRIVATE);
        return preferences.getString("user_role", "");
    }
}

