package org.example.mappers;

import org.example.entity.PetEntity;
import org.example.pet.Dog;
import org.example.pet.Pet;
import org.example.pet.enums.PetType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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


}
