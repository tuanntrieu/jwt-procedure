package com.example.demojwt.dto.request;


import lombok.Getter;

@Getter
public class PaginationRequestDto {
    private Integer pageNo ;
    private Integer pageSize;
    private String sortBy;


    public PaginationRequestDto() {
        this.pageNo = 0;
        this.pageSize = 5;
        this.sortBy = "name";
    }

    public Integer getPageNo() {
        if (pageNo < 0) return 0;
        return pageNo;
    }

    public Integer getPageSize() {
        if (pageSize < 1) {
            pageSize = 5;
        }
        return pageSize;
    }

}
