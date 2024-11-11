package com.example.vinillos_app_misw.data.repositories

import android.content.Context
import com.example.vinillos_app_misw.data.adapters.ArtistAdapter
import com.example.vinillos_app_misw.data.model.Performer
import com.example.vinillos_app_misw.data.network.VolleyBroker
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class ArtistRepository(context: Context,private val artistAdapter: ArtistAdapter) {

    private val volleyBroker: VolleyBroker = VolleyBroker(context)
    private val gson = Gson()

    suspend fun getArtists(): List<Performer> {
        return withContext(Dispatchers.IO) {
            suspendCoroutine { continuation ->
                val request = VolleyBroker.getRequest(
                    "musicians",
                    { response ->
                        val artistListType = object : TypeToken<List<Performer>>() {}.type
                        val artists: List<Performer> = gson.fromJson(response, artistListType)
                        continuation.resume(artists)
                    },
                    { error ->
                        continuation.resumeWithException(Exception(error.message ?: "Unknown error"))
                    }
                )
                volleyBroker.instance.add(request)
            }
        }
    }

    suspend fun getArtist(artistId: Int): Performer {
        return withContext(Dispatchers.IO) {
            suspendCoroutine { continuation ->
                val request = VolleyBroker.getRequest(
                    "musicians/$artistId",
                    { response ->
                        val artistType = object : TypeToken<Performer>() {}.type
                        val artist: Performer = gson.fromJson(response, artistType)
                        continuation.resume(artist)
                    },
                    { error ->
                        continuation.resumeWithException(Exception(error.message ?: "Unknown error"))
                    }
                )
                volleyBroker.instance.add(request)
            }
        }
    }

    fun saveArtistID(artistID: Int) {
        artistAdapter.saveArtistID(artistID)
    }

    fun getArtistId(): Int? {
        return artistAdapter.getArtistId()
    }

    fun clearArtistID() {
        return artistAdapter.clearArtistID()
    }
}