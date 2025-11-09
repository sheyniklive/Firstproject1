package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.PersonCreateDto;
import org.example.dto.PersonResponseDto;
import org.example.person.Person;
import org.example.person.PersonApiMapper;
import org.example.repository.PersonRepository;
import org.example.repository.PersonRepositoryV2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class PersonServiceV2 {

    private final PersonRepository repo;
    private final PersonRepositoryV2 repoV2;

    public PersonResponseDto createPerson(PersonCreateDto dto) {
        Person person = PersonApiMapper.toDomainFromDto(dto);
        repoV2.save(person);
        log.info("Создан и загружен в БД персон: {}", person);
        return PersonApiMapper.toResponseDtoFromDomain(person);
    }

    public Optional<PersonResponseDto> getPersonById(UUID id) {
        return repoV2.findById(id)
                .map(PersonApiMapper::toResponseDtoFromDomain);
    }

    public List<PersonResponseDto> getAllPersons() {
        return repoV2.findAll().stream()
                .map(PersonApiMapper::toResponseDtoFromDomain)
                .toList();
    }

    public Optional<PersonResponseDto> fullUpdatePerson(UUID id, PersonCreateDto dto) {
        return repo.findById(id)
                .map(person -> {
                    person.setName(dto.getName());
                    person.setSurname(dto.getSurname());
                    person.setAge(dto.getAge());
                    person.setPets(new ArrayList<>());
                    repo.save(person);
                    return PersonApiMapper.toResponseDtoFromDomain(person);
                });
    }

    public boolean deletePersonById(UUID id) {
        return repoV2.deleteById(id);
    }
}
