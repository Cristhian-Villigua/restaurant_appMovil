package com.example.project_mobileapplicacion

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.project_mobileapplicacion.cloud.FirebaseService
import com.example.project_mobileapplicacion.database.AppDataBase
import com.example.project_mobileapplicacion.model.UserEntity
import kotlinx.coroutines.launch

class UpdateProfileActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etLastname: EditText
    private lateinit var etBirthday: EditText
    private lateinit var etPhone: EditText
    private lateinit var tvEmail: TextView
    private lateinit var btnUpdate: Button

    private var user: UserEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_profile)

        etName = findViewById(R.id.etName)
        etLastname = findViewById(R.id.etLastname)
        etBirthday = findViewById(R.id.etBirthday)
        etPhone = findViewById(R.id.etPhone)
        tvEmail = findViewById(R.id.tvEmail)
        btnUpdate = findViewById(R.id.btnUpdate)

        // Asumimos que tenemos el email del usuario logueado. 
        // En una app real, lo obtendrías del intent o de SharedPreferences.
        val userEmail = "test@example.com" 

        lifecycleScope.launch {
            FirebaseService.getByEmail(userEmail) { firebaseUser ->
                if (firebaseUser != null) {
                    user = firebaseUser
                    etName.setText(user?.name)
                    etLastname.setText(user?.lastname)
                    etBirthday.setText(user?.birthday)
                    etPhone.setText(user?.phone)
                    tvEmail.text = user?.email
                } else {
                    Toast.makeText(this@UpdateProfileActivity, "Usuario no encontrado", Toast.LENGTH_SHORT).show()
                }
            }
        }

        btnUpdate.setOnClickListener {
            updateUser()
        }
    }

    private fun updateUser() {
        val name = etName.text.toString()
        val lastname = etLastname.text.toString()
        val birthday = etBirthday.text.toString()
        val phone = etPhone.text.toString()

        if (user != null) {
            val updatedUser = user!!.copy(
                name = name,
                lastname = lastname,
                birthday = birthday,
                phone = phone
            )

            lifecycleScope.launch {
                // Actualizar en Firebase
                FirebaseService.update(updatedUser)

                // Actualizar en Room
                val userDao = AppDataBase.getInstance(applicationContext).userDao()
                userDao.update(updatedUser)

                Toast.makeText(this@UpdateProfileActivity, "Perfil actualizado correctamente", Toast.LENGTH_SHORT).show()
                finish()
            }
        } else {
            Toast.makeText(this, "No se pudo actualizar el perfil", Toast.LENGTH_SHORT).show()
        }
    }
}