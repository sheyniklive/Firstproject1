package org.example.exception;

public class InvalidAgeException extends RuntimeException {
    final int invalidAge;

    public InvalidAgeException(Integer age) {
        super(String.format("Неверно введен возраст: %d, должно быть от 0 до 150 лет", age));
        invalidAge = age;
    }
}
