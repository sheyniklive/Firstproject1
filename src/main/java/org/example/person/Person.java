package org.example.person;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.pet.Pet;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class Person {
    private String name;
    private String surname;
    private Integer age;
    private List<Pet> pets;
}