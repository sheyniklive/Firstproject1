package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.PersonCreateDto;
import org.example.dto.PersonResponseDto;
import org.example.person.Person;
import org.example.person.PersonApiMapper;
import org.example.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository repoV2;

    public PersonResponseDto createPerson(PersonCreateDto dto) {
        Person person = PersonApiMapper.toDomain(dto);
        if (person == null) {
            throw new IllegalArgumentException("Персон не может быть пустым");
        }
        Person savedPerson = repoV2.save(person);
        log.info("Создан и загружен в БД персон: {}", savedPerson);
        return PersonApiMapper.toResponse(savedPerson);
    }

    public PersonResponseDto getPersonById(UUID id) {
        Person person = repoV2.findByIdOrThrow(id);
        log.info("Персон с id {} загружен", id);
        return PersonApiMapper.toResponse(person);
    }

    public List<PersonResponseDto> getAllPersons() {
        List<PersonResponseDto> persons = repoV2.findAll().stream()
                .map(PersonApiMapper::toResponse)
                .toList();
        log.info("Персоны загружены из БД");
        return persons;
    }

    public PersonResponseDto fullUpdatePerson(UUID id, PersonCreateDto dto) {
        Person person = repoV2.findByIdOrThrow(id);
        person.setName(dto.getName());
        person.setSurname(dto.getSurname());
        person.setAge(dto.getAge());
        person.setPets(new ArrayList<>());
        Person updatedPerson = repoV2.save(person);
        log.info("Персон с id {} обновлен", id);
        return PersonApiMapper.toResponse(updatedPerson);
    }

    public void deletePersonById(UUID id) {
        repoV2.deleteByIdOrThrow(id);
        log.info("Персон с id: {} успешно удален", id);
    }
}
