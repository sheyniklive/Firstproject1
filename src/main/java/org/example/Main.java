package org.example;

import org.example.calculator.CalculatorService;
import org.example.person.PersonService;

import java.util.Scanner;

public class Main {
    static Scanner console = new Scanner(System.in);
    static String input;

    public static void main(String[] args) {
        System.out.println("Привет, выбери, чем ты хочешь заняться: \n 1 - Имя Фамилия, \n 2 - Калькулятор, \n или введи 'exit' для выхода");
        input = console.nextLine();
        while (!input.equals("exit") && !input.equals("1") && !input.equals("2")) {
            System.out.println("только 1, 2 или ''exit");
            input = console.nextLine();
        }
        switch (input) {
            case "1":
                PersonService personService = new PersonService();
                personService.askImFam();
                break;
            case "2":
                CalculatorService calculatorService = new CalculatorService();
                calculatorService.calculate();
                break;
            case "exit":
                System.out.println("До новых встреч!");
                System.exit(0);
                break;
        }
    }
}