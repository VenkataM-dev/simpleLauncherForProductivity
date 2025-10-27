package com.example.simplelauncherforproductivity.presentation.viewmodel


import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.simplelauncherforproductivity.data.database.AppDatabase
import com.example.simplelauncherforproductivity.data.repository.ConfigRepository
import com.example.simplelauncherforproductivity.domain.repositories.ConfiguredAppRepository

class SettingsViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {

            val database = AppDatabase.getDatabase(application)

            val configuredAppDao = database.configuredAppDao()
            val configDao = database.configDao()

            val configRepository = ConfigRepository(configDao)
            val configuredAppRepository = ConfiguredAppRepository(configuredAppDao)


            @Suppress("UNCHECKED_CAST")
            return SettingsViewModel(application, configRepository, configuredAppRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
