package com.lab.failurereport.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "failures")
data class FailureReport(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val location: String,
    val inventoryNumber: String,
    val description: String,
    val photoPath: String?,
    val timestamp: Long = System.currentTimeMillis()
)
