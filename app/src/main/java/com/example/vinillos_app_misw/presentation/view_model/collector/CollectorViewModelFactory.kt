package com.example.vinillos_app_misw.presentation.view_model.collector

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.vinillos_app_misw.data.repositories.CollectorRepository

class CollectorViewModelFactory(private val repository: CollectorRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CollectorViewModel::class.java)) {
            return CollectorViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}