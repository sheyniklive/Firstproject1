package org.example.pet;

import org.example.dto.PetDto;
import org.example.pet.enums.PetType;

public class PetApiMapper {

    public static Pet toDomain(PetDto petDto) {
        PetType type = PetType.fromCode(petDto.getType());
        String name = petDto.getName();
        return switch (type) {
            case CAT -> new Cat(name);
            case DOG -> new Dog(name);
            case GOOSE -> new Goose(name);
        };
    }

    public static PetDto toResponse(Pet pet) {
        return new PetDto(
                pet.getType().getCode(),
                pet.getName()
        );
    }
}
