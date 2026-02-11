package com.example.dto;

import java.util.List;

public class PageResult<T> {

    private List<T> items;
    private Long totalElements;
    private int page;
    private int size;
    private int totalPages;
    private boolean hasNext;
    private boolean hasPrevious;

    public List<T> getItems() { return items; }
    public long getTotalElements() { return totalElements; }
    public int getPage() { return page; }
    public int getSize() { return size; }
    public int getTotalPages() { return totalPages; }
    public boolean isHasNext() { return hasNext; }
    public boolean isHasPrevious() { return hasPrevious; }

}
