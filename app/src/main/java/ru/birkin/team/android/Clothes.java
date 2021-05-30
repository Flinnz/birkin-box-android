package ru.birkin.team.android;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

public class Clothes {
    public final Bitmap clothesPhoto;
    public final String name;
    public final String description;
    public final List<LaundryRule> laundryRules;

    public Clothes(Bitmap clothesPhoto, String name, String description) {
        this.clothesPhoto = clothesPhoto;
        this.name = name;
        this.description = description;
        this.laundryRules = new ArrayList<LaundryRule>();
    }

    public Clothes(Bitmap clothesPhoto, String name, String description, List<LaundryRule> laundryRules) {
        this.clothesPhoto = clothesPhoto;
        this.name = name;
        this.description = description;
        this.laundryRules = laundryRules;
    }
}
