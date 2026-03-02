package org.acme.category;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.acme.book.Book;

import java.util.List;

@Entity
@Table(name = "category")
public class Category {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true, name = "name")
    private String name;

    @OneToMany (orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<Book> books;


    public Category() {}
    public Category(String name) {
        this.name = name;
    }
    public Category(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

}
