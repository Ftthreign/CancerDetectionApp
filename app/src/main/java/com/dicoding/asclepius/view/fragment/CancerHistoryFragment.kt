package com.dicoding.asclepius.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.data.local.entity.CancerHistoryEntity
import com.dicoding.asclepius.databinding.FragmentHistoryBinding
import com.dicoding.asclepius.view.adapter.CancerHistoryAdapter
import com.dicoding.asclepius.view.viewmodel.MainViewModel
import com.dicoding.asclepius.view.viewmodel.ResultViewModel
import com.dicoding.asclepius.view.viewmodel.ViewModelFactory

class CancerHistoryFragment : Fragment(), CancerHistoryAdapter.OnItemButtonClickListener {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<ResultViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }
    private val adapter = CancerHistoryAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireActivity())

        viewModel.getCancerHistory().observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    override fun onItemButtonClick(cancerHistory: CancerHistoryEntity) {
        viewModel.deleteCancerHistory(cancerHistory)
    }
}