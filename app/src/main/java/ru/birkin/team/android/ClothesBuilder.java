package ru.birkin.team.android;

import android.graphics.Bitmap;

public class ClothesBuilder {
    private Bitmap photo;
    private String name;
    private String description;

    public ClothesBuilder setPhoto(Bitmap photo) {
        this.photo = photo;
        return this;
    }

    public ClothesBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public ClothesBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public Clothes build() {
        return new Clothes(photo, name, description);
    }
}
