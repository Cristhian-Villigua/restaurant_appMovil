package com.example.project_mobileapplicacion


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.project_mobileapplicacion.database.AppDataBase
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnVerRegister = findViewById<Button>(R.id.btnVerRegister)

        btnVerRegister.setOnClickListener {
            startActivity(Intent(this, ListActivity::class.java))
        }

    }
}