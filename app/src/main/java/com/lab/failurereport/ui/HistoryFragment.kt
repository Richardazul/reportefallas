package com.lab.failurereport.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.lab.failurereport.data.FailureReport
import com.lab.failurereport.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FailureViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = FailureAdapter(
            showDelete = false,
            onItemClick = { report -> openDetail(report) },
            onDelete = { }
        )
        binding.recyclerViewHistory.adapter = adapter

        viewModel.failures.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list.sortedBy { it.timestamp })
            binding.tvEmptyHistory.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun openDetail(report: FailureReport) {
        val intent = Intent(requireContext(), DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_REPORT_ID, report.id)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
