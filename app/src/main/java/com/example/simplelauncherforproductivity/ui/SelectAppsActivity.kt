package com.example.simplelauncherforproductivity.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.simplelauncherforproductivity.data.entity.AppInfo
import coil.compose.AsyncImage

class SelectAppsActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val previouslySelected = intent.getStringArrayExtra("PREVIOUSLY_SELECTED_PACKAGES")?.toSet() ?: emptySet()

        val pm = packageManager
        val mainIntent = Intent(Intent.ACTION_MAIN, null).addCategory(Intent.CATEGORY_LAUNCHER)
        val allApps = pm.queryIntentActivities(mainIntent, 0).map {
            AppInfo(
                label = it.loadLabel(pm),
                packageName = it.activityInfo.packageName,
                icon = it.loadIcon(pm)
            )
        }.sortedBy { it.label.toString().lowercase() }

        setContent {
            MaterialTheme {
                SelectAppsScreen(
                    allApps = allApps,
                    previouslySelected = previouslySelected,
                    onSelectionConfirmed = { selectedPackages ->
                        val resultIntent = Intent()
                        resultIntent.putExtra("SELECTED_PACKAGES", selectedPackages.toTypedArray())
                        setResult(Activity.RESULT_OK, resultIntent)
                        finish()
                    }
                )
            }
        }
    }
}

@Composable
fun SelectAppsScreen(
    allApps: List<AppInfo>,
    previouslySelected: Set<String>,
    onSelectionConfirmed: (List<String>) -> Unit
) {
    val selectedPackages = remember { mutableStateOf(previouslySelected) }

    Scaffold(
        bottomBar = {
            Button(
                onClick = { onSelectionConfirmed(selectedPackages.value.toList()) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Select Apps")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            items(allApps) { app ->
                SelectableAppRow(
                    app = app,
                    isSelected = app.packageName in selectedPackages.value,
                    onToggle = {
                        val newSet = selectedPackages.value.toMutableSet()
                        if (it) {
                            newSet.add(app.packageName.toString())
                        } else {
                            newSet.remove(app.packageName.toString())
                        }
                        selectedPackages.value = newSet
                    }
                )
            }
        }
    }
}

@Composable
fun SelectableAppRow(
    app: AppInfo,
    isSelected: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle(!isSelected) }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = app.icon,
            contentDescription = app.label.toString(),
            modifier = Modifier.size(40.dp)
        )

        Text(
            text = app.label.toString(),
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp)
        )

        Checkbox(
            checked = isSelected,
            onCheckedChange = { onToggle(it) }
        )
    }
}
