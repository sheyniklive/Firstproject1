package org.example.exception;

import lombok.Getter;

@Getter
public class PersonNotFoundException extends RuntimeException {

    public PersonNotFoundException(String id) {
        super(String.format("Не найдено человека:  %s", id));
    }
}
