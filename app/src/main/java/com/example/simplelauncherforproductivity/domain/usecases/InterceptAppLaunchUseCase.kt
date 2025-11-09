package com.example.simplelauncherforproductivity.domain.usecases

import android.accessibilityservice.AccessibilityService
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import com.example.simplelauncherforproductivity.data.dao.ConfiguredAppDAO
import com.example.simplelauncherforproductivity.data.database.AppDatabase
import com.example.simplelauncherforproductivity.data.entity.AppStatus
import com.example.simplelauncherforproductivity.domain.repositories.ConfiguredAppRepository
import com.example.simplelauncherforproductivity.ui.WarningActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class InterceptAppLaunchUseCase: AccessibilityService() {

    private val job = SupervisorJob()

    private val serviceScope = CoroutineScope(Dispatchers.Main + job)

    private var lastLaunchedPackage: String? = null

    private var lastLaunchTime: Long = 0

    private lateinit var configuredAppDao: ConfiguredAppDAO

    operator fun invoke(context: Context, packageName: String) {
        val launchIntent: Intent? = context.packageManager.getLaunchIntentForPackage(packageName)
        launchIntent?.let {
            context.startActivity(it)
        }
    }

    override fun onCreate() {
        super.onCreate()
        configuredAppDao = AppDatabase.getDatabase(this).configuredAppDao()
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event?.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            val packageName = event.packageName?.toString() ?: return

            if (packageName == applicationContext.packageName || packageName == "android") {
                return
            }

            val currentTime = System.currentTimeMillis()
            if (packageName == lastLaunchedPackage && (currentTime - lastLaunchTime) < 2000) {
                return
            }

            Log.d("AppLaunchDetector", "Detected window change for package: $packageName")

            serviceScope.launch(Dispatchers.IO) {
                val launchedAppStatus = configuredAppDao.getStatusForApp(packageName)

                if (launchedAppStatus == AppStatus.UNPRODUCTIVE) {
                    Log.d("AppLaunchDetector", "UNPRODUCTIVE app detected: $packageName. Triggering warning.")

                    lastLaunchedPackage = packageName
                    lastLaunchTime = System.currentTimeMillis()

                    launch(Dispatchers.Main) {
                        triggerWarning(packageName)
                    }
                }
            }
        }
    }

    private fun triggerWarning(packageName: String) {
        val intent = Intent(this, WarningActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            putExtra("PACKAGE_NAME", packageName)
        }
        startActivity(intent)
    }

    override fun onInterrupt() {
        Log.d("AppLaunchDetector", "Service interrupted.")
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}