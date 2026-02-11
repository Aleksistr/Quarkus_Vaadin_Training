package org.acme.pagination;

import io.quarkus.panache.common.Sort;

public class SortRequest {

    public enum Direction { ASC, DESC }

    public String sortBy;
    public Direction direction = Direction.ASC;

    public SortRequest() {}

    public SortRequest(String sortBy, Direction direction) {
        this.sortBy = sortBy;
        this.direction = direction == null ? Direction.ASC : direction;
    }

    public Sort toPanacheSort() {
        if (sortBy == null || sortBy.isBlank()) {
            return Sort.by("id").ascending();
        }

        return direction == Direction.DESC ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();
    }

}
