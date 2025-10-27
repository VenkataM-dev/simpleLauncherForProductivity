package com.example.simplelauncherforproductivity.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "config")
data class ConfigEntity (
    @PrimaryKey val id: Int,
    val slideCount: Int,
    val gridSize: Int
)