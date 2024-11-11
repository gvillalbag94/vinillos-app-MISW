package com.example.vinillos_app_misw.data.repositories

import android.content.Context
import com.example.vinillos_app_misw.data.adapters.CollectorAdapter
import com.example.vinillos_app_misw.data.model.Collector
import com.example.vinillos_app_misw.data.network.VolleyBroker
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class CollectorRepository(context: Context, private val collectorAdapter: CollectorAdapter) {

    private val volleyBroker: VolleyBroker = VolleyBroker(context)
    private val gson = Gson()

    suspend fun getCollectors(): List<Collector> {
        return withContext(Dispatchers.IO) {
            suspendCoroutine { continuation ->
                val request = VolleyBroker.getRequest(
                    "collectors",
                    { response ->
                        val collectorListType = object : TypeToken<List<Collector>>() {}.type
                        val collectors: List<Collector> = gson.fromJson(response, collectorListType)
                        continuation.resume(collectors)
                    },
                    { error ->
                        continuation.resumeWithException(Exception(error.message ?: "Unknown error"))
                    }
                )
                volleyBroker.instance.add(request)
            }
        }
    }

    suspend fun getCollector(collectorId: Int): Collector {
        return withContext(Dispatchers.IO) {
            suspendCoroutine { continuation ->
                val request = VolleyBroker.getRequest(
                    "collectors/$collectorId",
                    { response ->
                        val collectorType = object : TypeToken<Collector>() {}.type
                        val collector: Collector = gson.fromJson(response, collectorType)
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