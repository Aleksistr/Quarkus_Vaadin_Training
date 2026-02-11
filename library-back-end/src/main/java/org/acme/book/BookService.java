package org.acme.book;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class BookService {

    @Inject
    private BookRepository bookRepository;

    public List<Book> getAllBooks() {
        return bookRepository.listAll();
    }

    public Book createBook(Book book){
        bookRepository.persist(book);
        return book;
    }

    public void deleteBook(Long id){
        bookRepository.deleteById(id);
    }
}
