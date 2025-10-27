package com.example.simplelauncherforproductivity.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DigitalClock(modifier: Modifier = Modifier) {
    var currentTime by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        val sdf = SimpleDateFormat("h:mm a", Locale.getDefault())
        while (true) {
            currentTime = sdf.format(Date())
            delay(1000)
        }
    }

    Text(
        text = currentTime,
        fontSize = 56.sp,
        fontWeight = FontWeight.Bold,
        modifier = modifier
    )
}
