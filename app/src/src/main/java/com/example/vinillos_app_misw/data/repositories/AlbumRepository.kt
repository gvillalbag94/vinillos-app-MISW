package com.example.vinillos_app_misw.data.repositories

import android.content.Context
import com.example.vinillos_app_misw.data.adapters.AlbumAdapter
import com.example.vinillos_app_misw.data.model.Album
import com.example.vinillos_app_misw.data.network.VolleyBroker
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class AlbumRepository(context: Context, private val albumAdapter: AlbumAdapter) {

    private val volleyBroker: VolleyBroker = VolleyBroker(context)
    private val gson = Gson()

    fun getAlbums(successCallback: (List<Album>) -> Unit, errorCallback: (String) -> Unit) {
        val request = VolleyBroker.getRequest(
            "albums",
            { response ->
                val albumListType = object : TypeToken<List<Album>>() {}.type
                val albums: List<Album> = gson.fromJson(response, albumListType)
                successCallback(albums)
            },
            { error ->
                errorCallback(error.message ?: "Unknown error")
            }
        )
        volleyBroker.instance.add(request)
    }

    fun getAlbum(albumId: Int,successCallback: (Album) -> Unit, errorCallback: (String) -> Unit) {
        val request = VolleyBroker.getRequest(
            "albums/$albumId",
            { response ->
                val albumType = object : TypeToken<Album>() {}.type
                val album: Album = gson.fromJson(response, albumType)
                successCallback(album)
            },
            { error ->
                errorCallback(error.message ?: "Unknown error")
            }
        )
        volleyBroker.instance.add(request)
    }

    fun saveAlbumID(albumID: Int) {
        albumAdapter.saveAlbumID(albumID)
    }

    fun getAlbumId(): Int? {
        return albumAdapter.getAlbumId()
    }

    fun clearAlbumID() {
        return albumAdapter.clearAlbumID()
    }

}