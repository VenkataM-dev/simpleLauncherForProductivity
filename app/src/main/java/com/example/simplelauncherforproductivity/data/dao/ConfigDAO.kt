package com.example.simplelauncherforproductivity.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.example.simplelauncherforproductivity.data.entity.ConfigEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface ConfigDao {
    @Query("SELECT * FROM config WHERE id = 0")
    fun observeConfig(): Flow<ConfigEntity?>

    @Upsert
    suspend fun updateConfig(config: ConfigEntity)
}
