package com.example.books;

import com.example.books.entities.Book;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.data.binder.Binder;

public class BookAddDialog extends Dialog {

    private final BookService bookService = new BookService();
    private final Binder<Book> binder = new Binder<>(Book.class);

    private Runnable onSaveCallback;

    public BookAddDialog() {

        setHeaderTitle("Add a Book");
        final BookFormLayout bookFormLayout = new BookFormLayout();
        add(bookFormLayout);

        binder.forField(bookFormLayout.getTitleField())
                .asRequired("Book title is required")
                .bind(Book::getTitle, Book::setTitle);

        binder.forField(bookFormLayout.getPriceField())
                .asRequired("Book price is required")
                .withValidator(value -> value > 0, value -> "Price should be greater than 0")
                .bind(Book::getPrice, Book::setPrice);

        binder.forField(bookFormLayout.getCategorySelect())
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
