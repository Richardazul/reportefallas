package com.lab.failurereport.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.lab.failurereport.data.FailureReport
import com.lab.failurereport.databinding.FragmentTicketsBinding

class TicketsFragment : Fragment() {

    private var _binding: FragmentTicketsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FailureViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTicketsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = FailureAdapter(
            showDelete = true,
            onItemClick = { report -> openDetail(report) },
            onDelete = { report -> confirmDelete(report) }
        )
        binding.recyclerViewTickets.adapter = adapter

        viewModel.failures.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
            binding.tvEmptyTickets.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun openDetail(report: FailureReport) {
        val intent = Intent(requireContext(), DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_REPORT_ID, report.id)
        startActivity(intent)
    }

    private fun confirmDelete(report: FailureReport) {
        AlertDialog.Builder(requireContext())
            .setTitle("Eliminar ticket")
            .setMessage("¿Deseas eliminar el ticket del equipo ${report.inventoryNumber}?")
            .setPositiveButton("Eliminar") { _, _ -> viewModel.delete(report) }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
