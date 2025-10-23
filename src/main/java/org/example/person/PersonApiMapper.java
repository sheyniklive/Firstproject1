package org.example.person;

import org.example.dto.PersonCreateDto;
import org.example.dto.PersonResponseDto;
import org.example.pet.PetApiMapper;

import java.util.ArrayList;
import java.util.UUID;

public class PersonApiMapper {

    public static Person toDomain(PersonCreateDto dto) {
        return new Person(
                UUID.randomUUID(),// null сделать
                dto.getName(),
                dto.getSurname(),
                dto.getAge(),
                new ArrayList<>());
    }

    public static PersonResponseDto toResponse(Person person) {
        return new PersonResponseDto(
                person.getId(),
                person.getName(),
                person.getSurname(),
                person.getAge(),
                person.getPets().stream()
                        .map(PetApiMapper::toResponse)
                        .toList());
    }
}
