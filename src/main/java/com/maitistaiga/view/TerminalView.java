package com.maitistaiga.view;

import java.util.Scanner;

public class TerminalView {
    private Scanner scanner = new Scanner(System.in);

    public String[] showLoginScreen() {
        System.out.println("=== Maitinimo istaiga Login ===");
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();
        System.out.println("Prisijungiama...");
        return new String[]{username, password}; // Return both inputs
    }

    public void showAdminMenu() {
        System.out.println("=== Admin Menu ===");
        System.out.println("1. Prideti mait istaiga");
        System.out.println("2. Redaguoti mait istaiga");
        System.out.println("3. Istrinti mait istaiga");
        System.out.println("4. Prideti patiekala");
        System.out.println("5. Redaguoti patiekala");
        System.out.println("6. Istrinti patiekala");
        System.out.println("7. Logout");
        System.out.print("Choose an option: ");
    }

    public void showReaderMenu() {
        System.out.println("=== Client Menu ===");
        System.out.println("1. Ieskoti patiekalo");
        System.out.println("2. Uzsakyti patiekala");
        System.out.println("3. Rodyti maitinimo istaigas");
        System.out.println("4. Logout");
        System.out.print("Choose an option: ");
    }

    public String getInput() {
        return scanner.nextLine().trim();
    }

    public void showMessage(String message) {
        System.out.println(message);
    }
}