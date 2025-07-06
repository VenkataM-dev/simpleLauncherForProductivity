package com.example.simplelauncherforproductivity

import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.simplelauncherforproductivity.ui.theme.SimpleLauncherForProductivityTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SimpleLauncherForProductivityTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    createHorizontalScroller()
                }
            }
        }
    }
}

@Composable
fun createHorizontalScroller() {
    val pageCount = 3
    val pagerState = rememberPagerState(pageCount = {
        pageCount
    })
    HorizontalPager(state = pagerState) { page ->
        when (page) {
            0 -> HomeScreenPage()
            pageCount-1 -> AppDrawerPage()
            else ->
                Text(
                    text = "Page: $page",
                    modifier = Modifier.fillMaxSize()
                )
        }
    }
}

@Composable
fun AppDrawerPage() {
    val context = LocalContext.current
    val pm = context.packageManager
    val apps = remember {
        pm.getInstalledApplications(PackageManager.GET_META_DATA)
            .filter { pm.getLaunchIntentForPackage(it.packageName) != null }
            .map {
                AppInfo(
                    name = it.loadLabel(pm).toString(),
                    icon = it.loadIcon(pm),
                    packageName = it.packageName
                )
            }
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(apps, key = { it.packageName }) { app ->
            AppIcon(app)
        }
    }
}

@Composable
fun HomeScreenPage() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Welcome!",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(16.dp))

            AppGrid()
        }
    }
}

@Composable
fun AppGrid() {
    val context = LocalContext.current
    val pm = context.packageManager
    val apps = remember {
        pm.getInstalledApplications(PackageManager.GET_META_DATA)
            .filter { pm.getLaunchIntentForPackage(it.packageName) != null }
            .map {
                AppInfo(
                    name = it.loadLabel(pm).toString(),
                    icon = it.loadIcon(pm),
                    packageName = it.packageName
                )
            }
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(apps, key = { it.packageName }) { app ->
            AppIcon(app)
        }
    }
}

@Composable
fun AppIcon(app: AppInfo) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .padding(8.dp)
            .clickable {
                val intent = context.packageManager.getLaunchIntentForPackage(app.packageName)
                if (intent != null) context.startActivity(intent)
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
//        Icon(
//            painter = painterResource(R.drawable.),
//            contentDescription = app.name,
//            modifier = Modifier.size(48.dp),
//            tint = Color.Unspecified
//        )
        Text(
            text = app.name,
            style = MaterialTheme.typography.labelSmall,
            color = Color.White,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}


data class AppInfo(
    val name: String,
    val icon: Drawable,
    val packageName: String
)

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SimpleLauncherForProductivityTheme {
        Greeting("Android")
    }
}