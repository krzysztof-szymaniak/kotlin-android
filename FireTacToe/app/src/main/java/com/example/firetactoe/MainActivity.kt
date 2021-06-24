package com.example.firetactoe

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firetactoe.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

const val DB_URL = "https://tictactoe-76f06-default-rtdb.europe-west1.firebasedatabase.app/"

data class Move(val symbol: String, val x : Int, val y: Int)
data class Game(val code: String, val moveX: Move?, val moveO: Move?, val online: Boolean, val joined: Boolean)

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        this.supportActionBar!!.hide()
        database = Firebase.database(DB_URL).reference
    }

    fun start3x3(view: View) {
        var code: String
        database.child("games").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                code = generateCode()
                while (checkIfExists(dataSnapshot, code)){
                    code = generateCode()
                }
                createNewGame(code)
                val intent = Intent(this@MainActivity, Board3::class.java)
                intent.putExtra("symbol", "X")
                intent.putExtra("code", code)
                intent.putExtra("db", DB_URL)
                startActivity(intent)
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
        
    }
    fun createNewGame(code: String){
        val game = Game(code, null, null, online = true, joined = false)
        database.child("games").child(code).setValue(game)
    }
    fun generateCode(): String {
        val bobTheBuilder = StringBuilder()
        val length = 6
        for ( i in 1 .. length ){
            val digit = (0..9).random()
            bobTheBuilder.append(digit)
        }
        return bobTheBuilder.toString()
    }

    fun joinGame(view: View) {
        Toast.makeText(applicationContext, "Please Wait...", Toast.LENGTH_SHORT)
            .show()
        val code = binding.inputGameCode.text.toString().filter { !it.isWhitespace() }
        if (code.isEmpty()){
            Toast.makeText(applicationContext, "Game code is empty", Toast.LENGTH_SHORT)
                .show()
            return
        }
        database.child("games").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    if(checkIfExists(dataSnapshot, code)){
                        binding.inputGameCode.setText("")
                        val intent = Intent(this@MainActivity, Board3::class.java)
                        intent.putExtra("symbol", "O")
                        intent.putExtra("code", code)
                        intent.putExtra("db", DB_URL)
                        startActivity(intent)

                    }
                    else{
                        Toast.makeText(applicationContext, "Invalid game code", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    fun checkIfExists(snapshot: DataSnapshot, code: String): Boolean {
        val data = snapshot.children
        data.forEach {
            val c = it.child("code").value
            if (c == code) {
                return true
            }
        }
        return false
    }


    fun logout(view: View){
        val auth = FirebaseAuth.getInstance()
        auth.signOut()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

}