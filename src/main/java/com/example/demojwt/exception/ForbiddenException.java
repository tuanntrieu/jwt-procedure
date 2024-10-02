package com.example.demojwt.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
public class ForbiddenException extends RuntimeException{
    private String message;
    private HttpStatus status;

    public ForbiddenException(String message) {
        this.message = message;
        this.status = HttpStatus.FORBIDDEN;
    }
}