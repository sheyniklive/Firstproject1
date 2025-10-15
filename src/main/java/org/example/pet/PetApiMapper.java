package org.example.pet;

import org.example.dto.PetDto;

public class PetApiMapper {

    public static Pet toDomain(PetDto petDto) {
        String type = petDto.getType();
        String name = petDto.getName();
        return switch (type) {
            case "cat" -> new Cat(name);
            case "dog" -> new Dog(name);
            case "goose" -> new Goose(name);
            default -> throw new IllegalArgumentException("Недопустимый тип питомца: " + type);
        };
    }

    public static PetDto toResponse(Pet pet) {
        return new PetDto(
                pet.getType(),
                pet.getName()
        );
    }
}
