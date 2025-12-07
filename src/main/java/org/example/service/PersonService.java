package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.PersonCreateDto;
import org.example.dto.PersonResponseDto;
import org.example.entity.PersonEntity;
import org.example.exception.PersonNotFoundException;
import org.example.person.Person;
import org.example.person.PersonApiMapper;
import org.example.person.PersonEntityMapper;
import org.example.repository.PersonRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository personRepo;

    @Transactional
    public PersonResponseDto createPerson(PersonCreateDto dto) {
        Person person = PersonApiMapper.toDomain(dto);
        if (person == null) {
            throw new IllegalArgumentException("Персон не может быть пустым");
        }
        PersonEntity savedPerson = personRepo.save(PersonEntityMapper.toEntity(person));
        log.info("Создан и загружен в БД персон: {}", savedPerson);
        return PersonApiMapper.toResponse(PersonEntityMapper.toDomain(savedPerson));
    }

    @Transactional(readOnly = true)
    public PersonResponseDto getPersonById(UUID id) {
        PersonEntity personEntity = getPersonEntityById(id);
        log.info("Персон с id {} загружен", id);
        return PersonApiMapper.toResponse(PersonEntityMapper.toDomain(personEntity));
    }

    @Transactional(readOnly = true)
    public List<PersonResponseDto> getAllPersons() {
        List<PersonResponseDto> persons = personRepo.findAll().stream()
                .filter(Objects::nonNull)
                .map(PersonEntityMapper::toDomain)
                .map(PersonApiMapper::toResponse)
                .toList();
        log.info("Персоны загружены из БД");
        return persons;
    }

    @Transactional
    public PersonResponseDto fullUpdatePerson(UUID id, PersonCreateDto dto) {
        PersonEntity entity = getPersonEntityById(id);
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setAge(dto.getAge());
        entity.getPets().clear();

        PersonEntity updatedPerson = personRepo.save(entity);
        log.info("Персон с id {} обновлен", id);
        return PersonApiMapper.toResponse(PersonEntityMapper.toDomain(updatedPerson));
    }

    @Transactional
    public void deletePersonById(UUID id) {
        PersonEntity entity = getPersonEntityById(id);
        personRepo.delete(entity);
        log.info("Персон с id: {} успешно удален", id);
    }

    private PersonEntity getPersonEntityById(UUID id) {
        return personRepo.findById(id)
                .orElseThrow(() -> new PersonNotFoundException(id));
    }
}
