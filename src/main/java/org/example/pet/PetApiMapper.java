package org.example.pet;

import org.example.dto.PetDto;
import org.example.entity.PetEntity;
import org.example.pet.enums.PetType;

public class PetApiMapper {

    public static Pet toDomainFromDto(PetDto petDto) {
        if (petDto == null) {
            return null;
        }
        PetType type = PetType.fromCode(petDto.getType());
        String name = petDto.getName();
        return switch (type) {
            case CAT -> new Cat(name);
            case DOG -> new Dog(name);
            case GOOSE -> new Goose(name);
        };
    }

    public static PetDto toResponseDtoFromDomain(Pet pet) {
        if (pet == null) {
            return null;
        }
        return new PetDto(
                pet.getType().getCode(),
                pet.getName()
        );
    }

    public static PetEntity toEntityFromDomain(Pet pet) {
        if (pet == null) {
            return null;
        }
        PetEntity petEntity = new PetEntity();
        petEntity.setName(pet.getName());
        petEntity.setType(pet.getType());
        return petEntity;
    }

    public static Pet toDomainFromEntity(PetEntity petEntity) {
        if (petEntity == null) {
            return null;
        }
        PetType type = petEntity.getType();
        String name = petEntity.getName();
        return switch (type) {
            case CAT -> new Cat(name);
            case DOG -> new Dog(name);
            case GOOSE -> new Goose(name);
        };
    }
}
