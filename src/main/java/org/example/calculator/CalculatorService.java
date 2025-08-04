package org.example.calculator;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;

import static org.example.Main.menuStack;

@UtilityClass
@Slf4j
public class CalculatorService {
    String input;
    double result;
    double a;
    double b;
    String operator;
    Scanner console = new Scanner(System.in);

    public void calculate() {
        log.info("Привет, введи первое число (или выйди в главное меню с помощью 'exit': ");
        a = getOperand();

        while (!isExit()) {
            log.info("введи +, -, * или / (или для выхода введи 'exit'):");
            operator = getOperator();

            log.info("введи второе число (или для выхода введи 'exit'):");
            b = getOperand();

            doCalculate();
            log.info(String.valueOf(result));
            a = Double.parseDouble(String.valueOf(result));
            log.info("Если хочешь продолжить :");
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
                break;
        }
    }

    private String getOperator() {
        input = console.nextLine();
        while (!input.equals("+") && !input.equals("-") && !input.equals("*") && !input.equals("/") && !input.equals("exit")) {
            log.warn("нужно только +, -, *,  / или exit - введено что-то другое, введи еще раз");
            input = console.nextLine();
        }
        if (isExit()) {
            menuStack.pop();
        } else {
            return input;
        }
        return input;
    }

    private double getOperand() {
        input = console.nextLine();
        if (isExit()) {
            menuStack.pop();
        } else {
            while (!isNumeric()) {
                log.warn("введено не число, введи еще раз");
                input = console.nextLine();
                if (isExit()) {
                    menuStack.pop();
                }
            }
            return Double.parseDouble(input);
        }
        return Double.parseDouble(input);
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
