package com.example.bazarhub.Models;

import java.io.Serializable;

public class Category implements Serializable {
    private int id;
    private String name;
    private String image;

    // Constructor
    public Category(int id, String name, String image) {
        this.id = id;
        this.name = name;
        this.image = image;
    }
    @Override
    public String toString() {
        return name;
    }
    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
