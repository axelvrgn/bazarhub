package com.example.bazarhub.Api;

import com.example.bazarhub.AddCategoryRequest;
import com.example.bazarhub.Models.Category;
import com.example.bazarhub.Models.Product;
import com.example.bazarhub.UpdateCategoryRequest;
import com.example.bazarhub.Models.User;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @GET("categories")
    Call<List<Category>> getCategories();

    @GET("categories")
    Call<List<Category>> getCategoriesPaginated(@Query("offset") int offset ,@Query("limit") int limit);

    @GET("categories/{id}")
    Call<Category> getCategory(@Path("id") int categoryId);

    @POST("categories/")
    Call<Category> addCategory(@Body AddCategoryRequest addCategoryRequest);


    @PUT("categories/{id}")
    Call<Category> updateCategory(@Path("id") int categoryId, @Body UpdateCategoryRequest request);



    @DELETE("categories/{id}")
    Call<Boolean> deleteCategory(@Path("id") int categoryId);


    @GET("products")
    Call<List<Product>> getProducts();

    @GET("products")
    Call<List<Product>> getProductsPaginated(@Query("offset") int offset ,@Query("limit") int limit);

    @GET("products")
    Call<List<Product>> getProductsByCategory(@Query("categoryId") int categoryId);


    @GET("users")
    Call<List<User>> getUsers();

    @DELETE("users/{id}")
    Call<Boolean> deleteUser(@Path("id") int userId);
    @GET("products/{id}")
    Call<Product> getProductById(@Path("id") int productId);

    @POST("products")
    Call<ResponseBody> createProduct(@Body Product product);

    @PUT("products/{id}")
    Call<Product> updateProduct(@Path("id") int productId, @Body Product updatedProduct);


    @DELETE("products/{id}")
    Call<Boolean> deleteProduct(@Path("id") int productId);





}
