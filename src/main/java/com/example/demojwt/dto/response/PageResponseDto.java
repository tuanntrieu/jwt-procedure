package com.example.demojwt.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PageResponseDto<T> {
    private Long totalElements;

    private Integer totalPages;

    private Integer pageNo;

    private Integer pageSize;

    private String sort;

    private List<T> items;

    public List<T> getItems() {
        return items == null ? null : new ArrayList<>(items);
    }
}
