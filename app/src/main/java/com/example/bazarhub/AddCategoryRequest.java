package com.example.bazarhub;

import com.google.gson.annotations.SerializedName;

public class AddCategoryRequest {

    @SerializedName("name")
    private String name;

    @SerializedName("image")
    private String image;

    public AddCategoryRequest(String name, String image) {
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
