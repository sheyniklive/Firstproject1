package org.example.exception;

public class PersonNotFoundException extends RuntimeException {
    final String invalidName;

    public PersonNotFoundException(String name) {
        super(String.format("Не найдено человека с именем %s", name));
        invalidName = name;
    }
}
