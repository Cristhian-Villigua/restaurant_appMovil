package com.example.project_mobileapplicacion.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class Order(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val tableId: Int,
    val status: String, // "pendiente", "en preparación", "listo", "servido", "pagado"
    val timestamp: Long,
    val total: Double
)