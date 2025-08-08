package org.example;

import lombok.extern.slf4j.Slf4j;
import org.example.calculator.CalculatorService;
import org.example.person.PersonService;
import org.example.pet.PetService;
import org.example.util.ExitsUtils;

import java.util.*;

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
        Map<String, Runnable> menu = new HashMap<>();
        menu.put("1", personService::processPersonMenu);
        menu.put("2", calculatorService::calculate);
        menu.put("3", petService::processPetServiceMenu);
        if (menuStack.isEmpty()) {
            menuStack.addLast(() -> main.mainMenu(menu));
        }
        while (!menuStack.isEmpty()) {
            menuStack.peekLast().run();
        }
    }

    private void mainMenu(Map<String, Runnable> menu) {
        log.info("хорошо, теперь выбери, чем ты хочешь заняться:");
        log.info("1 - поделаем что-то с людьми,");
        log.info("2 - Калькулятор,");
        log.info("3 - повзаимодействуем с возможными питомцами");
        log.info("или введи exit для выхода");
        String input = console.nextLine();
        while (!input.equals("exit") && !menu.containsKey(input)) {
            log.warn("неверный ввод, повтори: только 1, 2, 3 или exit");
            input = console.nextLine();
        }
        if (input.equals("exit")) {
            ExitsUtils.doExit();
        }
        Runnable next = menu.get(input);
        menuStack.addLast(next);
        next.run();
    }
}