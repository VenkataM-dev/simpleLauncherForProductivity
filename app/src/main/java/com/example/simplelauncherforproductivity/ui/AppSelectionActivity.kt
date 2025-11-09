package com.example.simplelauncherforproductivity.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.simplelauncherforproductivity.data.entity.AppInfo
import com.example.simplelauncherforproductivity.presentation.viewmodel.AllAppsViewModel
import com.example.simplelauncherforproductivity.ui.components.AppIcon
import com.example.simplelauncherforproductivity.ui.theme.SimpleLauncherForProductivityTheme

class AppSelectionActivity : ComponentActivity() {

    private val viewModel: AllAppsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SimpleLauncherForProductivityTheme {
                val allApps by viewModel.apps.collectAsStateWithLifecycle()
                AppSelectionScreen(
                    allApps = allApps,
                    onConfirm = { selectedPackages ->
                        // Return the selected packages to the calling activity
                        val resultIntent = Intent().apply {
                            putExtra("SELECTED_APPS", selectedPackages.toTypedArray())
                        }
                        setResult(Activity.RESULT_OK, resultIntent)
                        finish()
                    }
                )
            }
        }
    }
}

@Composable
fun AppSelectionScreen(
    allApps: List<AppInfo>,
    onConfirm: (List<String>) -> Unit
) {
    var selectedApps by remember { mutableStateOf<Set<String>>(emptySet()) }

    Scaffold(
        bottomBar = {
            // Confirm button only shows when at least one app is selected
            if (selectedApps.isNotEmpty()) {
                BottomAppBar {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(onClick = { onConfirm(selectedApps.toList()) }) {
                            Text("Confirm (${selectedApps.size})")
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 90.dp), // Let the grid adapt to screen size
            modifier = Modifier.padding(paddingValues)
        ) {
            items(allApps, key = { it.packageName }) { app ->
                val isSelected = selectedApps.contains(app.packageName)
                SelectableAppIcon(
                    app = app,
                    isSelected = isSelected,
                    onAppClick = {
                        selectedApps = if (isSelected) {
                            selectedApps - app.packageName.toString()
                        } else {
                            selectedApps + app.packageName.toString()
                        }
                    }
                )
            }
        }
    }
}

// A variation of AppIcon that shows a selection state
@Composable
private fun SelectableAppIcon(
    app: AppInfo,
    isSelected: Boolean,
    onAppClick: (AppInfo) -> Unit
) {
    val modifier = if (isSelected) {
        Modifier.border(2.dp, MaterialTheme.colorScheme.primary, shape = MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
    } else {
        Modifier
    }

    AppIcon(
        app = app,
        onAppClick = onAppClick,
        modifier = modifier
    )
}
