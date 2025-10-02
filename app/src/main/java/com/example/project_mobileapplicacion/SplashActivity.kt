package com.example.project_mobileapplicacion

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }

    override fun onStart() {
        super.onStart()

        Handler().postDelayed({
            val userLoggedIn = true
            val intent = if (userLoggedIn) {
                Intent(this, MainActivity::class.java)
            } else {
                Intent(this, RegisterActivity::class.java)
            }
            startActivity(intent)
            finish()
        }, 2000)
    }
}
