package com.example.project_mobileapplicacion.admin

import android.os.Bundle
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

class UpdateProfileActivity : AppCompatActivity() {

    private lateinit var editName: EditText
    private lateinit var editLastname: EditText
    private lateinit var editBirthday: EditText
    private lateinit var editPhone: EditText
    private lateinit var editEmail: TextView
    private lateinit var btnUpdate: Button

    private var user: UserEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_profile)

        editName = findViewById(R.id.etName)
        editLastname = findViewById(R.id.etLastname)
        editBirthday = findViewById(R.id.etBirthday)
        editPhone = findViewById(R.id.etPhone)
        editEmail = findViewById(R.id.tvEmail)
        btnUpdate = findViewById(R.id.btnUpdate)

        val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val userEmail = sharedPreferences.getString("userEmail", null)

        if (userEmail != null) {
            lifecycleScope.launch {
                FirebaseService.getByEmail(userEmail) { fetchedUser ->
                    if (fetchedUser != null) {
                        user = fetchedUser
                        editName.setText(fetchedUser.name)
                        editLastname.setText(fetchedUser.lastname)
                        editBirthday.setText(fetchedUser.birthday)
                        editPhone.setText(fetchedUser.phone)
                        editEmail.text = "Correo electrónico: ${fetchedUser.email}"
                        btnUpdate.isEnabled = true
                    } else {
                        Toast.makeText(this@UpdateProfileActivity, "Usuario no encontrado", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        btnUpdate.setOnClickListener {
            updateUser()
        }
    }

    private fun updateUser() {
        val name = editName.text.toString()
        val lastname = editLastname.text.toString()
        val birthday = editBirthday.text.toString()
        val phone = editPhone.text.toString()

        if (name.isEmpty() || lastname.isEmpty() || birthday.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        user?.let { currentUser ->
            val updatedUser = currentUser.copy(
                name = name,
                lastname = lastname,
                birthday = birthday,
                phone = phone
            )

            lifecycleScope.launch {
                FirebaseService.update(updatedUser)

                val userDao = AppDataBase.Companion.getInstance(applicationContext).userDao()
                userDao.update(updatedUser)
                Toast.makeText(this@UpdateProfileActivity, "Perfil actualizado correctamente", Toast.LENGTH_SHORT).show()
                finish()
            }
        } ?: run {
            Toast.makeText(this, "No se pudo actualizar el perfil", Toast.LENGTH_SHORT).show()
        }
    }
}