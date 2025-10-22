package com.example.project_mobileapplicacion.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.project_mobileapplicacion.model.RoleEntity

@Dao
interface RoleDao {
    @Insert
    suspend fun insert(role: RoleEntity)

    @Query("SELECT * FROM role")
    suspend fun getAll(): List<RoleEntity>

    @Query("SELECT * FROM role WHERE id= :roleId")
    suspend fun getById(roleId: Int): RoleEntity
}