package ru.birkin.team.android;

import android.graphics.Bitmap;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Clothes {
    @PrimaryKey(autoGenerate = true)
    public long clothesId;
    public BitmapWithPath clothesPhoto;
    public String name;
    public String description;

    public Clothes(BitmapWithPath clothesPhoto, String name, String description) {
        this.clothesPhoto = clothesPhoto;
        this.name = name;
        this.description = description;
    }
}
