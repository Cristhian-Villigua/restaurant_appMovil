package com.example.project_mobileapplicacion.auth

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.project_mobileapplicacion.R
import com.example.project_mobileapplicacion.cloud.FirebaseService
import com.example.project_mobileapplicacion.database.AppDataBase
import com.example.project_mobileapplicacion.model.UserEntity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Locale

class RegisterActivity : AppCompatActivity() {

    private lateinit var tilName: TextInputLayout
    private lateinit var tilLastname: TextInputLayout
    private lateinit var tilBirthday: TextInputLayout
    private lateinit var tilPhone: TextInputLayout
    private lateinit var tilEmail: TextInputLayout
    private lateinit var tilPassword: TextInputLayout

    private lateinit var etName: TextInputEditText
    private lateinit var etLastname: TextInputEditText
    private lateinit var etBirthday: TextInputEditText
    private lateinit var etPhone: TextInputEditText
    private lateinit var etEmail: TextInputEditText
    private lateinit var etPassword: TextInputEditText

    private lateinit var btnRegister: Button
    private lateinit var loginLink: TextView

    private var nameTouched = false
    private var lastnameTouched = false
    private var birthdayTouched = false
    private var phoneTouched = false
    private var emailTouched = false
    private var passwordTouched = false
    private var emailExists = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Layouts
        tilName = findViewById(R.id.tilName)
        tilLastname = findViewById(R.id.tilLastname)
        tilBirthday = findViewById(R.id.tilBirthday)
        tilPhone = findViewById(R.id.tilPhone)
        tilEmail = findViewById(R.id.tilEmail)
        tilPassword = findViewById(R.id.tilPassword)

        // EditTexts
        etName = findViewById(R.id.etName)
        etLastname = findViewById(R.id.etLastname)
        etBirthday = findViewById(R.id.etBirthday)
        etPhone = findViewById(R.id.etPhone)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)

        // Botones y links
        btnRegister = findViewById(R.id.btnRegister)
        loginLink = findViewById(R.id.LoginLink)

        // Validación en tiempo real
        setupRealtimeValidation()

        // DatePicker
        etBirthday.setOnClickListener { showDatePickerDialog() }

        // Click para registrar
        btnRegister.setOnClickListener {
            if (validateForm()) {
                registerUser(
                    etName.text.toString().trim(),
                    etLastname.text.toString().trim(),
                    etBirthday.text.toString().trim(),
                    etPhone.text.toString().trim(),
                    etEmail.text.toString().trim(),
                    etPassword.text.toString()
                )
            }
        }

        // Ir a login
        loginLink.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navigateToLogin()
            }
        })
    }

    private fun setupRealtimeValidation() {
        etName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (nameTouched) ValidationActivity.validateName(this@RegisterActivity, tilName, s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { if (etName.isFocused) nameTouched = true }
        })
        etName.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) ValidationActivity.validateName(this, tilName, etName.text.toString()) else nameTouched = true
        }

        etLastname.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (lastnameTouched) ValidationActivity.validateLastname(this@RegisterActivity, tilLastname, s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { if (etLastname.isFocused) lastnameTouched = true }
        })
        etLastname.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) ValidationActivity.validateLastname(this, tilLastname, etLastname.text.toString()) else lastnameTouched = true
        }

        etBirthday.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus && etBirthday.text?.isNotEmpty() == true && birthdayTouched) {
                ValidationActivity.validateBirthday(this, tilBirthday, etBirthday.text.toString())
            } else if (hasFocus) birthdayTouched = true
        }

        etPhone.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (phoneTouched) ValidationActivity.validatePhone(this@RegisterActivity, tilPhone, s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { if (etPhone.isFocused) phoneTouched = true }
        })
        etPhone.setOnFocusChangeListener { _, hasFocus -> if (hasFocus) phoneTouched = true }

        etEmail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val email = s.toString()

                // Primero validación local
                val isValidLocal = ValidationActivity.validateEmail(
                    this@RegisterActivity,
                    tilEmail,
                    email
                )

                if (!emailTouched) return
                if (!isValidLocal) return   // No consultar Firebase si el formato es incorrecto

                // Si el correo es valido, consultar Firebase
                checkEmailInFirebase(email)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (etEmail.isFocused) emailTouched = true
            }
        })
        etEmail.setOnFocusChangeListener { _, hasFocus -> if (hasFocus) emailTouched = true }

        etPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (passwordTouched) ValidationActivity.validatePassword(this@RegisterActivity, tilPassword, s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { if (etPassword.isFocused) passwordTouched = true }
        })
        etPassword.setOnFocusChangeListener { _, hasFocus -> if (hasFocus) passwordTouched = true }
    }

    private fun checkEmailInFirebase(email: String) {
        FirebaseService.checkEmailExists(email) { exists ->
            runOnUiThread {

                emailExists = exists

                if (exists) {
                    val colorError = getColorStateList(R.color.red_primary)
                    tilEmail.setBoxStrokeColorStateList(colorError)
                    tilEmail.helperText = "Este correo ya está registrado"
                    tilEmail.setHelperTextColor(colorError)
                    tilEmail.endIconMode = TextInputLayout.END_ICON_CUSTOM
                    tilEmail.endIconDrawable = getDrawable(R.drawable.ic_form_warning)
                } else {
                    val green = getColorStateList(R.color.green_success)
                    tilEmail.helperText = null
                    tilEmail.isCounterEnabled = false
                    tilEmail.setHelperTextColor(green)
                    tilEmail.setBoxStrokeColorStateList(green)
                    tilEmail.endIconMode = TextInputLayout.END_ICON_CUSTOM
                    tilEmail.endIconDrawable = getDrawable(R.drawable.ic_form_check)
                }
            }
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            R.style.SpinnerDatePicker,
            { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = String.format(Locale.US, "%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear)
                etBirthday.setText(formattedDate)
                birthdayTouched = true
                ValidationActivity.validateBirthday(this, tilBirthday, formattedDate)
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    private fun validateForm(): Boolean {
        var valid = true
        if (!ValidationActivity.validateName(this, tilName, etName.text.toString())) valid = false
        if (!ValidationActivity.validateLastname(this, tilLastname, etLastname.text.toString())) valid = false
        if (!ValidationActivity.validateBirthday(this, tilBirthday, etBirthday.text.toString())) valid = false
        if (!ValidationActivity.validatePhone(this, tilPhone, etPhone.text.toString())) valid = false
        if (!ValidationActivity.validateEmail(this, tilEmail, etEmail.text.toString())) valid = false
        if (!ValidationActivity.validatePassword(this, tilPassword, etPassword.text.toString())) valid = false
        if (emailExists) valid = false
        if (!valid) Toast.makeText(this, "Por favor, corrija los errores", Toast.LENGTH_SHORT).show()
        return valid
    }

    private fun registerUser(name: String, lastname: String, birthday: String, phone: String, email: String, password: String) {
        val db = AppDataBase.getInstance(applicationContext)
        val userDao = db.userDao()
        val database = db.openHelper.writableDatabase
        val roleId = getRoleIdByName(database, "Usuario")

        val userEntity = UserEntity(
            name = name,
            lastname = lastname,
            birthday = birthday,
            phone = phone,
            email = email,
            password = password,
            roleId = roleId
        )

        lifecycleScope.launch {
            try {
                userDao.insert(userEntity)
                FirebaseService.store(userEntity)
                Toast.makeText(this@RegisterActivity, "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this@RegisterActivity, "Error al registrar usuario: ${e.message}", Toast.LENGTH_SHORT).show()
            }
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            finish()
        }
    }
    private fun getRoleIdByName(db: SupportSQLiteDatabase, roleName: String): Int {
        val cursor = db.query("SELECT id FROM role WHERE role = ?", arrayOf(roleName))
        cursor.use {
            if (it.moveToFirst()) {
                return it.getInt(it.getColumnIndexOrThrow("id"))
            }
        }
        return -1
    }
    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }
        startActivity(intent)
        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        navigateToLogin()
        return true
    }
}