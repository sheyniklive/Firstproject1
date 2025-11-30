package org.example.person;

import org.example.entity.PersonEntity;
import org.example.pet.PetEntityMapper;

import java.util.ArrayList;

public class PersonEntityMapper {

    public static PersonEntity toEntity(Person person) {
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
                    .map(PetEntityMapper::toEntity)
                    .forEach(entity::addPet);
        }
        return entity;
    }

    public static Person toDomain(PersonEntity entity) {
        if (entity == null) {
            return null;
        }
        Person person = new Person();
        person.setId(entity.getId());
        person.setName(entity.getName());
        person.setSurname(entity.getSurname());
        person.setAge(entity.getAge());
        person.setPets(new ArrayList<>());
        return person;
    }
}
