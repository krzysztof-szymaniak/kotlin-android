package com.example.firetactoe

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firetactoe.databinding.ActivityBoard5Binding

class Board5 : AppCompatActivity() {
    private lateinit var binding: ActivityBoard5Binding
    private val board = Array(5) { CharArray(5) }
    private val symbol: CharArray = charArrayOf('O', 'X')
    private var turn = 1;
    private var isOver = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBoard5Binding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        Toast.makeText(this, "Zaczyna X", Toast.LENGTH_SHORT).show()
    }

    private fun sendDataBackToPreviousActivity(winner: String) {
        val intent = Intent().apply {
            putExtra("winner", winner)
        }
        setResult(Activity.RESULT_OK, intent)
    }

    private fun end_dialog(winner: String, draw: Boolean) {
        isOver = true
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Koniec Gry!")
        val message = if (! draw) {
            "Wygrywa $winner"
        } else {
            "Remis"
        }
        builder.setMessage(message)
        builder.setPositiveButton("OK") { dialog, which -> finish() }
        sendDataBackToPreviousActivity(winner)
        builder.show()
    }

    private fun make_move(x: Int, y: Int) {
        board[x][y] = symbol[turn]
        val winner = symbol[turn].toString()
        if (board[x][y] == board[(x + 1) % 5][y]
            && board[x][y] == board[(x + 2) % 5][y]
            && board[x][y] == board[(x + 3) % 5][y]
            && board[x][y] == board[(x + 4) % 5][y]
        ) {
            end_dialog(winner, false) // row wise
        } else if (board[x][y] == board[x][(y + 1) % 5]
            && board[x][y] == board[x][(y + 2) % 5]
            && board[x][y] == board[x][(y + 3) % 5]
            && board[x][y] == board[x][(y + 4) % 5]
        ) {
            end_dialog(winner, false) // column wise
        } else if (x == y
            && board[x][y] == board[(x + 1) % 5][(y + 1) % 5]
            && board[x][y] == board[(x + 2) % 5][(y + 2) % 5]
            && board[x][y] == board[(x + 3) % 5][(y + 3) % 5]
            && board[x][y] == board[(x + 4) % 5][(y + 4) % 5]
        ) {
            end_dialog(winner, false) // top down diagonal
        } else if (x + y == 4
            && board[x][y] == board[(x + 1) % 5][(y + 4) % 5]
            && board[x][y] == board[(x + 2) % 5][(y + 3) % 5]
            && board[x][y] == board[(x + 3) % 5][(y + 2) % 5]
            && board[x][y] == board[(x + 4) % 5][(y + 1) % 5]
        ) {
            end_dialog(winner, false) // down top diagonal
        }
        turn = (turn + 1) % 2
        Toast.makeText(this, "Ruch " + symbol[turn].toString(), Toast.LENGTH_SHORT).show()
        for (i in 0..4) {
            for (j in 0..4) {
                if (board[i][j] == '\u0000') // check if free field exist
                    return
            }
        }
        if (! isOver)
            end_dialog("Remis", true) // else call draw
    }

    fun b00(view: View) {
        val x = 0
        val y = 0
        binding.b00.isClickable = false
        binding.b00.text = symbol[turn].toString()
        make_move(x, y)
    }

    fun b01(view: View) {
        val x = 0
        val y = 1
        binding.b01.isClickable = false
        binding.b01.text = symbol[turn].toString()
        make_move(x, y)
    }

    fun b02(view: View) {
        val x = 0
        val y = 2
        binding.b02.isClickable = false
        binding.b02.text = symbol[turn].toString()
        make_move(x, y)
    }

    fun b03(view: View) {
        val x = 0
        val y = 3
        binding.b03.isClickable = false
        binding.b03.text = symbol[turn].toString()
        make_move(x, y)
    }

    fun b04(view: View) {
        val x = 0
        val y = 4
        binding.b04.isClickable = false
        binding.b04.text = symbol[turn].toString()
        make_move(x, y)
    }

    fun b10(view: View) {
        val x = 1
        val y = 0
        binding.b10.isClickable = false
        binding.b10.text = symbol[turn].toString()
        make_move(x, y)
    }

    fun b11(view: View) {
        val x = 1
        val y = 1
        binding.b11.isClickable = false
        binding.b11.text = symbol[turn].toString()
        make_move(x, y)
    }

    fun b12(view: View) {
        val x = 1
        val y = 2
        binding.b12.isClickable = false
        binding.b12.text = symbol[turn].toString()
        make_move(x, y)
    }

    fun b13(view: View) {
        val x = 1
        val y = 3
        binding.b13.isClickable = false
        binding.b13.text = symbol[turn].toString()
        make_move(x, y)
    }

    fun b14(view: View) {
        val x = 1
        val y = 4
        binding.b14.isClickable = false
        binding.b14.text = symbol[turn].toString()
        make_move(x, y)
    }

    fun b20(view: View) {
        val x = 2
        val y = 0
        binding.b20.isClickable = false
        binding.b20.text = symbol[turn].toString()
        make_move(x, y)
    }

    fun b21(view: View) {
        val x = 2
        val y = 1
        binding.b21.isClickable = false
        binding.b21.text = symbol[turn].toString()
        make_move(x, y)
    }

    fun b22(view: View) {
        val x = 2
        val y = 2
        binding.b22.isClickable = false
        binding.b22.text = symbol[turn].toString()
        make_move(x, y)
    }

    fun b23(view: View) {
        val x = 2
        val y = 3
        binding.b23.isClickable = false
        binding.b23.text = symbol[turn].toString()
        make_move(x, y)
    }

    fun b24(view: View) {
        val x = 2
        val y = 4
        binding.b24.isClickable = false
        binding.b24.text = symbol[turn].toString()
        make_move(x, y)
    }

    fun b30(view: View) {
        val x = 3
        val y = 0
        binding.b30.isClickable = false
        binding.b30.text = symbol[turn].toString()
        make_move(x, y)
    }

    fun b31(view: View) {
        val x = 3
        val y = 1
        binding.b31.isClickable = false
        binding.b31.text = symbol[turn].toString()
        make_move(x, y)
    }

    fun b32(view: View) {
        val x = 3
        val y = 2
        binding.b32.isClickable = false
        binding.b32.text = symbol[turn].toString()
        make_move(x, y)
    }

    fun b33(view: View) {
        val x = 3
        val y = 3
        binding.b33.isClickable = false
        binding.b33.text = symbol[turn].toString()
        make_move(x, y)
    }

    fun b34(view: View) {
        val x = 3
        val y = 4
        binding.b34.isClickable = false
        binding.b34.text = symbol[turn].toString()
        make_move(x, y)
    }

    fun b40(view: View) {
        val x = 4
        val y = 0
        binding.b40.isClickable = false
        binding.b40.text = symbol[turn].toString()
        make_move(x, y)
    }

    fun b41(view: View) {
        val x = 4
        val y = 1
        binding.b41.isClickable = false
        binding.b41.text = symbol[turn].toString()
        make_move(x, y)
    }

    fun b42(view: View) {
        val x = 4
        val y = 2
        binding.b42.isClickable = false
        binding.b42.text = symbol[turn].toString()
        make_move(x, y)
    }

    fun b43(view: View) {
        val x = 4
        val y = 3
        binding.b43.isClickable = false
        binding.b43.text = symbol[turn].toString()
        make_move(x, y)
    }

    fun b44(view: View) {
        val x = 4
        val y = 4
        binding.b44.isClickable = false
        binding.b44.text = symbol[turn].toString()
        make_move(x, y)
    }

}