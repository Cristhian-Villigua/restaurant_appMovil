package com.example.project_mobileapplicacion.auth

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.edit
import androidx.appcompat.app.AppCompatActivity
import com.example.project_mobileapplicacion.MainActivity
import com.example.project_mobileapplicacion.R
import com.example.project_mobileapplicacion.menu.KitchenActivity
import com.example.project_mobileapplicacion.waiter.ScanQrActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    private lateinit var tilEmail: TextInputLayout
    private lateinit var tilPassword: TextInputLayout
    private lateinit var etEmail: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var btnLogin: Button
    private lateinit var registerLink: TextView
    private lateinit var guestLink: TextView
    private var emailTouched = false
    private var passwordTouched = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        tilEmail = findViewById(R.id.tilEmail)
        etEmail = findViewById(R.id.etEmail)

        tilPassword = findViewById(R.id.tilPassword)
        etPassword = findViewById(R.id.etPassword)

        btnLogin = findViewById(R.id.btnLogin)
        registerLink = findViewById(R.id.RegisterLink)
        guestLink = findViewById(R.id.GuestLink)

        setupRealtimeValidation()
        insertDefaultUsers()

        btnLogin.setOnClickListener {
            if (validateForm()) {
                loginUser(
                    etEmail.text.toString().trim(),
                    etPassword.text.toString().trim()
                )
            }
        }

        registerLink.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }
        guestLink.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun insertDefaultUsers() {
        val dbFirestore = FirebaseFirestore.getInstance()

        val adminUser = hashMapOf(
            "name" to "Admin",
            "lastname" to "Admin",
            "birthday" to "1990-01-01",
            "phone" to "123456789",
            "email" to "admin@admin.com",
            "password" to "admin123",
            "role" to "Administrador"
        )

        val cocineroUser = hashMapOf(
            "name" to "Cocinero",
            "lastname" to "Cocinero",
            "birthday" to "1995-02-01",
            "phone" to "987654321",
            "email" to "cocinero@example.com",
            "password" to "cocinero123",
            "role" to "Cocinero"
        )

        val meseroUser = hashMapOf(
            "name" to "Mesero",
            "lastname" to "Mesero",
            "birthday" to "1992-05-15",
            "phone" to "555123456",
            "email" to "mesero@example.com",
            "password" to "mesero123",
            "role" to "Mesero"
        )

        dbFirestore.collection("users").document("admin@admin.com").set(adminUser)
        dbFirestore.collection("users").document("cocinero@example.com").set(cocineroUser)
        dbFirestore.collection("users").document("mesero@example.com").set(meseroUser)
    }

    private fun setupRealtimeValidation() {
        etEmail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (emailTouched) ValidationActivity.validateEmail(this@LoginActivity, tilEmail, s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (etEmail.isFocused) emailTouched = true
            }
        })
        etEmail.setOnFocusChangeListener { _, hasFocus -> if (hasFocus) emailTouched = true }

        etPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (passwordTouched) ValidationActivity.validatePassword(this@LoginActivity, tilPassword, s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (etPassword.isFocused) passwordTouched = true
            }
        })
        etPassword.setOnFocusChangeListener { _, hasFocus -> if (hasFocus) passwordTouched = true }
    }

    private fun validateForm(): Boolean {
        var valid = true
        if (!ValidationActivity.validateEmail(this, tilEmail, etEmail.text.toString())) valid = false
        if (!ValidationActivity.validatePassword(this, tilPassword, etPassword.text.toString())) valid = false
        if (!valid) Toast.makeText(this, "Por favor, corrija los errores", Toast.LENGTH_SHORT).show()
        return valid
    }

    private fun loginUser(email: String, password: String) {
        val db = FirebaseFirestore.getInstance()

        db.collection("users").whereEqualTo("email", email).get()
            .addOnSuccessListener { documents ->

                if (documents.isEmpty) {
                    Toast.makeText(this, "Correo no encontrado", Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                }

                val document = documents.documents[0]

                val storedPassword = document.getString("password")
                val role = document.getString("role") ?: "Usuario"

                if (storedPassword == password) {

                    val userPrefs = getSharedPreferences("UserPrefs", MODE_PRIVATE)
                    userPrefs.edit {
                        putString("userEmail", email)
                        putString("userRole", role)
                        apply()
                    }

                    Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()

                    android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                        when (role) {
                            "Administrador" -> startActivity(Intent(this, MainActivity::class.java))
                            "Cocinero" -> startActivity(Intent(this, KitchenActivity::class.java))
                            "Mesero" -> startActivity(Intent(this, ScanQrActivity::class.java))
                            else -> startActivity(Intent(this, MainActivity::class.java))
                        }
                    }, 100)

                } else {
                    Toast.makeText(this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error al iniciar sesión: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }
}