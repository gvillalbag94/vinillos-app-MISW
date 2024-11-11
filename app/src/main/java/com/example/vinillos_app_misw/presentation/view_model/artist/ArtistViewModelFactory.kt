package com.example.vinillos_app_misw.presentation.view_model.artist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.vinillos_app_misw.data.repositories.ArtistRepository

class ArtistViewModelFactory(private val repository: ArtistRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ArtistViewModel::class.java)) {
            return ArtistViewModel(repository) as T
        }
        throw IllegalArgumentException("Error ViewModel class")
    }
}