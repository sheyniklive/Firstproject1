package org.example.exception;

import lombok.Getter;

@Getter
public class InvalidAgeException extends RuntimeException {
    private final int invalidAge;

    public InvalidAgeException(String input) {
        super(String.format("Неверно введен возраст: %s, должно быть от 0 до 150 лет", input));
        invalidAge = Integer.parseInt(input);
    }
}
