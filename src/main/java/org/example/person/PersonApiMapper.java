package org.example.person;

import org.example.dto.PersonCreateDto;
import org.example.dto.PersonResponseDto;
import org.example.pet.PetApiMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

public class PersonApiMapper {

    public static Person toDomain(PersonCreateDto dto) {
        if (dto == null) {
            return null;
        }
        return new Person(
                null,
                dto.getName(),
                dto.getSurname(),
                dto.getAge(),
                new ArrayList<>());
    }

    public static PersonResponseDto toResponse(Person person) {
        if (person == null) {
            return null;
        }
        return new PersonResponseDto(
                person.getId(),
                person.getName(),
                person.getSurname(),
                person.getAge(),
                Optional.ofNullable(person.getPets())
                        .orElse(Collections.emptyList())
                        .stream()
                        .map(PetApiMapper::toResponse)
                        .toList());
    }
}
