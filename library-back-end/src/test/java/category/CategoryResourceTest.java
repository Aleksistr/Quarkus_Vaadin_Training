package category;

import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.acme.category.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@QuarkusTest
public class CategoryResourceTest {

    @Inject
    EntityManager em;

    @BeforeEach
    @Transactional
    public void setup() throws IOException {
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
    void testListCategory () {
        given()
                .when()
                .get("/category")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("size()", equalTo(2));
    }

    @Test
    @TestTransaction
    void testCreateCategory () {

        Category category = new Category();
        category.setName("Fantasy");

        given()
                .contentType(ContentType.JSON)
                .body(category)
                .when()
                .post("/category")
                .then()
                .statusCode(201);

        given()
                .when()
                .get("/category")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("size()", equalTo(3));
    }

    @Test
    @TestTransaction
    void testDeleteCategory () {
        given()
                .when()
                .delete("/category/1")
                .then()
                .statusCode(204);
        given()
                .when()
                .get("/category")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("size()", equalTo(1));
    }
}
