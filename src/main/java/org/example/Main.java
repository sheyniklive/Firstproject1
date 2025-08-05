package org.example;

import lombok.extern.slf4j.Slf4j;
import org.example.calculator.CalculatorService;
import org.example.pet.PetService;
import org.example.util.ExitsUtils;
import org.example.person.PersonService;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Scanner;

@Slf4j
public class Main {
    static Scanner console = new Scanner(System.in);
    static String input;
    public static final Deque<Runnable> menuStack = new ArrayDeque<>();
    static boolean running = true;

    public static void main(String[] args) {
        ExitsUtils.addExits();
        while (running) {
            if (menuStack.isEmpty()) {
                menuStack.addLast(Main::mainMenu);
            }
            while (!menuStack.isEmpty()) {
                menuStack.removeLast().run();
            }
        }
    }

    public static void mainMenu() {
        log.info("хорошо, теперь выбери, чем ты хочешь заняться:");
        log.info("1 - поделаем что-то с людьми,");
        log.info("2 - Калькулятор,");
        log.info("3 - повзаимодействуем с возможными питомцами");
        log.info("или введи exit для выхода");
        input = console.nextLine();
        while (!input.equals("exit") && !input.equals("1") && !input.equals("2") && !input.equals("3")) {
            log.warn("неверный ввод, повтори: только 1, 2, 3 или exit");
            input = console.nextLine();
        }
        switch (input) {
            case "1":
                menuStack.addLast(PersonService::processPersonMenu);
                return;
            case "2":
                menuStack.addLast(CalculatorService::calculate);
                return;
            case "3":
                menuStack.addLast(PetService::processPetServiceMenu);
                return;
            case "exit":
                ExitsUtils.doExit();
                running = false;
                break;
            default:
                break;
        }
    }
}