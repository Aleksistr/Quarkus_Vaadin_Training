package org.acme.pagination;

import java.util.List;

public class PageResult<T> {

    public List<T> items;
    public long totalElements;

    public PageResult() {}

    public PageResult(List<T> items, long totalElements) {
        this.items = items;
        this.totalElements = totalElements;
    }
}
