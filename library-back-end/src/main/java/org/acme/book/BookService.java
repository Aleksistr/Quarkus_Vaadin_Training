package org.acme.book;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import org.acme.category.Category;
import org.acme.category.CategoryRepository;
import org.acme.pagination.PageResult;

import java.util.List;

@ApplicationScoped
public class BookService {

    @Inject
    BookRepository bookRepository;

    @Inject
    CategoryRepository categoryRepository;

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

    public Book updateBook(Long id, BookUpdateRequest bookUpdateRequest){
       Book book = bookRepository.findById(id);
       if (book == null){
           throw new WebApplicationException("Book not found", 404);
       }

       book.setTitle(bookUpdateRequest.title);
       book.setPrice(bookUpdateRequest.price);

       if (bookUpdateRequest.categoryId != null){
           Category category = categoryRepository.findById(bookUpdateRequest.categoryId);
           if (category == null){
               throw new WebApplicationException("Category not found", 404);
           } else {
               book.setCategory(category);
           }
       } else {
           book.setCategory(null);
       }

       bookRepository.persist(book);
       return book;
    }

    public void deleteBook(Long id){
        bookRepository.deleteById(id);
    }
}
