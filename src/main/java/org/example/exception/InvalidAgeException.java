package org.example.exception;

import lombok.Getter;

@Getter
public class InvalidAgeException extends RuntimeException {
    private final int invalidAge;

    public InvalidAgeException(Integer age) {
        super(String.format("Неверно введен возраст: %d, должно быть от 0 до 150 лет", age));
        invalidAge = age;
    }
}
