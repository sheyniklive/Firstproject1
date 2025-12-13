package org.example.repository;

import lombok.RequiredArgsConstructor;
import org.example.entity.PersonEntity;
import org.example.exception.PersonNotFoundException;
import org.example.person.Person;
import org.example.person.PersonEntityMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PersonRepository {

    private final PersonCrudRepository personCrudRepo;

    public Person save(Person person) {
        PersonEntity entity = PersonEntityMapper.toEntity(person);
        PersonEntity savedEntity = personCrudRepo.save(entity);
        return PersonEntityMapper.toDomain(savedEntity);
    }

    public Person findByIdOrThrow(UUID id) {
        PersonEntity entity = personCrudRepo.findById(id)
                .orElseThrow(() -> new PersonNotFoundException(id));
        return PersonEntityMapper.toDomain(entity);
    }

    public List<Person> findAll() {
        return personCrudRepo.findAll().stream()
                .map(PersonEntityMapper::toDomain)
                .toList();
    }

    public Integer deleteById(UUID id) {
        return personCrudRepo.deletePersonById(id);
    }

    public boolean existsById(UUID id) {
        return personCrudRepo.existsById(id);
    }

}
