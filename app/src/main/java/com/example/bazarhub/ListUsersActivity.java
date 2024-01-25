package com.example.bazarhub;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bazarhub.Adapters.UserAdapter;
import com.example.bazarhub.Api.ApiService;
import com.example.bazarhub.Api.RetrofitClient;
import com.example.bazarhub.Models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListUsersActivity extends AppCompatActivity implements UserAdapter.OnUserClickListener {

    private RecyclerView revUsers;

    private List<User> originalUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        ToolbarHelper.setupToolbar(this);

        loadAllUsers();

    }

    private void loadAllUsers() {
        ApiService api = RetrofitClient.getClient().create(ApiService.class);
        Call<List<User>> call = api.getUsers();

        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()) {
                    List<User> users = response.body();
                    originalUsers = users;
                    showUsers(users);
                } else {
                    Toast.makeText(ListUsersActivity.this, "Failed to fetch users", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(ListUsersActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showUsers(List<User> users) {
        int spanCount = 2;
        revUsers = findViewById(R.id.revUsers);
        revUsers.setLayoutManager(new GridLayoutManager(ListUsersActivity.this, spanCount));

        UserAdapter userAdapter = new UserAdapter(ListUsersActivity.this, users);
        userAdapter.setOnUserClickListener(this);
        userAdapter.setUserRole(getUserRole());
        revUsers.setAdapter(userAdapter);
    }


    @Override
    public void onDeleteClick(User user) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<Boolean> call = apiService.deleteUser(user.getId());

        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful() && response.body() != null && response.body()) {
                    Toast.makeText(ListUsersActivity.this, "User deleted successfully", Toast.LENGTH_SHORT).show();

                    loadAllUsers();
                } else {
                    Toast.makeText(ListUsersActivity.this, "Failed to delete user", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(ListUsersActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getUserRole() {
        SharedPreferences preferences = getSharedPreferences("user_data", MODE_PRIVATE);
        return preferences.getString("user_role", "");
    }

}
