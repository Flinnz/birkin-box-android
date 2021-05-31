package ru.birkin.team.android;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

import io.reactivex.rxjava3.core.Completable;

@Dao
public interface LaundryRuleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(LaundryRule laundryRule);
}
