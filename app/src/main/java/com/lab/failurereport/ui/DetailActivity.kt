package com.lab.failurereport.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import coil.load
import com.lab.failurereport.R
import com.lab.failurereport.data.FailureDatabase
import com.lab.failurereport.data.FailureReport
import com.lab.failurereport.databinding.ActivityDetailBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_REPORT_ID = "extra_report_id"
    }

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        WindowCompat.setDecorFitsSystemWindows(window, true)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val reportId = intent.getLongExtra(EXTRA_REPORT_ID, -1L)
        if (reportId == -1L) { finish(); return }

        loadReport(reportId)
    }

    private fun loadReport(id: Long) {
        val dao = FailureDatabase.getDatabase(this).failureDao()
        lifecycleScope.launch {
            // Consulta puntual fuera del hilo principal
            val report: FailureReport? = withContext(Dispatchers.IO) {
                dao.getById(id)
            }
            report?.let { bindReport(it) } ?: finish()
        }
    }

    private fun bindReport(report: FailureReport) {
        binding.tvDetailLocation.text = report.location
        binding.tvDetailInventory.text = report.inventoryNumber
        binding.tvDetailDescription.text = report.description
        binding.tvDetailDate.text = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            .format(Date(report.timestamp))

        if (!report.photoPath.isNullOrEmpty()) {
            val file = File(report.photoPath)
            if (file.exists()) {
                binding.ivDetailPhoto.visibility = View.VISIBLE
                binding.ivDetailPhoto.load(file) { crossfade(true) }
            } else {
                binding.ivDetailPhoto.setImageResource(R.drawable.ic_image_placeholder)
            }
        } else {
            binding.ivDetailPhoto.setImageResource(R.drawable.ic_image_placeholder)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
