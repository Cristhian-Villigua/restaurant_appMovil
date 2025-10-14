package com.example.project_mobileapplicacion.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.project_mobileapplicacion.dao.UserDao
import com.example.project_mobileapplicacion.model.UserEntity

@Database(entities = [UserEntity::class], version = 1, exportSchema = false)

abstract class AppDataBase: RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object{
        @Volatile
        private var INSTANCE: AppDataBase? = null

        fun getInstance(context: Context): AppDataBase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java,
                    "Project_MobileApplication.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}