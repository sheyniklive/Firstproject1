package org.example.services;

import org.example.dto.PersonCreateDto;
import org.example.dto.PersonResponseDto;
import org.example.exception.PersonNotFoundException;
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
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
        UUID savedId = UUID.randomUUID();
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
        when(repo.findByIdOrThrow(id)).thenReturn(person);

        PersonResponseDto result = service.getById(id);

        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getName()).isEqualTo("Иван");
        verify(repo).findByIdOrThrow(id);
    }

    @Test
    @DisplayName("getById: пробрасывает PersonNotFoundException из репозитория")
    void getById_shouldPropagatePNF() {
        UUID id = UUID.randomUUID();
        when(repo.findByIdOrThrow(id)).thenThrow(new PersonNotFoundException(id));

        assertThatThrownBy(() -> service.getById(id))
                .isInstanceOf(PersonNotFoundException.class);
    }

    @Test
    @DisplayName("list: маппит всех людей в ответы")
    void list_shouldMapAll() {
        Person p1 = new Person(UUID.randomUUID(), "Иван", "Петров", 30, "i@mail.ru", new ArrayList<>());
        Person p2 = new Person(UUID.randomUUID(), "Пётр", "Иванов", 40, "p@mail.ru", new ArrayList<>());
        when(repo.findAll()).thenReturn(List.of(p1, p2));

        List<PersonResponseDto> result = service.list();

        assertThat(result)
                .hasSize(2)
                .extracting(PersonResponseDto::getName)
                .containsExactly("Иван", "Пётр");
    }

    @Test
    @DisplayName("list: пустой список отдаёт как пустой")
    void list_shouldReturnEmpty_whenNoPersons() {
        when(repo.findAll()).thenReturn(List.of());
        assertThat(service.list()).isEmpty();
    }

    @Test
    @DisplayName("update: при существующем id сохраняет с этим id и возвращает ответ")
    void update_shouldSaveWithGivenId() {
        UUID id = UUID.randomUUID();
        PersonCreateDto createDto = new PersonCreateDto("Иван", "Петров", 31, "ivan@mail.ru");
        when(repo.existsById(id)).thenReturn(true);
        Person saved = new Person(id, "Иван", "Петров", 31, "ivan@mail.ru", new ArrayList<>());
        when(repo.save(any(Person.class))).thenReturn(saved);

        PersonResponseDto result = service.update(id, createDto);

        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getAge()).isEqualTo(31);

        ArgumentCaptor<Person> captor = ArgumentCaptor.forClass(Person.class);
        verify(repo).save(captor.capture());
        Person givedToRepo = captor.getValue();
        assertThat(givedToRepo.getId()).isEqualTo(id);
    }

    @Test
    @DisplayName("update: если id не существует в БД — PersonNotFoundException и не пойдет в репо")
    void update_shouldThrowPNF_whenIdIsNotExistInDB() {
        UUID id = UUID.randomUUID();
        PersonCreateDto createDto = new PersonCreateDto("Иван", "Петров", 31, "ivan@mail.ru");
        when(repo.existsById(id)).thenReturn(false);

        assertThatThrownBy(() -> service.update(id, createDto))
                .isInstanceOf(PersonNotFoundException.class);
        verify(repo, never()).save(any());
    }

    @Test
    @DisplayName("deleteById: при удалённой строке просто отрабатывает")
    void deleteById_shouldSucceed_whenRowDeleted() {
        UUID id = UUID.randomUUID();
        when(repo.deleteById(id)).thenReturn(1);

        service.deleteById(id);

        verify(repo).deleteById(id);
    }

    @Test
    @DisplayName("deleteById: если ничего не удалено — PersonNotFoundException")
    void deleteById_shouldThrowPNF_whenNothingDeleted() {
        UUID id = UUID.randomUUID();
        when(repo.deleteById(id)).thenReturn(0);

        assertThatThrownBy(() -> service.deleteById(id))
                .isInstanceOf(PersonNotFoundException.class);
    }
}
