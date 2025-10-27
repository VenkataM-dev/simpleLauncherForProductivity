package com.example.simplelauncherforproductivity.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.simplelauncherforproductivity.data.dao.ConfigDao
import com.example.simplelauncherforproductivity.data.dao.ConfiguredAppDAO
import com.example.simplelauncherforproductivity.data.entity.ConfigEntity
import com.example.simplelauncherforproductivity.data.entity.ConfiguredApp

@Database(entities = [ConfigEntity::class, ConfiguredApp::class], version = 2)
abstract class AppDatabase : RoomDatabase() {

    abstract fun configuredAppDao(): ConfiguredAppDAO
    abstract fun configDao(): ConfigDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                                context.applicationContext,
                                AppDatabase::class.java,
                                "app_database"
                            ).fallbackToDestructiveMigration(false).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
