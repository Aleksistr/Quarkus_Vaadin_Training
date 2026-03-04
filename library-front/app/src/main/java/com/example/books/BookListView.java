package com.example.books;

import com.example.books.entities.Book;
import com.example.books.entities.BookFilter;
import com.example.ui.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.NumberRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import org.jspecify.annotations.NonNull;

import java.text.NumberFormat;

@PageTitle(BookListView.TITLE)
@RouteAlias(value = "", layout =  MainLayout.class)
@Route(value = BookListView.ROUTE, layout = MainLayout.class)
public class BookListView extends VerticalLayout {

    public static final String ROUTE = "books";
    public static final String TITLE = "Books";

    private final BookService bookService = new BookService();
    private final Grid<Book> grid = new Grid<>(Book.class, false);
    private final BookDataProvider dataProvider;

    public BookListView() {
        this.dataProvider = new BookDataProvider(bookService);

        grid.addColumn(Book::getTitle).setHeader("Title").setKey("title").setSortable(true);
        grid.addColumn(
                new NumberRenderer<>(Book::getPrice, NumberFormat.getCurrencyInstance())
        ).setHeader("Price").setKey("price").setSortable(true);
        grid.addColumn(
                new ComponentRenderer<>(book ->
                    new Span(book.getCategory() != null ? book.getCategory().getName() : "")
                )
        ).setHeader("Category").setKey("category").setSortable(true);

        grid.addColumn(new ComponentRenderer<>(book -> {
            HorizontalLayout buttons = new HorizontalLayout();
            buttons.setWidthFull();

            Button deleteButton = new Button("Delete", clickEvent -> {
                this.deleteBook(book);
            });
            deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
            deleteButton.setSuffixComponent(VaadinIcon.CLOSE.create());

            Button updateButton = new Button("Update", clickEvent -> {
                BookUpdateDialog bookUpdateDialog = new BookUpdateDialog(book);
                bookUpdateDialog.setOnSaveCallBack(() -> grid.getDataProvider().refreshAll());
                bookUpdateDialog.open();

            });
            updateButton.addThemeVariants(ButtonVariant.LUMO_WARNING);
            updateButton.setSuffixComponent(VaadinIcon.PENCIL.create());

            buttons.add(updateButton, deleteButton);
            return buttons;
        })).setHeader("Action");

        grid.setPageSize(20);
        grid.setDataProvider(dataProvider);

        HorizontalLayout gridHeaderAction = getHorizontalLayout();

        add(gridHeaderAction, grid);
        expand(grid);
    }

    private @NonNull HorizontalLayout getHorizontalLayout() {
        HorizontalLayout gridHeaderAction =  new HorizontalLayout();
        gridHeaderAction.setWidthFull();
        gridHeaderAction.setAlignItems(Alignment.END);

        BookFilterBar bookFilterBar = new BookFilterBar();
        bookFilterBar.addBookFilterListener(e -> {
            BookFilter filter = e.getFilter();
            dataProvider.applyFilter(filter);
        });

        Button addBookButton = new Button("Add Book");

        BookAddDialog bookAddDialog = new BookAddDialog();
        bookAddDialog.setOnSaveCallback(() -> grid.getDataProvider().refreshAll());

        addBookButton.addClickListener(e -> {
            bookAddDialog.open();
        });

        gridHeaderAction.add(bookFilterBar, addBookButton);
        return gridHeaderAction;
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

    private void removeBook(Book book) {
        this.bookService.removeBook(book);
        grid.getDataProvider().refreshAll();
    }
}
