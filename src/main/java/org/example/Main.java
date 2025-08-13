package org.example;

import lombok.extern.slf4j.Slf4j;
import org.example.calculator.CalculatorService;
import org.example.exception.InvalidMenuChoiceException;
import org.example.person.PersonService;
import org.example.pet.PetService;
import org.example.util.ExitsUtils;
import org.example.validator.Validators;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.Scanner;

@Slf4j
public class Main {
    public static final Deque<Runnable> menuStack = new ArrayDeque<>();
    public static final PersonService personService = new PersonService();
    public static final PetService petService = new PetService();
    public static final CalculatorService calculatorService = new CalculatorService();
    public static final Scanner console = new Scanner(System.in);
    public final Map<String, Runnable> choiceMainMenu = Map.of(
            "1", personService::processPersonMenu,
            "2", calculatorService::calculate,
            "3", petService::processPetServiceMenu);


    public static void main(String[] args) {
        ExitsUtils.addExits();
        Main main = new Main();
        if (menuStack.isEmpty()) {
            menuStack.addLast(main::mainMenu);
        }
        while (!menuStack.isEmpty()) {
            menuStack.peekLast().run();
        }
    }

    private void mainMenu() {
        log.info("хорошо, теперь выбери, чем ты хочешь заняться:");
        log.info("1 - поделаем что-то с людьми,");
        log.info("2 - Калькулятор,");
        log.info("3 - повзаимодействуем с возможными питомцами");
        log.info("или введи exit для выхода");
        String input;
        while (true) {
            try {
                input = console.nextLine();
                Validators.choiceMainMenu.validate(input);
                break;
            } catch (InvalidMenuChoiceException e) {
                log.error("", e);
                log.info("Попробуй еще, варианты: {} или exit", choiceMainMenu.keySet());
            }
        }
        if (input.equals("exit")) {
            ExitsUtils.doExit();
            return;
        }
        Runnable next = choiceMainMenu.get(input);
        menuStack.addLast(next);
    }
}