package com.newsofukraine.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [NewsEntity::class], version = 1)
abstract class LocalDataBase: RoomDatabase() {
    abstract fun newsDao(): NewsDao
}
