package org.example.pet;

import org.example.entity.PetEntity;
import org.example.pet.enums.PetType;

public class PetEntityMapper {

    public static PetEntity toEntity(Pet pet) {
        if (pet == null) {
            return null;
        }
        PetEntity petEntity = new PetEntity();
        petEntity.setName(pet.getName());
        petEntity.setType(pet.getType());
        return petEntity;
    }

    public static Pet toDomain(PetEntity petEntity) {
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
