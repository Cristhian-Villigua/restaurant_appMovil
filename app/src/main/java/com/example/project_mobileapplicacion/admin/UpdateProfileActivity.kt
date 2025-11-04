package com.example.project_mobileapplicacion.admin

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.project_mobileapplicacion.R
import com.example.project_mobileapplicacion.auth.ValidationActivity
import com.example.project_mobileapplicacion.cloud.FirebaseService
import com.example.project_mobileapplicacion.database.AppDataBase
import com.example.project_mobileapplicacion.model.UserEntity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Locale

class UpdateProfileActivity : AppCompatActivity() {
    private var user: UserEntity? = null
    private lateinit var editTilName: TextInputLayout
    private lateinit var editEtName: TextInputEditText
    private lateinit var editTilLastname: TextInputLayout
    private lateinit var editEtLastname: TextInputEditText
    private lateinit var editTilBirthday: TextInputLayout
    private lateinit var editEtBirthday: TextInputEditText
    private lateinit var editTilPhone: TextInputLayout
    private lateinit var editEtPhone: TextInputEditText
    private lateinit var editTilEmail: TextInputLayout
    private lateinit var editEtEmail: TextInputEditText
    private lateinit var btnUpdate: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Referencias
        editTilName = findViewById(R.id.editTilName)
        editEtName = findViewById(R.id.editEtName)
        editTilLastname = findViewById(R.id.editTilLastname)
        editEtLastname = findViewById(R.id.editEtLastname)
        editTilBirthday = findViewById(R.id.editTilBirthday)
        editEtBirthday = findViewById(R.id.editEtBirthday)
        editTilPhone = findViewById(R.id.editTilPhone)
        editEtPhone = findViewById(R.id.editEtPhone)
        editTilEmail = findViewById(R.id.editTilEmail)
        editEtEmail = findViewById(R.id.editEtEmail)
        btnUpdate = findViewById(R.id.btnUpdate)

        // Habilitar los campos para edición
        enableFields()

        // DatePicker
        editEtBirthday.setOnClickListener { showDatePickerDialog() }

        // Guardar cambios
        btnUpdate.setOnClickListener { saveProfile() }

        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val userEmail = sharedPreferences.getString("userEmail", null)

        if (userEmail != null) {
            FirebaseService.getByEmail(userEmail) { fetchedUser ->
                if (fetchedUser != null) {
                    user = fetchedUser
                    loadUserData(fetchedUser)
                    setupRealtimeValidation()
                } else {
                    Toast.makeText(this, "Error al cargar datos", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Habilitar todos los campos para edición (excepto email)
    private fun enableFields() {
        editEtName.isEnabled = true
        editEtLastname.isEnabled = true
        editEtBirthday.isEnabled = true
        editEtPhone.isEnabled = true
        editEtEmail.isEnabled = false
    }

    // Cargar datos del usuario en los campos
    private fun loadUserData(user: UserEntity) {
        editEtName.setText(user.name)
        editEtLastname.setText(user.lastname)
        editEtBirthday.setText(user.birthday)
        editEtPhone.setText(user.phone)
        editEtEmail.setText(user.email)
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
                editEtBirthday.setText(formattedDate)
                ValidationActivity.validateBirthday(this, editTilBirthday, formattedDate)
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    // Validación en tiempo real mientras el usuario escribe
    private fun setupRealtimeValidation() {
        // Validación para Nombre
        editEtName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                ValidationActivity.validateName(this@UpdateProfileActivity, editTilName, s.toString().trim())
            }
        })

        // Validación para Apellido
        editEtLastname.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                ValidationActivity.validateLastname(this@UpdateProfileActivity, editTilLastname, s.toString().trim())
            }
        })

        // Validación para Fecha de Nacimiento
        editEtBirthday.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                ValidationActivity.validateBirthday(this@UpdateProfileActivity, editTilBirthday, s.toString().trim())
            }
        })

        // Validación para Teléfono
        editEtPhone.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                ValidationActivity.validatePhone(this@UpdateProfileActivity, editTilPhone, s.toString().trim())
            }
        })
    }

    // Guardar cambios en Firebase y room database
    private fun saveProfile() {
        val name = editEtName.text.toString().trim()
        val lastname = editEtLastname.text.toString().trim()
        val birthday = editEtBirthday.text.toString().trim()
        val phone = editEtPhone.text.toString().trim()

        // Validar todos los campos (email ya está validado al cargar)
        val isNameValid = ValidationActivity.validateName(this, editTilName, name)
        val isLastnameValid = ValidationActivity.validateLastname(this, editTilLastname, lastname)
        val isBirthdayValid = ValidationActivity.validateBirthday(this, editTilBirthday, birthday)
        val isPhoneValid = ValidationActivity.validatePhone(this, editTilPhone, phone)

        if (!isNameValid || !isLastnameValid || !isBirthdayValid || !isPhoneValid) {
            Toast.makeText(this, "Por favor corrige los campos marcados", Toast.LENGTH_SHORT).show()
            return
        }

        user?.let { currentUser ->
            val updatedUser = currentUser.copy(name = name, lastname = lastname, birthday = birthday, phone = phone)
            lifecycleScope.launch {
            FirebaseService.update(updatedUser)
            val userDao = AppDataBase.getInstance(applicationContext).userDao()
            userDao.update(updatedUser)
            Toast.makeText(this@UpdateProfileActivity, "Perfil actualizado correctamente", Toast.LENGTH_SHORT).show()
            finish()
            }
        } ?: run {
            Toast.makeText(this, "No se pudo actualizar el perfil", Toast.LENGTH_SHORT).show()
        }
    }
}