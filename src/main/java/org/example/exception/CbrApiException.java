package org.example.exception;

public class CbrApiException extends RuntimeException {

    public CbrApiException(String message) {
        super(message);
    }

    public CbrApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
