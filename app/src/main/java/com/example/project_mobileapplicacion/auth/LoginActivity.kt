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

        // Layouts
        tilEmail = findViewById(R.id.tilEmail)
        etEmail = findViewById(R.id.etEmail)

        // EditTexts
        tilPassword = findViewById(R.id.tilPassword)
        etPassword = findViewById(R.id.etPassword)

        // Botones y links
        btnLogin = findViewById(R.id.btnLogin)
        registerLink = findViewById(R.id.RegisterLink)
        guestLink = findViewById(R.id.GuestLink)

        // Validación en tiempo real
        setupRealtimeValidation()

        // Acción de login con validación
        btnLogin.setOnClickListener {
            if (validateForm()) {
                loginUser(
                    etEmail.text.toString().trim(),
                    etPassword.text.toString().trim()
                )
            }
        }

        // Ir a register
        registerLink.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }
        // Ir a invitado
        guestLink.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun setupRealtimeValidation() {
        etEmail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (emailTouched) ValidationActivity.validateEmail(this@LoginActivity, tilEmail, s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { if (etEmail.isFocused) emailTouched = true }
        })
        etEmail.setOnFocusChangeListener { _, hasFocus -> if (hasFocus) emailTouched = true }

        etPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (passwordTouched) ValidationActivity.validatePassword(this@LoginActivity, tilPassword, s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { if (etPassword.isFocused) passwordTouched = true }
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
                } else {
                    for (document in documents) {
                        val storedPassword = document.getString("password")
                        if (storedPassword == password) {
                            Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()

                            val userPrefs = getSharedPreferences("UserPrefs", MODE_PRIVATE)
                            userPrefs.edit {
                                putString("userEmail", email)
                            }

                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        .addOnFailureListener { exception ->
            Toast.makeText(this, "Error al iniciar sesión: ${exception.message}", Toast.LENGTH_SHORT).show()
        }
    }
}