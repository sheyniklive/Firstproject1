package org.example.repository.mapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.example.person.Person;
import org.example.pet.Pet;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@UtilityClass
@Slf4j
public class PersonMapper {
    public static final ObjectMapper mapper = new ObjectMapper();

    public static Person mapPerson(ResultSet rs) throws SQLException {
        UUID uuid = UUID.fromString(rs.getString("id"));
        String name = rs.getString("name");
        String surname = rs.getString("surname");
        Integer age = rs.getInt("age");
        String petsJson = rs.getString("pets");
        List<Pet> pets = new ArrayList<>();
        if (petsJson != null && !petsJson.isEmpty() && !petsJson.equals("[]")) {
            try {
                pets = mapper.readValue(petsJson, new TypeReference<List<Pet>>() {
                });
            } catch (Exception e) {
                log.error("Ошибка при десериализации питомцев при сборке персона", e);
            }
        }
        return new Person(uuid, name, surname, age, pets);
    }
}
