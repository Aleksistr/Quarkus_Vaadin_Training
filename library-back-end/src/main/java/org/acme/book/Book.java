package org.acme.book;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.acme.category.Category;

@Entity
@Table(name = "BOOK")
public class Book {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true, name = "title")
    private String title;

    @NotNull
    @Column(name = "price")
    private Double price;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(
            name = "category_id",
            nullable = true,
            foreignKey = @ForeignKey(name = "fk_book_category")
    )
    private Category category;

    public Book() {}

    public Book(Long id, String title, Double price, Category category) {
        this.id = id;
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
    public Long getId() { return id; }

    public void setCategory(Category category) { this.category = category; }
    public void setPrice(Double price) {
        this.price = price;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setId(Long id) { this.id = id; }

}
