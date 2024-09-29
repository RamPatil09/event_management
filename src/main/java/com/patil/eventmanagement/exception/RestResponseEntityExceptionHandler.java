package com.patil.eventmanagement.exception;

import com.patil.eventmanagement.dto.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EventManagementCustomException.class)
    public ResponseEntity<ErrorResponse> handleEventManagementCustomerException(EventManagementCustomException eventManagementCustomException) {
        // Build and return a standardized error response with message and code
        return new ResponseEntity<>(new ErrorResponse().builder()
                .errorMessage(eventManagementCustomException.getMessage())
                .errorCode(eventManagementCustomException.getErrorCode())
                .build(), HttpStatus.NOT_FOUND); // Return 404 NOT FOUND status
    }

}

