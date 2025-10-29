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
import androidx.activity.OnBackPressedCallback
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
    private lateinit var name: EditText
    private lateinit var lastname: EditText
    private lateinit var birthday: EditText
    private lateinit var phone: EditText
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var btnRegister: Button
    private lateinit var tvLoginLink: TextView

    private var nameTouched = false
    private var lastnameTouched = false
    private var birthdayTouched = false
    private var phoneTouched = false
    private var emailTouched = false
    private var passwordTouched = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        name = findViewById(R.id.Name)
        lastname = findViewById(R.id.Lastname)
        birthday = findViewById(R.id.Birthday)
        phone = findViewById(R.id.Phone)
        email = findViewById(R.id.Email)
        password = findViewById(R.id.Password)
        btnRegister = findViewById(R.id.btnRegister)
        tvLoginLink = findViewById(R.id.LoginLink)

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
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                startActivity(intent)
                finish()
            }
        })
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
                val formattedDate = String.format(Locale.US, "%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear)
                birthday.setText(formattedDate)
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    private fun addValidationListeners(name: EditText, lastname: EditText, birthday: EditText, phone: EditText, email: EditText, password: EditText) {
        name.setOnFocusChangeListener { _, hasFocus -> if (hasFocus) nameTouched = true }
        lastname.setOnFocusChangeListener { _, hasFocus -> if (hasFocus) lastnameTouched = true }
        birthday.setOnFocusChangeListener { _, hasFocus -> if (hasFocus) birthdayTouched = true }
        phone.setOnFocusChangeListener { _, hasFocus -> if (hasFocus) phoneTouched = true }
        email.setOnFocusChangeListener { _, hasFocus -> if (hasFocus) emailTouched = true }
        password.setOnFocusChangeListener { _, hasFocus -> if (hasFocus) passwordTouched = true }

        name.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!nameTouched) {
                    name.error = null
                    return
                }

                if (s.isNullOrBlank()) {
                    if (!name.isFocused) name.error = "El nombre es requerido"
                    else name.error = null
                    return
                }

                if (s.toString().length < 2 || s.toString().length > 50) {
                    name.error = "El nombre debe tener entre 2 y 50 caracteres"
                } else if (!s.toString().matches(Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$"))) {
                    name.error = "El nombre solo puede contener letras"
                } else {
                    name.error = null
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { if (name.isFocused) nameTouched = true }
        })

        lastname.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!lastnameTouched) {
                    lastname.error = null
                    return
                }

                if (s.isNullOrBlank()) {
                    if (!lastname.isFocused) lastname.error = "El apellido es requerido"
                    else lastname.error = null
                    return
                }

                if (s.toString().length < 2 || s.toString().length > 50) {
                    lastname.error = "El apellido debe tener entre 2 y 50 caracteres"
                } else if (!s.toString().matches(Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$"))) {
                    lastname.error = "El apellido solo puede contener letras"
                } else {
                    lastname.error = null
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { if (lastname.isFocused) lastnameTouched = true }
        })

        birthday.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!birthdayTouched) {
                    birthday.error = null
                    return
                }

                if (s.isNullOrBlank()) {
                    if (!birthday.isFocused) birthday.error = "La fecha de nacimiento es requerida"
                    else birthday.error = null
                    return
                }

                val format = SimpleDateFormat("dd/MM/yyyy", Locale.US)
                try {
                    val date = format.parse(s.toString())
                    if (date != null) {
                        val calendar = Calendar.getInstance()
                        calendar.time = date
                        val year = calendar.get(Calendar.YEAR)
                        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
                        if (currentYear - year < 18) {
                            birthday.error = "Debe ser mayor de 18 años"
                        } else {
                            birthday.error = null
                        }
                    } else {
                        birthday.error = "Formato de fecha inválido (dd/MM/yyyy)"
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    birthday.error = "Formato de fecha inválido (dd/MM/yyyy)"
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { if (birthday.isFocused) birthdayTouched = true }
        })

        phone.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!phoneTouched) {
                    phone.error = null
                    return
                }

                if (s.isNullOrBlank()) {
                    if (!phone.isFocused) phone.error = "El teléfono es requerido"
                    else phone.error = null
                    return
                }

                if (s.toString().length != 10) {
                    phone.error = "El teléfono debe tener 10 dígitos"
                } else {
                    phone.error = null
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { if (phone.isFocused) phoneTouched = true }
        })

        email.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!emailTouched) {
                    email.error = null
                    return
                }

                if (s.isNullOrBlank()) {
                    if (!email.isFocused) email.error = "El correo es requerido"
                    else email.error = null
                    return
                }

                if (s.toString().length > 100) {
                    email.error = "El correo debe tener máximo 100 caracteres"
                } else if (!Patterns.EMAIL_ADDRESS.matcher(s.toString()).matches()) {
                    email.error = "Ingrese un correo válido"
                } else {
                    email.error = null
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { if (email.isFocused) emailTouched = true }
        })

        password.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!passwordTouched) {
                    password.error = null
                    return
                }

                if (s.isNullOrBlank()) {
                    if (!password.isFocused) password.error = "La contraseña es requerida"
                    else password.error = null
                    return
                }

                if (s.toString().length < 8) {
                    password.error = "La contraseña debe tener al menos 8 caracteres"
                } else if (s.toString().length > 20) {
                    password.error = "La contraseña no puede superar 20 caracteres"
                } else {
                    password.error = null
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { if (password.isFocused) passwordTouched = true }
        })
    }

    private fun validateForm(name: EditText, lastname: EditText, birthday: EditText, phone: EditText, email: EditText, password: EditText): Boolean {
        var valid = true

        // Nombre
        val nameText = name.text.toString().trim()
        if (nameText.isEmpty()) {
            name.error = "El nombre es requerido"
            valid = false
        } else if (nameText.length < 2 || nameText.length > 50) {
            name.error = "El nombre debe tener entre 2 y 50 caracteres"
            valid = false
        } else if (!nameText.matches(Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$"))) {
            name.error = "El nombre solo puede contener letras"
            valid = false
        } else {
            name.error = null
        }

        // Apellido
        val lastnameText = lastname.text.toString().trim()
        if (lastnameText.isEmpty()) {
            lastname.error = "El apellido es requerido"
            valid = false
        } else if (lastnameText.length < 2 || lastnameText.length > 50) {
            lastname.error = "El apellido debe tener entre 2 y 50 caracteres"
            valid = false
        } else if (!lastnameText.matches(Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$"))) {
            lastname.error = "El apellido solo puede contener letras"
            valid = false
        } else {
            lastname.error = null
        }

        // Fecha
        val birthdayText = birthday.text.toString().trim()
        if (birthdayText.isEmpty()) {
            birthday.error = "La fecha de nacimiento es requerida"
            valid = false
        } else {
            val format = SimpleDateFormat("dd/MM/yyyy", Locale.US)
            try {
                val date = format.parse(birthdayText)
                if (date != null) {
                    val calendar = Calendar.getInstance()
                    calendar.time = date
                    val year = calendar.get(Calendar.YEAR)
                    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
                    if (currentYear - year < 18) {
                        birthday.error = "Debe ser mayor de 18 años"
                        valid = false
                    } else {
                        birthday.error = null
                    }
                } else {
                    birthday.error = "Formato de fecha inválido (dd/MM/yyyy)"
                    valid = false
                }
            } catch (e: Exception) {
                birthday.error = "Formato de fecha inválido (dd/MM/yyyy)"
                valid = false
            }
        }

        // Teléfono
        val phoneText = phone.text.toString().trim()
        if (phoneText.isEmpty()) {
            phone.error = "El teléfono es requerido"
            valid = false
        } else if (phoneText.length != 10) {
            phone.error = "El teléfono debe tener 10 dígitos"
            valid = false
        } else {
            phone.error = null
        }

        // Email
        val emailText = email.text.toString().trim()
        if (emailText.isEmpty()) {
            email.error = "El correo es requerido"
            valid = false
        } else if (emailText.length > 100) {
            email.error = "El correo debe tener máximo 100 caracteres"
            valid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            email.error = "Ingrese un correo válido"
            valid = false
        } else {
            email.error = null
        }


        val passwordText = password.text.toString()
        if (passwordText.isEmpty()) {
            password.error = "La contraseña es requerida"
            valid = false
        } else if (passwordText.length < 8) {
            password.error = "La contraseña debe tener al menos 8 caracteres"
            valid = false
        } else if (passwordText.length > 20) {
            password.error = "La contraseña no puede superar 20 caracteres"
            valid = false
        } else {
            password.error = null
        }

        if (!valid) Toast.makeText(this, "Por favor, corrija los errores", Toast.LENGTH_SHORT).show()
        return valid
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
            clear()
            val session = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(session)
        }

    }

    private fun clear() {
        name.setText("")
        lastname.setText("")
        birthday.setText("")
        phone.setText("")
        email.setText("")
        password.setText("")

        nameTouched = false
        lastnameTouched = false
        birthdayTouched = false
        phoneTouched = false
        emailTouched = false
        passwordTouched = false
    }

    override fun onSupportNavigateUp(): Boolean {
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        startActivity(intent)
        finish()
        return true
    }
}
