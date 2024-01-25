package com.example.bazarhub;

import com.google.gson.annotations.SerializedName;

public class UpdateProfileRequest {

    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("email")
    private String email;
    @SerializedName("password")
    private String password;

    @SerializedName("avatar")
    private String avatar;


    public UpdateProfileRequest() {
    }

    public UpdateProfileRequest(int id, String name, String password, String avatar,String email) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.avatar = avatar;
        this.email = email;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
