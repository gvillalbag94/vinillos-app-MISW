package com.example.vinillos_app_misw.presentation.view_model.album

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vinillos_app_misw.data.model.Album
import com.example.vinillos_app_misw.data.repositories.AlbumRepository

class AlbumViewModel( private val repository: AlbumRepository) : ViewModel() {

    private val _albums = MutableLiveData<List<Album>>()
    val albums: LiveData<List<Album>> get() = _albums

    private val _album = MutableLiveData<Album>()
    val album: LiveData<Album> get() = _album

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    init {
        getAlbums()
    }

     fun getAlbums() {
        repository.getAlbums(
            successCallback = { albumList ->
                _albums.value = albumList
            },
            errorCallback = { errorMessage ->
                _error.value = errorMessage
            }
        )
    }

    fun getAlbum(id: Int) {
        repository.getAlbum(
            id,
            successCallback = { album ->
                _album.value = album
            },
            errorCallback = { errorMessage ->
                _error.value = errorMessage
            }
        )
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