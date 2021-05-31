package ru.birkin.team.android;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {LaundryRule.class, Clothes.class, ClothesLaundryRuleRef.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class ClothesDatabase extends RoomDatabase {
    public abstract ClothesWithLaundryRulesDao clothesWithLaundryRulesDao();
    public abstract ClothesDao clothesDao();
    public abstract LaundryRuleDao laundryRuleDao();
}
