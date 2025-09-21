package org.example.repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.config.DbConfig;
import org.example.person.Person;
import org.example.repository.mapper.PersonMapper;

import java.sql.*;
import java.util.*;

import static org.example.repository.mapper.PersonMapper.mapPerson;
import static org.example.repository.mapper.PersonMapper.mapper;

@AllArgsConstructor
@Slf4j
public class PersonRepository {

    private final DbConfig dbConfig;

    public void save(Person person) {

        String jsonPets = prepareJsonPets(person);

        String sqlSelect = "SELECT 1 FROM persons WHERE id = ? FOR UPDATE";
        String sqlUpdate = "UPDATE persons SET name = ?, surname = ?, age = ?, pets = ? WHERE id = ?";
        String sqlInsert = "INSERT INTO persons (id, name, surname, age, pets) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(dbConfig.getDbUrl(), dbConfig.getUser(), dbConfig.getPassword())) {
            conn.setAutoCommit(false);

            try {
                boolean exists;
                try (PreparedStatement ps = conn.prepareStatement(sqlSelect)) {
                    ps.setString(1, String.valueOf(person.getId()));
                    try (ResultSet rs = ps.executeQuery()) {
                        exists = rs.next();
                    }
                }

                if (exists) {
                    try (PreparedStatement ps = conn.prepareStatement(sqlUpdate)) {
                        ps.setString(1, person.getName());
                        ps.setString(2, person.getSurname());
                        ps.setInt(3, person.getAge());
                        ps.setString(4, jsonPets);
                        ps.setString(5, String.valueOf(person.getId()));
                        ps.executeUpdate();
                    }
                } else {
                    try (PreparedStatement ps = conn.prepareStatement(sqlInsert)) {
                        ps.setString(1, String.valueOf(person.getId()));
                        ps.setString(2, person.getName());
                        ps.setString(3, person.getSurname());
                        ps.setInt(4, person.getAge());
                        ps.setString(5, jsonPets);
                        ps.executeUpdate();
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

    // поиск по name, как в ТЗ, но с появлением UUID, он уже не уникальный
    public Optional<Person> findByName(String name) {
        String sqlSelect = "SELECT id, name, surname, age, pets FROM persons WHERE name = ?";

        try (Connection conn = DriverManager.getConnection(dbConfig.getDbUrl(), dbConfig.getUser(), dbConfig.getPassword())) {
            try (PreparedStatement ps = conn.prepareStatement(sqlSelect)) {
                ps.setString(1, name);
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next() ? Optional.of(PersonMapper.mapPerson(rs)) : Optional.empty();
                }
            }
        } catch (SQLException e) {
            log.error("Ошибка при выгрузке персона из БД", e);
            return Optional.empty();
        }
    }

    // поэтому добавил поиск по UUID
    public Optional<Person> findById(UUID id) {
        String sqlSelect = "SELECT id, name, surname, age, pets FROM persons WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(dbConfig.getDbUrl(), dbConfig.getUser(), dbConfig.getPassword())) {
            try (PreparedStatement ps = conn.prepareStatement(sqlSelect)) {
                ps.setString(1, String.valueOf(id));
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next() ? Optional.of(PersonMapper.mapPerson(rs)) : Optional.empty();
                }
            }
        } catch (SQLException e) {
            log.error("Ошибка при выгрузке персона из БД", e);
            return Optional.empty();

        }
    }

    public List<Person> findAll() {
        String sqlSelect = "SELECT id, name, surname, age, pets FROM persons";

        try (Connection conn = DriverManager.getConnection(dbConfig.getDbUrl(), dbConfig.getUser(), dbConfig.getPassword());
             PreparedStatement ps = conn.prepareStatement(sqlSelect);
             ResultSet rs = ps.executeQuery()) {
            List<Person> persons = new ArrayList<>();
            while (rs.next()) {
                persons.add(mapPerson(rs));
            }
            return persons;

        } catch (SQLException e) {
            log.error("Ошибка при выгрузке персонов из БД, будет выведен пустой список", e);
            return Collections.emptyList();
        }
    }

    public boolean isExistDbData() {
        String sqlCheckSelect = "SELECT EXISTS(SELECT 1 FROM persons LIMIT 1)";

        try (Connection conn = DriverManager.getConnection(dbConfig.getDbUrl(), dbConfig.getUser(), dbConfig.getPassword());
             PreparedStatement ps = conn.prepareStatement(sqlCheckSelect);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getBoolean(1);
            }
        } catch (SQLException e) {
            log.error("Не получилось сделать запрос о наличии содержимого в БД", e);
        }
        return false;
    }


    private String prepareJsonPets(Person person) {
        String jsonPets = "[]";
        try {
            jsonPets = mapper.writeValueAsString(person.getPets());
        } catch (Exception e) {
            log.error("Ошибка при подготовке питомцев в JSON - в БД пойдет пустой", e);
        }
        return jsonPets;
    }

}




