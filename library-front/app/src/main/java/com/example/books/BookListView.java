package com.example.books;

import com.example.ui.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.NumberRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

import java.text.NumberFormat;

@PageTitle(BookListView.TITLE)
@RouteAlias(value = "", layout =  MainLayout.class)
@Route(value = BookListView.ROUTE, layout = MainLayout.class)
public class BookListView extends VerticalLayout {

    public static final String ROUTE = "books";
    public static final String TITLE = "Books";

    private final BookService bookService = new BookService();
    private final Grid<Book> grid = new Grid<>(Book.class, false);

    public BookListView() {
        setSizeFull();

        grid.addColumn(Book::getTitle).setHeader("Title").setKey("title").setSortable(true);
        grid.addColumn(
                new NumberRenderer<>(Book::getPrice, NumberFormat.getCurrencyInstance())
        ).setHeader("Price").setKey("price").setSortable(true);
        grid.addColumn(
                new ComponentRenderer<>(book ->
                    new Span(book.getCategory().getName())
                )
        ).setHeader("Category").setKey("category").setSortable(true);

        grid.addColumn(new ComponentRenderer<>(book -> {
            HorizontalLayout buttons = new HorizontalLayout();
            buttons.setWidthFull();

            Button deleteButton = new Button("Delete", clickEvent -> {
                this.deleteBook(book);
            });
            deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

            buttons.add(deleteButton);
            return buttons;
        })).setHeader("Action");

        Button addBookButton = new Button("Add Book");

        BookAddDialog bookAddDialog = new BookAddDialog();
        bookAddDialog.setOnSaveCallback(() -> grid.getDataProvider().refreshAll());

        addBookButton.addClickListener(e -> {
            bookAddDialog.open();
        });

        BookDataProvider provider = new BookDataProvider();
        grid.setDataProvider(provider.getDataProvider());

        add(addBookButton, grid);
        expand(grid);
    }

    /*
     * Confirm the Book delete
     */
    private void deleteBook(Book book) {
        ConfirmDialog confirmDialog = new ConfirmDialog();
        confirmDialog.setHeader("Confirmation - Delete book");
        confirmDialog.setText("Are you sure you want to delete this book ".concat(book.getTitle()).concat("?"));

        confirmDialog.setCancelable(true);

        confirmDialog.setCancelText("No");
        confirmDialog.setConfirmText("Yes");
        confirmDialog.addConfirmListener(e -> {
           this.removeBook(book);
        });

        confirmDialog.open();
    }

    /*
     * Call the back end service to delete the book
     */
    private void removeBook(Book book) {
        this.bookService.removeBook(book);
        grid.getDataProvider().refreshAll();
    }
}
