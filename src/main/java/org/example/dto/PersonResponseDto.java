package org.example.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.person.Person;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
public class PersonResponseDto {
    private UUID id;
    private String name;
    private String surname;
    private int age;
    private List<PetDto> pets;

    public PersonResponseDto(Person person) {
        this.id = person.getId();
        this.name = person.getName();
        this.surname = person.getSurname();
        this.age = person.getAge();
        this.pets = person.getPets().stream()
                .map(PetDto::new)
                .collect(Collectors.toList());
    }
}
