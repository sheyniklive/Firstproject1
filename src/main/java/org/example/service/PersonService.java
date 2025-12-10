package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.PersonCreateDto;
import org.example.dto.PersonResponseDto;
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
    public PersonResponseDto createPerson(PersonCreateDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Входные данные не могут быть пустыми");
        }
        Person person = PersonApiMapper.toDomain(dto);
        Person savedPerson = personRepo.save(person);
        log.info("Создан и загружен в БД персон: {}", savedPerson);
        return PersonApiMapper.toResponse(savedPerson);
    }

    @Transactional(readOnly = true)
    public PersonResponseDto getPersonById(UUID id) {
        Person person = personRepo.findById(id);
        log.info("Персон с id {} загружен", id);
        return PersonApiMapper.toResponse(person);
    }

    @Transactional(readOnly = true)
    public List<PersonResponseDto> getAllPersons() {
        List<PersonResponseDto> persons = personRepo.findAll().stream()
                .map(PersonApiMapper::toResponse)
                .toList();
        log.info("Персоны загружены из БД");
        return persons;
    }

    @Transactional
    public PersonResponseDto fullUpdatePerson(UUID id, PersonCreateDto dto) {
        Person futurePerson = PersonApiMapper.toDomain(dto);
        Person updatedPerson = personRepo.update(id, futurePerson);
        PersonResponseDto response = PersonApiMapper.toResponse(updatedPerson);
        log.info("Персон с id {} обновлен", id);
        return response;
    }

    @Transactional
    public void deletePersonById(UUID id) {
        personRepo.deleteById(id);
        log.info("Персон с id: {} успешно удален", id);
    }

}
