package com.example.project_mobileapplicacion.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "menu_items")
data class MenuItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val description: String,
    val price: Double,
    val category: String, // "entrada", "plato fuerte", "postre", "bebida"
    val imageUrl: String
)