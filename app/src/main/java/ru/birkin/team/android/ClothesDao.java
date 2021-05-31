package ru.birkin.team.android;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.internal.operators.completable.CompletableAmb;

@Dao
public interface ClothesDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Single<Long> insert(Clothes clothes);
    @Query("SELECT * FROM Clothes WHERE clothesId = :id")
    Maybe<Clothes> findClothesById(int id);
    @Delete
    Completable remove(Clothes clothes);
}
