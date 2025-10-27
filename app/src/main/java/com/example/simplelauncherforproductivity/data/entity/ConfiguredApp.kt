package com.example.simplelauncherforproductivity.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.simplelauncherforproductivity.data.entity.AppStatus

@Entity(tableName = "configured_apps")
data class ConfiguredApp(
    @PrimaryKey val packageName: String,
    val appLabel: String,
    val status: AppStatus
)