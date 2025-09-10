package org.example.repository;

import lombok.AllArgsConstructor;
import org.example.config.DbConfig;
import org.example.person.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class PersonRepository() {

    private static final Logger log = LoggerFactory.getLogger(PersonRepository.class);
    private final DbConfig dbConfig;

    public void save(Person person) {
        String sql = """
                MERGE INTO persons AS p
                USING (VALUES (?, ?, ?, ?)) AS v(name, surname, age, pets)
                ON p.name = v.name
                WHEN MATCHED THEN
                    UPDATE SET
                        surname = v.surname,
                        age = v.age,
                        pets = v.pets
                WHEN NOT MATCHED THEN
                    INSERT (name, surname, age, pets)
                    VALUES (v.name,v.surname,v.age,v.pets)
                """;
        try (Connection conn = DriverManager.getConnection(dbConfig.getDbUrl(), dbConfig.getUser(), dbConfig.getPassword());
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, person.getName());
            ps.setString(2, person.getSurname());
            ps.setInt(3, person.getAge());
            ps.setString(4, person.getPets());  // подумать как сразу давать после маппера
            ps.executeUpdate();
        } catch (SQLException e) {
            log.error("Ошибка при сохранении персона в БД", e);
        }


    }


    public Optional<Person> findByName(String name) {

    }


    public List<Person> findAll() {

    }


}
