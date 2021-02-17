package com.ech.votoelectronicoseguridadinformatica;

import android.graphics.drawable.Drawable;

public class Candidate {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Candidate(String name, Drawable icon, String description) {
        this.name = name;
        this.icon = icon;
        this.description = description;
    }
    public Candidate() {

    }
    private String name;
    private Drawable icon;
    private String description;
}
