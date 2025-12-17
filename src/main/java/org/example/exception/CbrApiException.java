package org.example.exception;

public class CbrApiException extends RuntimeException {
    public CbrApiException(String currencyCode) {
        super(String.format("Currency with code '%s' not found", currencyCode));
    }
}
