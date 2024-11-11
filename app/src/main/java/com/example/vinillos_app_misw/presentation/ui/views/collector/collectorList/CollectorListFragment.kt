package com.example.vinillos_app_misw.presentation.ui.views.collector.collectorList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vinillos_app_misw.data.adapters.CollectorAdapter
import com.example.vinillos_app_misw.data.model.Collector
import com.example.vinillos_app_misw.data.repositories.CollectorRepository
import com.example.vinillos_app_misw.databinding.FragmentCollectorListBinding
import com.example.vinillos_app_misw.presentation.view_model.collector.CollectorViewModel
import com.example.vinillos_app_misw.presentation.view_model.collector.CollectorViewModelFactory


class CollectorListFragment : Fragment(), CollectorListAdapter.OnCollectorClickListener {

    private lateinit var collectorViewModel: CollectorViewModel

    private lateinit var collectorAdapter: CollectorListAdapter

    private var _binding: FragmentCollectorListBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCollectorListBinding.inflate(inflater, container, false)
        val collectorAdapter = CollectorAdapter(requireContext())
        val repository = CollectorRepository(requireContext(),collectorAdapter)
        val factory = CollectorViewModelFactory(repository)
        collectorViewModel = ViewModelProvider(this, factory)[CollectorViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.collectorsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        collectorViewModel.collectors.observe(viewLifecycleOwner) { collectors ->
            collectorAdapter = CollectorListAdapter(collectors, this)
            binding.collectorsRecyclerView.adapter = collectorAdapter
        }
        collectorViewModel.getCollectors()
    }

    override fun onCollectorClick(collector: Collector) {
        TODO("Not yet implemented")
    }
}