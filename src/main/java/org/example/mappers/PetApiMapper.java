package org.example.mappers;

import org.example.dto.PetCreateDto;
import org.example.dto.PetResponseDto;
import org.example.pet.Cat;
import org.example.pet.Dog;
import org.example.pet.Goose;
import org.example.pet.Pet;

public class PetApiMapper {

    public static Pet toDomain(PetCreateDto petCreateDto) {
        if (petCreateDto == null) {
            return null;
        }
        String name = petCreateDto.getName();
        return switch (petCreateDto.getType()) {
            case CAT -> new Cat(name);
            case DOG -> new Dog(name);
            case GOOSE -> new Goose(name);
        };
    }

    public static PetResponseDto toResponse(Pet pet) {
        if (pet == null) {
            return null;
        }
        return new PetResponseDto(
                pet.getId(),
                pet.getName(),
                pet.getType(),
                pet.getOwnerId());
    }
}
