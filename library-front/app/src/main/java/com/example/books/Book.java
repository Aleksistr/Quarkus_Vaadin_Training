package com.example.books;

public class Book {

    private Long id;
    private String title;
    private Double price;

    public Book () {
    }

    public Book (Long id, String title, Double price) {
        this.id = id;
        this.title = title;
        this.price = price;
    }

    public Long getId() {
        return this.id;
    }
    public String getTitle() {
        return this.title;
    }
    public Double getPrice() { return  this.price; }

    public void setTitle(String title) {
        this.title = title;
    }
    public void setPrice(Double price) { this.price = price; }
}
