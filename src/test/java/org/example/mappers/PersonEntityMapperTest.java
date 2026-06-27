package org.example.mappers;

import org.example.entity.PersonEntity;
import org.example.entity.PetEntity;
import org.example.person.Person;
import org.example.pet.Cat;
import org.example.pet.Pet;
import org.example.pet.enums.PetType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class PersonEntityMapperTest {

    @Test
    @DisplayName("toEntity: успешно маппит все поля кроме питомцев")
    void toEntity_shouldMapAllFields_withoutPets() {
        UUID personId = UUID.randomUUID();
        Person person = new Person(personId, "Вася", "Пупкин", 15, "11_11@mail.ru", new ArrayList<>());

        PersonEntity personEntity = PersonEntityMapper.toEntity(person);

        assertThat(personEntity).isNotNull();
        assertThat(personEntity.getId()).isEqualTo(personId);
        assertThat(personEntity.getName()).isEqualTo("Вася");
        assertThat(personEntity.getSurname()).isEqualTo("Пупкин");
        assertThat(personEntity.getAge()).isEqualTo(15);
        assertThat(personEntity.getEmail()).isEqualTo("11_11@mail.ru");
        assertThat(personEntity.getPets()).isEmpty();
    }

    @Test
    @DisplayName("toEntity: маппит питомца и создает ссылку на конкретного человека")
    void toEntity_shouldMapPet_withBackReferenceToPersonEntity() {
        Pet cat = new Cat("Рибис");
        UUID personId = UUID.randomUUID();
        Person person = new Person(personId, "Вася", "Пупкин", 15, "11_11@mail.ru", List.of(cat));

        PersonEntity personEntity = PersonEntityMapper.toEntity(person);

        assertThat(personEntity).isNotNull();
        assertThat(personEntity.getPets()).hasSize(1)
                .allSatisfy(petEntity -> {
                    assertThat(petEntity.getName()).isEqualTo("Рибис");
                    assertThat(petEntity.getType()).isEqualTo(PetType.CAT);
                    assertThat(petEntity.getOwner()).isSameAs(personEntity);
                });
    }

    @Test
    @DisplayName("toEntity: у Entity остается null в id, если у Person был null")
    void toEntity_shouldEntityIdIsNull_whenPersonIdIsNull() {
        Person person = new Person(null, "Вася", "Пупкин", 15, "11_11@mail.ru", new ArrayList<>());

        PersonEntity personEntity = PersonEntityMapper.toEntity(person);

        assertThat(personEntity).isNotNull();
        assertThat(personEntity.getId()).isNull();
    }

    @Test
    @DisplayName("toEntity: на null возвращает null")
    void toEntity_shouldReturnNull_whenPersonIsNull() {
        assertThat(PersonEntityMapper.toEntity(null)).isNull();
    }

    @Test
    @DisplayName("toDomain: переносит простые поля")
    void toDomain_shouldMapAllSingleFields() {
        UUID id = UUID.randomUUID();
        PersonEntity entity = new PersonEntity();
        entity.setId(id);
        entity.setName("Иван");
        entity.setSurname("Петров");
        entity.setAge(30);
        entity.setEmail("ivan@mail.ru");

        Person person = PersonEntityMapper.toDomain(entity);

        assertThat(person).isNotNull();
        assertThat(person.getId()).isEqualTo(id);
        assertThat(person.getName()).isEqualTo("Иван");
        assertThat(person.getSurname()).isEqualTo("Петров");
        assertThat(person.getAge()).isEqualTo(30);
        assertThat(person.getEmail()).isEqualTo("ivan@mail.ru");
    }

    @Test
    @DisplayName("toDomain: в Person всегда пустой pets")
    void toDomain_shouldIgnorePetEntities() {
        PetEntity petEntity = new PetEntity();
        petEntity.setId(1L);
        petEntity.setName("Пит");
        petEntity.setType(PetType.CAT);

        UUID id = UUID.randomUUID();
        PersonEntity entity = new PersonEntity();
        entity.setId(id);
        entity.setName("Иван");
        entity.setSurname("Петров");
        entity.setAge(30);
        entity.setEmail("ivan@mail.ru");
        entity.addPet(petEntity);

        Person person = PersonEntityMapper.toDomain(entity);

        assertThat(person).isNotNull();
        assertThat(person.getPets()).isEmpty();
    }

    @Test
    @DisplayName("toDomain: на null возвращает null")
    void toDomain_shouldReturnNull_whenEntityIsNull() {
        assertThat(PersonEntityMapper.toDomain(null)).isNull();
    }
}
