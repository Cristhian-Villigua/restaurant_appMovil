package com.example.project_mobileapplicacion.admin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
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
    private lateinit var btnUpdate: Button
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
        btnUpdate = findViewById(R.id.btnUpdate)

        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val userEmail = sharedPreferences.getString("userEmail", null)

        if (userEmail != null) {
            FirebaseService.getByEmail(userEmail) { fetchedUser ->
                if (fetchedUser != null) {
                    user = fetchedUser
                    editName.text = "Nombre: ${fetchedUser.name}"
                    editLastname.text = "Apellido: ${fetchedUser.lastname}"
                    editBirthday.text = "Fecha de nacimiento: ${fetchedUser.birthday}"
                    editPhone.text = "Teléfono: ${fetchedUser.phone}"
                    editEmail.text = "Correo electrónico: ${fetchedUser.email}"

                    btnUpdate.isEnabled = true
                }
            }
        } else {
            setAsGuest()
        }

        logout.setOnClickListener {
            logoutSession()
        }

        btnUpdate.setOnClickListener {
            if(user != null){
                startActivity(Intent(this, UpdateProfileActivity::class.java))
            }else {
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
        btnUpdate.visibility = Button.GONE
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
}

