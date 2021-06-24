package com.example.arkanoid

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query


@Dao
interface Dao {
    @Insert
    fun insert(brickEntity: BrickEntity)

    @Query("SELECT * FROM brickentity")
    fun getAll() : List<BrickEntity>

    @Query("DELETE FROM brickentity")
    fun clear()
}
