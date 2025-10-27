package com.example.simplelauncherforproductivity.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

class WarningActivity : ComponentActivity() {

    companion object {
        const val EXTRA_PACKAGE_NAME = "extra_package_name"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // The package name of the app we want to launch if the user clicks "Continue"
        val packageName = intent.getStringExtra(EXTRA_PACKAGE_NAME)

        setContent {
            // We use a transparent theme/color to see the screen underneath
            Surface(color = Color.Black.copy(alpha = 0.6f)) {
                WarningDialog(
                    onContinue = {
                        // User wants to continue. Set result and finish.
                        val resultData = Intent()
                        resultData.putExtra(EXTRA_PACKAGE_NAME, packageName)
                        setResult(Activity.RESULT_OK, resultData)
                        finish()
                    },
                    onCancel = {
                        // User canceled. Set result and finish.
                        setResult(Activity.RESULT_CANCELED)
                        finish()
                    }
                )
            }
        }
    }

    // Prevent the user from dismissing the activity by pressing the back button
    override fun onBackPressed() {
        // Do nothing
    }
}

@Composable
fun WarningDialog(
    onContinue: () -> Unit,
    onCancel: () -> Unit
) {
    // Dialog is a composable that shows content in a floating window, perfect for this.
    Dialog(onDismissRequest = onCancel) {
        Card(
            shape = MaterialTheme.shapes.extraLarge,
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Are you sure?",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "An unproductive app is being opened.",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    OutlinedButton(
                        onClick = onCancel,
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 4.dp)
                    ) {
                        Text("Cancel")
                    }
                    Button(
                        onClick = onContinue,
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 4.dp)
                    ) {
                        Text("Continue")
                    }
                }
            }
        }
    }
}
