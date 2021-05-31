package ru.birkin.team.android;

import android.app.Application;

import androidx.room.Room;

public class BirkinBoxApplication extends Application {
    public static BirkinBoxApplication instance;
    private ClothesDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        database = Room.databaseBuilder(this, ClothesDatabase.class, "database")
                .build();
    }

    public static BirkinBoxApplication getInstance() {
        return instance;
    }

    public ClothesDatabase getDatabase() {
        return database;
    }
}
