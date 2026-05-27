package com.lab.failurereport.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FailureReport::class], version = 1, exportSchema = false)
abstract class FailureDatabase : RoomDatabase() {

    abstract fun failureDao(): FailureDao

    companion object {
        @Volatile
        private var INSTANCE: FailureDatabase? = null

        fun getDatabase(context: Context): FailureDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    FailureDatabase::class.java,
                    "failure_database"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
