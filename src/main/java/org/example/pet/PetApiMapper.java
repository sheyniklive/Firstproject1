package org.example.pet;

import org.example.dto.PetCreateDto;
import org.example.dto.PetResponseDto;
import org.example.entity.PetEntity;

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

    public static PetResponseDto toResponse(PetEntity petEntity) {
        if (petEntity == null) {
            return null;
        }
        return new PetResponseDto(
                petEntity.getId(),
                petEntity.getName(),
                petEntity.getType(),
                petEntity.getOwner().getId());
    }
}
