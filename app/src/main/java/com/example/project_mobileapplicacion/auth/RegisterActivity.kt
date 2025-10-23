package com.example.project_mobileapplicacion.auth

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.project_mobileapplicacion.R
import com.example.project_mobileapplicacion.cloud.FirebaseService
import com.example.project_mobileapplicacion.database.AppDataBase
import com.example.project_mobileapplicacion.model.UserEntity
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
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
            showDatePickerDialog(birthday)
        }

        addValidationListeners(name, lastname, birthday, phone, email, password)

        btnRegister.setOnClickListener {
            if (validateForm(name, lastname, birthday, phone, email, password)) {
                registerUser(
                    name.text.toString().trim(),
                    lastname.text.toString().trim(),
                    birthday.text.toString().trim(),
                    phone.text.toString().trim(),
                    email.text.toString().trim(),
                    password.text.toString()
                )
            }
        }

        tvLoginLink.setOnClickListener {
            val session = Intent(this, LoginActivity::class.java)
            startActivity(session)
            finish()
        }
    }

    private fun showDatePickerDialog(birthday: EditText) {
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

    private fun addValidationListeners(name: EditText, lastname: EditText, birthday: EditText, phone: EditText, email: EditText, password: EditText) {
        name.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().length < 2 || s.toString().length > 50) {
                    name.error = "El nombre debe tener entre 2 y 50 caracteres"
                } else if (!s.toString().matches(Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$"))) {
                    name.error = "El nombre solo puede contener letras"
                } else {
                    name.error = null
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        lastname.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().length < 2 || s.toString().length > 50) {
                    lastname.error = "El apellido debe tener entre 2 y 50 caracteres"
                } else if (!s.toString().matches(Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$"))) {
                    lastname.error = "El apellido solo puede contener letras"
                } else {
                    lastname.error = null
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        birthday.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val format = SimpleDateFormat("dd/MM/yyyy", Locale.US)
                try {
                    val date = format.parse(s.toString())
                    val calendar = Calendar.getInstance()
                    calendar.time = date
                    val year = calendar.get(Calendar.YEAR)
                    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
                    if (currentYear - year < 18) {
                        birthday.error = "Debe ser mayor de 18 años"
                    } else {
                        birthday.error = null
                    }
                } catch (e: Exception) {
                    birthday.error = "Formato de fecha inválido (dd/MM/yyyy)"
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        phone.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().length != 10) {
                    phone.error = "El teléfono debe tener 10 dígitos"
                } else {
                    phone.error = null
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        email.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().length > 100) {
                    email.error = "El correo debe tener máximo 100 caracteres"
                } else if (!Patterns.EMAIL_ADDRESS.matcher(s.toString()).matches()) {
                    email.error = "Ingrese un correo válido"
                } else {
                    email.error = null
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        password.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().length < 8) {
                    password.error = "La contraseña debe tener al menos 8 caracteres"
                } else if (s.toString().length > 20) {
                    password.error = "La contraseña no puede superar 20 caracteres"
                } else {
                    password.error = null
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun validateForm(name: EditText, lastname: EditText, birthday: EditText, phone: EditText, email: EditText, password: EditText): Boolean {
        if (name.error != null || lastname.error != null || birthday.error != null || phone.error != null || email.error != null || password.error != null) {
            Toast.makeText(this, "Por favor, corrija los errores", Toast.LENGTH_SHORT).show()
            return false
        }

        if (name.text.isEmpty() || lastname.text.isEmpty() || birthday.text.isEmpty() || phone.text.isEmpty() || email.text.isEmpty() || password.text.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun registerUser(name: String, lastname: String, birthday: String, phone: String, email: String, password: String) {
        val db = AppDataBase.getInstance(applicationContext)
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