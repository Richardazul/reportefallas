package com.lab.failurereport.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FailureDao {

    @Query("SELECT * FROM failures ORDER BY timestamp DESC")
    fun getAll(): LiveData<List<FailureReport>>

    @Insert
    suspend fun insert(report: FailureReport): Long

    @Query("SELECT * FROM failures WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): FailureReport?

    @Delete
    suspend fun delete(report: FailureReport)
}
