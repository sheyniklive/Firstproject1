package org.example.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.example.exception.InvalidMenuChoiceException;
import org.example.person.Person;
import org.example.person.PersonHolder;
import org.example.util.ExitsUtils;
import org.example.validator.Validators;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

import static org.example.Main.menuStack;

@Slf4j
public class JsonService {
    private final Scanner console = new Scanner(System.in);
    private final ObjectMapper mapper = new ObjectMapper();
    private String fileName;

    {
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    private final Map<String, Runnable> choiceJsonServiceMenu = Map.of(
            "1", this::savePersonsToFile,
            "2", this::loadPersonsFromFile,
            "3", this::showJsonContent
    );

    public void processJsonService() {
        log.info("выбери, как будем взаимодействовать с Json`ом:");
        log.info("1 - сохраним персонов в файл");
        log.info("2 - выгрузим из файла");
        log.info("3 - посмотрим содержимое файла");
        log.info("exit - вернемся в главное меню");
        String input;
        while (true) {
            try {
                input = console.nextLine();
                Validators.choiceMenuOf3.validate(input);
                break;
            } catch (InvalidMenuChoiceException e) {
                log.error("неверный выбор меню", e);
                log.info("повтори: {} или exit", choiceJsonServiceMenu.keySet());
            }
        }
        if (input.equalsIgnoreCase("exit")) {
            menuStack.removeLast();
            return;
        }
        Runnable next = choiceJsonServiceMenu.get(input);
        menuStack.addLast(next);
    }

    private void savePersonsToFile() {
        log.info("введи exit для выхода или название файла, в который будем сохранять:");
        fileName = console.nextLine().trim();
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
            log.info("пойдем в начало");
            return;
        }
        ExitsUtils.informingBack();
    }

    private void loadPersonsFromFile() {
        log.info("введи exit или имя файла, из которого будем брать (без '.json'):");
        fileName = console.nextLine().trim();
        if (fileName.equalsIgnoreCase("exit")) {
            menuStack.removeLast();
            return;
        }
        if (fileName.isEmpty()) {
            log.warn("введено пустое имя файла, начнем сначала");
            return;
        }
        String fullFileName = fileName.endsWith(".json") ? fileName : fileName + ".json";
        File file = new File(fullFileName);
        if (!file.exists()) {
            log.warn("файл {} не найден, идем в начало", fullFileName);
            return;
        }
        Map<String, Person> loadedPersonsFromJson;
        try {
            loadedPersonsFromJson = mapper.readValue(file, new TypeReference<>() {
            });
        } catch (IOException e) {
            log.error("ошибка при чтении из JSON", e);
            log.info("давай попробуем снова");
            return;
        }
        for (Map.Entry<String, Person> entry : loadedPersonsFromJson.entrySet()) {
            String key = entry.getKey();
            Person person = entry.getValue();
            PersonHolder.personHolder.put(key, person);
        }
    }

    private void showJsonContent() {
        log.info("давай имя файла, содержимое которого будем смотреть (без.json):");
        fileName = console.nextLine().trim();
        if (fileName.equalsIgnoreCase("exit")) {
            menuStack.removeLast();
            return;
        }
        if (fileName.isEmpty()) {
            log.warn("ты ввел пустое имя, идем по новой");
            return;
        }
        String fullFileName = fileName.endsWith(".json") ? fileName : fileName + ".json";
        File file = new File(fullFileName);
        if (!file.exists()) {
            log.warn("не обнаружено файла {}, пошли в начало", fullFileName);
            return;
        }
        //как посмотреть?
    }

}
