package org.example.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.example.exception.InvalidMenuChoiceException;
import org.example.person.PersonHolder;
import org.example.validator.Validators;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import static org.example.Main.menuStack;

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
        log.info("введи exit для выхода или название файла, в который будем сохранять:");
        String fileName = console.nextLine().trim();
        if (fileName.equalsIgnoreCase("exit")) {
            menuStack.removeLast();
            return;
        }
        if (fileName.isEmpty()) {
            log.warn("пустое имя файла, начнем сначала");
            return;
        }
        String fullFileName = fileName.endsWith(".json") ? fileName : fileName + ".json";
        File file = new File(fullFileName);
        if (file.exists()) {
            log.warn("файл {} уже существует - перезаписать? (1 - да, 0 - нет)", fullFileName);
            while (true) {
                String choiceActionWithFile = console.nextLine();
                try {
                    Validators.yesNo.validate(choiceActionWithFile);
                    if (!choiceActionWithFile.equals("1")) {
                        log.warn("отмена записи в файл - идем в начало");
                        return;
                    }
                    break;
                } catch (InvalidMenuChoiceException e) {
                    log.error("неверный ввод действия", e);
                    log.info("повтори 1 или 0");
                }
            }
        }
        try {
            mapper.writeValue(file, PersonHolder.personHolder);
            log.info("файл {} успешно сохранен", fullFileName);
        } catch (IOException e) {
            log.error("Ошибка при сохранении JSON-файла", e);
        }
    }


}
