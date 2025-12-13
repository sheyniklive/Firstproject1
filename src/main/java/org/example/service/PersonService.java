package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.PersonCreateDto;
import org.example.dto.PersonResponseDto;
import org.example.exception.PersonNotFoundException;
import org.example.person.Person;
import org.example.person.PersonApiMapper;
import org.example.repository.PersonRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository personRepo;

    @Transactional
    public PersonResponseDto create(PersonCreateDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Входные данные не могут быть пустыми");
        }
        Person person = PersonApiMapper.toDomain(dto);
        Person savedPerson = personRepo.save(person);
        log.info("Создан и загружен в БД персон: {}", savedPerson);
        return PersonApiMapper.toResponse(savedPerson);
    }

    @Transactional(readOnly = true)
    public PersonResponseDto getById(UUID id) {
        Person person = personRepo.findByIdOrThrow(id);
        log.info("Персон с id {} загружен", id);
        return PersonApiMapper.toResponse(person);
    }

    @Transactional(readOnly = true)
    public List<PersonResponseDto> list() {
        List<PersonResponseDto> persons = personRepo.findAll().stream()
                .map(PersonApiMapper::toResponse)
                .toList();
        log.info("Персоны загружены из БД");
        return persons;
    }

    @Transactional
    public PersonResponseDto update(UUID id, PersonCreateDto dto) {
        if (!personRepo.existsById(id)) {
            throw new PersonNotFoundException(id);
        }
        Person futurePerson = PersonApiMapper.toDomain(dto);
        futurePerson.setId(id);
        Person updatedPerson = personRepo.save(futurePerson);
        PersonResponseDto response = PersonApiMapper.toResponse(updatedPerson);
        log.info("Персон с id {} обновлен", id);
        return response;
    }

    @Transactional
    public void deleteById(UUID id) {
        Integer deleted = personRepo.deleteById(id);
        if (deleted == 0) {
            throw new PersonNotFoundException(id);
        }
        log.info("Персон с id: {} успешно удален", id);
    }
}
