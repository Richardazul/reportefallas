package com.lab.failurereport.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.lab.failurereport.R
import com.lab.failurereport.data.FailureReport
import com.lab.failurereport.databinding.ItemFailureBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FailureAdapter(
    private val showDelete: Boolean,
    private val onItemClick: (FailureReport) -> Unit,
    private val onDelete: (FailureReport) -> Unit
) : ListAdapter<FailureReport, FailureAdapter.ViewHolder>(DiffCallback) {

    inner class ViewHolder(private val binding: ItemFailureBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(report: FailureReport) {
            binding.tvLocation.text = report.location
            binding.tvInventory.text = "Inv: ${report.inventoryNumber}"
            binding.tvDescription.text = report.description
            binding.tvDate.text = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                .format(Date(report.timestamp))

            if (!report.photoPath.isNullOrEmpty()) {
                val file = File(report.photoPath)
                if (file.exists()) {
                    binding.ivPhoto.load(file) {
                        crossfade(true)
                        placeholder(R.drawable.ic_image_placeholder)
                    }
                } else {
                    binding.ivPhoto.setImageResource(R.drawable.ic_image_placeholder)
                }
            } else {
                binding.ivPhoto.setImageResource(R.drawable.ic_image_placeholder)
            }

            binding.btnDelete.visibility = if (showDelete) View.VISIBLE else View.GONE

            binding.root.setOnClickListener { onItemClick(report) }
            binding.btnDelete.setOnClickListener { onDelete(report) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFailureBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object DiffCallback : DiffUtil.ItemCallback<FailureReport>() {
        override fun areItemsTheSame(a: FailureReport, b: FailureReport) = a.id == b.id
        override fun areContentsTheSame(a: FailureReport, b: FailureReport) = a == b
    }
}
