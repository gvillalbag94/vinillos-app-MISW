package com.example.vinillos_app_misw.presentation.view_model.album

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vinillos_app_misw.data.model.AlbumWithDetails
import com.example.vinillos_app_misw.data.repositories.AlbumRepository
import kotlinx.coroutines.launch

class AlbumViewModel( private val repository: AlbumRepository) : ViewModel() {

    private val _albums = MutableLiveData<List<AlbumWithDetails>>()
    val albums: LiveData<List<AlbumWithDetails>> get() = _albums

    private val _album = MutableLiveData<AlbumWithDetails>()
    val album: LiveData<AlbumWithDetails> get() = _album

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    init {
        getAlbums()
    }

    fun getAlbums() {
        viewModelScope.launch {
            try {
                val albumList = repository.getAlbums()
                _albums.value = albumList
            } catch (e: Exception) {
                _error.value = e.message ?: "An error occurred"
            }
        }
    }

    fun getAlbum(id: Int) {
        viewModelScope.launch {
            try {
                val albumItem = repository.getAlbum(id)
                _album.value = albumItem
            } catch (e: Exception) {
                _error.value = e.message ?: "An error occurred"
            }
        }
    }

    private val _albumID = MutableLiveData<Int?>()
    val albumId: LiveData<Int?> get() = _albumID

    fun saveAlbumID(albumId: Int) {
        repository.saveAlbumID(albumId)
    }

    fun getAlbumId() {
        _albumID.value = repository.getAlbumId()
    }

    fun clearAlbumID() {
        repository.clearAlbumID()
        _albumID.value = null
    }
}