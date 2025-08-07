package org.example;

import lombok.extern.slf4j.Slf4j;
import org.example.calculator.CalculatorService;
import org.example.person.PersonService;
import org.example.pet.PetService;
import org.example.util.ExitsUtils;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Scanner;

@Slf4j
public class Main {
    public static final Deque<Runnable> menuStack = new ArrayDeque<>();
    public static final PersonService personService = new PersonService();
    public static final PetService petService = new PetService();
    public static final CalculatorService calculatorService = new CalculatorService();
    public static final Scanner console = new Scanner(System.in);

    public static void main(String[] args) {
        ExitsUtils.addExits();
        Main main = new Main();
        menuStack.addLast(main::mainMenu);
        while (!menuStack.isEmpty()) {
            menuStack.peekLast().run();
        }
    }

    public void mainMenu() {
        log.info("хорошо, теперь выбери, чем ты хочешь заняться:");
        log.info("1 - поделаем что-то с людьми,");
        log.info("2 - Калькулятор,");
        log.info("3 - повзаимодействуем с возможными питомцами");
        log.info("или введи exit для выхода");
        String input = console.nextLine();
        while (!input.equals("exit") && !input.equals("1") && !input.equals("2") && !input.equals("3")) {
            log.warn("неверный ввод, повтори: только 1, 2, 3 или exit");
            input = console.nextLine();
        }
        switch (input) {
            case "1":
                menuStack.addLast(personService::processPersonMenu);
                return;
            case "2":
                menuStack.addLast(calculatorService::calculate);
                return;
            case "3":
                menuStack.addLast(petService::processPetServiceMenu);
                return;
            case "exit":
                ExitsUtils.doExit();
                break;
            default:
                break;
        }
    }
}