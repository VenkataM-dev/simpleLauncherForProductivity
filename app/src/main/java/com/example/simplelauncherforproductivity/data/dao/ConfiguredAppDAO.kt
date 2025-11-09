package com.example.simplelauncherforproductivity.data.dao

import androidx.room.*
import com.example.simplelauncherforproductivity.data.entity.AppStatus
import com.example.simplelauncherforproductivity.data.entity.ConfiguredApp
import kotlinx.coroutines.flow.Flow

@Dao
interface ConfiguredAppDAO {
    @Query("SELECT * FROM configured_apps")
    fun observeAll(): Flow<List<ConfiguredApp>>

    @Query("SELECT * FROM configured_apps WHERE status = :status")
    fun observeAppsWithStatus(status: AppStatus): Flow<List<ConfiguredApp>>

    @Query("SELECT * FROM configured_apps WHERE packageName = :packageName")
    suspend fun getAppByPackageName(packageName: String): ConfiguredApp?

    // Replaces the entire list. This is the simplest way to update.
    @Transaction
    suspend fun replaceAll(apps: List<ConfiguredApp>) {
        deleteAll()
        insertAll(apps)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(apps: List<ConfiguredApp>)

    @Query("DELETE FROM configured_apps")
    suspend fun deleteAll()

    @Query("SELECT status FROM configured_apps WHERE packageName = :packageName")
    suspend fun getStatusForApp(packageName: String): AppStatus
}
