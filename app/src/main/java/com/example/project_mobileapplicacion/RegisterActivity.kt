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
        // onCreate se llama una sola vez, cuando la actividad se crea.
        Toast.makeText(this, "Actividad Creada (onCreate)", Toast.LENGTH_SHORT).show()

        // (Opcional pero recomendado) Se ajustaron los nombres de las variables para seguir las convenciones de Kotlin.
        val inputName: EditText = findViewById(R.id.Name)
        val inputLastname: EditText = findViewById(R.id.Lastname)
        val inputBirthday: EditText = findViewById(R.id.Birthday)
        val inputPhone: EditText = findViewById(R.id.Phone)
        val inputEmail: EditText = findViewById(R.id.Email)
        val inputPassword: EditText = findViewById(R.id.Password)
        val btnRegister: Button = findViewById(R.id.btnRegister)
        val tvLoginLink: TextView = findViewById(R.id.LoginLink)

        btnRegister.setOnClickListener {
            // Se usan los nombres de las variables de la UI para obtener el texto.
            val name = inputName.text.toString()
            val lastname = inputLastname.text.toString()
            val birthday = inputBirthday.text.toString()
            val phone = inputPhone.text.toString()
            val email = inputEmail.text.toString()
            val password = inputPassword.text.toString()

            if(name.isEmpty() || lastname.isEmpty() || birthday.isEmpty() || phone.isEmpty() || email.isEmpty() || password.isEmpty()){
                Toast.makeText(this, "Por favor complete los campos", Toast.LENGTH_SHORT).show()
            } else{
                registerUser(name,lastname,birthday,phone,email,password)
            }
        }

        tvLoginLink.setOnClickListener {
            val session = Intent(this, LoginActivity::class.java)
            startActivity(session)
        }
    }

    // --- INICIO DE CAMBIOS: CICLO DE VIDA ---

    override fun onStart() {
        super.onStart()
        // onStart se llama cuando la actividad se vuelve visible para el usuario, pero aún no puede interactuar con ella.
        Toast.makeText(this, "Actividad Iniciada (onStart)", Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        // onResume se llama cuando la actividad está en primer plano y el usuario ya puede interactuar con ella.
        Toast.makeText(this, "Actividad Reanudada (onResume)", Toast.LENGTH_SHORT).show()
    }

    // --- FIN DE CAMBIOS ---


    private fun registerUser(name: String, lastname: String, birthday: String, phone: String, email: String, password: String){
        Toast.makeText(this, "Usuario registrado: $name $lastname", Toast.LENGTH_SHORT).show()

        val session = Intent(this, LoginActivity::class.java)
        startActivity(session)
    }
}
