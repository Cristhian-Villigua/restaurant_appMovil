package com.example.project_mobileapplicacion


import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.project_mobileapplicacion.database.AppDataBase
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        showAllUsers()
    }
    private fun showAllUsers() {
        val db = AppDataBase.getInstance(applicationContext)
        val userDao = db.userDao()

        lifecycleScope.launch {
            // Collect el Flow de la base de datos
            userDao.getAll().collect { users ->
                if (users.isNotEmpty()) {
                    val usersInfo = users.joinToString("\n") {
                        "ID: ${it.id}, Nombre: ${it.name} ${it.lastname}, Correo: ${it.email}"
                    }
                    Toast.makeText(this@MainActivity, usersInfo, Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this@MainActivity, "No hay usuarios registrados.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}