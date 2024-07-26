package com.newsofukraine.data.local

import androidx.room.Database

@Database(entities = [NewsEntity::class], version = 1)
abstract class LocalDataBase {
    abstract fun newsDao(): NewsDao
}
