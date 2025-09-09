package org.example.repository;

import lombok.AllArgsConstructor;
import org.example.person.Person;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class PersonRepository {
/*
    private String url = "jdbc:postgresql://как подтянуть из env";
    private String user;
    private String password;

    public PersonRepository() {}
подумать

     */

    public void save(Person person) {
        String sql = "INSERT INTO persons (name, surname, age, petsJson) " +
                "VALUES (?, ?, ?, ?) " +
                "ON CONFLICT (name) " +
                "DO UPDATE SET" +
                "surname = EXCLUDED.surname, " +
                "age = EXCLUDED.age, " +
                "pets = EXCLUDED.pets";
try (Connection conn = DriverManager.getConnection(url, user, password))




    }


    public Optional<Person> findByName(String name) {

    }


    public List<Person> findAll() {

    }


}
