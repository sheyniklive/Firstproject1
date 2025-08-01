package org.example.person;

import java.util.HashMap;
import java.util.Map;

public class PersonHolder {
    public static Map<String, Person> personHolder = new HashMap<>();

    public void addPersonToHolder(Person person) {
        personHolder.put(person.getName(), person);
    }
}