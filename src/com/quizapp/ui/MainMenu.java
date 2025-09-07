package com.quizapp.ui;

import com.quizapp.util.InputUtil;

public class MainMenu {

    private final AdminDashboard adminDashboard = new AdminDashboard();
    private final UserDashboard userDashboard = new UserDashboard();

    public void start() {
        boolean running = true;

        while (running) {
            showMenu();
            int choice = InputUtil.getInt(0, 2);

            switch (choice) {
                case 1:
                    adminDashboard.adminValidation();
                    break;
                case 2:
                    userDashboard.userChoice();
                    break;
                case 0:
                    System.out.println("Exiting... Thank you for using QuizApp!");
                    running = false;
            }
        }
    }

    private void showMenu() {
        System.out.println();
        System.out.println("==============================");
        System.out.println("         Main Menu");
        System.out.println("==============================");
        System.out.println("1. Admin Login");
        System.out.println("2. User Login");
        System.out.println("0. Exit");
    }
}
