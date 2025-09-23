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
        Person person = new Person();
        person.setId(UUID.fromString(rs.getString("id")));
        person.setName(rs.getString("name"));
        person.setSurname(rs.getString("surname"));
        person.setAge(rs.getInt("age"));
        String petsJson = rs.getString("pets");
        if (petsJson != null && !petsJson.isEmpty() && !petsJson.equals("[]")) {
            try {
                List<Pet> pets = mapper.readValue(petsJson, new TypeReference<>() {
                });
                person.setPets(pets);
            } catch (Exception e) {
                log.error("Ошибка при сборке персона", e);
                person.setPets(new ArrayList<>());
            }
        } else {
            person.setPets(new ArrayList<>());
        }
        return person;
    }
}
