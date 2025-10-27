package com.example.simplelauncherforproductivity.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.simplelauncherforproductivity.R
import com.example.simplelauncherforproductivity.data.entity.ConfiguredApp
import com.example.simplelauncherforproductivity.presentation.viewmodel.SettingsViewModelFactory
import com.example.simplelauncherforproductivity.presentation.viewmodel.SettingsViewModel
import kotlin.collections.map
import kotlin.collections.toTypedArray
import coil.compose.AsyncImage
import com.example.simplelauncherforproductivity.ui.theme.SimpleLauncherForProductivityTheme

class SettingsPageActivity : ComponentActivity() {

    companion object {
        const val PREFS_NAME = "SimpleLauncherPrefs"
        const val KEY_SLIDE_COUNT = "slide_count"
        const val KEY_APPS_PER_ROW = "apps_per_row"
    }

    private val viewModel: SettingsViewModel by viewModels {
        SettingsViewModelFactory(application)
    }

    private val selectAppsLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val selectedPackages =
                    result.data?.getStringArrayExtra("SELECTED_PACKAGES") ?: emptyArray()
                // Now we can call the ViewModel to handle the result
                viewModel.onUnproductiveAppsSelected(selectedPackages)
            }
        }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SimpleLauncherForProductivityTheme {
                val slideCount by viewModel.slideCount.collectAsStateWithLifecycle()
                val appsPerRow by viewModel.appsPerRow.collectAsStateWithLifecycle()
                val unproductiveApps by viewModel.unproductiveApps.collectAsStateWithLifecycle()

                Scaffold(
                    topBar = { TopAppBar(title = { Text("Settings") }) },
                    bottomBar = {
                        Button(
                            onClick = {
                                viewModel.saveSettings()
                                setResult(RESULT_OK)
                                finish()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .windowInsetsPadding(WindowInsets.navigationBars)
                                .padding(16.dp)
                        ) {
                            Text("Save")
                        }
                    }
                ) { innerPadding ->
                    SettingsScreen(
                        modifier = Modifier.padding(innerPadding),
                        slideCount = slideCount,
                        onSlideCountChange = { viewModel.onSlideCountChange(it) },
                        appsPerRow = appsPerRow,
                        onAppsPerRowChange = { viewModel.onAppsPerRowChange(it) },
                        unproductiveApps = unproductiveApps,
                        onAddUnproductiveClick = {
                            val intent = Intent(this, SelectAppsActivity::class.java)
                            intent.putExtra(
                                "PREVIOUSLY_SELECTED_PACKAGES",
                                unproductiveApps.map { it.packageName }.toTypedArray()
                            )
                            selectAppsLauncher.launch(intent)
                        },
                        onSaveClick = {
                            viewModel.saveSettings()
                            setResult(RESULT_OK)
                            finish()
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    modifier: Modifier,
    slideCount: Int,
    onSlideCountChange: (Int) -> Unit,
    appsPerRow: Int,
    onAppsPerRowChange: (Int) -> Unit,
    unproductiveApps: List<ConfiguredApp>,
    onAddUnproductiveClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    Column(
        modifier = modifier.padding(16.dp)
    ) {
        SettingsRow(
            label = "Number of slides",
            value = slideCount,
            onValueChange = onSlideCountChange,
            range = 1..9
        )

        Spacer(modifier = Modifier.height(16.dp))

        SettingsRow(
            label = "Apps per Row (Drawer)",
            value = appsPerRow,
            onValueChange = onAppsPerRowChange,
            range = 2..8
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Unproductive Apps Section
        Text(
            "Unproductive Apps",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            item {
                IconButton(onClick = onAddUnproductiveClick) {
                    Icon(
                        painterResource(id = R.drawable.ic_add),
                        contentDescription = "Add Unproductive App",
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
            items(unproductiveApps) { app ->
                val packageManager = LocalContext.current.packageManager
                // Load the icon drawable using the package name
                val iconDrawable = try {
                    packageManager.getApplicationIcon(app.packageName)
                } catch (e: Exception) {
                    Log.d("Settings page", "No icon found for app")
                    null
                }

                if (iconDrawable != null) {
                    AsyncImage(
                        // Use the loaded drawable with rememberDrawablePainter
                        model = iconDrawable,
                        contentDescription = app.appLabel,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun SettingsRow(
    label: String,
    value: Int,
    onValueChange: (Int) -> Unit,
    range: IntRange
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(label, modifier = Modifier.weight(1f))
        IconButton(onClick = { if (value > range.first) onValueChange(value - 1) }) {
            Icon(painterResource(id = R.drawable.ic_remove), contentDescription = "Decrease")
        }
        Text(
            text = value.toString(),
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(40.dp)
        )
        IconButton(onClick = { if (value < range.last) onValueChange(value + 1) }) {
            Icon(painterResource(id = R.drawable.ic_plus), contentDescription = "Increase")
        }
    }
}
