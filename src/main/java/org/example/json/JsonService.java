package org.example.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.example.person.PersonHolder;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

@Slf4j
public class JsonService {
    private final Scanner console = new Scanner(System.in);
    private final ObjectMapper mapper = new ObjectMapper();

    {
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public void processJsonService() {
    }

    private void savePersonsToFile() {
        log.info("Напиши название файла без '.json', в который будем сохранять:");
        String fileName = console.nextLine().trim();
        if (fileName.isEmpty()) {
            log.warn("пустое имя файла, сворачиваемся");
            return;
        }
        try {
            mapper.writeValue(new File(fileName.endsWith(".json") ? fileName : fileName + ".json"), PersonHolder.personHolder);
            log.info("файл {}.json успешно сохранен", fileName);
        } catch (IOException e) {
            log.error("Ошибка при сохранении JSON-файла", e);
        }
    }


}
