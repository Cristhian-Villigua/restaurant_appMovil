package com.example.project_mobileapplicacion


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.project_mobileapplicacion.admin.IndexActivity
import com.example.project_mobileapplicacion.admin.ListActivity
import com.example.project_mobileapplicacion.database.AppDataBase
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnVerRegister = findViewById<Button>(R.id.btnVerRegister)
        val btnAdmin = findViewById<Button>(R.id.btnAdmin)


        btnVerRegister.setOnClickListener {
            startActivity(Intent(this, ListActivity::class.java))
        }

        btnAdmin.setOnClickListener {
            startActivity(Intent(this, IndexActivity::class.java))
        }

    }
}