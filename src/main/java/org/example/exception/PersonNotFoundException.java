package org.example.exception;

import lombok.Getter;

@Getter
public class PersonNotFoundException extends RuntimeException {
    private final String invalidNameOrId;

    public PersonNotFoundException(String wantPerson) {
        super(String.format("Не найдено человека с именем или ID %s", wantPerson));
        invalidNameOrId = wantPerson;
    }
}
