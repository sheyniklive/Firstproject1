package org.example.validator;

import org.example.exception.InvalidAgeException;
import org.example.exception.InvalidMenuChoiceException;

public class Validators {
    public static final MenuInputValidator choiceMenuOf4 = input -> {
        if (!input.equals("1") && !input.equals("2") && !input.equals("3") && !input.equals("4") && !input.equals("exit")) {
            throw new InvalidMenuChoiceException(input);
        }
        return true;
    };
    public static final MenuInputValidator choiceMenuOf3 = input -> {
        if (!input.equals("1") && !input.equals("2") && !input.equals("3") && !input.equals("exit")) {
            throw new InvalidMenuChoiceException(input);
        }
        return true;
    };
    public static final MenuInputValidator choiceMenuOf2 = input -> {
        if (!input.equals("1") && !input.equals("2") && !input.equals("exit")) {
            throw new InvalidMenuChoiceException(input);
        }
        return true;
    };
    public static final MenuInputValidator yesNo = input -> {
        if (!input.equals("1") && !input.equals("0")) {
            throw new InvalidMenuChoiceException(input);
        }
        return true;
    };
    public static final MenuInputValidator isNumeric = input -> {
        try {
            Double.parseDouble(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    };
    public static final MenuInputValidator isValidAge = input -> {
        if (Integer.parseInt(input) <= 0 || Integer.parseInt(input) >= 150) {
            throw new InvalidAgeException(input);
        }
        return true;
    };

}
