package com.library.model;

public class Book {
    private int id;
    private String title;
    private String summary;
    private String isbn;
    private int pageCount;
    private int categoryId;

    public Book(int id, String title, String summary, String isbn, int pageCount, int categoryId) {
        this.id = id;
        this.title = title;
        this.summary = summary;
        this.isbn = isbn;
        this.pageCount = pageCount;
        this.categoryId = categoryId;
    }

    // Getters and setters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getSummary() { return summary; }
    public String getIsbn() { return isbn; }
    public int getPageCount() { return pageCount; }
    public int getCategoryId() { return categoryId; }
}