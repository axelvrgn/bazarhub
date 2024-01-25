package com.example.bazarhub.Api;

import com.example.bazarhub.AuthResponse;
import com.example.bazarhub.CreateUserRequest;
import com.example.bazarhub.Auth.EmailAvailabilityRequest;
import com.example.bazarhub.Auth.EmailAvailabilityResponse;
import com.example.bazarhub.Auth.LoginRequest;
import com.example.bazarhub.Models.User;
import com.example.bazarhub.Models.UserResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface AuthApi {

    @POST("auth/login")
    Call<AuthResponse> loginUser(@Body LoginRequest loginRequest);

    @GET("auth/profile")
    Call<UserResponse> getUserProfile(@Header("Authorization") String authorization);


    @GET("users/{userId}")
    Call<List<UserResponse>> getUserDetails(@Path("userId") String userId);

    @POST("users/is-available")
    Call<EmailAvailabilityResponse> checkEmailAvailability(@Body EmailAvailabilityRequest request);

    @POST("users/")
    Call<UserResponse> createUser(@Body CreateUserRequest request);



    @PUT("users/{id}")
    Call<User> updateUserProfile(@Path("id") int userId, @Body User user);


    @POST("auth/logout")
    Call<Void> logoutUser(@Header("Authorization") String authorization);

}

