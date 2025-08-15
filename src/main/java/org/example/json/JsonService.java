package org.example.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.person.PersonHolder;

import java.util.Scanner;

@Slf4j
public class JsonService {
    private final Scanner console = new Scanner(System.in);

    public void processJsonService() {
        ObjectMapper mapper = new ObjectMapper();
    }

    public void savePersonsToFile() {
        log.info("Напиши название файла, в который будем сохранять:");
        String fileName = console.nextLine();

    }


}
