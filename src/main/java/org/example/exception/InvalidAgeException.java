package org.example.exception;

import lombok.Getter;

@Getter
public class InvalidAgeException extends RuntimeException {
    private final int invalidAge;

    public InvalidAgeException(String input) {
        super(String.format("Неверно введен возраст: %s", input));
        invalidAge = Integer.parseInt(input);
    }
}
