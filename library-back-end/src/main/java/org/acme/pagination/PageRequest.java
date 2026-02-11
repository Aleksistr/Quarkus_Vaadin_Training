package org.acme.pagination;

import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;

public class PageRequest {

    public int page;
    public int size;
    public SortRequest sort = new SortRequest("id", SortRequest.Direction.ASC);

    public PageRequest() {
    }
    public PageRequest(int page, int size, SortRequest sort) {
        this.page = Math.max(page, 0);
        this.size = size <= 0 ? 10 : size;
        this.sort = sort == null ? new SortRequest("id", SortRequest.Direction.ASC) : sort;
    }

    public Page toPanachePage() {
        return Page.of(page, size);
    }

    public Sort toPanacheSort() {
        return sort.toPanacheSort();
    }
}
