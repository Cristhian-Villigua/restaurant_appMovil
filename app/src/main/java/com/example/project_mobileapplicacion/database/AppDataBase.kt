package com.example.project_mobileapplicacion.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.project_mobileapplicacion.dao.UserDao
import com.example.project_mobileapplicacion.dao.RoleDao
import com.example.project_mobileapplicacion.model.UserEntity
import com.example.project_mobileapplicacion.model.RoleEntity

@Database(entities = [UserEntity::class, RoleEntity::class], version = 4, exportSchema = false)
abstract class AppDataBase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun roleDao(): RoleDao

    companion object {
        @Volatile
        private var INSTANCE: AppDataBase? = null

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE users ADD COLUMN docId TEXT")
            }
        }

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE users ADD COLUMN photoBase64 TEXT NOT NULL DEFAULT ''")
            }
        }

        private val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `role` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `role` TEXT NOT NULL
                    );
                """
                )

                db.execSQL("INSERT INTO role (role) VALUES ('Administrador')")
                db.execSQL("INSERT INTO role (role) VALUES ('Cocinero')")
                db.execSQL("INSERT INTO role (role) VALUES ('Mesero')")
                db.execSQL("INSERT INTO role (role) VALUES ('Usuario')")
                db.execSQL("INSERT INTO role (role) VALUES ('Invitado')")
            }
        }

        fun getInstance(context: Context): AppDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java,
                    "Project_MobileApplication.db"
                )
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
