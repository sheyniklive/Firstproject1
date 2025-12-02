package org.example.pet.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PetType {
    CAT("Кот(-шка)"),
    DOG("Собака"),
    GOOSE("Гусь");

    private final String typeNameForUI;
}
