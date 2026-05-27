package com.lab.failurereport.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.lab.failurereport.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FailureViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.failures.observe(viewLifecycleOwner) { list ->
            binding.tvTotalTickets.text = list.size.toString()
        }

        binding.btnNewTicket.setOnClickListener {
            startActivity(Intent(requireContext(), ReportActivity::class.java))
        }

        binding.btnGoHistory.setOnClickListener {
            (activity as? MainActivity)?.goToHistorySection()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
