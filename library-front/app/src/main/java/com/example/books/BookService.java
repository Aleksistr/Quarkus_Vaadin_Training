package com.example.books;

import com.example.dto.PageResult;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class BookService {

    private static final String BASE_URL = "http://localhost:8080/books/";

    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public PageResult<Book> getBooks(int page, int size, String sort, BookFilter filter){
        String json = mapper.writeValueAsString(filter);
        try {
            String url = BASE_URL.concat("search")
                    .concat("?page=").concat(String.valueOf(page))
                    .concat("&size=").concat(String.valueOf(size))
                    .concat("&sort=").concat(String.valueOf(sort));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Accept", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return mapper.readValue(response.body(),
                    mapper.getTypeFactory().constructParametricType(PageResult.class, Book.class)
            );

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Failed to fetch paginated books", e);
        }
    }

    public Book addBook(Book book){
        String json = mapper.writeValueAsString(book);

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL))
                    .header("Accept", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return mapper.readValue(response.body(), Book.class);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Failed to add a books", e);
        }
    }

    public void removeBook(Book book){
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL.concat("/").concat(book.getId().toString())))
                    .header("Accept", "application/json")
                    .DELETE()
                    .build();

            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Failed to remove a books", e);
        }
    }
}
