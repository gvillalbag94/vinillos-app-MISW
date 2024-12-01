package com.example.vinillos_app_misw.presentation.ui.views.collector.collectorList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.vinillos_app_misw.data.model.Collector
import com.example.vinillos_app_misw.data.model.CollectorWithDetails
import com.example.vinillos_app_misw.databinding.CollectorCardBinding


class CollectorListAdapter(private val collectors: List<CollectorWithDetails>, private val listener: OnCollectorClickListener)
    : RecyclerView.Adapter<CollectorListAdapter.CollectorViewHolder>() {

    interface OnCollectorClickListener {
        fun onCollectorClick(collector: CollectorWithDetails)
    }

    inner class CollectorViewHolder(private val binding: CollectorCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(collector: CollectorWithDetails) {
            binding.collector = collector

            // Set up click listener
            binding.albumButton.setOnClickListener {
                listener.onCollectorClick(collector)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectorViewHolder {
        val binding = CollectorCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CollectorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CollectorViewHolder, position: Int) {
        holder.bind(collectors[position])
    }

    override fun getItemCount(): Int = collectors.size
}