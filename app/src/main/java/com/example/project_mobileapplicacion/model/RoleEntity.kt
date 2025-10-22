package com.example.project_mobileapplicacion.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "role")

data class RoleEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val role: String
)