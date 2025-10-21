package com.example.project_mobileapplicacion.auth

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.project_mobileapplicacion.auth.LoginActivity
import com.example.project_mobileapplicacion.R
import com.example.project_mobileapplicacion.cloud.FirebaseService
import com.example.project_mobileapplicacion.database.AppDataBase
import com.example.project_mobileapplicacion.model.UserEntity
import kotlinx.coroutines.launch
import java.util.Calendar

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

            val datePickerDialog = DatePickerDialog(
                this,
                R.style.SpinnerDatePicker,
                { _, selectedYear, selectedMonth, selectedDay ->
                    val formattedDate = String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear)
                    birthday.setText(formattedDate)
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }

        btnRegister.setOnClickListener {
            val nameStr = name.text.toString().trim()
            val lastnameStr = lastname.text.toString().trim()
            val birthdayStr = birthday.text.toString().trim()
            val phoneStr = phone.text.toString().trim()
            val emailStr = email.text.toString().trim()
            val passwordStr = password.text.toString()

            if (nameStr.isEmpty()) {
                name.error = "El nombre es obligatorio"
                name.requestFocus()
                return@setOnClickListener
            }
            if (nameStr.length < 2 || nameStr.length > 50) {
                name.error = "El nombre debe tener entre 2 y 50 caracteres"
                name.requestFocus()
                return@setOnClickListener
            }
            if (!nameStr.matches(Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$"))) {
                name.error = "El nombre solo puede contener letras"
                name.requestFocus()
                return@setOnClickListener
            }

            if (lastnameStr.isEmpty()) {
                lastname.error = "El apellido es obligatorio"
                lastname.requestFocus()
                return@setOnClickListener
            }
            if (lastnameStr.length < 2 || lastnameStr.length > 50) {
                lastname.error = "El apellido debe tener entre 2 y 50 caracteres"
                lastname.requestFocus()
                return@setOnClickListener
            }
            if (!lastnameStr.matches(Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$"))) {
                lastname.error = "El apellido solo puede contener letras"
                lastname.requestFocus()
                return@setOnClickListener
            }

            if (birthdayStr.isEmpty()) {
                birthday.error = "La fecha de nacimiento es obligatoria"
                birthday.requestFocus()
                return@setOnClickListener
            }

            if (!phoneStr.matches(Regex("^\\d{10}$"))) {
                phone.error = "El teléfono debe tener 10 dígitos"
                phone.requestFocus()
                return@setOnClickListener
            }

            if (emailStr.isEmpty() || emailStr.length > 100) {
                email.error = "El correo debe tener máximo 100 caracteres"
                email.requestFocus()
                return@setOnClickListener
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(emailStr).matches()) {
                email.error = "Ingrese un correo válido"
                email.requestFocus()
                return@setOnClickListener
            }

            if (passwordStr.length < 8) {
                password.error = "La contraseña debe tener al menos 8 caracteres"
                password.requestFocus()
                return@setOnClickListener
            }
            if (passwordStr.length > 20) {
                password.error = "La contraseña no puede superar 20 caracteres"
                password.requestFocus()
                return@setOnClickListener
            }

            registerUser(nameStr, lastnameStr, birthdayStr, phoneStr, emailStr, passwordStr)
        }

        tvLoginLink.setOnClickListener {
            val session = Intent(this, LoginActivity::class.java)
            startActivity(session)
            finish()
        }
    }

    private fun registerUser(name: String, lastname: String, birthday: String, phone: String, email: String, password: String){
        val db = AppDataBase.Companion.getInstance(applicationContext)
        val userDao = db.userDao()

        val userEntity = UserEntity(
            name = name,
            lastname = lastname,
            birthday = birthday,
            phone = phone,
            email = email,
            password = password
        )
        lifecycleScope.launch {
            try {
                userDao.insert(userEntity)
                FirebaseService.store(userEntity)
                Toast.makeText(this@RegisterActivity, "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this@RegisterActivity, "Error al registrar usuario: ${e.message}", Toast.LENGTH_SHORT).show()
            }
            val session = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(session)
            finish()
        }

    }
}