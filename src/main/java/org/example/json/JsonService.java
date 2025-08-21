package org.example.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Scanner;

import static org.example.Main.menuStack;

@Slf4j
public class JsonService {
    private final Scanner console = new Scanner(System.in);
    private File file;
    private String input;
    private String fullFileName;
    private boolean needReturn;

    private enum FileActionVariety {SAVE, LOAD, SHOW}

    private final ObjectMapper mapper = new ObjectMapper();

    {
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    private final Path directory = Paths.get("data", "json");

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
        while (true) {
            try {
                input = console.nextLine().trim();
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
        needReturn = false;
        beginActionJson("введи exit для выхода или название файла, в который будем сохранять:", FileActionVariety.SAVE);
        if (needReturn) {
            return;
        }
        try {
            mapper.writeValue(file, PersonHolder.personHolder);
            log.info("файл {} успешно сохранен ({} персон), путь: {}", fullFileName, PersonHolder.personHolder.size(), file.getAbsolutePath());
        } catch (IOException e) {
            log.error("Ошибка при сохранении JSON-файла", e);
            log.info("пойдем в начало");
            return;
        }
        ExitsUtils.informingBack();
    }

    private void loadPersonsFromFile() {
        needReturn = false;
        beginActionJson("введи exit или имя файла, из которого будем брать (без '.json'):", FileActionVariety.LOAD);
        if (needReturn) {
            return;
        }
        Map<String, Person> loadedPersonsFromJson;
        try {
            loadedPersonsFromJson = mapper.readValue(file, new TypeReference<Map<String, Person>>() {
            });
        } catch (IOException e) {
            log.error("ошибка при чтении из JSON", e);
            log.info("давай попробуем снова");
            return;
        }
        PersonHolder.personHolder.putAll(loadedPersonsFromJson);
        log.info("из файла {} в хранилище успешно загружено {} персон, путь: {}", fullFileName, loadedPersonsFromJson.size(), file.getAbsolutePath());
        ExitsUtils.informingBack();
    }

    private void showJsonContent() {
        needReturn = false;
        beginActionJson("давай имя файла, содержимое которого будем смотреть (без.json):", FileActionVariety.SHOW);
        if (needReturn) {
            return;
        }
        try {
            JsonNode tree = mapper.readTree(file);
            String showJson = mapper.writeValueAsString(tree);
            log.info("содержимое файла {} (путь: {}): \n{}", fullFileName, file.getAbsolutePath(), showJson);
        } catch (IOException e) {
            log.error("проблема при работе с JSON", e);
            log.info("попробуем снова");
            return;
        }
        ExitsUtils.informingBack();
    }

    private void beginActionJson(String ask, FileActionVariety variant) {
        log.info(ask);
        input = console.nextLine().trim();
        if (ifExitOrEmpty(input)) {
            needReturn = true;
            return;
        }
        try {
            Path path = pathToJson(input);
            fullFileName = path.getFileName().toString();
            if (variant == FileActionVariety.SAVE) {
                if (Files.exists(path)) {
                    log.warn("файл {} уже существует - перезаписать? (1 - да, 0 - нет)", fullFileName);
                    confirmOverwrite();
                    if (needReturn) {
                        return;
                    }
                }
            } else {
                if (Files.notExists(path)) {
                    log.warn("файл {} не найден, идем в начало", fullFileName);
                    needReturn = true;
                    return;
                }
            }
            file = path.toFile();
        } catch (IOException e) {
            log.error("ошибка при создании пути к файлу - начнем вновь", e);
            needReturn = true;
        }
    }

    private void confirmOverwrite() {
        while (true) {
            String choiceConfirmFile = console.nextLine().trim();
            try {
                Validators.yesNo.validate(choiceConfirmFile);
                if (!"1".equals(choiceConfirmFile)) {
                    log.warn("отмена записи в файл - идем в начало");
                    needReturn = true;
                }
                break;
            } catch (InvalidMenuChoiceException e) {
                log.error("неверный ввод действия", e);
                log.info("повтори 1 или 0");
            }
        }
    }

    private boolean ifExitOrEmpty(String input) {
        if (input.equalsIgnoreCase("exit")) {
            menuStack.removeLast();
            return true;
        }
        if (input.isEmpty()) {
            log.warn("введено пустое имя файла, начнем сначала");
            return true;
        }
        return false;
    }

    private Path pathToJson(String input) throws IOException {
        checkDirectory();
        String name = sanitise(input);
        String fileName = name.endsWith(".json") ? name : name + ".json";
        return directory.resolve(fileName);
    }

    private void checkDirectory() throws IOException {
        if (Files.notExists(directory)) {
            Files.createDirectories(directory);
        }
    }

    private String sanitise(String input) {
        return input.replaceAll("[\\\\/:*?\"<>|]", "_").trim();
    }
}
