package com.example.arkanoid

import android.graphics.Color
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.room.Room

class MainActivity : AppCompatActivity() {
    private lateinit var database: AppDatabase
    private lateinit var dao: Dao
    private lateinit var gameView: GameView
    lateinit var bricks: MutableList<Brick>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        gameView = findViewById(R.id.game_view)
        gameView.parentActivity = this
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "database"
        ).allowMainThreadQueries()  // terrible practice but in terms of small app its sufficient
            .build()
        dao = database.dao()
        val bricksEntity = dao.getAll()
        bricks = mutableListOf()
        if (bricksEntity.isNotEmpty()){
            val border = Paint().apply {
                style = Paint.Style.STROKE
                strokeWidth = 5f
                color = Color.BLACK
            }
            for (be in bricksEntity) {
                val color = Paint().apply { color = be.color }
                bricks.add(Brick(be.x, be.y, be.width, be.height, color, border, false))
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        dao.clear()
        for(b in gameView.bricks){
            dao.insert(BrickEntity(0, b.x, b.y, b.width, b.height, b.color.color))
        }
    }

}