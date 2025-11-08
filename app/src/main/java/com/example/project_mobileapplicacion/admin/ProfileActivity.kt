package com.example.project_mobileapplicacion.admin

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.project_mobileapplicacion.MainActivity
import com.example.project_mobileapplicacion.R
import com.example.project_mobileapplicacion.auth.LoginActivity
import com.example.project_mobileapplicacion.cloud.FirebaseService
import com.example.project_mobileapplicacion.model.UserEntity
import com.google.android.material.textfield.TextInputEditText

class ProfileActivity : AppCompatActivity() {
    private var user: UserEntity? = null
    private lateinit var editEtName: TextInputEditText
    private lateinit var editEtLastname: TextInputEditText
    private lateinit var editEtBirthday: TextInputEditText
    private lateinit var editEtPhone: TextInputEditText
    private lateinit var editEtEmail: TextInputEditText
    private lateinit var btnEdit: Button
    private lateinit var logout: ImageView
    private lateinit var logo: ImageView
    private lateinit var btnBack : LinearLayout
    private lateinit var imgUser: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        editEtName = findViewById(R.id.editEtName)
        editEtLastname = findViewById(R.id.editEtLastname)
        editEtBirthday = findViewById(R.id.editEtBirthday)
        editEtPhone = findViewById(R.id.editEtPhone)
        editEtEmail = findViewById(R.id.editEtEmail)
        btnEdit = findViewById(R.id.btnUpdate)
        logout = findViewById(R.id.logoutIcon)
        logo = findViewById(R.id.logoImage)
        btnBack = findViewById(R.id.btnBack)

        btnEdit.text = "Editar"
        imgUser = findViewById(R.id.imgUser)

        logout.setOnClickListener { logoutSession() }

        btnEdit.setOnClickListener {
            if (user != null) {
                startActivity(Intent(this, UpdateProfileActivity::class.java))
            } else {
                Toast.makeText(this, "Por favor, espera mientras se carga tu perfil.", Toast.LENGTH_SHORT).show()
            }
        }
        btnBack.setOnClickListener {
            startActivity(Intent(this@ProfileActivity, MainActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        loadProfileData()
    }

    private fun loadProfileData() {
        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val userEmail = sharedPreferences.getString("userEmail", null)
        if (userEmail != null) {
            FirebaseService.getByEmail(userEmail) { fetchedUser ->
                if (fetchedUser != null) {
                    user = fetchedUser
                    loadUserData(fetchedUser)
                    btnEdit.isEnabled = true
                } else {
                    Toast.makeText(this, "No se encontraron datos del usuario", Toast.LENGTH_SHORT).show()
                    setAsGuest()
                }
            }
        } else {
            setAsGuest()
        }
    }

    private fun loadUserData(fetchedUser: UserEntity) {
        editEtName.setText(fetchedUser.name)
        editEtLastname.setText(fetchedUser.lastname)
        editEtBirthday.setText(fetchedUser.birthday)
        editEtPhone.setText(fetchedUser.phone)
        editEtEmail.setText(fetchedUser.email)

        fetchedUser.photoBase64?.let { photoBase64 ->
            val bitmap = decodeFromBase64(photoBase64)
            imgUser.setImageBitmap(bitmap)
        }
    }
    private fun decodeFromBase64(base64: String): Bitmap? {
        return try {
            val decodedString = Base64.decode(base64, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    private fun setAsGuest() {
        editEtName.setText("Invitado")
        editEtLastname.setText("N/A")
        editEtBirthday.setText("N/A")
        editEtPhone.setText("N/A")
        editEtEmail.setText("N/A")
        imgUser.setImageResource(R.drawable.img)
        btnEdit.visibility = Button.GONE
    }

    private fun logoutSession() {
        val userPrefs = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        userPrefs.edit().clear().apply()
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}