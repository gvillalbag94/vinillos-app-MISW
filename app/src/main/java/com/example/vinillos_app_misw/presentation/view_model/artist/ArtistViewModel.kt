package com.example.vinillos_app_misw.presentation.view_model.artist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vinillos_app_misw.data.model.Performer
import com.example.vinillos_app_misw.data.repositories.ArtistRepository
import kotlinx.coroutines.launch

class ArtistViewModel( private val repository: ArtistRepository) : ViewModel() {
    private val _artists = MutableLiveData<List<Performer>>()
    val artists: LiveData<List<Performer>> get() = _artists

    private val _artist = MutableLiveData<Performer>()
    val artist: LiveData<Performer> get() = _artist

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    init {
        getArtists()
    }

    fun getArtists() {
        viewModelScope.launch {
            try {
                val artistList = repository.getArtists()
                _artists.value = artistList
            } catch (e: Exception) {
                _error.value = e.message ?: "An error occurred"
            }
        }
    }

    fun getArtist(id: Int) {
        viewModelScope.launch {
            try {
                val artistItem = repository.getArtist(id)
                _artist.value = artistItem
            } catch (e: Exception) {
                _error.value = e.message ?: "An error occurred"
            }
        }
    }

    private val _artistID = MutableLiveData<Int?>()
    val artistId: LiveData<Int?> get() = _artistID

    fun saveArtistID(artistId: Int) {
        repository.saveArtistID(artistId)
    }

    fun getArtistId() {
        _artistID.value = repository.getArtistId()
    }

    fun clearArtistID() {
        repository.clearArtistID()
        _artistID.value = null
    }
}