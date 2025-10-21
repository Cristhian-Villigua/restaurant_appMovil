package com.example.project_mobileapplicacion.admin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.project_mobileapplicacion.R
import com.example.project_mobileapplicacion.cloud.FirebaseService

class IndexActivity: AppCompatActivity() {
    private lateinit var userName: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_index)

        userName = findViewById(R.id.userName)

        val userPrefs = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val userEmail = userPrefs.getString("userEmail", null)

        if (userEmail != null) {
            FirebaseService.getByEmail(userEmail){user->
                if(user!=null){
                    userName.text = "Bienvenido, ${user.name} ${user.lastname}"
                }else{
                    userName.text = "Usuario no encontrado"
                }
            }
        }else{
            userName.text = "Invitado"
        }
    }
    fun onProfileClick(view: android.view.View){
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
    }
}