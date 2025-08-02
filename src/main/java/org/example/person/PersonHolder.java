package org.example.person;

import java.util.HashMap;
import java.util.Map;

public class PersonHolder {
    PersonHolder() {
    }

    public static final Map<String, Person> personHolder = new HashMap<>();
}