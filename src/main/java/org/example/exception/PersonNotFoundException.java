package org.example.exception;

import lombok.Getter;

import java.util.UUID;

@Getter
public class PersonNotFoundException extends RuntimeException {
    private final UUID invalidId;

public PersonNotFoundException(String Id) {
        super(String.format("Не найдено человека:  %s", Id));
        invalidId = UUID.fromString(Id);
    }
}
