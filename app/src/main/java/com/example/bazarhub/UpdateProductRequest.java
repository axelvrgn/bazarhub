package com.example.bazarhub;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UpdateProductRequest {

    @SerializedName("title")
    private String title;

    @SerializedName("price")
    private double price;

    @SerializedName("description")
    private String description;

    @SerializedName("images")
    private List<String> images;

    @SerializedName("category_id")
    private int categoryId;

    public UpdateProductRequest(String title, double price, String description, List<String> images, int categoryId) {
        this.title = title;
        this.price = price;
        this.description = description;
        this.images = images;
        this.categoryId = categoryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
}
