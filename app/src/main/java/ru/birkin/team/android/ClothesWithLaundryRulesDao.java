package ru.birkin.team.android;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface ClothesWithLaundryRulesDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Completable insert(ClothesLaundryRuleRef ClothesLaundryRuleRef);

    @Transaction
    @Query("SELECT * FROM Clothes")
    Flowable<List<ClothesWithLaundryRules>> getClothesWithRules();

    @Transaction
    @Query("SELECT * FROM Clothes WHERE clothesId = :id")
    Flowable<ClothesWithLaundryRules> getClothesWithRulesById(long id);

    @Query("DELETE FROM ClothesLaundryRuleRef WHERE clothesId = :id")
    Completable removeClothesReferences(long id);
}
