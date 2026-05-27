package com.lab.failurereport.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.lab.failurereport.data.FailureDatabase
import com.lab.failurereport.data.FailureReport
import kotlinx.coroutines.launch

class FailureViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = FailureDatabase.getDatabase(application).failureDao()

    val failures = dao.getAll()

    fun insert(report: FailureReport) = viewModelScope.launch {
        dao.insert(report)
    }

    fun delete(report: FailureReport) = viewModelScope.launch {
        dao.delete(report)
    }
}
