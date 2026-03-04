package com.example.books;

import com.example.books.entities.Book;
import com.example.books.entities.BookFilter;
import com.example.dto.PageResult;
import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.provider.QuerySortOrder;

import java.util.stream.Stream;

public class BookDataProvider extends AbstractBackEndDataProvider<Book, BookFilter> {

    private final BookService bookService;
    private BookFilter bookFilter = new BookFilter();

    public BookDataProvider(BookService bookService) {
        this.bookService = bookService;
    }

    @Override
    protected Stream<Book> fetchFromBackEnd(Query<Book, BookFilter> query) {
        // Pagination
        int offset = query.getOffset();
        int limit = query.getLimit() > 0 ? query.getLimit() : 20;
        int page = offset / limit;

        String sort = query.getSortOrders().isEmpty()
                ? null
                : toSortQuery(query.getSortOrders().getFirst());
        PageResult<Book> result = bookService.getBooks(page, limit, sort, bookFilter);

        return result.getItems().stream();
    }

    @Override
    protected int sizeInBackEnd(Query<Book, BookFilter> query) {

        String sort = query.getSortOrders().isEmpty()
                ? null
                : toSortQuery(query.getSortOrders().get(0));
        PageResult<Book> result = bookService.getBooks(1, 20, sort, bookFilter);

        return result.getTotalElements();
    }

    public void applyFilter(BookFilter filter) {
        bookFilter = filter;
        refreshAll();
    }

    private String toSortQuery(QuerySortOrder order) {
        return order.getSorted() + "," + (order.getDirection().getShortName().equals("asc") ? "asc" : "desc");
    }

}
