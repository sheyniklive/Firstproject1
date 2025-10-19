package org.example.exception;

import lombok.Getter;

import java.util.UUID;

@Getter
public class PersonNotFoundException extends RuntimeException {

    private UUID invalidId;

    public PersonNotFoundException(UUID id) {
        super(String.format("Не найдено человека с id:  %s", id));
        this.invalidId = id;
    }

    public PersonNotFoundException() {
        super("Не найдено ни одного человека");
        this.invalidId = null;

    }
}
