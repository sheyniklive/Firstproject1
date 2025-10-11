package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.PersonCreateDto;
import org.example.dto.PersonResponseDto;
import org.example.exception.PersonNotFoundException;
import org.example.person.Person;
import org.example.repository.PersonRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class PersonServiceV2 {

    private final PersonRepository repo;

    public PersonResponseDto createPerson(PersonCreateDto dto) {
        Person person = new Person(
                UUID.randomUUID(),
                dto.getName(),
                dto.getSurname(),
                dto.getAge(),
                new ArrayList<>());
        repo.save(person);
        log.info("Создан и загружен в БД персон: {}", person);
        return new PersonResponseDto(person);
    }

    public PersonResponseDto getPersonById(UUID id) {
        Person person = repo.findById(id)
                .orElseThrow(() -> new PersonNotFoundException(String.valueOf(id)));
        return new PersonResponseDto(person);
    }

    public List<PersonResponseDto> getAllPersons() {
        List<Person> persons = repo.findAll();

        return persons.stream()
                .map(PersonResponseDto::new)
                .collect(Collectors.toList());
    }
}
