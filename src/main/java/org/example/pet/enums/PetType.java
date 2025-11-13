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

     public static PetType fromMenuChoice(String menuChoice) {
        return switch (menuChoice) {
            case "1" -> CAT;
            case "2" -> DOG;
            case "3" -> GOOSE;
            default -> throw new IllegalArgumentException("Неверный выбор меню: " + menuChoice);
        };
    }
}
