package com.example.books;

import com.example.settings.category.Category;
import com.example.settings.category.CategoryService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.renderer.ComponentRenderer;

import java.util.List;

public class BookAddDialog extends Dialog {

    private final BookService bookService = new BookService();
    private final CategoryService categoryService = new CategoryService();
    private final Binder<Book> binder = new Binder<>(Book.class);

    private Runnable onSaveCallback;

    public BookAddDialog() {
        List<Category> categories = categoryService.getCategories();

        setHeaderTitle("Add a Book");

        // Define the form Layout
        FormLayout formLayout = new FormLayout();
        TextField titleField = new TextField("Title");
        titleField.setRequired(true);
        titleField.setWidthFull();

        Select<Category> categoryField = new Select<>("Category");
        categoryField.setItems(categories);
        categoryField.setRenderer(new ComponentRenderer<Component, Category>(category -> new Span(category.getName())));

        NumberField priceField = new NumberField("Price");
        formLayout.add(titleField, priceField, categoryField);
        add(formLayout);

        binder.forField(titleField)
                .asRequired("Book title is required")
                .bind(Book::getTitle, Book::setTitle);

        binder.forField(priceField)
                .asRequired("Book price is required")
                .withValidator(value -> value > 0, value -> "Price should be greater than 0")
                .bind(Book::getPrice, Book::setPrice);

        binder.forField(categoryField)
                .asRequired("Book category is required")
                .bind(Book::getCategory, Book::setCategory);

        Button saveButton = new Button("Save", event -> {
            saveBook();
            this.close();
        });

        Button cancelButton = new Button("Cancel", event -> {
            this.close();
        });
        cancelButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

        getFooter().add(saveButton, cancelButton);
    }

    private void saveBook () {
        Book book = new Book();
        if (!binder.writeBeanIfValid(book)) {
            Notification.show("Invalid book");
            return;
        }

        try {
            Book created = bookService.addBook(book);
            Notification.show("Book added: "  + created.getTitle());

            if (onSaveCallback != null) {
                binder.readBean(null);
                onSaveCallback.run();
            }
            close();
        }
        catch (Exception e) {
            Notification.show("Error while saving the book: " + e.getMessage());
        }
    }

    public void setOnSaveCallback(Runnable onSaveCallback) {
        this.onSaveCallback = onSaveCallback;
    }
}
