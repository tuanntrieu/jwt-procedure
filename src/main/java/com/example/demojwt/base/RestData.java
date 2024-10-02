package com.example.demojwt.base;

import com.example.demojwt.enums.RestStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RestData<T> {
    RestStatus status;

    int statusCode;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    T message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    T data;

    public RestData(T data) {
        this.status = RestStatus.SUCCESS;
        this.data = data;
        this.statusCode = 200;
    }

    public RestData(int statusCode,T data) {
        this.status = RestStatus.SUCCESS;
        this.data = data;
        this.statusCode = statusCode;
    }

    public static RestData<?> error(int statusCode,Object message) {
        return new RestData<>(RestStatus.ERROR,statusCode, message, null);
    }
}
