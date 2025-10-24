package org.example.person;

import org.example.dto.PersonCreateDto;
import org.example.dto.PersonResponseDto;
import org.example.entity.PersonEntity;
import org.example.entity.PetEntity;
import org.example.pet.Pet;
import org.example.pet.PetApiMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PersonApiMapper {

    public static Person toDomainFromDto(PersonCreateDto dto) {
        return new Person(
                null,
                dto.getName(),
                dto.getSurname(),
                dto.getAge(),
                new ArrayList<>());
    }

    public static PersonResponseDto toResponseDtoFromDomain(Person person) {
        return new PersonResponseDto(
                person.getId(),
                person.getName(),
                person.getSurname(),
                person.getAge(),
                person.getPets().stream()
                        .map(PetApiMapper::toResponseDtoFromDomain)
                        .toList());
    }

    public static PersonEntity toEntityFromDomain(Person person) {
        if (person == null) {
            return null;
        }
        PersonEntity entity = new PersonEntity();
        entity.setName(person.getName());
        entity.setSurname(person.getSurname());
        entity.setAge(person.getAge());
        person.getPets().stream()
                .map(PetApiMapper::toEntityFromDomain)
                .forEach(entity::addPet);

        return entity;
    }

    public static Person toDomainFromEntity(PersonEntity entity) {
        Person person = new Person();
        person.setId(entity.getId());
        person.setId(entity.getId());
        person.setName(entity.getName());
        person.setSurname(entity.getSurname());
        person.setAge(entity.getAge());
        List<Pet> pets = entity.getPets().stream()
                .map(PetApiMapper::toDomainFromEntity)
                .toList();
        person.setPets(pets);
        return person;
    }

}
