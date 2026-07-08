package org.example.services;

import org.example.dto.PersonCreateDto;
import org.example.dto.PersonResponseDto;
import org.example.person.Person;
import org.example.repository.PersonRepository;
import org.example.service.PersonService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    @Mock
    private PersonRepository repo;

    @InjectMocks
    private PersonService service;

    @Test
    @DisplayName("create: сохраняет человека и возвращает ответ с присвоенным id")
    void create_shouldSaveAndReturnResponse() {
        PersonCreateDto dto = new PersonCreateDto("Иван", "Петров", 30, "ivan@mail.ru");
        UUID savedId= UUID.randomUUID();
        Person saved = new Person(savedId, "Иван", "Петров", 30, "ivan@mail.ru", new ArrayList<>());
        when(repo.save(any(Person.class))).thenReturn(saved);

        PersonResponseDto result = service.create(dto);

        assertThat(result.getId()).isEqualTo(savedId);
        assertThat(result.getName()).isEqualTo("Иван");

        ArgumentCaptor<Person> captor = ArgumentCaptor.forClass(Person.class);
        verify(repo).save(captor.capture());
        Person givedToRepo = captor.getValue();
        assertThat(givedToRepo.getId()).isNull();
        assertThat(givedToRepo.getEmail()).isEqualTo("ivan@mail.ru");
    }

    @Test
    @DisplayName("create: на null кидает IllegalArgumentException и не трогает репозиторий")
    void create_shouldThrow_whenDtoIsNull() {
        assertThatThrownBy(() -> service.create(null))
        .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Входные данные не могут быть пустыми");

        verifyNoInteractions(repo);
    }

    @Test
    @DisplayName("getById: возвращает ответ по найденному человеку")
    void getById_shouldReturnResponse() {
        UUID id = UUID.randomUUID();
        Person person = new Person(id, "Иван", "Петров", 30, "ivan@mail.ru", new ArrayList<>());
        when(repo.save(any(Person.class))).thenReturn(person);

        PersonResponseDto result = service.getById(id);

        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getName()).isEqualTo("Иван");
        verify(repo).findByIdOrThrow(id);
    }





}
