package com.example.cinema_booking_mobile.model;

public class PerplexityModel {
    private String id;
    private String name;
    private String category;
    private String description;
    private String strengths;
    private String weaknesses;

    public PerplexityModel(String id, String name, String category, String description, String strengths, String weaknesses) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.description = description;
        this.strengths = strengths;
        this.weaknesses = weaknesses;
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
    public String getStrengths() { return strengths; }
    public String getWeaknesses() { return weaknesses; }
}