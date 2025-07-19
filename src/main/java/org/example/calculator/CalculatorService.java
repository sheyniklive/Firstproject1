package org.example.calculator;

import java.util.Scanner;

public class CalculatorService {
    String input;
    double result;
    double a;
    double b;
    String operator;
    Scanner console = new Scanner(System.in);


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
                System.out.println("Миша, всё хуйня давай по-новой");
        }
    }

    private String getOperator() {
        input = console.nextLine();
        if (isExit()) {
            System.exit(0);
        } else {
            while (!input.equals("+") && !input.equals("-") && !input.equals("*") && !input.equals("/")) {
                System.out.println("только +, -, * или /");
                input = console.nextLine();
            }
        }
        return input;
    }

    private double getOperand() {
        input = console.nextLine();
        if (isExit()) {
            System.exit(0);
            return 0;
        } else {
            if (!isNumeric()) {
                while (!isNumeric()) {
                    System.out.println("только число");
                    input = console.nextLine();
                    if (isExit()) {
                        System.exit(0);
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
        if (input.equalsIgnoreCase("exit")) {
            System.out.println("Хорошего дня");
            return true;
        }
        return false;
    }
}
