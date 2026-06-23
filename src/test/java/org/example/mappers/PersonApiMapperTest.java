package org.example.mappers;

import org.example.dto.PersonCreateDto;
import org.example.dto.PersonResponseDto;
import org.example.person.Person;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class PersonApiMapperTest {

    @Test
    @DisplayName("toDomain: маппит все поля из DTO в Person")
    void toDomain_shouldMapAllFields() {
        PersonCreateDto personCreateDto = new PersonCreateDto("Биба", "Боба", 2, "horoshihcheloveka@mail.ru");

        Person person = PersonApiMapper.toDomain(personCreateDto);

        assertThat(person).isNotNull();
        assertThat(person.getName()).isEqualTo(personCreateDto.getName());
        assertThat(person.getSurname()).isEqualTo(personCreateDto.getSurname());
        assertThat(person.getAge()).isEqualTo(personCreateDto.getAge());
        assertThat(person.getEmail()).isEqualTo(personCreateDto.getEmail());
        assertThat(person.getId()).isNull();
        assertThat(person.getPets()).isEmpty();
    }

    @Test
    @DisplayName("toDomain: на null возвращвет null")
    void toDomain_shouldReturnNull_whenDtoIsNull() {
        Person person = PersonApiMapper.toDomain(null);

        assertThat(person).isNull();
    }

    @Test
    @DisplayName("toResponse: маппит все поля из Person в DTO")
    void toResponse_shouldMapAllFields() {
        Person person = new Person(UUID.randomUUID(), "Веня", "Дизельков", 3, "forsag@mail.ru", new ArrayList<>());

        PersonResponseDto personResponseDto = PersonApiMapper.toResponse(person);

        assertThat(personResponseDto).isNotNull();
        assertThat(personResponseDto.getId()).isEqualTo(person.getId());
        assertThat(personResponseDto.getName()).isEqualTo(person.getName());
        assertThat(personResponseDto.getSurname()).isEqualTo(person.getSurname());
        assertThat(personResponseDto.getAge()).isEqualTo(person.getAge());
        assertThat(personResponseDto.getEmail()).isEqualTo(person.getEmail());
    }

    @Test
    @DisplayName("toResponse: на null возвращвет null")
    void toResponse_shouldReturnNull_whenPersonIsNull() {
        PersonResponseDto personResponseDto = PersonApiMapper.toResponse(null);

        assertThat(personResponseDto).isNull();
    }
}
