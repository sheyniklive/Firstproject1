package org.example;

import java.util.Scanner;

public class Main {
    static boolean isExit = false;
    static Scanner console = new Scanner(System.in);
    static String input;

    public static void main(String[] args) {
        System.out.println("Привет, выбери, чем ты хочешь заняться: \n 1 - Имя Фамилия, \n 2 - Калькулятор, \n или введи 'exit' для выхода");
        input = console.nextLine();
        while (!isExit) {
            if (input.equals("1")) {


            }
        }

        private void getChoice () {
            input = console.nextLine();
            if (isExit()) {
                System.exit(0);
            } else {
                input = console.nextLine();
                if (input.equals("1")) {


                }
            }

            private boolean isExit () {
                if (input.equalsIgnoreCase("exit")) {
                    System.out.println("Хорошего дня");
                    return true;
                }
                return false;
            }


        }
    }