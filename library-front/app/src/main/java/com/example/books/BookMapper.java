package com.example.books;

import com.example.books.entities.Book;
import com.example.books.entities.BookUpdateRequest;

public class BookMapper {

    public BookUpdateRequest toBookUpdateRequest(Book book) {
        BookUpdateRequest bookUpdateRequest = new BookUpdateRequest();
        bookUpdateRequest.title =  book.getTitle();
        bookUpdateRequest.price =  book.getPrice();

        if (book.getCategory() != null) {
            bookUpdateRequest.categoryId = book.getCategory().getId();
        }

        return bookUpdateRequest;
    }
}
