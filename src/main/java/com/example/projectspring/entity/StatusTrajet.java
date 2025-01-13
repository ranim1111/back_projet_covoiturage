package com.example.projectspring.entity;

public enum StatusTrajet {
    ACTIF("Actif"),
    TERMINE("Terminé"),
    CANCELLED("Annulé");

    private final String label;

    StatusTrajet(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}

