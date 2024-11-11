package com.example.vinillos_app_misw.data.adapters

import android.content.Context
import android.content.SharedPreferences

class CollectorAdapter(context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        "CollectorPrefers",
        Context.MODE_PRIVATE,
    )

    fun saveCollectorID(collectorID: Int) {
        val collectorIdToString = collectorID.toString();
        sharedPreferences.edit().putString("collector_id_key", collectorIdToString).apply()
    }

    fun getCollectorId(): Int? {
        return sharedPreferences.getString("collector_id_key", null)?.toInt()
    }

    fun clearCollectorID() {
        sharedPreferences.edit().remove("collector_id_key").apply()
        return
    }
}
