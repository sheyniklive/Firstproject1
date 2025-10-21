package org.example.pet.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PetType {
    CAT("cat", "Кот(-шка)"),
    DOG("dog", "Собака"),
    GOOSE("goose", "Гусь");

    private final String code;
    private final String typeNameForUI;

    public static PetType fromCode(String code) {
        for (PetType petType : PetType.values()) {
            if (petType.getCode().equals(code)) {
                return petType;
            }
        }
        throw new IllegalArgumentException("Неизвестный тип питомца: " + code);
    }

    public static PetType fromMenuChoice(String menuChoice) {
        return switch (menuChoice) {
            case "1" -> CAT;
            case "2" -> DOG;
            case "3" -> GOOSE;
            default -> throw new IllegalArgumentException("Неверный выбор меню: " + menuChoice);
        };
    }
}
