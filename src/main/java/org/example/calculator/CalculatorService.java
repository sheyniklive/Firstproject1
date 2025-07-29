package org.example.calculator;

import org.example.exits.Exits;

import java.util.Scanner;

public class CalculatorService {
    String input;
    double result;
    double a;
    double b;
    String operator;
    Scanner console = new Scanner(System.in);
    private final Exits exits = new Exits();

    public void calculate() {
        System.out.println("Привет, введи первое число (или выйди с помощью 'exit': ");
        a = getOperand();

        while (!isExit()) {
            System.out.println("введи +, -, * или / (или для выхода введи 'exit'):");
            operator = getOperator();

            System.out.println("введи второе число (или для выхода введи 'exit'):");
            b = getOperand();

            doCalculate();
            System.out.println(result);
            a = result;
            System.out.println("Если хочешь продолжить :");
        }
    }

    private void doCalculate() {
        switch (operator) {
            case "+":
                result = a + b;
                break;
            case "-":
                result = a - b;
                break;
            case "*":
                result = a * b;
                break;
            case "/":
                result = b != 0 ? a / b : 0;
                break;
            default:
                System.out.println("Миша, всё хуйня давай по-новой, щас выйдем и зайдешь по масти");
                exits.doExit();
        }
    }

    private String getOperator() {
        input = console.nextLine();
        while (!input.equals("+") && !input.equals("-") && !input.equals("*") && !input.equals("/") && !input.equals("exit")) {
            System.out.println("только +, -, *,  / или exit");
            input = console.nextLine();
        }
        if (isExit()) {
            exits.doExit();
        } else
            return input;

        return input;
    }

    private double getOperand() {
        input = console.nextLine();
        if (isExit()) {
            exits.doExit();
            return 0;
        } else {
            if (!isNumeric()) {
                while (!isNumeric()) {
                    System.out.println("только число");
                    input = console.nextLine();
                    if (isExit()) {
                        exits.doExit();
                    }
                }
            }
            return Double.parseDouble(input);
        }
    }

    private boolean isNumeric() {
        try {
            Double.parseDouble(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isExit() {
        return input.equalsIgnoreCase("exit");
    }
}
