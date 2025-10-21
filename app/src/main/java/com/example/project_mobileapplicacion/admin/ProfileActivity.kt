package com.example.project_mobileapplicacion.admin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.project_mobileapplicacion.R
import com.example.project_mobileapplicacion.auth.LoginActivity
import com.example.project_mobileapplicacion.cloud.FirebaseService
import com.example.project_mobileapplicacion.model.UserEntity


class ProfileActivity : AppCompatActivity() {
    private var user: UserEntity? = null
    private lateinit var editName: TextView
    private lateinit var editLastname: TextView
    private lateinit var editBirthday: TextView
    private lateinit var editPhone: TextView
    private lateinit var editEmail: TextView
    private lateinit var updateProfileButton: Button
    private lateinit var logout: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        logout = findViewById(R.id.logoutIcon)
        editName = findViewById(R.id.editName)
        editLastname = findViewById(R.id.editLastname)
        editBirthday = findViewById(R.id.editBirthday)
        editPhone = findViewById(R.id.editPhone)
        editEmail = findViewById(R.id.editEmail)
        updateProfileButton = findViewById(R.id.updateProfileButton)

        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val userEmail = sharedPreferences.getString("userEmail", null)

        if (userEmail != null) {
            FirebaseService.getByEmail(userEmail) { fetchedUser ->
                if (fetchedUser != null) {
                    user = fetchedUser  // Asignar el usuario correctamente
                    editName.text = "Nombre: ${fetchedUser.name}"
                    editLastname.text = "Apellido: ${fetchedUser.lastname}"
                    editBirthday.text = "Fecha de nacimiento: ${fetchedUser.birthday}"
                    editPhone.text = "Teléfono: ${fetchedUser.phone}"
                    editEmail.text = "Correo electrónico: ${fetchedUser.email}"

                    updateProfileButton.isEnabled = true
                }
            }
        } else {
            setAsGuest()
        }

        logout.setOnClickListener {
            logoutSession()
        }

        updateProfileButton.setOnClickListener {
            if (user != null) {
                showEditProfileDialog()
            } else {
                Toast.makeText(this, "Por favor espera mientras se carga tu perfil.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setAsGuest() {
        editName.text = "Nombre: Invitado"
        editLastname.text = "Apellido: N/A"
        editBirthday.text = "Fecha de nacimiento: N/A"
        editPhone.text = "Teléfono: N/A"
        editEmail.text = "Correo electrónico: N/A"
        updateProfileButton.visibility = Button.GONE
    }

    private fun logoutSession() {
        val userPrefs = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val edit = userPrefs.edit()
        edit.clear()
        edit.apply()

        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun showEditProfileDialog() {
        val currentUser = user ?: return

        val nameEditText = EditText(this).apply {
            setText(editName.text.toString().replace("Nombre: ", ""))
        }
        val lastnameEditText = EditText(this).apply {
            setText(editLastname.text.toString().replace("Apellido: ", ""))
        }
        val birthdayEditText = EditText(this).apply {
            setText(editBirthday.text.toString().replace("Fecha de nacimiento: ", ""))
        }
        val phoneEditText = EditText(this).apply {
            setText(editPhone.text.toString().replace("Teléfono: ", ""))
        }
        val emailEditText = EditText(this).apply {
            setText(editEmail.text.toString().replace("Correo electrónico: ", ""))
        }

        val dialog = AlertDialog.Builder(this)
            .setTitle("Actualizar Perfil")
            .setView(LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                addView(nameEditText)
                addView(lastnameEditText)
                addView(birthdayEditText)
                addView(phoneEditText)
                addView(emailEditText)
            })
            .setPositiveButton("Save") { _, _ ->
                val updatedUser = UserEntity(
                    id = currentUser.id,
                    name = nameEditText.text.toString(),
                    lastname = lastnameEditText.text.toString(),
                    birthday = birthdayEditText.text.toString(),
                    phone = phoneEditText.text.toString(),
                    email = emailEditText.text.toString(),
                    password = currentUser.password
                )
                FirebaseService.update(updatedUser)
                editName.text = "Nombre: ${updatedUser.name}"
                editLastname.text = "Apellido: ${updatedUser.lastname}"
                editBirthday.text = "Fecha de nacimiento: ${updatedUser.birthday}"
                editPhone.text = "Teléfono: ${updatedUser.phone}"
                editEmail.text = "Correo electrónico: ${updatedUser.email}"

                Toast.makeText(this@ProfileActivity, "Perfil actualizado", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null)
            .create()
        dialog.show()
    }
}

