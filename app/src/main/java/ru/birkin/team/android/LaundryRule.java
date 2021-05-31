package ru.birkin.team.android;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity
public class LaundryRule {
    @PrimaryKey
    @NonNull
    public String alias;
    public String displayName;
    public int drawableId;
    public LaundryRule(@NotNull String alias, String displayName, int drawableId) {
        this.alias = alias;
        this.displayName = displayName;
        this.drawableId = drawableId;
    }
}
