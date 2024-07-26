package com.newsofukraine.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface NewsDao {

    @Insert
    fun insert (newsEntity: NewsEntity);

    @Query("SELECT * FROM NewsEntity")
    fun read(): List<NewsEntity>

    @Delete
    fun delete (newsEntity: NewsEntity);
}