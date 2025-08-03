package org.example.person;

import lombok.ToString;

import java.util.HashMap;
import java.util.Map;
@ToString
public class PersonHolder {
    PersonHolder() {
    }

    public static final Map<String, Person> personHolder = new HashMap<>();
}