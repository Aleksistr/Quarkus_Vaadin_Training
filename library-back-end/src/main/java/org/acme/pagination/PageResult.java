package org.acme.pagination;

import java.util.List;

public class PageResult<T> {

    public List<T> items;
    public long totalElements;
    public int page;
    public int size;
    public int totalPages;
    public boolean hasNext;
    public boolean hasPrevious;

    public PageResult() {}


    public PageResult(List<T> items, long totalElements, int page, int size) {
        this.items = items;
        this.totalElements = totalElements;
        this.page = page;
        this.size = size;
        this.totalPages = (int) Math.ceil((double) totalElements / size);
        this.hasPrevious = page > 0;
        this.hasNext = (page + 1) < this.totalPages;
    }
}
