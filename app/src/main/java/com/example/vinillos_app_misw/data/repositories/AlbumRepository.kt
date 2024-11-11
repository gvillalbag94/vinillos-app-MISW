package com.example.vinillos_app_misw.data.repositories

import android.content.Context
import com.example.vinillos_app_misw.data.adapters.AlbumAdapter
import com.example.vinillos_app_misw.data.model.Album
import com.example.vinillos_app_misw.data.network.VolleyBroker
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


class AlbumRepository(context: Context, private val albumAdapter: AlbumAdapter) {

    private val volleyBroker: VolleyBroker = VolleyBroker(context)
    private val gson = Gson()

    suspend fun getAlbums(): List<Album> {
        return withContext(Dispatchers.IO) {
            suspendCoroutine { continuation ->
                val request = VolleyBroker.getRequest(
                    "albums",
                    { response ->
                        val albumListType = object : TypeToken<List<Album>>() {}.type
                        val albums: List<Album> = gson.fromJson(response, albumListType)
                        continuation.resume(albums)
                    },
                    { error ->
                        continuation.resumeWithException(Exception(error.message ?: "Unknown error"))
                    }
                )
                volleyBroker.instance.add(request)
            }
        }
    }

    suspend fun getAlbum(albumId: Int): Album {
        return withContext(Dispatchers.IO) {
            suspendCoroutine { continuation ->
                val request = VolleyBroker.getRequest(
                    "albums/$albumId",
                    { response ->
                        val albumType = object : TypeToken<Album>() {}.type
                        val album: Album = gson.fromJson(response, albumType)
                        continuation.resume(album)
                    },
                    { error ->
                        continuation.resumeWithException(Exception(error.message ?: "Unknown error"))
                    }
                )
                volleyBroker.instance.add(request)
            }
        }
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