package com.example.simplelauncherforproductivity.presentation.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplelauncherforproductivity.data.entity.AppStatus
import com.example.simplelauncherforproductivity.data.entity.ConfiguredApp
import com.example.simplelauncherforproductivity.data.repository.ConfigRepository
import com.example.simplelauncherforproductivity.domain.repositories.ConfiguredAppRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SettingsViewModel(
    application: Application,
    private val configRepository: ConfigRepository,
    private val configuredAppRepository: ConfiguredAppRepository
) : AndroidViewModel(application) {

     val slideCount: StateFlow<Int> = configRepository.configFlow
         .map { it.slideCount }
         .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 1)

    val appsPerRow: StateFlow<Int> = configRepository.configFlow
        .map { it.gridSize }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 4)

    val unproductiveApps: StateFlow<List<ConfiguredApp>> = configuredAppRepository.observeUnproductiveApps()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _tempAppsPerRow = MutableStateFlow(appsPerRow.value)
    val tempAppsPerRow: StateFlow<Int> = _tempAppsPerRow

    init {
        viewModelScope.launch {
            appsPerRow.collect { _tempAppsPerRow.value = it }
        }
    }

    fun onSlideCountChange(newCount: Int) {
        viewModelScope.launch {
            val currentConfig = configRepository.configFlow.first()
            configRepository.updateConfig(
                slideCount = newCount,
                gridSize = currentConfig.gridSize
            )
        }
    }

    fun onAppsPerRowChange(newCount: Int) {
        viewModelScope.launch {
            val currentConfig = configRepository.configFlow.first()
            configRepository.updateConfig(
                slideCount = currentConfig.slideCount,
                gridSize = newCount
            )
        }
    }

    fun onUnproductiveAppsSelected(selectedPackages: Array<String>) {
        viewModelScope.launch {
            val pm = getApplication<Application>().packageManager
            val newUnproductiveApps = selectedPackages.mapNotNull { packageName ->
                try {
                    val appInfo = pm.getApplicationInfo(packageName, 0)
                    ConfiguredApp(
                        appLabel = pm.getApplicationLabel(appInfo).toString(),
                        packageName = packageName,
                        status = AppStatus.UNPRODUCTIVE
                    )
                } catch (e: Exception) { null }
            }
            // Use the correct repository to save the apps
            configuredAppRepository.replaceUnproductiveApps(newUnproductiveApps)
        }
    }

    fun saveSettings() {
        Log.d("SettingsViewModel", "Save/Done action triggered.")
    }
}