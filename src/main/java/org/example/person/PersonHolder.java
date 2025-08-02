package org.example.person;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
@Slf4j
public class PersonHolder {
    public static Map<String, Person> personHolder = new HashMap<>();
}