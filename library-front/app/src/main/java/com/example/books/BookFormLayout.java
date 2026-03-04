package com.example.books;

import com.example.settings.category.entities.Category;
import com.example.settings.category.CategoryService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;

import java.util.List;

public class BookFormLayout extends FormLayout {

    private final CategoryService categoryService = new CategoryService();

    // Define all the fields
    private final TextField titleField = new TextField("Title");
    private final Select<Category> categorySelect = new Select<>("Category");
    private final NumberField priceField = new NumberField("Price");

    public BookFormLayout() {
        List<Category> categories = categoryService.getCategories();
        categorySelect.setItems(categories);
        categorySelect.setRenderer(new ComponentRenderer<Component, Category>(category -> new Span(category.getName())));
        categorySelect.setEmptySelectionAllowed(true);

        add(titleField, categorySelect, priceField);
    }

    // Define getters for each fileds
    public TextField getTitleField() { return titleField; }
    public Select<Category> getCategorySelect() { return categorySelect; }
    public NumberField getPriceField() { return priceField; }

}
