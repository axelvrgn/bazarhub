package com.example.bazarhub;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
public class AdminPanelActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);

        ToolbarHelper.setupToolbar(this);

        findViewById(R.id.btnToAddCategory).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminPanelActivity.this, AddCategoryActivity.class));
            }
        });

        findViewById(R.id.btnToViewCategories).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminPanelActivity.this, CategoriesActivity.class));
            }
        });


        findViewById(R.id.btnToViewProducts).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminPanelActivity.this, ListProductsActivity.class));
            }
        });


        findViewById(R.id.btnToAddProduct).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminPanelActivity.this, AddProductActivity.class));
            }
        });

        findViewById(R.id.btnToViewUsers).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminPanelActivity.this, ListUsersActivity.class));
            }
        });




    }

}
