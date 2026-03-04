package com.example.settings.category.entities;

public class Category {

    private Long id;
    private String name;

    public Category() {}

    public Category(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() { return this.id; }

    public String getName() { return this.name; }
    public void setName(String name) { this.name = name; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category)) return false;
        Category c = (Category) o;
        return id != null && id.equals(c.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

}
