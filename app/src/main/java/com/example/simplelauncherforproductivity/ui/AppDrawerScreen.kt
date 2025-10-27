package com.example.simplelauncherforproductivity.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.simplelauncherforproductivity.data.entity.AppInfo
import com.example.simplelauncherforproductivity.ui.components.AppIcon

@Composable
fun AppDrawerScreen(
    allApps: List<AppInfo>,
    appsPerRow: Int,
    onAppClick: (AppInfo) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(appsPerRow),
        contentPadding = PaddingValues(
            start = 8.dp,
            top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding(),
            end = 8.dp,
            bottom = 8.dp
        )
    ) {
        items(
            items = allApps,
            key = { app -> app.packageName }
        ) { app ->
            AppIcon(
                app = app,
                onAppClick = onAppClick
            )
        }
    }
}
