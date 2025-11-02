package com.example.project_mobileapplicacion

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.example.project_mobileapplicacion.auth.LoginActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.activity_splash)
        val logo = findViewById<ImageView>(R.id.logoImage)
        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        logo.startAnimation(fadeIn)

        // Evita múltiples lanzamientos del Splash
        if (isTaskRoot) {
            lifecycleScope.launch {
                delay(2000) // Espera 2 segundos para el splash
                navigateNext()
            }
        } else {
            finish()
        }
    }

    private fun navigateNext() {
        val userLoggedIn = false // Aquí coloca tu lógica real

        val nextIntent = if (userLoggedIn) {
            Intent(this, MainActivity::class.java)
        } else {
            Intent(this, LoginActivity::class.java)
        }

        // Limpia toda la pila de Activities para evitar el doble arranque
        nextIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(nextIntent)
        finish()
    }
}
