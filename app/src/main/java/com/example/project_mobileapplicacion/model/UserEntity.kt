package com.example.project_mobileapplicacion.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")

data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var docId: String? = null,
    val name: String,
    val lastname: String,
    val birthday: String,
    val phone: String,
    val email: String,
    val password: String
)