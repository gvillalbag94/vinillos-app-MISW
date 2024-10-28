package com.example.vinillos_app_misw.data.adapters

import android.content.Context
import android.content.SharedPreferences
import com.example.vinillos_app_misw.data.model.Usuario
import com.google.gson.Gson

/*
    * User adapter tiene la responsabilidad de crear y dar acceso a un espacio de memorial cache
    * en donde se guardara la selección del tipo de usuario que accede a la aplicacion.
*/
class UserAdapter(context: Context){
    /*
        * Instancia de acceso para el uso de la API Shared Preferences
    */
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        "MyPrefs",
        Context.MODE_PRIVATE,
        )

    /*
        * Instancia de acceso para el uso de la API Gson para el casteo de de datos tipo JSON
        * a la clse data Usuario.
    */
    private val gson = Gson()

    /*
        * saveUser es el metodo que permite guardar en la memoria cache el tipo de usuario
        * seleccionado a la hora de ingresar a la aplicacion
        *
        * Params: Recive un objeto tipo usuario con los datos del tipo de usuario seleccionado.
    */
    fun saveUser(usuario: Usuario) {
        val json = gson.toJson(usuario)
        sharedPreferences.edit().putString("usuario_key", json).apply()
    }

    /*
        * getUser es el metodo que permite obtener de la memoria cache el tipo de usuario
        * seleccionado a la hora de ingresar a la aplicacion
        *
        * return: retorna un objeto tipo usuario con los datos del tipo de usuario almacenado.
    */
    fun getUser(): Usuario? {
        val json = sharedPreferences.getString("usuario_key", null)
        return if (json != null) {
            gson.fromJson(json, Usuario::class.java)
        } else {
            null
        }
    }

    /*
        * clearUser es el metodo que permite limpiar el usuario almacenado de la memoria cache
    */
    fun clearUser() {
        sharedPreferences.edit().remove("usuario_key").apply()
         return
    }
}