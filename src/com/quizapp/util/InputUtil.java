package com.quizapp.util;

import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class InputUtil {

    static Scanner scan = new Scanner(System.in);

    public static int getInt(int from, int to) {

        while (true) {
            System.out.print("Enter: ");
            try {
                int option = scan.nextInt();
                scan.nextLine();

                if (option >= from && option <= to)
                    return option;
                else
                    System.out.println("Invalid Choice!");

            } catch (InputMismatchException e) {
                System.out.println("Invalid Input! enter[" + from + "-" + to + "]");
                scan.nextLine();
            }
        }
    }

    public static String getString() {
        while (true) {
            String input = scan.nextLine();

            if (input.isEmpty())
                System.out.println("Input cannot be empty. Try again.");
            else
                return input;
        }
    }

    public static String getPara() {
        System.out.print("Enter: ");
        StringBuilder content = new StringBuilder();
        while (scan.hasNextLine()) {
            String input =scan.nextLine() ;

            if (input.trim().equalsIgnoreCase("stop"))
                return "stop";
            if (input.trim().equalsIgnoreCase("END"))
                return content.toString().trim();
            else
                content.append(input).append('\n');
        }
        return "";
    }

    public static char getChar(char from, char to) { // a - d

        while (true) {
            System.out.print("Enter: ");
            try {
                char option = scan.next().charAt(0);
                scan.nextLine();
                option = Character.toLowerCase(option);

                if (option >= from && option <= to)
                    return option;
                else
                    System.out.println("Invalid Choice!");

            } catch (NoSuchElementException | IllegalStateException e) {
                System.out.println("Invalid Input! enter[" + (char)(from) + "-" + (char)(to) + "]");
                scan.nextLine();
            }
        }
    }

    public static boolean getYesNo() {
        System.out.println("(1. Yes, 2. No)");
        int choice = getInt(1, 2);
        return choice == 1;
    }
}
