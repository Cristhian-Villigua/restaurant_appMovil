package com.example.project_mobileapplicacion

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class RegisterActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val Name: EditText = findViewById(R.id.Name)
        val Lastname: EditText = findViewById(R.id.Lastname)
        val Birthday: EditText = findViewById(R.id.Birthday)
        val Phone: EditText = findViewById(R.id.Phone)
        val Email: EditText = findViewById(R.id.Email)
        val Password: EditText = findViewById(R.id.Password)
        val btnRegister: Button = findViewById(R.id.btnRegister)
        val btnLogin: TextView = findViewById(R.id.LoginLink)

        btnRegister.setOnClickListener {
            val Name = Name.text.toString()
            val Lastname = Lastname.text.toString()
            val Birthday = Birthday.text.toString()
            val Phone = Phone.text.toString()
            val Email = Email.text.toString()
            val Password = Password.text.toString()

            if(Name.isEmpty() || Lastname.isEmpty() || Birthday.isEmpty() || Phone.isEmpty() || Email.isEmpty() || Password.isEmpty()){
                Toast.makeText(this, "Por favor complete los campos", Toast.LENGTH_SHORT).show()
            } else{
                registerUser(Name,Lastname,Birthday,Phone,Email,Password)
            }
        }

        btnLogin.setOnClickListener {
            val session = Intent(this, LoginActivity::class.java)
            startActivity(session)
        }
    }
    private fun registerUser(Name: String, Lastname: String, Birthday: String, Phone: String, Email: String, Password: String){
        Toast.makeText(this, "Usuario registrado: $Name $Lastname", Toast.LENGTH_SHORT).show()

        val session = Intent(this, LoginActivity::class.java)
        startActivity(session)
    }
}