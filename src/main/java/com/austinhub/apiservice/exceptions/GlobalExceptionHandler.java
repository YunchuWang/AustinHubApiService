package com.austinhub.apiservice.exceptions;

import com.austinhub.common_models.ApiError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
class GlobalExceptionHandler {

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    /**
     * Handling null pointer exceptions
     */
    @ExceptionHandler(value = NullPointerException.class)
    public ResponseEntity<Object> resourceNotFoundException(NullPointerException ex) {
        log.error("Null pointer exception occurred! As a result of:", ex);
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    /**
     * Handling http exceptions
     */
    @ExceptionHandler(value = ResponseStatusException.class)
    public ResponseEntity<Object> exceptionHandler(ResponseStatusException ex, WebRequest request) {
        log.error("Unknown anomaly! As a result of:", ex);
        ApiError apiError = new ApiError(ex.getStatus());
        apiError.setMessage(ex.getReason());
        return buildResponseEntity(apiError);
    }
}