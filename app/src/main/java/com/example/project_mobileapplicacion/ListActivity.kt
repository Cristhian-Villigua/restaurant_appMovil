package com.example.project_mobileapplicacion

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project_mobileapplicacion.Adapter.UserAdapter
import com.example.project_mobileapplicacion.cloud.FirebaseService
import com.example.project_mobileapplicacion.database.AppDataBase
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ListActivity : AppCompatActivity() {
    private lateinit var rv: RecyclerView
    private lateinit var adapter: UserAdapter
    private lateinit var btnRoom : Button
    private lateinit var btnFirebase : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        rv = findViewById(R.id.rvUser)
        adapter = UserAdapter()
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter

        btnRoom = findViewById(R.id.btnRoom)
        btnFirebase = findViewById(R.id.btnFirebase)

        val db = AppDataBase.getInstance(this).userDao()

        btnRoom.setOnClickListener {
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED){
                    db.getAll().collectLatest { list -> adapter.submitList(list) }
                }
            }
        }
        btnFirebase.setOnClickListener {
            FirebaseService.index { listFirestore->
                adapter.submitList(listFirestore)
            }
        }
    }
}