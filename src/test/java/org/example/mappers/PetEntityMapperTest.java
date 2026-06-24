package org.example.mappers;

import org.example.entity.PersonEntity;
import org.example.entity.PetEntity;
import org.example.pet.Dog;
import org.example.pet.Goose;
import org.example.pet.Pet;
import org.example.pet.enums.PetType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class PetEntityMapperTest {
    @Test
    @DisplayName("toEntity: маппит все кроме id владельца")
    void toEntity_shouldMapFields_withoutOwner() {
        UUID ownerId = UUID.randomUUID();
        Pet dog = new Dog(33L, "Соб", ownerId);

        PetEntity entity = PetEntityMapper.toEntity(dog);

        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(33L);
        assertThat(entity.getName()).isEqualTo("Соб");
        assertThat(entity.getOwner()).isNull();
    }

    @Test
    @DisplayName("toEntity: id у Entity точно null, когда null у Pet`a")
    void toEntity_shouldIdNull_whenPetIdNull() {
        UUID ownerId = UUID.randomUUID();
        Pet dog = new Dog(null, "Соб", ownerId);

        PetEntity entity = PetEntityMapper.toEntity(dog);

        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isNull();
        assertThat(entity.getName()).isEqualTo("Соб");
        assertThat(entity.getType()).isEqualTo(PetType.DOG);
    }

    @Test
    @DisplayName("toEntity: на null возвращает null")
    void toEntity_shouldReturnNull_whenPetIsNull() {
        assertThat(PetEntityMapper.toEntity(null)).isNull();
    }

    @ParameterizedTest
    @EnumSource(PetType.class)
    @DisplayName("toDomain: для каждого типа создает питомца со всеми полями и ownerId из владельца")
    void toDomain_shouldMapAllFieldsAndOwnerId_forEachType(PetType type) {
        UUID ownerId = UUID.randomUUID();
        PersonEntity owner = new PersonEntity();
        owner.setId(ownerId);
        PetEntity petEntity = new PetEntity(77L, "Васька", type, owner);

        Pet pet = PetEntityMapper.toDomain(petEntity);

        assertThat(pet).isNotNull();
        assertThat(pet.getId()).isEqualTo(77L);
        assertThat(petEntity.getName()).isEqualTo("Васька");
        assertThat(pet.getType()).isEqualTo(type);
        assertThat(pet.getOwnerId()).isEqualTo(ownerId);
    }

    @Test
    @DisplayName("toDomain: создает Pet корректно типу из PetEntity")
    void toDomain_shouldCreateCorrectPet() {
        UUID ownerId = UUID.randomUUID();
        PersonEntity owner = new PersonEntity();
        owner.setId(ownerId);
        PetEntity petEntity = new PetEntity(5L, "Птыц-тыц-тыц", PetType.GOOSE, owner);

        Pet pet = PetEntityMapper.toDomain(petEntity);

        assertThat(pet).isNotNull().isInstanceOf(Goose.class);
    }

    @Test
    @DisplayName("toDomain: возвращает null, когда PetEntity null")
    void toDomain_shouldReturnNull_whenPetEntityIsNull() {
        assertThat(PetEntityMapper.toDomain(null)).isNull();
    }
}
