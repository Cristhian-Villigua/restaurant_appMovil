package com.example.project_mobileapplicacion

import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
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

        val name: EditText = findViewById(R.id.Name)
        val lastname: EditText = findViewById(R.id.Lastname)
        val birthday: EditText = findViewById(R.id.Birthday)
        val phone: EditText = findViewById(R.id.Phone)
        val email: EditText = findViewById(R.id.Email)
        val password: EditText = findViewById(R.id.Password)
        val btnRegister: Button = findViewById(R.id.btnRegister)
        val tvLoginLink: TextView = findViewById(R.id.LoginLink)

        birthday.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(this, { _, year1, month1, dayOfMonth ->
                birthday.setText("$dayOfMonth/${month1 + 1}/$year1")
            }, year, month, day).show()
        }
        btnRegister.setOnClickListener {
            val name = name.text.toString()
            val lastname = lastname.text.toString()
            val birthday = birthday.text.toString()
            val phone = phone.text.toString()
            val email = email.text.toString()
            val password = password.text.toString()

            if(name.isEmpty() || lastname.isEmpty() || birthday.isEmpty() || phone.isEmpty() || email.isEmpty() || password.isEmpty()){
                Toast.makeText(this, "Por favor complete los campos", Toast.LENGTH_SHORT).show()
            } else{
                registerUser(name, lastname, birthday, phone, email, password)
            }
        }

        tvLoginLink.setOnClickListener {
            val session = Intent(this, LoginActivity::class.java)
            startActivity(session)
            finish()
        }
    }

    private fun registerUser(name: String, lastname: String, birthday: String, phone: String, email: String, password: String){
        Toast.makeText(this, "Usuario registrado: $name $lastname", Toast.LENGTH_SHORT).show()

        val session = Intent(this, MainActivity::class.java)
        startActivity(session)
    }
}