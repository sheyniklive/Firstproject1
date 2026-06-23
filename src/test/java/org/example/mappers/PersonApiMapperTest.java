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
        assertThat(person.getName()).isEqualTo("Биба");
        assertThat(person.getSurname()).isEqualTo("Боба");
        assertThat(person.getAge()).isEqualTo(2);
        assertThat(person.getEmail()).isEqualTo("horoshihcheloveka@mail.ru");
        assertThat(person.getId()).isNull();
        assertThat(person.getPets()).isEmpty();
    }

    @Test
    @DisplayName("toDomain: на null возвращает null")
    void toDomain_shouldReturnNull_whenDtoIsNull() {
        Person person = PersonApiMapper.toDomain(null);

        assertThat(person).isNull();
    }

    @Test
    @DisplayName("toResponse: маппит все поля из Person в DTO")
    void toResponse_shouldMapAllFields() {
        UUID id = UUID.randomUUID();
        Person person = new Person(id, "Веня", "Дизельков", 3, "forsag@mail.ru", new ArrayList<>());

        PersonResponseDto personResponseDto = PersonApiMapper.toResponse(person);

        assertThat(personResponseDto).isNotNull();
        assertThat(personResponseDto.getId()).isEqualTo(id);
        assertThat(personResponseDto.getName()).isEqualTo("Веня");
        assertThat(personResponseDto.getSurname()).isEqualTo("Дизельков");
        assertThat(personResponseDto.getAge()).isEqualTo(3);
        assertThat(personResponseDto.getEmail()).isEqualTo("forsag@mail.ru");
    }

    @Test
    @DisplayName("toResponse: на null возвращает null")
    void toResponse_shouldReturnNull_whenPersonIsNull() {
        PersonResponseDto personResponseDto = PersonApiMapper.toResponse(null);

        assertThat(personResponseDto).isNull();
    }
}
