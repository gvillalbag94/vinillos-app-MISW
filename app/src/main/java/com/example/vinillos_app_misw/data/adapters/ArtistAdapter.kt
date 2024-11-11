package com.example.vinillos_app_misw.data.adapters

import android.content.Context
import android.content.SharedPreferences

class ArtistAdapter(context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        "ArtistPrefers",
        Context.MODE_PRIVATE,
    )

    fun saveArtistID(artistID: Int) {
        val artistIdToString = artistID.toString();
        sharedPreferences.edit().putString("artist_id_key", artistIdToString).apply()
    }

    fun getArtistId(): Int? {
        return sharedPreferences.getString("artist_id_key", null)?.toInt()
    }

    fun clearArtistID() {
        sharedPreferences.edit().remove("artist_id_key").apply()
        return
    }
}