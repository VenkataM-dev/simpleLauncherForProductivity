package com.example.simplelauncherforproductivity.domain.usecases

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import com.example.simplelauncherforproductivity.data.entity.AppInfo
import kotlin.text.lowercase

/**
 * A use case dedicated to fetching a list of all launchable applications
 * installed on the device.
 */
class GetInstalledAppsUseCase(private val context: Context) {

    fun execute(): List<AppInfo> {
        val packageManager = context.packageManager
        val mainIntent = Intent(Intent.ACTION_MAIN, null).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }

        return packageManager.queryIntentActivities(mainIntent, 0)
            .mapNotNull { resolveInfo ->
                try {
                    val appInfo = packageManager.getApplicationInfo(resolveInfo.activityInfo.packageName, 0)

                    AppInfo(
                        label = packageManager.getApplicationLabel(appInfo).toString(),
                        packageName = resolveInfo.activityInfo.packageName,
                        icon = packageManager.getApplicationIcon(resolveInfo.activityInfo.packageName)
                    )
                } catch (e: PackageManager.NameNotFoundException) {
                    null
                }
            }
            .sortedBy { it.label.toString().lowercase() }
    }
}
