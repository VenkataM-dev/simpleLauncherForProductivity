package com.example.simplelauncherforproductivity.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.simplelauncherforproductivity.domain.usecases.GetInstalledAppsUseCase
import com.example.simplelauncherforproductivity.domain.usecases.InterceptAppLaunchUseCase
import com.example.simplelauncherforproductivity.presentation.viewmodel.SettingsViewModelFactory
import com.example.simplelauncherforproductivity.presentation.viewmodel.SettingsViewModel
import com.example.simplelauncherforproductivity.ui.theme.SimpleLauncherForProductivityTheme

class SlideActivity : ComponentActivity() {

    private val settingsViewModel: SettingsViewModel by viewModels {
        SettingsViewModelFactory(application)
    }

    private val getInstalledAppsUseCase by lazy { GetInstalledAppsUseCase(this) }

    private val settingsLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val installedApps = getInstalledAppsUseCase.execute()

        setContent {
            SimpleLauncherForProductivityTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val slideCount by settingsViewModel.slideCount.collectAsStateWithLifecycle()
                    val appsPerRow by settingsViewModel.appsPerRow.collectAsStateWithLifecycle()

                    val pagerState = rememberPagerState(pageCount = { slideCount + 1 })

                    HorizontalPager(state = pagerState) { page ->
                        if (page < slideCount) {
                            HomeScreen(
                                slideNumber = page + 1,
                                onSettingsClick = { launchSettings() }
                            )
                        } else {
                            val context = LocalContext.current
                            AppDrawerScreen(
                                allApps = installedApps,
                                appsPerRow = appsPerRow,
                                onAppClick = { app ->
                                    InterceptAppLaunchUseCase()(context, app.packageName.toString())
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    private fun launchSettings() {
        val intent = Intent(this, SettingsPageActivity::class.java)
        settingsLauncher.launch(intent)
    }
}
