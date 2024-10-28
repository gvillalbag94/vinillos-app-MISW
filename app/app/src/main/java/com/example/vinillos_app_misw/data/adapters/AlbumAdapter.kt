package com.example.vinillos_app_misw.data.adapters

import android.content.Context
import android.content.SharedPreferences


/*
    * AlbumAdapter tiene la responsabilidad de crear y dar acceso a un espacio de memorial cache
    * en donde se guardara la selección del tipo de usuario que accede a la aplicacion.
*/
class AlbumAdapter(context: Context){
    /*
        * Instancia de acceso para el uso de la API Shared Preferences
    */
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        "AlbumPrefers",
        Context.MODE_PRIVATE,
    )


    /*
        * saveUser es el metodo que permite guardar en la memoria cache el tipo de usuario
        * seleccionado a la hora de ingresar a la aplicacion
        *
        * Params: Recive un objeto tipo usuario con los datos del tipo de usuario seleccionado.
    */
    fun saveAlbumID(albumID: Int) {
        val albumIdToString = albumID.toString();
        sharedPreferences.edit().putString("album_id_key", albumIdToString).apply()
    }

    /*
        * getUser es el metodo que permite obtener de la memoria cache el tipo de usuario
        * seleccionado a la hora de ingresar a la aplicacion
        *
        * return: retorna un objeto tipo usuario con los datos del tipo de usuario almacenado.
    */
    fun getAlbumId(): Int? {
        return sharedPreferences.getString("album_id_key", null)?.toInt()
    }

    /*
        * clearUser es el metodo que permite limpiar el usuario almacenado de la memoria cache
    */
    fun clearAlbumID() {
        sharedPreferences.edit().remove("album_id_key").apply()
        return
    }
}