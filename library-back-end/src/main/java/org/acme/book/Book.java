package org.acme.book;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import org.acme.category.Category;

@Entity
public class Book extends PanacheEntity {

    @NotBlank
    private String title;
    private Double price;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    public Book() {}

    public Book(String title, Double price, Category category) {
        this.title = title;
        this.price = price;
        this.category = category;
    }

    public String getTitle() {
        return title;
    }
    public Double getPrice() {
        return price;
    }
    public Category getCategory() { return category; }

    public void setCategory(Category category) { this.category = category; }
    public void setPrice(Double price) {
        this.price = price;
    }
    public void setTitle(String title) {
        this.title = title;
    }

}
