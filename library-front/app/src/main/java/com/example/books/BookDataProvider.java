package com.example.books;

import com.example.dto.PageResult;
import com.vaadin.flow.data.provider.CallbackDataProvider;

public class BookDataProvider {

    private final BookService bookService = new BookService();

    public CallbackDataProvider<Book, String> getDataProvider () {
        return new CallbackDataProvider<>(
                query -> {
                    int page = query.getOffset() / query.getLimit();
                    int size = query.getLimit();

                    // SORT extraction
                    String sortBy = "id";
                    String sortDirection = "ASC";

                    if (!query.getSortOrders().isEmpty()) {
                        var sortOrder = query.getSortOrders().get(0);
                        sortBy = sortOrder.getSorted();
                        sortDirection = sortOrder.getDirection().name();
                    }

                    String filter = query.getFilter().orElse(null);
                    PageResult<Book> result = bookService.getBooks(page, size, sortBy, sortDirection, filter);

                    return result.getItems().stream();
                },
                query -> {
                    PageResult<Book> result = bookService.getBooks(
                            0,
                            query.getLimit(),
                            "id",
                            "ASC",
                            query.getFilter().orElse(null)
                    );
                    return (int) result.getTotalElements();
                }
        );
    }
}
