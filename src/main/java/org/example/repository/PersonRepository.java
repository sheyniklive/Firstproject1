package org.example.repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.config.DbConfig;
import org.example.person.Person;

import java.sql.*;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Slf4j
public class PersonRepository() {

    private final DbConfig dbConfig;

    public void save(Person person) {

        String sqlSelect = "SELECT id, name, surname, age, pets FROM persons WHERE name = ? FOR UPDATE";
        String sqlUpdate = "UPDATE persons SET surname = ?, age = ?, pets = ? WHERE name = ?";
        String sqInsert = "INSERT INTO persons (name, surname, age, pets) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(dbConfig.getDbUrl(), dbConfig.getUser(), dbConfig.getPassword())) {
            conn.setAutoCommit(false);
            boolean exists;

            try (PreparedStatement ps = conn.prepareStatement(sqlSelect)) {
                ps.setString(1, person.getName());
                try (ResultSet rs = ps.executeQuery()) {
                    exists = rs.next();
                }
            }
            if (exists) {
                try (PreparedStatement ps = conn.prepareStatement(sqlUpdate)) {
                    ps.setString(1, person.getSurname());
                    ps.setInt(2, person.getAge());
                    ps.setString(3, person.getPets());// подумать с objectmapper`ом
                    ps.setString(4, person.getName());
                    ps.executeUpdate();
                }
            } else {
                try (PreparedStatement ps = conn.prepareStatement(sqInsert)) {
                    ps.setString(1, person.getName());
                    ps.setString(2, person.getSurname());
                    ps.setInt(3, person.getAge());
                    ps.setString(4, person.getPets());  // подумать как сразу давать после маппера
                    ps.executeUpdate();
                }
            }
            conn.commit();
        } catch (SQLException e) {
            log.error("Ошибка при сохранении персона в БД", e);
        }
    }


        public Optional<Person> findByName (String name){

        }


        public List<Person> findAll () {

        }


    }
