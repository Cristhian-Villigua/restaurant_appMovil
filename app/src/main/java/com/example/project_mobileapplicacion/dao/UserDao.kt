package com.example.project_mobileapplicacion.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.project_mobileapplicacion.model.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao{
    @Insert
    suspend fun insert(user: UserEntity)

    @Query("SELECT * FROM users ORDER BY id DESC")
    fun getAll(): Flow<List<UserEntity>>

    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    suspend fun getById(userId: Int): UserEntity?

    @Update
    suspend fun update(user: UserEntity)

    @Query("DELETE FROM users WHERE id = :userId")
    suspend fun deleteById(userId: Int)

    @Query("DELETE FROM users")
    suspend fun deleteAll()
}