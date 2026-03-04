package com.example.books.entities;

import com.example.settings.category.entities.Category;

public class Book {

    private Long id;
    private String title;
    private Double price;
    private Category category;

    public Book () {
    }

    public Book (Long id, String title, Double price, Category category) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.category = category;
    }

    public Long getId() {
        return this.id;
    }
    public String getTitle() {
        return this.title;
    }
    public Double getPrice() { return  this.price; }
    public Category getCategory() { return this.category; }

    public void setTitle(String title) { this.title = title; }
    public void setPrice(Double price) { this.price = price; }
    public void setCategory(Category category) { this.category = category; }
}
