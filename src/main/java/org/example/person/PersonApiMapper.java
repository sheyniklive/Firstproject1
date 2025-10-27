package org.example.person;

import org.example.dto.PersonCreateDto;
import org.example.dto.PersonResponseDto;
import org.example.entity.PersonEntity;
import org.example.pet.Pet;
import org.example.pet.PetApiMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class PersonApiMapper {

    public static Person toDomainFromDto(PersonCreateDto dto) {
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

    public static PersonResponseDto toResponseDtoFromDomain(Person person) {
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
                        .map(PetApiMapper::toResponseDtoFromDomain)
                        .toList());
    }

    public static PersonEntity toEntityFromDomain(Person person) {
        if (person == null) {
            return null;
        }
        PersonEntity entity = new PersonEntity();
        if (person.getId() != null) {
            entity.setId(person.getId());
        }
        entity.setName(person.getName());
        entity.setSurname(person.getSurname());
        entity.setAge(person.getAge());

        if (person.getPets() != null && !person.getPets().isEmpty()) {
            person.getPets().stream()
                    .map(PetApiMapper::toEntityFromDomain)
                    .forEach(entity::addPet);
        }
        return entity;
    }

    public static Person toDomainFromEntity(PersonEntity entity) {
        if (entity == null) {
            return null;
        }
        Person person = new Person();
        person.setId(entity.getId());
        person.setName(entity.getName());
        person.setSurname(entity.getSurname());
        person.setAge(entity.getAge());
        List<Pet> pets = new ArrayList<>();
        if (entity.getPets() != null && !entity.getPets().isEmpty()) {
            pets = entity.getPets().stream()
                    .map(PetApiMapper::toDomainFromEntity)
                    .toList();
        }
        person.setPets(pets);
        return person;
    }

}
