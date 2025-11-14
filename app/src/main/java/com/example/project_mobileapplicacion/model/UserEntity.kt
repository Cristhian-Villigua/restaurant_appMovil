package com.example.project_mobileapplicacion.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "users",
    foreignKeys = [ForeignKey(
        entity = RoleEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("roleId"),
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["roleId"])]
)
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var docId: String? = null,
    val name: String,
    val lastname: String,
    val birthday: String,
    val phone: String,
    val email: String,
    val password: String,
    val photoBase64: String? = null,
    @ColumnInfo(name = "roleId")
    val roleId: Int = 5
)