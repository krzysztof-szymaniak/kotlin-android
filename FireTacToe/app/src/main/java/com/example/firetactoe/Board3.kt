package com.example.firetactoe

import android.app.AlertDialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.firetactoe.databinding.ActivityBoard3Binding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.lang.Exception

class Board3 : AppCompatActivity() {
    private lateinit var binding: ActivityBoard3Binding
    private val board = Array(3) { arrayOfNulls<String>(3) }
    private var isOver = false
    private var isMyTurn = false
    private var code = ""
    private var mySymbol = ""
    private var movesLeft = 9
    private val buttons = Array(3) { arrayOfNulls<Button>(3) }
    private lateinit var game: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        code = intent.getStringExtra("code").toString()
        mySymbol = intent.getStringExtra("symbol").toString()

        val DB_URL = intent.getStringExtra("db").toString()
        game = Firebase.database(DB_URL).reference.child("games").child(code)

        this.supportActionBar!!.title =
            "Game code: \t${code.substring((0..2))} ${code.substring((3..5))}"
        this.supportActionBar!!.setBackgroundDrawable(
            ColorDrawable(
                ContextCompat.getColor(
                    this,
                    R.color.colorPrimaryDark
                )
            )
        )

        binding = ActivityBoard3Binding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        Toast.makeText(this, "My symbol: $mySymbol", Toast.LENGTH_SHORT).show()
        isMyTurn = mySymbol == "X"
        if (!isMyTurn){
            game.child("joined").setValue(true)
        }
        loadButtons()
        startListening()
        listenIfOnline()
    }

    private fun loadButtons() {
        buttons[0][0] = binding.topleft
        buttons[1][0] = binding.top
        buttons[2][0] = binding.topright
        buttons[0][1] = binding.midleft
        buttons[1][1] = binding.mid
        buttons[2][1] = binding.midright
        buttons[0][2] = binding.botleft
        buttons[1][2] = binding.bot
        buttons[2][2] = binding.botright
    }

    private fun startListening() {
        val symbol = if (mySymbol == "X") "O" else "X"
        game.child("move$symbol").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val x = Integer.parseInt(dataSnapshot.child("x").value.toString())
                    val y = Integer.parseInt(dataSnapshot.child("y").value.toString())
                    val s = dataSnapshot.child("symbol").value.toString()
                    make_move(x, y, s)
                    buttons[x][y]?.text = s
                    buttons[x][y]?.isClickable = false
                    isMyTurn = true
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun listenIfOnline() {
        game.child("online").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (!dataSnapshot.value.toString().toBoolean()) { // user is offline
                        val builder = AlertDialog.Builder(this@Board3)
                        builder.setTitle("Game over!")
                        builder.setMessage("Opponent has disconnected :<")
                        builder.setPositiveButton("OK") { _, _ ->
                            deleteGame()
                            finish()
                        }
                        try {
                            builder.show()
                        }
                        catch (ex: Exception){
                            finish()
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun deleteGame() {
        game.removeValue()
    }

    private fun endDialog(winner: String, draw: Boolean) {
        isOver = true
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Game over!")
        val message = if (!draw) {
            "$winner wins!"
        } else {
            "Draw!"
        }
        builder.setMessage(message)
        builder.setPositiveButton("OK") { _, _ ->
            deleteGame()
            finish()
        }
        builder.show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        game.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    game.child("online").setValue(false)
                    if (! dataSnapshot.child("joined").value.toString().toBoolean()){
                        deleteGame()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })

    }

    private fun make_move(x: Int, y: Int, symbol: String) {
        if (symbol == mySymbol) {
            game.child("move$mySymbol").setValue(Move(mySymbol, x, y))
        }
        isMyTurn = false
        board[x][y] = symbol
        val winner = symbol
        if (board[x][y] == board[(x + 1) % 3][y] && board[x][y] == board[(x + 2) % 3][y]) {
            endDialog(winner, false) // row wise
        } else if (board[x][y] == board[x][(y + 1) % 3] && board[x][y] == board[x][(y + 2) % 3]) {
            endDialog(winner, false) // column wise
        } else if (x == y
            && board[x][y] == board[(x + 1) % 3][(y + 1) % 3]
            && board[x][y] == board[(x + 2) % 3][(y + 2) % 3]
        ) {
            endDialog(winner, false) // top down diagonal
        } else if (x + y == 2
            && board[x][y] == board[(x + 1) % 3][(y + 2) % 3]
            && board[x][y] == board[(x + 2) % 3][(y + 1) % 3]
        ) {
            endDialog(winner, false) // down top diagonal
        }
        movesLeft--
        if (movesLeft == 0)
            endDialog("Draw", true) // else call draw
    }

    fun botright(view: View) {
        val x = 2
        val y = 2
        if (!isMyTurn)
            return
        binding.botright.isClickable = false
        binding.botright.text = mySymbol
        make_move(x, y, mySymbol)
    }

    fun bot(view: View) {
        val x = 1
        val y = 2
        if (!isMyTurn) {
            Toast.makeText(this, "Not your turn!", Toast.LENGTH_SHORT).show()
            return
        }
        isMyTurn = false
        binding.bot.isClickable = false
        binding.bot.text = mySymbol
        make_move(x, y, mySymbol)
    }

    fun botleft(view: View) {
        val x = 0
        val y = 2
        if (!isMyTurn) {
            Toast.makeText(this, "Not your turn!", Toast.LENGTH_SHORT).show()
            return
        }
        isMyTurn = false
        binding.botleft.isClickable = false
        binding.botleft.text = mySymbol
        make_move(x, y, mySymbol)
    }

    fun midright(view: View) {
        val x = 2
        val y = 1
        if (!isMyTurn) {
            Toast.makeText(this, "Not your turn!", Toast.LENGTH_SHORT).show()
            return
        }
        isMyTurn = false
        binding.midright.isClickable = false
        binding.midright.text = mySymbol
        make_move(x, y, mySymbol)
    }

    fun mid(view: View) {
        val x = 1
        val y = 1
        if (!isMyTurn) {
            Toast.makeText(this, "Not your turn!", Toast.LENGTH_SHORT).show()
            return
        }
        isMyTurn = false
        binding.mid.isClickable = false
        binding.mid.text = mySymbol
        make_move(x, y, mySymbol)
    }

    fun midleft(view: View) {
        val x = 0
        val y = 1
        if (!isMyTurn) {
            Toast.makeText(this, "Not your turn!", Toast.LENGTH_SHORT).show()
            return
        }
        isMyTurn = false
        binding.midleft.isClickable = false
        binding.midleft.text = mySymbol
        make_move(x, y, mySymbol)
    }

    fun top(view: View) {
        val x = 1
        val y = 0
        if (!isMyTurn) {
            Toast.makeText(this, "Not your turn!", Toast.LENGTH_SHORT).show()
            return
        }
        isMyTurn = false
        binding.top.isClickable = false
        binding.top.text = mySymbol
        make_move(x, y, mySymbol)
    }

    fun topright(view: View) {
        val x = 2
        val y = 0
        if (!isMyTurn) {
            Toast.makeText(this, "Not your turn!", Toast.LENGTH_SHORT).show()
            return
        }
        isMyTurn = false
        binding.topright.isClickable = false
        binding.topright.text = mySymbol
        make_move(x, y, mySymbol)
    }

    fun topleft(view: View) {
        val x = 0
        val y = 0
        if (!isMyTurn) {
            Toast.makeText(this, "Not your turn!", Toast.LENGTH_SHORT).show()
            return
        }
        isMyTurn = false
        binding.topleft.isClickable = false
        binding.topleft.text = mySymbol
        make_move(x, y, mySymbol)
    }

}