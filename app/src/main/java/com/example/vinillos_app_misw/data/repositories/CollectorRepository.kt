package com.example.vinillos_app_misw.data.repositories

import android.content.Context
import com.example.vinillos_app_misw.data.adapters.CollectorAdapter
import com.example.vinillos_app_misw.data.database.dao.CollectorDao
import com.example.vinillos_app_misw.data.model.Album
import com.example.vinillos_app_misw.data.model.AlbumWithDetails
import com.example.vinillos_app_misw.data.model.Collector
import com.example.vinillos_app_misw.data.model.CollectorAlbum
import com.example.vinillos_app_misw.data.model.CollectorWithDetails
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

class CollectorRepository(
    context: Context,
    private val collectorAdapter: CollectorAdapter,
    private val collectorDao: CollectorDao,
    ) {

    private val volleyBroker: VolleyBroker = VolleyBroker(context)
    private val gson = Gson()

    suspend fun getCollectors(): List<CollectorWithDetails> {
        return withContext(Dispatchers.IO) {
//            val localCollectors = collectorDao.getCollectors()
//            if (localCollectors.isNotEmpty()) {
//                return@withContext localCollectors
//            }
            val networkCollectors = fetchCollectorsFromNetwork()
//            collectorDao.insertCollectorsWithDetails(networkCollectors)
            return@withContext networkCollectors
        }
    }

    private suspend fun fetchCollectorsFromNetwork(): List<CollectorWithDetails> {
        return withContext(Dispatchers.IO) {
            suspendCoroutine { continuation ->
                val request = VolleyBroker.getRequest(
                    "collectors",
                    { response ->

                        val albums: List<CollectorWithDetails> = parseCollectorsJson(response)
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

    private fun parseCollectorsJson(json: String): List<CollectorWithDetails> {
        if (json.isEmpty()) return emptyList()

        val jsonArray = JSONArray(json)

        return (0 until jsonArray.length()).map { index ->
            val jsonObject = jsonArray.getJSONObject(index)

            val collector = Collector(
                id = jsonObject.getInt("id"),
                name = jsonObject.getString("name"),
                telephone = jsonObject.getString("telephone"),
                email = jsonObject.getString("email"),
            )

            val favoritePerformers = if (jsonObject.has("favoritePerformers") && !jsonObject.isNull("favoritePerformers")) {
                jsonObject.getJSONArray("favoritePerformers").let { tracksArray ->
                    (0 until tracksArray.length()).map { i ->
                        val performer = tracksArray.getJSONObject(i)
                        Performer(
                            id = performer.getInt("id"),
                            name = performer.getString("name"),
                            image = performer.getString("image"),
                            description = performer.getString("description"),
                            birthDate = performer.optString("birthDate", "1970-01-01T00:00:00.000Z"),
                            collectorId = collector.id,
                            albumId = null,
                        )
                    }
                }
            } else emptyList()

            val collectorAlbums = if (jsonObject.has("collectorAlbums") && !jsonObject.isNull("collectorAlbums")) {
                jsonObject.getJSONArray("collectorAlbums").let { performersArray ->
                    (0 until performersArray.length()).map { i ->
                        val performer = performersArray.getJSONObject(i)
                        CollectorAlbum(
                            id = performer.getInt("id"),
                            collectorId = collector.id,
                            status = performer.getString("status"),
                            price = performer.getDouble("price"),
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
                            albumId = null,
                            collectorId = collector.id,
                        )
                    }
                }
            } else emptyList()

            CollectorWithDetails(
                collector = collector,
                favoritePerformers = favoritePerformers,
                collectorAlbums = collectorAlbums,
                comments = comments
            )
        }
    }


    suspend fun getCollector(collectorId: Int): CollectorWithDetails {
        return withContext(Dispatchers.IO) {
            suspendCoroutine { continuation ->
                val request = VolleyBroker.getRequest(
                    "collectors/$collectorId",
                    { response ->
                        val collector: CollectorWithDetails = parserCollectorJson(response)
                        continuation.resume(collector)
                    },
                    { error ->
                        continuation.resumeWithException(Exception(error.message ?: "Unknown error"))
                    }
                )
                volleyBroker.instance.add(request)
            }
        }
    }

    private fun parserCollectorJson(json: String) : CollectorWithDetails {
        val jsonObject = JSONObject(json)

        val collector = Collector(
            id = jsonObject.getInt("id"),
            name = jsonObject.getString("name"),
            telephone = jsonObject.getString("telephone"),
            email = jsonObject.getString("email"),
        )

        val favoritePerformers = if (jsonObject.has("favoritePerformers") && !jsonObject.isNull("favoritePerformers")) {
            jsonObject.getJSONArray("favoritePerformers").let { tracksArray ->
                (0 until tracksArray.length()).map { i ->
                    val performer = tracksArray.getJSONObject(i)
                    Performer(
                        id = performer.getInt("id"),
                        name = performer.getString("name"),
                        image = performer.getString("image"),
                        description = performer.getString("description"),
                        birthDate = performer.optString("birthDate", "1970-01-01T00:00:00.000Z"),
                        collectorId = collector.id,
                        albumId = null,
                    )
                }
            }
        } else emptyList()

        val collectorAlbums = if (jsonObject.has("collectorAlbums") && !jsonObject.isNull("collectorAlbums")) {
            jsonObject.getJSONArray("collectorAlbums").let { performersArray ->
                (0 until performersArray.length()).map { i ->
                    val performer = performersArray.getJSONObject(i)
                    CollectorAlbum(
                        id = performer.getInt("id"),
                        collectorId = collector.id,
                        status = performer.getString("status"),
                        price = performer.getDouble("price"),
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
                        albumId = null,
                        collectorId = collector.id,
                    )
                }
            }
        } else emptyList()

        return CollectorWithDetails(
            collector = collector,
            favoritePerformers = favoritePerformers,
            collectorAlbums = collectorAlbums,
            comments = comments
        )
    }

    fun saveCollectorID(collectorID: Int) {
        collectorAdapter.saveCollectorID(collectorID)
    }

    fun getCollectorId(): Int? {
        return collectorAdapter.getCollectorId()
    }

    fun clearCollectorID() {
        return collectorAdapter.clearCollectorID()
    }
}