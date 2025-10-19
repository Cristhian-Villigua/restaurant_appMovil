package com.example.project_mobileapplicacion.Auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.project_mobileapplicacion.MainActivity
import com.example.project_mobileapplicacion.R
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val email : EditText = findViewById(R.id.Email)
        val password : EditText = findViewById(R.id.Password)
        val btnLogin : Button = findViewById(R.id.btnLogin)
        val btnRegister : TextView = findViewById(R.id.RegisterLink)
        val btnGuest = findViewById<TextView>(R.id.btnGuest)


        btnLogin.setOnClickListener {
            val email = email.text.toString()
            val password = password.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                loginUser(email, password)
            } else {
                Toast.makeText(this@LoginActivity, "Por favor ingrese todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        btnRegister.setOnClickListener {
            val session = Intent(this, RegisterActivity::class.java)
            startActivity(session)
            finish()
        }

        btnGuest.setOnClickListener {
            val session = Intent(this, MainActivity::class.java)
            startActivity(session)
        }
    }
    fun loginUser(email: String, password: String) {
        val db = FirebaseFirestore.getInstance()

        db.collection("users")
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    Toast.makeText(this@LoginActivity, "Correo no encontrado", Toast.LENGTH_SHORT).show()
                } else {
                    for (document in documents) {
                        val storedPassword = document.getString("password")
                        if (storedPassword == password) {
                            Toast.makeText(this@LoginActivity, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this@LoginActivity, "Contraseña incorrecta", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this@LoginActivity, "Error al iniciar sesión: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }
}