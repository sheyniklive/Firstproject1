package org.example.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.config.DbConfig;
import org.example.person.Person;
import org.example.pet.Pet;
import org.example.repository.mapper.PersonMapper;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.*;

import static org.example.repository.mapper.PersonMapper.mapPerson;
import static org.example.repository.mapper.PersonMapper.mapper;

@Repository
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
            boolean exists;
            try (PreparedStatement psSelect = conn.prepareStatement(sqlSelect)) {
                psSelect.setString(1, String.valueOf(person.getId()));
                try (ResultSet rs = psSelect.executeQuery()) {
                    exists = rs.next();
                }
            }
            if (exists) {
                try (PreparedStatement psUpdate = conn.prepareStatement(sqlUpdate)) {
                    psUpdate.setString(1, person.getName());
                    psUpdate.setString(2, person.getSurname());
                    psUpdate.setInt(3, person.getAge());
                    psUpdate.setString(4, jsonPets);
                    psUpdate.setString(5, String.valueOf(person.getId()));
                    psUpdate.executeUpdate();
                }
            } else {
                try (PreparedStatement psInsert = conn.prepareStatement(sqlInsert)) {
                    psInsert.setString(1, String.valueOf(person.getId()));
                    psInsert.setString(2, person.getName());
                    psInsert.setString(3, person.getSurname());
                    psInsert.setInt(4, person.getAge());
                    psInsert.setString(5, jsonPets);
                    psInsert.executeUpdate();
                }
            }
            log.info("в БД успешно отправлен персон: {}", person);
        } catch (SQLException e) {
            log.error("Ошибка при сохранении персона в БД", e);
        }
    }

    public boolean deleteById(UUID id) {
        String sqlDelete = "DELETE FROM persons WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(dbConfig.getDbUrl(), dbConfig.getUser(), dbConfig.getPassword());
             PreparedStatement ps = conn.prepareStatement(sqlDelete)) {

            ps.setString(1, String.valueOf(id));
            int rowAffected = ps.executeUpdate();

            if (rowAffected > 0) {
                log.info("Успешно удален из БД персон с id: {}", id);
                return true;
            } else {
                log.warn("Не удалось удалить из БД персона с id: {}", id);
                return false;
            }
        } catch (SQLException e) {
            log.error("Ошибка при удалении персона с id({}) из БД", id, e);
            return false;
        }
    }

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
            log.error("Ошибка при выгрузке персон из БД, будет выведен пустой список", e);
            return Collections.emptyList();
        }
    }

    public Map<String, String> showAllNames() {
        String sqlSelectAllNamesAndId = "SELECT id, name FROM persons";
        try (Connection conn = DriverManager.getConnection(dbConfig.getDbUrl(), dbConfig.getUser(), dbConfig.getPassword());
             PreparedStatement ps = conn.prepareStatement(sqlSelectAllNamesAndId);
             ResultSet rs = ps.executeQuery()) {
            Map<String, String> namesAndId = new TreeMap<>();
            while (rs.next()) {
                namesAndId.put(rs.getString("name"), rs.getString("id"));
            }
            return namesAndId;
        } catch (SQLException e) {
            log.error("Ошибка при выгрузке имен из БД", e);
            return Collections.emptyMap();
        }
    }

    public boolean isExistDbData() {
        String sqlCheckSelect = "SELECT EXISTS(SELECT 1 FROM persons LIMIT 1)";

        try (Connection conn = DriverManager.getConnection(dbConfig.getDbUrl(), dbConfig.getUser(), dbConfig.getPassword());
             PreparedStatement ps = conn.prepareStatement(sqlCheckSelect);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getBoolean(1);
            } else return false;
        } catch (SQLException e) {
            log.error("Не получилось сделать запрос о наличии содержимого в БД", e);
            return false;
        }
    }

    private String prepareJsonPets(Person person) {
        String jsonPets = "[]";
        try {
            List<Pet> pets = person.getPets();
            jsonPets = mapper.writerFor(new TypeReference<List<Pet>>() {
                    })
                    .writeValueAsString(pets);
            log.info("перед отправкой в БД подготовили персону по имени '{}' json-строку в поле питомцев: {}", person.getName(), jsonPets);
        } catch (Exception e) {
            log.error("Ошибка при подготовке питомцев в JSON - в БД пойдет пустой", e);
        }
        return jsonPets;
    }

}




