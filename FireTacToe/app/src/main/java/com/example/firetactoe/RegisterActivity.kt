package com.example.firetactoe

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth


class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.supportActionBar?.hide()
        setContentView(R.layout.activity_register)

        //Get Firebase auth instance
        val auth = FirebaseAuth.getInstance()
        val btnSignIn = findViewById<View>(R.id.sign_in_button) as Button
        val btnSignUp = findViewById<View>(R.id.sign_up_button) as Button
        val inputEmail = findViewById<View>(R.id.email) as EditText
        val inputPassword = findViewById<View>(R.id.password) as EditText

        btnSignIn.setOnClickListener{
            finish()
        }
        btnSignUp.setOnClickListener{
                val email = inputEmail.text.toString().trim { it <= ' ' }
                val password: String = inputPassword.text.toString().trim { it <= ' ' }
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(applicationContext, "Enter email address!", Toast.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(applicationContext, "Enter password!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                if (password.length < 6) {
                    Toast.makeText(
                        applicationContext,
                        "Password too short, enter minimum 6 characters!",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
                //create user
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this
                    ) { task ->
                        if (task.isSuccessful) {
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(
                                this, "Authentication failed." + task.exception,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

        }
    }
}