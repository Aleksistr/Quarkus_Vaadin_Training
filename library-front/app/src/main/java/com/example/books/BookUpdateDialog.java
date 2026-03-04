package com.example.books;

import com.example.books.entities.Book;
import com.example.books.entities.BookUpdateRequest;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.binder.Binder;

public class BookUpdateDialog extends Dialog {

    private final BookService bookService = new BookService();
    private final BookMapper bookMapper = new BookMapper();
    private final Binder<Book> binder = new Binder<>(Book.class);
    private Runnable onSaveCallBack;

    public BookUpdateDialog(Book book) {
        setHeaderTitle("Update Book : ".concat(book.getTitle()));
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

        binder.readBean(book);

        Button saveButton = new Button("Save", event -> {
            this.updateBook(book.getId());
        });

        Button resetButton = new Button("Reset", event -> {
            binder.readBean(book);
        });
        resetButton.addThemeVariants(ButtonVariant.LUMO_WARNING);

        Button cancelButton = new Button("Cancel", event -> {
            this.close();
        });
        cancelButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

        HorizontalLayout footer = new HorizontalLayout(
                new HorizontalLayout(resetButton),
                new HorizontalLayout(saveButton, cancelButton)
        );

        footer.setWidthFull();
        footer.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

        getFooter().add(footer);
    }

    private void updateBook(Long bookId) {
        Book book = new Book();

        if (!binder.writeBeanIfValid(book)) {
            Notification.show("Invalid Book");
            return;
        }

        BookUpdateRequest updateRequest = bookMapper.toBookUpdateRequest(book);
        try {
            Book updated = bookService.updateBook(updateRequest, bookId);
            Notification.show("Book Updated : ".concat(updated.getTitle()));

            if (onSaveCallBack != null) {
                binder.readBean(null);
                onSaveCallBack.run();
            }

            close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Notification.show("Update Failed");
        }
    }

    public void setOnSaveCallBack(Runnable onSaveCallBack) {
        this.onSaveCallBack = onSaveCallBack;
    }
}
