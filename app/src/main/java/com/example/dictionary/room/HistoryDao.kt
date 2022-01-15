package com.example.dictionary.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

/**
 * Все запросы в БД будут асинхронные, т к suspend функции.
 */
@Dao
interface HistoryDao {

    // Получить весь список слов
    @Query("SELECT * FROM HistoryEntity")
    suspend fun all(): List<HistoryEntity>

    // Получить конкретное слово
    @Query("SELECT * FROM HistoryEntity WHERE word LIKE :word")
    suspend fun getDataByWord(word: String): HistoryEntity

    // Сохранить новое слово
// OnConflictStrategy.IGNORE - дубликаты не будут сохраняться
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entity: HistoryEntity)

    // Вставить список слов
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(entities: List<HistoryEntity>)

    @Update
    suspend fun update(entity: HistoryEntity)

    @Delete
    suspend fun delete(entity: HistoryEntity)
}