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
import java.util.UUID;
import java.util.stream.Collectors;

public class PersonApiMapper {

    public static Person toDomainFromDto(PersonCreateDto dto) {
        return new Person(
                UUID.randomUUID(),// null сделать
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
        PersonEntity entity = new PersonEntity();
        entity.setId(person.getId());
        entity.setName(person.getName());
        entity.setSurname(person.getSurname());
        entity.setAge(person.getAge());
        Set<PetEntity> pets = person.getPets().stream()
                .map(PetApiMapper::toEntityFromDomain)// FK от персона как добавить
                .collect(Collectors.toSet());
        entity.setPets(pets);
        return entity;
    }
    public static Person toDomainFromEntity(PersonEntity entity) {
        Person person = new Person();
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
