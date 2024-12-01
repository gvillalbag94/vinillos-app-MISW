package com.example.vinillos_app_misw.presentation.view_model.album

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vinillos_app_misw.data.model.Album
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

    private val _addAlbum = MutableLiveData<Boolean>()
    val addAlbum: LiveData<Boolean> get() = _addAlbum

    private val _addTrack = MutableLiveData<Boolean>()
    val addTrack: LiveData<Boolean> get() = _addTrack

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
                _error.value = e.message ?: "Ocurrio un error"
            }
        }
    }

    fun addAlbum(album: Album) {
        viewModelScope.launch {
            try {
                val addAlbumItem = repository.addAlbum(album)
                _addAlbum.value = addAlbumItem
            } catch (e: Exception) {
                _addAlbum.value = false
                _error.value = e.message ?: "Ocurrio un error"
            }
        }
    }



    fun fetchAlbums() {
        viewModelScope.launch {
            try {
                val albums = repository.fetchAlbums()
                _albums.value = albums
            } catch (e: Exception) {
                _addAlbum.value = false
                _error.value = e.message ?: "Ocurrio un error"
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

    fun saveUpdateValue(refresh: Boolean) {
        repository.setUpdateAlbum(refresh)
    }

    fun getUpdateValue(): Boolean {
        return repository.getUpdateAlbum()
    }


}