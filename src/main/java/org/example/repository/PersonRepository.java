package org.example.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final ObjectMapper mapper = new ObjectMapper();

    public void save(Person person) {
        String jsonPets = null;
        try {
            jsonPets = mapper.writeValueAsString(person.getPets());
        } catch (Exception e) {
            log.error("Ошибка при подготовке питомцев в JSON - в БД пойдет пустой", e);
        }

        String sqlSelect = "SELECT 1 FROM persons WHERE name = ? FOR UPDATE";
        String sqlUpdate = "UPDATE persons SET surname = ?, age = ?, pets = ? WHERE name = ?";
        String sqlInsert = "INSERT INTO persons (name, surname, age, pets) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(dbConfig.getDbUrl(), dbConfig.getUser(), dbConfig.getPassword())) {
            conn.setAutoCommit(false);

            try {
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
                        ps.setString(3, jsonPets);
                        ps.setString(4, person.getName());
                        ps.executeUpdate();
                    }
                } else {
                    Savepoint savepoint = conn.setSavepoint();
                    try (PreparedStatement ps = conn.prepareStatement(sqlInsert)) {
                        ps.setString(1, person.getName());
                        ps.setString(2, person.getSurname());
                        ps.setInt(3, person.getAge());
                        ps.setString(4, jsonPets);
                        ps.executeUpdate();
                    } catch (SQLException e) {
                        if ("23505".equals(e.getSQLState())) {
                            conn.rollback(savepoint);
                            try (PreparedStatement ps = conn.prepareStatement(sqlUpdate)) {
                                ps.setString(1, person.getSurname());
                                ps.setInt(2, person.getAge());
                                ps.setString(3, jsonPets);
                                ps.setString(4, person.getName());
                                ps.executeUpdate();
                            }
                        } else {
                            throw e;
                        }
                    }
                }
                conn.commit();
            } catch (SQLException connEx) {
                try {
                    conn.rollback();
                } catch (SQLException rbEx) {
                    log.error("Не удалось откатить транзакцию", rbEx);
                }
                throw connEx;
            } finally {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException autoCommitEx) {
                    log.warn("Не удалось вернуть auto-commit", autoCommitEx);
                }
            }
        } catch (SQLException e) {
            log.error("Ошибка при сохранении персона в БД", e);
        }
    }


    public Optional<Person> findByName(String name) {
        String sqlSelect = "SELECT id, name, surname, age, pets FROM persons WHERE name = ?";

        try (Connection conn = DriverManager.getConnection(dbConfig.getDbUrl(), dbConfig.getUser(), dbConfig.getPassword()) {
            try (PreparedStatement ps) {

            }

        }
    }


    public List<Person> findAll() {

    }


}



