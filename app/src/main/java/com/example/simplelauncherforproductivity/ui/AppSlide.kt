package com.example.simplelauncherforproductivity.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.simplelauncherforproductivity.data.entity.AppInfo
import com.example.simplelauncherforproductivity.ui.components.AppIcon

@Composable
fun AppSlide(
    pageNumber: Int,
    appsPerRow: Int,
    allApps: List<AppInfo>,
    onAppClick: (AppInfo) -> Unit
) {
    val appsOnThisPage = remember(allApps, pageNumber, appsPerRow) {
        val pageSize = appsPerRow * 5
        val startIndex = (pageNumber - 1) * pageSize
        if (startIndex >= allApps.size) {
            emptyList()
        } else {
            val endIndex = (startIndex + pageSize).coerceAtMost(allApps.size)
            allApps.subList(startIndex, endIndex)
        }
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(appsPerRow),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(
            items = appsOnThisPage,
            key = { app -> "${app.packageName}_page$pageNumber" }
        ) {
        }
    }
}
