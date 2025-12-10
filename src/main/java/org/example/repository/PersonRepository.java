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

    public Person findById(UUID id) {
        return PersonEntityMapper.toDomain(findPersonEntityByIdOrThrow(id));
    }

    public List<Person> findAll() {
        return personCrudRepo.findAll().stream()
                .map(PersonEntityMapper::toDomain)
                .toList();
    }

    public Person update(UUID id, Person futurePerson) {
        PersonEntity entity = findPersonEntityByIdOrThrow(id);
        entity.setName(futurePerson.getName());
        entity.setSurname(futurePerson.getSurname());
        entity.setAge(futurePerson.getAge());
        entity.getPets().clear();

        return PersonEntityMapper.toDomain(personCrudRepo.save(entity));
    }

    public void deleteById(UUID id) {
        PersonEntity entity = findPersonEntityByIdOrThrow(id);
        personCrudRepo.delete(entity);
    }

    private PersonEntity findPersonEntityByIdOrThrow(UUID id) {
        return personCrudRepo.findById(id)
                .orElseThrow(() -> new PersonNotFoundException(id));
    }
}
