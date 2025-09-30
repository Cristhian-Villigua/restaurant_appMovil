package com.example.project_mobileapplicacion


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class LoginActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val Email : EditText = findViewById(R.id.Email)
        val Password : EditText = findViewById(R.id.Password)
        val btnLogin : Button = findViewById(R.id.btnLogin)
        val btnRegister : Button = findViewById(R.id.btnRegister)

        btnLogin.setOnClickListener {
            val Email = Email.text.toString()
            val Password = Password.text.toString()
        }

        btnRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}