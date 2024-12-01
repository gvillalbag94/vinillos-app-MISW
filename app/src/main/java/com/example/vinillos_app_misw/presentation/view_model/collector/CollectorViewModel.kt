package com.example.vinillos_app_misw.presentation.view_model.collector

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vinillos_app_misw.data.model.Collector
import com.example.vinillos_app_misw.data.model.CollectorWithDetails
import com.example.vinillos_app_misw.data.repositories.CollectorRepository
import kotlinx.coroutines.launch

class CollectorViewModel( private val repository: CollectorRepository) : ViewModel() {

    private val _collectors = MutableLiveData<List<CollectorWithDetails>>()
    val collectors: LiveData<List<CollectorWithDetails>> get() = _collectors

    private val _collector = MutableLiveData<CollectorWithDetails>()
    val collector: LiveData<CollectorWithDetails> get() = _collector

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    init {
        getCollectors()
    }

    fun getCollectors() {
        viewModelScope.launch {
            try {
                val collectorList = repository.getCollectors()
                _collectors.value = collectorList
            } catch (e: Exception) {
                _error.value = e.message ?: "An error occurred"
            }
        }
    }

    fun getCollector(id: Int) {
        viewModelScope.launch {
            try {
                val collectorItem = repository.getCollector(id)
                _collector.value = collectorItem
            } catch (e: Exception) {
                _error.value = e.message ?: "An error occurred"
            }
        }
    }

    private val _collectorID = MutableLiveData<Int?>()
    val collectorId: LiveData<Int?> get() = _collectorID

    fun saveCollectorID(collectorId: Int) {
        repository.saveCollectorID(collectorId)
    }

    fun getCollectorId() {
        _collectorID.value = repository.getCollectorId()
    }

    fun clearCollectorID() {
        repository.clearCollectorID()
        _collectorID.value = null
    }
}