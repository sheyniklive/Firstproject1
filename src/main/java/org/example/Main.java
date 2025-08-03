package org.example;

import lombok.extern.slf4j.Slf4j;
import org.example.calculator.CalculatorService;
import org.example.pet.PetService;
import org.example.util.ExitsUtils;
import org.example.person.PersonService;

import java.util.Scanner;

@Slf4j
public class Main {
    static Scanner console = new Scanner(System.in);
    static String input;

    public static void main(String[] args) {
        ExitsUtils.addExits();
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
                PersonService.processPerson();
                break;
            case "2":
                CalculatorService.calculate();
                break;
            case "3":
                PetService.processPetService();
                break;
            case "exit":
                ExitsUtils.doExit();
                break;
        }
    }
}