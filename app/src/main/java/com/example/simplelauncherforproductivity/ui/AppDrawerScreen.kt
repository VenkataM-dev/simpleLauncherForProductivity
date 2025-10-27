package com.example.simplelauncherforproductivity.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.simplelauncherforproductivity.data.entity.AppInfo
import coil.compose.AsyncImage

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

@Composable
private fun AppIcon(
    app: AppInfo,
    onAppClick: (AppInfo) -> Unit
) {
    Column(
        modifier = Modifier
            // Make the whole column clickable
            .clickable { onAppClick(app) }
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AsyncImage(
            model = app.icon,
            contentDescription = app.label as String?,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f) // Makes the icon container a square
        )
        Text(
            text = app.label,
            textAlign = TextAlign.Center,
            maxLines = 1, // Ensure the app name doesn't wrap to a second line
            overflow = TextOverflow.Ellipsis, // Add "..." if the name is too long
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

