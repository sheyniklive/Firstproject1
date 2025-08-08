package org.example.validator;

public class Validators {
    public static final MenuInputValidator choiceMenu = input -> input.equals("1") || input.equals("2") || input.equals("3") || input.equals("exit");
    public static final MenuInputValidator yesNo = input -> input.equals("1") || input.equals("0");
    public static final MenuInputValidator isNumeric = input -> {
        try {
            Double.parseDouble(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    };
}
