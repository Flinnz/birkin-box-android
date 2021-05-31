package ru.birkin.team.android;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface ClothesDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Single<Long> insert(Clothes clothes);
    @Query("SELECT * FROM Clothes WHERE clothesId = :id")
    Maybe<Clothes> findClothesById(int id);

}
