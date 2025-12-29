package org.example.exception;

public class CurrencyNotFoundException extends RuntimeException {
    public CurrencyNotFoundException(String currencyCode) {
        super(String.format("Currency with code '%s' not found", currencyCode));
    }
}
