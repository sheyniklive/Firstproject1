package org.example.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.example.exception.InvalidMenuChoiceException;
import org.example.json.enums.FileActionVariety;
import org.example.person.Person;
import org.example.repository.PersonRepository;
import org.example.util.ExitsUtils;
import org.example.validator.Validators;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static org.example.Main.menuStack;

@Slf4j
public class JsonService {
    private final Scanner console = new Scanner(System.in);
    private boolean needReturn;

    private final ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT, SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS);

    private final Path directory = Paths.get("data", "json");

    private final Map<String, Runnable> choiceJsonServiceMenu = Map.of(
            "1", this::savePersonsToFile,
            "2", this::loadPersonsFromFile,
            "3", this::showJsonContent
    );

    private final PersonRepository repo;

    public JsonService(PersonRepository personRepository) {
        this.repo = personRepository;
    }

    public void processJsonService() {
        log.info("выбери, как будем взаимодействовать с Json`ом:");
        log.info("1 - сохраним персонов в файл");
        log.info("2 - выгрузим из файла");
        log.info("3 - посмотрим содержимое файла");
        log.info("exit - вернемся в главное меню");
        String input;
        while (true) {
            input = console.nextLine().trim();
            try {
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
        File jsonFile = prepareJsonFile("введи exit для выхода или название файла, в который будем сохранять:", FileActionVariety.SAVE);
        if (needReturn || jsonFile == null) {
            return;
        }
        try {
            mapper.writeValue(jsonFile, repo.findAll());
            log.info("в файл '{}' из БД успешно сохранено {} персон", jsonFile.getAbsolutePath(), repo.findAll().size());
        } catch (IOException e) {
            log.error("Ошибка при сохранении JSON-файла", e);
            log.info("пойдем в начало");
            return;
        }
        ExitsUtils.informingBack();
    }

    private void loadPersonsFromFile() {
        needReturn = false;
        File jsonFile = prepareJsonFile("введи exit или имя файла, из которого будем брать (без '.json'):", FileActionVariety.LOAD);
        if (needReturn || jsonFile == null) {
            return;
        }
        List<Person> loadedPersonsFromJson;
        try {
            loadedPersonsFromJson = mapper.readValue(jsonFile, new TypeReference<List<Person>>() {
            });
        } catch (IOException e) {
            log.error("ошибка при чтении из JSON", e);
            log.info("давай попробуем снова");
            return;
        }
        for (Person person : loadedPersonsFromJson) {
            repo.save(person);
        }
        log.info("из файла '{}' в БД успешно загружено {} персон", jsonFile.getAbsolutePath(), loadedPersonsFromJson.size());
        ExitsUtils.informingBack();
    }

    private void showJsonContent() {
        needReturn = false;
        File jsonFile = prepareJsonFile("давай имя файла, содержимое которого будем смотреть (без.json): ", FileActionVariety.SHOW);
        if (needReturn || jsonFile == null) {
            return;
        }
        try {
            String showJson = mapper.readTree(jsonFile).toPrettyString();
            log.info("содержимое файла (путь: {}): \n{}", jsonFile.getAbsolutePath(), showJson);
        } catch (IOException e) {
            log.error("проблема при работе с JSON", e);
            log.info("попробуем снова");
            return;
        }
        ExitsUtils.informingBack();
    }

    private File prepareJsonFile(String ask, FileActionVariety variant) {
        log.info(ask);
        String input = console.nextLine().trim();
        if (ifExitOrEmptyInput(input)) {
            needReturn = true;
            return null;
        }
        File file;
        Path path;
        try {
            path = pathToJson(input);
            String fullFileName = path.getFileName().toString();
            if (variant == FileActionVariety.SAVE) {
                if (Files.exists(path)) {
                    log.warn("файл {} уже существует - перезаписать? (1 - да, 0 - нет)", fullFileName);
                    confirmOverwrite();
                    if (needReturn) {
                        return null;
                    }
                }
            } else {
                if (Files.notExists(path)) {
                    log.warn("файл {} не найден, идем в начало", fullFileName);
                    needReturn = true;
                    return null;
                }
            }
            file = path.toFile();
        } catch (IOException e) {
            log.error("ошибка при создании пути к файлу - начнем вновь", e);
            needReturn = true;
            return null;
        }
        return file;
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

    private boolean ifExitOrEmptyInput(String input) {
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
        createDirectoryIfNotExist();
        String name = sanitise(input);
        String fileName = name.endsWith(".json") ? name : name + ".json";
        return directory.resolve(fileName);
    }

    private void createDirectoryIfNotExist() throws IOException {
        if (Files.notExists(directory)) {
            Files.createDirectories(directory);
        }
    }

    private String sanitise(String input) {
        return input.replaceAll("[\\\\/:*?\"<>|]", "_").trim();
    }
}
