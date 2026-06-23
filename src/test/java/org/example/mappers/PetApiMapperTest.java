package org.example.mappers;

import org.example.dto.PetCreateDto;
import org.example.dto.PetResponseDto;
import org.example.pet.Cat;
import org.example.pet.Pet;
import org.example.pet.enums.PetType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class PetApiMapperTest {

    @ParameterizedTest
    @EnumSource(PetType.class)
    @DisplayName("toDomain: создает нужный Pet из DTO с учетом PetType")
    void toDomain_shouldCreateCorrectPet_forEchType(PetType petType) {
        PetCreateDto dto = new PetCreateDto("Животин", petType);

        Pet pet = PetApiMapper.toDomain(dto);

        assertThat(pet).isNotNull();
        assertThat(pet.getName()).isEqualTo("Животин");
        assertThat(pet.getType()).isEqualTo(petType);
        assertThat(pet.getId()).isNull();
        assertThat(pet.getOwnerId()).isNull();
    }

    @Test
    @DisplayName("toDomain: для типа CAT создает объект именно класса Cat")
    void toDomain_shouldCreateCatInstance_forCatType() {
        PetCreateDto dto = new PetCreateDto("Сева", PetType.CAT);

        Pet pet = PetApiMapper.toDomain(dto);

        assertThat(pet).isInstanceOf(Cat.class);
    }

    @Test
    @DisplayName("toDomain: на null возвращает null")
    void toDomain_shouldReturnNull_whenDtoIsNull() {
        assertThat(PetApiMapper.toDomain(null)).isNull();
    }

    @Test
    @DisplayName("toResponse: маппит все поля из Pet в DTO")
    void toResponse_shouldMapAllFields() {
        UUID ownerId = UUID.randomUUID();
        Pet pet = new Cat(37L, "Барсик", ownerId);

        PetResponseDto dto = PetApiMapper.toResponse(pet);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(37L);
        assertThat(dto.getName()).isEqualTo("Барсик");
        assertThat(dto.getType()).isEqualTo(PetType.CAT);
        assertThat(dto.getOwnerId()).isEqualTo(ownerId);
    }

    @Test
    @DisplayName("toResponse: на null возвращает null")
    void toResponse_shouldReturnNull_whenPetIsNull() {
        assertThat(PetApiMapper.toResponse(null)).isNull();
    }
}
