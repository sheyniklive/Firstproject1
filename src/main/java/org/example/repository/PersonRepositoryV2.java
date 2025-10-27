package org.example.repository;

import org.example.entity.PersonEntity;
import org.example.person.Person;
import org.example.person.PersonApiMapper;

public class PersonRepositoryV2 {

    public void save(Person person) {
        PersonEntity entity = PersonApiMapper.toEntityFromDomain(person);

    }


}
