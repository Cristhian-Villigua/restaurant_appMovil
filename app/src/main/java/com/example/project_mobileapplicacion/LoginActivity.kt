package com.example.project_mobileapplicacion

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val Email : EditText = findViewById(R.id.Email)
        val Password : EditText = findViewById(R.id.Password)
        val btnLogin : Button = findViewById(R.id.btnLogin)
        val tvRegisterLink : TextView = findViewById(R.id.RegisterLink)

        btnLogin.setOnClickListener {
            val email = Email.text.toString()
            val password = Password.text.toString()

            if(email.isEmpty() || password.isEmpty()){
                Toast.makeText(this, "Por favor ingrese su correo y contraseña", Toast.LENGTH_SHORT).show()
            } else {
                loginUser(email, password)
            }
        }

        tvRegisterLink.setOnClickListener {
            val session = Intent(this, RegisterActivity::class.java)
            startActivity(session)
            finish()
        }

    }
    private fun loginUser(email: String, password: String) {
        Toast.makeText(this, "¡Bienvenido! Sesión iniciada.", Toast.LENGTH_SHORT).show()

        val session = Intent(this, MainActivity::class.java)
        startActivity(session)
    }
}