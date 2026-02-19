package org.acme.book;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class BookRepository implements PanacheRepository<Book> {

    public List<Book> findBooks(int page, int size, String sort, BookFilter filter) {
        PanacheQuery<Book> panacheQuery = buildQuery(sort, filter);
        return panacheQuery.page(Page.of(page, size)).list();
    }

    public long countBooks(BookFilter filter) {
        PanacheQuery<Book> panacheQuery = buildQuery(null, filter);
        return panacheQuery.count();
    }

    private PanacheQuery<Book> buildQuery(String sort, BookFilter filter) {
        StringBuilder query = new StringBuilder("1=1");
        Map<String, Object> params = new HashMap<>();

        if (filter.getTitle() != null && ! filter.getTitle().isEmpty()) {
            query.append(" AND LOWER(title) like :title");
            params.put("title", "%".concat(filter.getTitle()).concat("%"));
        }

        if (filter.getCategory() != null && filter.getCategory().getId() != null) {
                query.append(" AND category.id = :categoryId");
            params.put("categoryId", filter.getCategory().getId().toString());
        }

        if (filter.getMinPrice() != null && filter.getMinPrice() > 0) {
            query.append(" AND price >= :minPrice");
            params.put("minPrice", filter.getMinPrice());
        }

        if (filter.getMaxPrice() != null && filter.getMaxPrice() > 0) {
            query.append(" AND price <= :maxPrice");
            params.put("maxPrice", filter.getMaxPrice());
        }

        // Add the sort
        if (sort != null && !sort.isEmpty()) {
            query.append(" ORDER BY ").append(sort);
        }

        return find(query.toString(), params);
    }
}
