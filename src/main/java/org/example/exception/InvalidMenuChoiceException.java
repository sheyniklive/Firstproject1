package org.example.exception;

public class InvalidMenuChoiceException extends RuntimeException {
    public InvalidMenuChoiceException(String input) {
        super(String.format("Неверный выбор меню: %s", input));
    }
}
