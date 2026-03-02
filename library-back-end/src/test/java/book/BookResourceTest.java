package book;

import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.acme.book.Book;
import org.acme.book.BookFilter;
import org.acme.category.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.io.InputStream;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@QuarkusTest
public class BookResourceTest {

    @Inject
    EntityManager em;

    @BeforeEach
    @Transactional
    void cleanDatabase() throws IOException {
        em.createNativeQuery("DELETE FROM BOOK").executeUpdate();
        em.createNativeQuery("DELETE FROM CATEGORY").executeUpdate();

        InputStream is = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream("import.sql");


        String sql = new String(is.readAllBytes());
        for (String statement : sql.split(";")) {
            String trimmed = statement.trim();
            if (!trimmed.isEmpty()) {
                em.createNativeQuery(trimmed).executeUpdate();
            }
        }
    }



    @Test
    @TestTransaction
    void testBookSearchingWithoutParameter () {
        given()
               .contentType(ContentType.JSON)
               .body(new BookFilter())
               .when()
               .post("/books/search")
               .then()
               .statusCode(200)
               .contentType(ContentType.JSON)
               .body("totalElements", equalTo(5));
    }

    @Test
    @TestTransaction
    void testDeleteBook () {

        // Delete the book with the id 1
        given()
                .when()
                .delete("/books/1")
                .then()
                .statusCode(204);

        // Try to search back the book
        BookFilter filter = new BookFilter();
        filter.setTitle("Lost hours");

        given()
                .contentType(ContentType.JSON)
                .body(filter)
                .when()
                .post("/books/search")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("totalElements", equalTo(0));
    }

    @Test
    @TestTransaction
    void testCreateBook() {
        Book newBook = new Book();
        newBook.setTitle("Eragon");
        newBook.setCategory(new Category(1L, "Crime Novel"));
        newBook.setPrice(6.45);
        // Create the book
        given()
                .contentType(ContentType.JSON)
                .body(newBook)
                .when()
                .post("/books")
                .then()
                .statusCode(201);

        // Search the created book
        BookFilter filter = new BookFilter();
        filter.setTitle("Eragon");

        given()
                .contentType(ContentType.JSON)
                .body(filter)
                .when()
                .post("/books/search")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("totalElements", equalTo(1));

    }
}
