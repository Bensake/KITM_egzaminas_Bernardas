package com.library.controller;

import com.library.dao.*;
import com.library.model.Book;
import com.library.model.Category;
import com.library.model.User;
import com.library.view.TerminalView;

import java.sql.SQLException;
import java.util.List;

public class LibraryController {
    private TerminalView view;
    private UserDAO userDAO;
    private CategoryDAO categoryDAO;
    private BookDAO bookDAO;
    private ReservationDAO reservationDAO;
    private FavoriteDAO favoriteDAO;
    private User currentUser;

    public LibraryController() {
        this.view = new TerminalView();
        this.userDAO = new UserDAO();
        this.categoryDAO = new CategoryDAO();
        this.bookDAO = new BookDAO();
        this.reservationDAO = new ReservationDAO();
        this.favoriteDAO = new FavoriteDAO();
    }

    public void start() {
        while (true) {
            if (currentUser == null) {
                login();
            } else if (currentUser.getRole().equals("ADMIN")) {
                handleAdminMenu();
            } else {
                handleReaderMenu();
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
                case "1": // Add Category
                    view.showMessage("Enter category name:");
                    String catName = view.getInput();
                    if (catName.isEmpty()) throw new IllegalArgumentException("Category name cannot be empty.");
                    categoryDAO.addCategory(catName);
                    view.showMessage("Category added successfully.");
                    break;
                case "2": // Edit Category
                    listCategories();
                    view.showMessage("Enter category ID to edit:");
                    int catId = Integer.parseInt(view.getInput());
                    view.showMessage("Enter new name:");
                    String newCatName = view.getInput();
                    if (newCatName.isEmpty()) throw new IllegalArgumentException("Category name cannot be empty.");
                    categoryDAO.updateCategory(catId, newCatName);
                    view.showMessage("Category updated successfully.");
                    break;
                case "3": // Delete Category
                    listCategories();
                    view.showMessage("Enter category ID to delete:");
                    int delCatId = Integer.parseInt(view.getInput());
                    categoryDAO.deleteCategory(delCatId);
                    view.showMessage("Category deleted successfully.");
                    break;
                case "4": // Add Book
                    addBook();
                    break;
                case "5": // Edit Book
                    listBooks();
                    view.showMessage("Enter book ID to edit:");
                    int bookId = Integer.parseInt(view.getInput());
                    editBook(bookId);
                    break;
                case "6": // Delete Book
                    listBooks();
                    view.showMessage("Enter book ID to delete:");
                    int delBookId = Integer.parseInt(view.getInput());
                    bookDAO.deleteBook(delBookId);
                    view.showMessage("Book deleted successfully.");
                    break;
                case "7": // Logout
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

    private void handleReaderMenu() {
        view.showReaderMenu();
        String choice = view.getInput();
        try {
            switch (choice) {
                case "1": // Search Book
                    view.showMessage("Enter search keyword:");
                    String keyword = view.getInput();
                    List<Book> results = bookDAO.searchBooks(keyword);
                    if (results.isEmpty()) {
                        view.showMessage("No books found.");
                    } else {
                        for (Book book : results) {
                            view.showMessage(book.getId() + ": " + book.getTitle() + " (ISBN: " + book.getIsbn() + ")");
                        }
                    }
                    break;
                case "2": // Reserve Book
                    listBooks();
                    view.showMessage("Enter book ID to reserve:");
                    int resBookId = Integer.parseInt(view.getInput());
                    if (reservationDAO.reserveBook(currentUser.getId(), resBookId)) {
                        view.showMessage("Book reserved successfully.");
                    } else {
                        view.showMessage("Book is already reserved.");
                    }
                    break;
                case "3": // Add to Favorites
                    listBooks();
                    view.showMessage("Enter book ID to add to favorites:");
                    int favBookId = Integer.parseInt(view.getInput());
                    favoriteDAO.addFavorite(currentUser.getId(), favBookId);
                    view.showMessage("Book added to favorites.");
                    break;
                case "4": // Logout
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

    private void listCategories() throws SQLException {
        List<Category> categories = categoryDAO.getAllCategories();
        if (categories.isEmpty()) {
            view.showMessage("No categories found.");
        } else {
            for (Category cat : categories) {
                view.showMessage(cat.getId() + ": " + cat.getName());
            }
        }
    }

    private void listBooks() throws SQLException {
        List<Book> books = bookDAO.getAllBooks();
        if (books.isEmpty()) {
            view.showMessage("No books found.");
        } else {
            for (Book book : books) {
                view.showMessage(book.getId() + ": " + book.getTitle() + " (ISBN: " + book.getIsbn() + ")");
            }
        }
    }

    private void addBook() throws SQLException {
        view.showMessage("Enter book title:");
        String title = view.getInput();
        if (title.isEmpty()) throw new IllegalArgumentException("Title cannot be empty.");
        view.showMessage("Enter summary:");
        String summary = view.getInput();
        view.showMessage("Enter ISBN:");
        String isbn = view.getInput();
        if (isbn.length() != 13) throw new IllegalArgumentException("ISBN must be 13 characters.");
        view.showMessage("Enter page count:");
        int pageCount = Integer.parseInt(view.getInput());
        if (pageCount <= 0) throw new IllegalArgumentException("Page count must be positive.");
        listCategories();
        view.showMessage("Enter category ID:");
        int categoryId = Integer.parseInt(view.getInput());
        bookDAO.addBook(title, summary, isbn, pageCount, categoryId);
        view.showMessage("Book added successfully.");
    }

    private void editBook(int id) throws SQLException {
        Book book = bookDAO.getBookById(id);
        if (book == null) {
            view.showMessage("Book not found.");
            return;
        }
        view.showMessage("Current title: " + book.getTitle() + ". Enter new title (or press Enter to keep):");
        String title = view.getInput();
        if (title.isEmpty()) title = book.getTitle();
        view.showMessage("Current summary: " + book.getSummary() + ". Enter new summary (or press Enter to keep):");
        String summary = view.getInput();
        if (summary.isEmpty()) summary = book.getSummary();
        view.showMessage("Current ISBN: " + book.getIsbn() + ". Enter new ISBN (or press Enter to keep):");
        String isbn = view.getInput();
        if (isbn.isEmpty()) isbn = book.getIsbn();
        else if (isbn.length() != 13) throw new IllegalArgumentException("ISBN must be 13 characters.");
        view.showMessage("Current page count: " + book.getPageCount() + ". Enter new page count (or press Enter to keep):");
        String pageCountStr = view.getInput();
        int pageCount = pageCountStr.isEmpty() ? book.getPageCount() : Integer.parseInt(pageCountStr);
        if (pageCount <= 0) throw new IllegalArgumentException("Page count must be positive.");
        listCategories();
        view.showMessage("Current category ID: " + book.getCategoryId() + ". Enter new category ID (or press Enter to keep):");
        String catIdStr = view.getInput();
        int categoryId = catIdStr.isEmpty() ? book.getCategoryId() : Integer.parseInt(catIdStr);
        bookDAO.updateBook(id, title, summary, isbn, pageCount, categoryId);
        view.showMessage("Book updated successfully.");
    }
}