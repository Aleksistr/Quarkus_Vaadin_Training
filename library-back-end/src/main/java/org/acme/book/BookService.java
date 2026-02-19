package org.acme.book;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.pagination.PageResult;

import java.util.List;

@ApplicationScoped
public class BookService {

    @Inject
    BookRepository bookRepository;

   public PageResult<Book> searchBooks (
           int page,
           int size,
           String sort,
           BookFilter filter
   ) {
       List<Book> books = bookRepository.findBooks(page, size, sort, filter);
        long total = bookRepository.countBooks(filter);

        return new PageResult<Book>(books, total);
   }

    public Book createBook(Book book){
        bookRepository.persist(book);
        return book;
    }

    public void deleteBook(Long id){
        bookRepository.deleteById(id);
    }
}
