package org.example.pet;

import org.example.dto.PetDto;

public class PetApiMapper {

    public static Pet toDomain(PetDto petDto) {
        if (petDto == null) {
            return null;
        }
        String name = petDto.getName();
        return switch (petDto.getType()) {
            case CAT -> new Cat(name);
            case DOG -> new Dog(name);
            case GOOSE -> new Goose(name);
        };
    }

    public static PetDto toResponse(Pet pet) {
        if (pet == null) {
            return null;
        }
        return new PetDto(
                pet.getName(),
                pet.getType());
    }


}
