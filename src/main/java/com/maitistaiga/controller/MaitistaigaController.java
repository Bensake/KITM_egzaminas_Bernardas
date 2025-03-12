package com.maitistaiga.controller;

import com.maitistaiga.dao.*;
import com.maitistaiga.model.Patiekalas;
import com.maitistaiga.model.Maitistaiga;
import com.maitistaiga.model.User;
import com.maitistaiga.view.TerminalView;

import java.sql.SQLException;
import java.util.List;

public class MaitistaigaController {
    private TerminalView view;
    private UserDAO userDAO;
    private MaitistaigaDAO maitistaigaDAO;
    private PatiekalasDAO patiekalasDAO;
    private UzsakymasDAO uzsakymasDAO;
    private User currentUser;

    public MaitistaigaController() {
        this.view = new TerminalView();
        this.userDAO = new UserDAO();
        this.maitistaigaDAO = new MaitistaigaDAO();
        this.patiekalasDAO = new PatiekalasDAO();
        this.uzsakymasDAO = new UzsakymasDAO();
    }

    public void start() {
        while (true) {
            if (currentUser == null) {
                login();
            } else if (currentUser.getRole().equals("ADMIN")) {
                handleAdminMenu();
            } else {
                handleClientMenu();
            }
        }
    }

    private void login() {
        String[] credentials = view.showLoginScreen();
        String username = credentials[0];
        String password = credentials[1];
        currentUser = userDAO.authenticate(username, password);
        if (currentUser == null) {
            view.showMessage("Invalid credentials. Try again.");
        } else {
            view.showMessage("Logged in as " + currentUser.getUsername() + " (" + currentUser.getRole() + ")");
        }
    }

    private void handleAdminMenu() {
        view.showAdminMenu();
        String choice = view.getInput();
        try {
            switch (choice) {
                case "1":
                    view.showMessage("Iveskite maitinimo istaigos pavadinima:");
                    String catName = view.getInput();
                    if (catName.isEmpty()) throw new IllegalArgumentException("Istaigos pavadinimas negali buti tuscias.");
                    maitistaigaDAO.addMaitistaiga(catName);
                    view.showMessage("Maitinimo istaiga prideta.");
                    break;
                case "2":
                    rodytiMaitIstaigas();
                    view.showMessage("Iveskite maitinimo istaigos ID (redaguoti):");
                    int catId = Integer.parseInt(view.getInput());
                    view.showMessage("Iveskite nauja pavadinima:");
                    String newCatName = view.getInput();
                    if (newCatName.isEmpty()) throw new IllegalArgumentException("Maitinimo istaigos pavadinimas negali buti tuscias.");
                    maitistaigaDAO.updateMaitistaiga(catId, newCatName);
                    view.showMessage("Category updated successfully.");
                    break;
                case "3":
                    rodytiMaitIstaigas();
                    view.showMessage("Iveskite maitinimo istaigos ID (istrinti):");
                    int delCatId = Integer.parseInt(view.getInput());
                    maitistaigaDAO.deleteMaitistaiga(delCatId);
                    view.showMessage("Maitinimo istaiga istrinta.");
                    break;
                case "4":
                    pridetiPatiekala();
                    break;
                case "5":
                    rodytiPatiekalus();
                    view.showMessage("Iveskite patiekalo ID (redaguoti):");
                    int bookId = Integer.parseInt(view.getInput());
                    redaguotiPatiekala(bookId);
                    break;
                case "6":
                    rodytiPatiekalus();
                    view.showMessage("Iveskite patiekalo ID (istrinti):");
                    int delBookId = Integer.parseInt(view.getInput());
                    patiekalasDAO.istrintiPatiekala(delBookId);
                    view.showMessage("Patiekalas istrintas.");
                    break;
                case "7":
                    currentUser = null;
                    view.showMessage("Logged out.");
                    break;
                default:
                    view.showMessage("Invalid option.");
            }
        } catch (SQLException e) {
            view.showMessage("Database error: " + e.getMessage());
        } catch (NumberFormatException e) {
            view.showMessage("Invalid number format.");
        } catch (IllegalArgumentException e) {
            view.showMessage(e.getMessage());
        }
    }

    private void handleClientMenu() {
        view.showReaderMenu();
        String choice = view.getInput();
        try {
            switch (choice) {
                case "1":
                    view.showMessage("Enter search keyword:");
                    String keyword = view.getInput();
                    List<Patiekalas> results = patiekalasDAO.ieskotiPatiekalo(keyword);
                    if (results.isEmpty()) {
                        view.showMessage("Nerasta patiekalu.");
                    } else {
                        for (Patiekalas book : results) {
                            view.showMessage(book.getId() + ": " + book.getPavadinimas());
                        }
                    }
                    break;
                case "2":
                    rodytiPatiekalus();
                    view.showMessage("Iveskite patiekalo ID (uzsakyti):");
                    int resBookId = Integer.parseInt(view.getInput());
                    if (uzsakymasDAO.uzsakytiPatiekala(currentUser.getId(), resBookId)) {
                        view.showMessage("Patiekalas uzsakytas.");
                    } else {
                        view.showMessage("Patiekalas jau buvo uzsakytas.");
                    }
                    break;
                case "3":
                    rodytiMaitIstaigas();
                case "4":
                    currentUser = null;
                    view.showMessage("Logged out.");
                    break;
                default:
                    view.showMessage("Invalid option.");
            }
        } catch (SQLException e) {
            view.showMessage("Database error: " + e.getMessage());
        } catch (NumberFormatException e) {
            view.showMessage("Invalid number format.");
        }
    }

    private void rodytiMaitIstaigas() throws SQLException {
        List<Maitistaiga> maitIstaigos = maitistaigaDAO.getAllMaitistaiga();
        if (maitIstaigos.isEmpty()) {
            view.showMessage("Nerasta maitinimo istaigu.");
        } else {
            for (Maitistaiga cat : maitIstaigos) {
                view.showMessage(cat.getId() + ": " + cat.getName());
            }
        }
    }

    private void rodytiPatiekalus() throws SQLException {
        List<Patiekalas> patiekalai = patiekalasDAO.getVisusPatiekalus();
        if (patiekalai.isEmpty()) {
            view.showMessage("Nerasta patiekalu.");
        } else {
            for (Patiekalas book : patiekalai) {
                view.showMessage(book.getId() + ": " + book.getPavadinimas());
            }
        }
    }

    private void pridetiPatiekala() throws SQLException {
        view.showMessage("Iveskite patiekalo pavadinima");
        String title = view.getInput();
        if (title.isEmpty()) throw new IllegalArgumentException("Pavadinimas negali buti tuscias");
        view.showMessage("Iveskite aprasyma");
        String summary = view.getInput();
        rodytiMaitIstaigas();
        view.showMessage("Iveskite maitinimo istaigos ID:");
        int categoryId = Integer.parseInt(view.getInput());
        patiekalasDAO.pridetiPatiekala(title, summary, categoryId);
        view.showMessage("Patiekalas pridetas.");
    }

    private void redaguotiPatiekala(int id) throws SQLException {
        Patiekalas patiekalas = patiekalasDAO.getPatiekalasById(id);
        if (patiekalas == null) {
            view.showMessage("Patiekalas nerastas.");
            return;
        }
        view.showMessage("Esamas pavadinimas: " + patiekalas.getPavadinimas() + ". Iveskite nauja pavadinimas (arba spauskite enter naudoti ta pati):");
        String title = view.getInput();
        if (title.isEmpty()) title = patiekalas.getPavadinimas();
        view.showMessage("Esamas aprasymas: " + patiekalas.getAprasymas() + ". Iveskite nauja aprasyma (arba spauskite enter naudoti ta pati):");
        String summary = view.getInput();
        if (summary.isEmpty()) summary = patiekalas.getAprasymas();
        rodytiMaitIstaigas();
        view.showMessage("Esamas maitinimo istaigos ID: " + patiekalas.getMaitIstaigaId() + ". Iveskite nauja mait istaigos ID (arba spauskite enter naudoti ta pati):");
        String catIdStr = view.getInput();
        int categoryId = catIdStr.isEmpty() ? patiekalas.getMaitIstaigaId() : Integer.parseInt(catIdStr);
        patiekalasDAO.redaguotiPatiekala(id, title, summary, categoryId);
        view.showMessage("Patiekalas atnaujintas.");
    }
}