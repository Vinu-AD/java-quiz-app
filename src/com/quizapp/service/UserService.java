package com.quizapp.service;

import com.quizapp.util.FileUtil;
import com.quizapp.util.InputUtil;

public class UserService {

    public String userValidation() {
        while (true) {
            System.out.print("Enter UserName: ");
            String userName = InputUtil.getString();

            if (FileUtil.validateUserName(userName)) {

                System.out.print("Hi " + userName + ", enter your password: ");
                int attempts = 3;
                for(int i = 1; i <= attempts+1; i++) {
                    String password = InputUtil.getString();

                    if (FileUtil.validateUser(userName, password)) {
                        System.out.println("User Login Successful!");
                        return userName;
                    }
                    else {
                        if(i == attempts+1) break;
                        System.out.println("Incorrect password!");
                        System.out.print("\nPlease enter a valid password (Attempt "+ i +" of 3): ");
                    }
                }
                System.out.println("\nYour '"+ attempts +"' attempts are completed. Try again later...");
                return "";
            }
            else {
                System.out.println("Incorrect UserName! Try again.");
            }
        }
    }

    public String createNewUser() {

        while (true) {
            System.out.print("Enter UserName: ");
            String userName = InputUtil.getString();

            if (userName.matches("^[A-Za-z][A-Za-z0-9_]{2,14}$")) {
                if(!FileUtil.validateUserName(userName)) {
                    while (true) {
                        System.out.print("Enter password: ");
                        String password = InputUtil.getString();

                        if (password.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*])[A-Za-z\\d!@#$^&*]{6,15}$")) {
                            // store it in user.txt
                            FileUtil.writeUserToFile(userName, password);
                            System.out.println("Hello, " + userName + ", your account created");
                            return userName;
                        } else {
                            System.out.println("""
                                    Password Rules:
                                    - 6–15 characters
                                    - At least one uppercase and one lowercase letter
                                    - At least one number
                                    - At least one special character (@, #, $, %, etc.)""");
                        }
                    }
                }
                else {
                    System.out.println("UserName already Exist");
                }
            } else {
                System.out.println("Invalid UserName! Try again.");
                System.out.println("""
                        Username Rules:
                        - Must start with a letter
                        - Can contain letters, numbers, and underscores
                        - Length must be 3 to 15 characters""");
            }
        }
    }

    public String profileUpdate(String userName) {
        System.out.println();
        System.out.println("What do you want to change?");
        System.out.println("1. UserName");
        System.out.println("2. Password");
        System.out.println("3. Back to User Menu");
        int choice = InputUtil.getInt(1, 3);

        switch (choice) {
            case 1:
                userName = changeUserName(userName);
                break;
            case 2:
                changePassword(userName);
                break;
            case 3:
                System.out.println("Returning to User Menu...");
                return userName;
        }
        return userName;
    }

    private String changeUserName(String userName) {

        while (true) {
            System.out.print("Enter your New UserName: ");
            String newName = InputUtil.getString();
            if (newName.matches("^[A-Za-z][A-Za-z0-9_]{2,14}$")) {
                if(FileUtil.validateUserName(newName)) {
                    System.out.println("UserName already exists!");
                    continue;
                }
                while (true) {
                    System.out.print("Enter your password: ");
                    String password = InputUtil.getString();
                    if (FileUtil.updateUserName(userName, newName, password)) {
                        System.out.println("UserName changed successfully");
                        System.out.println("Your new userName is: " + newName);
                        return newName;
                    }
                    else {
                        System.out.println("Incorrect password! Try again.");
                    }
                }
            }
            else {
                System.out.println("Invalid UserName! Try again.");
                System.out.println("""
                    Username Rules:
                    - Must start with a letter
                    - Can contain letters, numbers, and underscores
                    - Length must be 3 to 15 characters""");
            }
        }
    }

    private void changePassword(String userName) {
        while (true) {
            System.out.print("Enter your Old Password:");
            String oldPassword = InputUtil.getString();
            if (FileUtil.validateUser(userName, oldPassword)) {
                while (true) {
                    System.out.print("Enter your New Password: ");
                    String newPassword = InputUtil.getString();
                    if (newPassword.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*])[A-Za-z\\d!@#$^&*]{6,15}$")) {
                        if(FileUtil.updatePassword(userName, oldPassword, newPassword)) {
                            System.out.println("Password changed successfully");
                            System.out.println("Password updated for user : " + userName);
                            return;
                        }
                        else {
                            System.out.println("Something went wrong! Try again.");
                        }
                    }
                    else {
                        System.out.println("Invalid Password! Try again.");
                        System.out.println("""
                            Password Rules:
                            - 6–15 characters
                            - At least one uppercase and one lowercase letter
                            - At least one number
                            - At least one special character (@, #, $, %, etc.)""");
                    }
                }
            }
            else {
                System.out.println("Incorrect Password! Try again.");
            }
        }
    }
}
