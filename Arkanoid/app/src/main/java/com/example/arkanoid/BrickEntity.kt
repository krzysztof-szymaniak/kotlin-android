package com.example.arkanoid

import android.graphics.Color
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.io.Serializable

@Entity
data class BrickEntity(
    @ColumnInfo(name = "brickID")
    @PrimaryKey(autoGenerate = true) val brickID: Int = 0,
    val x : Float,
    val y : Float,
    val width : Float,
    val height : Float,
    val color : Int
) : Serializable {
}


