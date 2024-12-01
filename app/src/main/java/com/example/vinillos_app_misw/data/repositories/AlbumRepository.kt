package com.example.vinillos_app_misw.data.repositories

import android.content.Context
import android.util.Log
import com.example.vinillos_app_misw.data.adapters.AlbumAdapter
import com.example.vinillos_app_misw.data.database.dao.AlbumDao
import com.example.vinillos_app_misw.data.model.Album
import com.example.vinillos_app_misw.data.model.AlbumWithDetails
import com.example.vinillos_app_misw.data.model.Collector
import com.example.vinillos_app_misw.data.model.Comment
import com.example.vinillos_app_misw.data.model.Performer
import com.example.vinillos_app_misw.data.model.Track
import com.example.vinillos_app_misw.data.network.VolleyBroker
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


class AlbumRepository(
    context: Context,
    private val albumAdapter: AlbumAdapter,
    private val albumDao: AlbumDao,
    ) {

    private val volleyBroker: VolleyBroker = VolleyBroker(context)

    suspend fun getAlbums(): List<AlbumWithDetails> {
        return withContext(Dispatchers.IO) {
            val localCollectors = albumDao.getAlbums()
            if (localCollectors.isNotEmpty()) {
                return@withContext localCollectors
            }
            return@withContext fetchAlbums()
        }
    }

    suspend fun fetchAlbums() : List<AlbumWithDetails> {
        return withContext(Dispatchers.IO) {
            val networkAlbums = fetchAlbumsFromNetwork()
            albumDao.clearTable()
            albumDao.insertAlbumsWithDetails(networkAlbums)
            return@withContext networkAlbums
        }
    }

    private suspend fun fetchAlbumsFromNetwork(): List<AlbumWithDetails> {
        return withContext(Dispatchers.IO) {
            suspendCoroutine { continuation ->
                val request = VolleyBroker.getRequest(
                    "albums",
                    { response ->
                        val albums: List<AlbumWithDetails> = parseAlbumsJson(response)
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

    private fun parseAlbumsJson(json: String): List<AlbumWithDetails> {
        if (json.isEmpty()) return emptyList()

        val jsonArray = JSONArray(json)

        return (0 until jsonArray.length()).map { index ->
            val jsonObject = jsonArray.getJSONObject(index)

            val album = Album(
                id = jsonObject.getInt("id"),
                name = jsonObject.getString("name"),
                cover = jsonObject.getString("cover"),
                releaseDate = jsonObject.getString("releaseDate"),
                description = jsonObject.getString("description"),
                genre = jsonObject.getString("genre"),
                recordLabel = jsonObject.getString("recordLabel")
            )

            val tracks = if (jsonObject.has("tracks") && !jsonObject.isNull("tracks")) {
                jsonObject.getJSONArray("tracks").let { tracksArray ->
                    (0 until tracksArray.length()).map { i ->
                        val track = tracksArray.getJSONObject(i)
                        Track(
                            id = track.getInt("id"),
                            name = track.getString("name"),
                            duration = track.getString("duration"),
                            albumId = album.id
                        )
                    }
                }
            } else emptyList()

            val performers = if (jsonObject.has("performers") && !jsonObject.isNull("performers")) {
                jsonObject.getJSONArray("performers").let { performersArray ->
                    (0 until performersArray.length()).map { i ->
                        val performer = performersArray.getJSONObject(i)
                        Performer(
                            id = performer.getInt("id"),
                            name = performer.getString("name"),
                            image = performer.getString("image"),
                            description = performer.getString("description"),
                            birthDate = performer.optString("birthDate", "1970-01-01T00:00:00.000Z"), // Optional
                            albumId = album.id,
                            collectorId = null,
                        )
                    }
                }
            } else emptyList()

            val comments = if (jsonObject.has("comments") && !jsonObject.isNull("comments")) {
                jsonObject.getJSONArray("comments").let { commentsArray ->
                    (0 until commentsArray.length()).map { i ->
                        val comment = commentsArray.getJSONObject(i)
                        Comment(
                            id = comment.getInt("id"),
                            description = comment.getString("description"),
                            rating = comment.getInt("rating"),
                            albumId = album.id,
                            collectorId = null,
                        )
                    }
                }
            } else emptyList()

            AlbumWithDetails(
                album = album,
                tracks = tracks,
                performers = performers,
                comments = comments
            )
        }
    }

    suspend fun getAlbum(albumId: Int): AlbumWithDetails {
        return withContext(Dispatchers.IO) {
            val networkAlbum = fetchAlbumFromNetwork(albumId)
            return@withContext networkAlbum
        }
    }

    private suspend fun fetchAlbumFromNetwork(albumId: Int): AlbumWithDetails {
        return withContext(Dispatchers.IO) {
            suspendCoroutine { continuation ->
                val request = VolleyBroker.getRequest(
                    "albums/$albumId",
                    { response ->
                        val album: AlbumWithDetails = parserAlbumJson(response)
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

    private fun parserAlbumJson(json: String) : AlbumWithDetails {
        val jsonObject = JSONObject(json)

        val album = Album(
            id = jsonObject.getInt("id"),
            name = jsonObject.getString("name"),
            cover = jsonObject.getString("cover"),
            releaseDate = jsonObject.getString("releaseDate"),
            description = jsonObject.getString("description"),
            genre = jsonObject.getString("genre"),
            recordLabel = jsonObject.getString("recordLabel")
        )

        val tracks = if (jsonObject.has("tracks") && !jsonObject.isNull("tracks")) {
            jsonObject.getJSONArray("tracks").let { tracksArray ->
                (0 until tracksArray.length()).map { i ->
                    val track = tracksArray.getJSONObject(i)
                    Track(
                        id = track.getInt("id"),
                        name = track.getString("name"),
                        duration = track.getString("duration"),
                        albumId = album.id
                    )
                }
            }
        } else emptyList()

        val performers = if (jsonObject.has("performers") && !jsonObject.isNull("performers")) {
            jsonObject.getJSONArray("performers").let { performersArray ->
                (0 until performersArray.length()).map { i ->
                    val performer = performersArray.getJSONObject(i)
                    Performer(
                        id = performer.getInt("id"),
                        name = performer.getString("name"),
                        image = performer.getString("image"),
                        description = performer.getString("description"),
                        birthDate = performer.optString("birthDate", "1970-01-01T00:00:00.000Z"), // Optional
                        albumId = album.id,
                        collectorId = null,
                    )
                }
            }
        } else emptyList()

        val comments = if (jsonObject.has("comments") && !jsonObject.isNull("comments")) {
            jsonObject.getJSONArray("comments").let { commentsArray ->
                (0 until commentsArray.length()).map { i ->
                    val comment = commentsArray.getJSONObject(i)
                    Comment(
                        id = comment.getInt("id"),
                        description = comment.getString("description"),
                        rating = comment.getInt("rating"),
                        albumId = album.id,
                        collectorId = null,
                    )
                }
            }
        } else emptyList()

        return AlbumWithDetails(
            album = album,
            tracks = tracks,
            performers = performers,
            comments = comments
        )
    }

    suspend fun addAlbum(album: Album) : Boolean {
        return withContext(Dispatchers.IO) {
            suspendCoroutine { continuation ->

                val postParams = mapOf<String, Any>(
                    "name" to album.name,
                    "cover" to album.cover,
                    "releaseDate" to  album.releaseDate,
                    "description" to album.description,
                    "genre" to album.genre,
                    "recordLabel" to album.recordLabel,
                )

                val request = VolleyBroker.postRequest(
                    "albums",
                    JSONObject(postParams),
                    { response ->
                        val albumWithDetails : AlbumWithDetails = parserAlbumJson(response.toString())
                        if ( albumWithDetails.album.id != 0) {
                            continuation.resume(true)
                        } else {
                            continuation.resume(false)
                        }
                    },
                    { error ->
                        if (error.networkResponse != null && error.networkResponse.statusCode == 400) {
                            val errorMessage = String(error.networkResponse.data)
                            Log.e("NetworkError", "400 Bad Request: $errorMessage")
                        } else {
                            // Handle other errors
                            Log.e("NetworkError", "Error: ${error.message}")
                        }
                        continuation.resume(false)
                    }
                )
                volleyBroker.instance.add(request)
            }
        }
    }

    suspend fun addTrack(albumID: Int, nameTrack: String, duration: String,) : Boolean {
        return withContext(Dispatchers.IO) {
            suspendCoroutine { continuation ->

                val postParams = mapOf<String, Any>(
                    "name" to nameTrack,
                    "duration" to duration,
                )

                val request = VolleyBroker.postRequest(
                    "albums/$albumID/tracks",
                    JSONObject(postParams),
                    { response ->

                        if ( response["id"] != 0) {
                            continuation.resume(true)
                        } else {
                            continuation.resume(false)
                        }
                    },
                    { error ->
                        if (error.networkResponse != null && error.networkResponse.statusCode == 400) {
                            val errorMessage = String(error.networkResponse.data)
                            Log.e("NetworkError", "400 Bad Request: $errorMessage")
                        } else {
                            // Handle other errors
                            Log.e("NetworkError", "Error: ${error.message}")
                        }
                        continuation.resume(false)
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