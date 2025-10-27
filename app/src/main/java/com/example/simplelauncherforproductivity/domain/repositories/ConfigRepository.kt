package com.example.simplelauncherforproductivity.data.repository

import com.example.simplelauncherforproductivity.data.dao.ConfigDao
import com.example.simplelauncherforproductivity.data.entity.ConfigEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ConfigRepository(private val configDao: ConfigDao) {

    val configFlow: Flow<ConfigEntity> = configDao.observeConfig().map {
        it ?: ConfigEntity(id = 0, slideCount = 3, gridSize = 4)
    }

    suspend fun updateGridSize(newGridSize: Int) {
        val newConfig = ConfigEntity(id = 0, slideCount = 3, gridSize = newGridSize)
        configDao.updateConfig(newConfig)
    }

    suspend fun updateConfig(slideCount: Int, gridSize: Int) {
        configDao.updateConfig(ConfigEntity(id = 0, slideCount = slideCount, gridSize = gridSize))
    }
}
