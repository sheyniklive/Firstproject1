package org.example.exception;

public class InvalidMenuChoiceException extends RuntimeException {
    public InvalidMenuChoiceException(String message) {
        super(message);
    }
}
