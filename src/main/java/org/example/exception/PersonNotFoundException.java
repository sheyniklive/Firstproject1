package org.example.exception;

import lombok.Getter;

@Getter
public class PersonNotFoundException extends RuntimeException {
    private final String invalidName;

    public PersonNotFoundException(String name) {
        super(String.format("Не найдено человека с именем %s", name));
        invalidName = name;
    }
}
