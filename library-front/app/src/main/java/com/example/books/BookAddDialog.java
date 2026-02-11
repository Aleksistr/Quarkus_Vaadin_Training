package com.example.books;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

public class BookAddDialog extends Dialog {

    private final BookService bookService = new BookService();
    private final Binder<Book> binder = new Binder<>(Book.class);
    private final TextField titleField = new TextField("Title");
    private final NumberField priceField = new NumberField("Price");

    private Runnable onSaveCallback;

    public BookAddDialog() {
        setHeaderTitle("Add a Book");

        // Define the form Layout
        FormLayout formLayout = new FormLayout();
        titleField.setRequired(true);
        titleField.setWidthFull();

        formLayout.add(titleField, priceField);
        add(formLayout);

        binder.forField(titleField)
                .asRequired("Book title is required")
                .bind(Book::getTitle, Book::setTitle);

        binder.forField(priceField)
                .asRequired("Book price is required")
                .withValidator(value -> value > 0, value -> "Price should be greater than 0")
                .bind(Book::getPrice, Book::setPrice);

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
