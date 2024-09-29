package com.patil.eventmanagement.exception;

import lombok.Data;

@Data
public class EventManagementCustomException extends RuntimeException {
    private String errorCode;

    public EventManagementCustomException(String errorMessage, String errorCode) {
        super(errorMessage);
        this.errorCode = errorCode;
    }
}
