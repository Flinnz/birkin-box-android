package ru.birkin.team.android;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class ClothesWithLaundryRules {
    @Embedded
    public Clothes clothes;
    @Relation(
            parentColumn = "clothesId",
            entity = LaundryRule.class,
            entityColumn = "alias",
            associateBy = @Junction(
                    value = ClothesLaundryRuleRef.class,
                    parentColumn = "clothesId",
                    entityColumn = "alias"
            )
    )
    public List<LaundryRule> laundryRules;
}
