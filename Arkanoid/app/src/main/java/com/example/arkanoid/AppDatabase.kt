package com.example.arkanoid

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [BrickEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dao(): Dao
}
