package com.workintech.course.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<ApiErrorResponse>handleApiException(ApiException apiException){
        log.error("ApiException: ",apiException.getMessage());
        ApiErrorResponse apiErrorResponse=new ApiErrorResponse(apiException.getHttpStatus().value(),apiException.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(apiErrorResponse,apiException.getHttpStatus());
    }

    @ExceptionHandler
    public ResponseEntity<ApiErrorResponse> handleOtherExceptions(Exception exception){
        log.error("Exception: ",exception.getMessage());
        ApiErrorResponse apiErrorResponse=new ApiErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),exception.getMessage(),LocalDateTime.now());
        return new ResponseEntity<>(apiErrorResponse,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
