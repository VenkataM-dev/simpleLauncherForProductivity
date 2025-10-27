package com.example.simplelauncherforproductivity.domain.repositories

import com.example.simplelauncherforproductivity.data.dao.ConfiguredAppDAO
import com.example.simplelauncherforproductivity.data.entity.AppStatus
import com.example.simplelauncherforproductivity.data.entity.ConfiguredApp
import kotlinx.coroutines.flow.Flow

class ConfiguredAppRepository(private val configuredAppDao: ConfiguredAppDAO) {

    fun observeUnproductiveApps(): Flow<List<ConfiguredApp>> {
        return configuredAppDao.observeAppsWithStatus(AppStatus.UNPRODUCTIVE)
    }

    suspend fun getAppByPackageName(packageName: String): ConfiguredApp? {
        return configuredAppDao.getAppByPackageName(packageName)
    }

    suspend fun replaceAll(apps: List<ConfiguredApp>) {
        configuredAppDao.replaceAll(apps)
    }

    suspend fun replaceUnproductiveApps(apps: List<ConfiguredApp>) {
        configuredAppDao.replaceAll(apps)
    }
}
