package com.example.demojwt.exception;

import com.example.demojwt.base.RestData;
import com.example.demojwt.base.VsResponseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;


@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<RestData<?>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        StringBuilder errors = new StringBuilder("Validation errors: ");

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.append(error.getDefaultMessage())
                        .append("; ")
        );
        return VsResponseUtil.error(HttpStatus.BAD_REQUEST, errors.toString());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<RestData<?>> handlerInternalServerError(Exception ex) {
        log.error(ex.getMessage(), ex);
        return VsResponseUtil.error(HttpStatus.INTERNAL_SERVER_ERROR, "Có lỗi xảy ra, vui lòng thử lại sau.");
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<RestData<?>> handleNotFoundException(NotFoundException ex) {
        log.error(ex.getMessage(), ex);
        return VsResponseUtil.error(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<RestData<?>> handleAccessDeniedException(ForbiddenException ex) {
        log.error(ex.getMessage(), ex);
        return VsResponseUtil.error(HttpStatus.FORBIDDEN, ex.getMessage());
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<RestData<?>> handleUnauthorizedException(UnauthorizedException ex) {
        log.error(ex.getMessage(), ex);
        return VsResponseUtil.error(ex.getStatus(), ex.getMessage());
    }

    @ExceptionHandler(InvalidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<RestData<?>> handleInvalidException(InvalidException ex) {
        log.error(ex.getMessage(), ex);
        return VsResponseUtil.error(ex.getStatus(), ex.getMessage());
    }
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<RestData<?>> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        log.error(ex.getMessage(), ex);
        return VsResponseUtil.error(ex.getStatus(), ex.getMessage());
    }
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<RestData<?>> handleMaxSizeException(MaxUploadSizeExceededException ex) {
        return VsResponseUtil.error(HttpStatus.PAYLOAD_TOO_LARGE, ex.getMessage());
    }


}
