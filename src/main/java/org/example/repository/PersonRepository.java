package org.example.repository;

import lombok.AllArgsConstructor;
import org.example.config.DbConfig;
import org.example.person.Person;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class PersonRepository() {

    private final DbConfig dbConfig;

    public void save(Person person) {
        String url = "jdbc:postgresql://" + dbConfig.getHost() + ":" + dbConfig.getPort() + "/" + dbConfig.getDbName();
        String sql = "INSERT INTO persons (name, surname, age, petsJson) " +
                "VALUES (?, ?, ?, ?) " +
                "ON CONFLICT (name) " +
                "DO UPDATE SET" +
                "surname = EXCLUDED.surname, " +
                "age = EXCLUDED.age, " +
                "pets = EXCLUDED.pets";
        try (Connection conn = DriverManager.getConnection(url, dbConfig.getUser(), dbConfig.getPassword()))



    }


    public Optional<Person> findByName(String name) {

    }


    public List<Person> findAll() {

    }


}
