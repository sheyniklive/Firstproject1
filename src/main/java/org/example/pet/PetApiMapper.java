package org.example.pet;

import org.example.dto.PetDto;
import org.example.entity.PetEntity;
import org.example.person.Person;
import org.example.pet.enums.PetType;

public class PetApiMapper {

    public static Pet toDomainFromDto(PetDto petDto) {
        PetType type = PetType.fromCode(petDto.getType());
        String name = petDto.getName();
        return switch (type) {
            case CAT -> new Cat(name);
            case DOG -> new Dog(name);
            case GOOSE -> new Goose(name);
        };
    }

    public static PetDto toResponseDtoFromDomain(Pet pet) {
        return new PetDto(
                pet.getType().getCode(),
                pet.getName()
        );
    }

    public static PetEntity toEntityFromDomain(Pet pet) {
        PetEntity entity = new PetEntity();


    }
    public static Pet toDomainFromEntity(PetEntity petEntity) {

    }
}
