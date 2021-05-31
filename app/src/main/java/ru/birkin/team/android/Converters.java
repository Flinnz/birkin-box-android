package ru.birkin.team.android;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.room.TypeConverter;

public class Converters {
    @TypeConverter
    public static BitmapWithPath fromPath(String path) {
        if (path == null) return null;
        Bitmap bmp = BitmapFactory.decodeFile(path);
        if (bmp == null) return null;
        return new BitmapWithPath(bmp, path);
    }

    @TypeConverter
    public static String bitmapWithPathToString(BitmapWithPath bmpWithPath) {
        return bmpWithPath == null ? null : bmpWithPath.path;
    }
}
