package com.maitistaiga.model;

public class Patiekalas {
    private int id;
    private String pavadinimas;
    private String aprasymas;
    private int maitIstaigaId;

    public Patiekalas(int id, String pavadinimas, String aprasymas, int maitIstaigaId) {
        this.id = id;
        this.pavadinimas = pavadinimas;
        this.aprasymas = aprasymas;
        this.maitIstaigaId = maitIstaigaId;
    }

    // Getters and setters
    public int getId() { return id; }
    public String getPavadinimas() { return pavadinimas; }
    public String getAprasymas() { return aprasymas; }
    public int getMaitIstaigaId() { return maitIstaigaId; }
}