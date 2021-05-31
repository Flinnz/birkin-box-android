package ru.birkin.team.android;

import androidx.room.Entity;

import org.jetbrains.annotations.NotNull;

@Entity(primaryKeys = {"clothesId", "alias"})
public class ClothesLaundryRuleRef {
    public long clothesId;
    @NotNull
    public String alias;
}
