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


class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.supportActionBar?.hide()
        //Get Firebase auth instance
        var auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        // set the view now
        setContentView(R.layout.activity_login)
        val inputEmail = findViewById<View>(R.id.email) as EditText
        val inputPassword = findViewById<View>(R.id.password) as EditText
        val btnRegister = findViewById<View>(R.id.btn_register) as Button
        val btnLogin = findViewById<View>(R.id.btn_login) as Button

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance()
        btnRegister.setOnClickListener{
                startActivity(Intent(this, RegisterActivity::class.java))
        }
        btnLogin.setOnClickListener{
                val email = inputEmail.text.toString()
                val password: String = inputPassword.text.toString()
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(applicationContext, "Enter email address!", Toast.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(applicationContext, "Enter password!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                //authenticate user
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(
                        this
                    ) { task ->

                        if (!task.isSuccessful) {
                            // there was an error
                            if (password.length < 6) {
                                inputPassword.error = getString(R.string.minimum_password)
                            } else {
                                Toast.makeText(
                                    this,
                                    getString(R.string.auth_failed),
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        } else {
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }

        }
    }
}