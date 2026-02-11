package com.example.settings.category;

import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;

public class CategoryService {

    private static final String BASE_URL = "http://localhost:8080/category";

    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public List<Category> getCategories() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL))
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return Collections.singletonList(mapper.readValue(response.body(), Category.class));
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Failed to get category list", e);
        }
    }
}
