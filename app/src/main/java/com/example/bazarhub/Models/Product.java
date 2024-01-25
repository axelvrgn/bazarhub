package com.example.bazarhub.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


import java.util.List;


public class Product implements Parcelable {
    private int id;
    private String title;
    private double price;
    private String description;
    private int categoryId; // Adjusted to store categoryId instead of Category object

    @SerializedName("images")
    private List<String> images;

    // Constructors, getters, and setters

    public Product(String title, double price, String description, int categoryId, List<String> images) {
        this.title = title;
        this.price = price;
        this.description = description;
        this.categoryId = categoryId;
        this.images = images;
    }

    protected Product(Parcel in) {
        id = in.readInt();
        title = in.readString();
        price = in.readDouble();
        description = in.readString();
        categoryId = in.readInt();
        images = in.createStringArrayList();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeDouble(price);
        dest.writeString(description);
        dest.writeInt(categoryId);
        dest.writeStringList(images);
    }

    // Getter and Setter methods for each field

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public List<String> getImages() {
        return images;
    }
    public void setImages(List<String> images) {
        this.images = images;
    }

}
