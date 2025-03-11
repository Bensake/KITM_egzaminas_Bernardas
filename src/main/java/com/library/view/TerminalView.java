package com.library.view;

import java.util.Scanner;

public class TerminalView {
    private Scanner scanner = new Scanner(System.in);

    public String[] showLoginScreen() {
        System.out.println("=== Library System Login ===");
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();
        System.out.println("Authenticating...");
        return new String[]{username, password}; // Return both inputs
    }

    public void showAdminMenu() {
        System.out.println("=== Admin Menu ===");
        System.out.println("1. Add Category");
        System.out.println("2. Edit Category");
        System.out.println("3. Delete Category");
        System.out.println("4. Add Book");
        System.out.println("5. Edit Book");
        System.out.println("6. Delete Book");
        System.out.println("7. Logout");
        System.out.print("Choose an option: ");
    }

    public void showReaderMenu() {
        System.out.println("=== Reader Menu ===");
        System.out.println("1. Search Book");
        System.out.println("2. Reserve Book");
        System.out.println("3. Add to Favorites");
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